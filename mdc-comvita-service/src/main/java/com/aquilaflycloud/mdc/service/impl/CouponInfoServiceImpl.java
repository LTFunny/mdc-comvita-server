package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.response.AlipayMarketingCashvoucherTemplateCreateResponse;
import com.alipay.api.response.AlipayMarketingCashvoucherTemplateModifyResponse;
import com.alipay.api.response.AlipayMarketingVoucherSendResponse;
import com.alipay.api.response.AlipayMarketingVoucherTemplatedetailQueryResponse;
import com.aquilaflycloud.auth.enums.UserTypeEnum;
import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.component.event.AfterCommitEvent;
import com.aquilaflycloud.mdc.component.event.AfterRollbackEvent;
import com.aquilaflycloud.mdc.enums.catalog.CatalogBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.*;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.extra.alipay.notify.AlipayCashVoucherNotify;
import com.aquilaflycloud.mdc.extra.alipay.notify.AlipayCashVoucherPay;
import com.aquilaflycloud.mdc.extra.alipay.request.VoucherSendRequest;
import com.aquilaflycloud.mdc.extra.alipay.request.VoucherTemplateCreateRequest;
import com.aquilaflycloud.mdc.extra.alipay.request.VoucherTemplateDetailQueryRequest;
import com.aquilaflycloud.mdc.extra.alipay.request.VoucherTemplateModifyRequest;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayOpenPlatformService;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.model.catalog.CatalogRel;
import com.aquilaflycloud.mdc.model.coupon.CouponAlipayRecord;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.param.coupon.*;
import com.aquilaflycloud.mdc.param.exchange.GoodsAuditParam;
import com.aquilaflycloud.mdc.result.coupon.CouponInfoResult;
import com.aquilaflycloud.mdc.result.coupon.CouponResult;
import com.aquilaflycloud.mdc.result.coupon.RelAnalysisResult;
import com.aquilaflycloud.mdc.result.coupon.StatisticsResult;
import com.aquilaflycloud.mdc.service.CouponInfoService;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * CouponInfoServiceImpl
 *
 * @author star
 * @date 2020-03-09
 */
@Service
public class CouponInfoServiceImpl implements CouponInfoService {
    @Resource
    private CouponInfoMapper couponInfoMapper;
    @Resource
    private CatalogInfoMapper catalogInfoMapper;
    @Resource
    private CatalogRelMapper catalogRelMapper;
    @Resource
    private CouponMemberRelMapper couponMemberRelMapper;
    @Resource
    private CouponAlipayRecordMapper couponAlipayRecordMapper;
    @Resource
    private ExchangeService exchangeService;
    @Resource
    private AlipayOpenPlatformService alipayOpenPlatformService;

    private CouponInfo stateHandler(CouponInfo couponInfo, Integer... withhold) {
        if (couponInfo == null) {
            throw new ServiceException("优惠券不存在");
        }
        DateTime now = DateTime.now();
        if (couponInfo.getAuditState() == AuditStateEnum.PENDING) {
            couponInfo.setState(CouponStateEnum.PENDING);
        } else if (couponInfo.getAuditState() == AuditStateEnum.REJECT) {
            couponInfo.setState(CouponStateEnum.REJECT);
        } else if (couponInfo.getState() != CouponStateEnum.DISABLE && couponInfo.getState() != CouponStateEnum.UNACTIVATION) {
            Integer withholdCount = 0;
            if (withhold.length > 0) {
                withholdCount = withhold[0];
            }
            if (couponInfo.getInventory() <= getTotalReceiveCount(couponInfo.getId()) - withholdCount) {
                couponInfo.setState(CouponStateEnum.SOLDOUT);
            } else if (couponInfo.getReleaseType() == ReleaseTypeEnum.REGULAR && couponInfo.getReleaseTime().after(now)) {
                couponInfo.setState(CouponStateEnum.NOTISSUED);
            } else if (couponInfo.getDisableType() == DisableTypeEnum.REGULAR && couponInfo.getDisableTime().before(now)) {
                couponInfo.setState(CouponStateEnum.DISABLE);
            } else if (couponInfo.getReceiveStartTime() != null && couponInfo.getReceiveStartTime().after(now)) {
                couponInfo.setState(CouponStateEnum.NOTRECEIVE);
            } else if (couponInfo.getReceiveEndTime() != null && couponInfo.getReceiveEndTime().before(now)) {
                couponInfo.setState(CouponStateEnum.EXPIRED);
            } else if (couponInfo.getReceiveEndTime() != null
                    && couponInfo.getReceiveEndTime().before(now.offsetNew(DateField.DAY_OF_YEAR, 3))) {
                couponInfo.setState(CouponStateEnum.EXPIRING);
            }
        }
        return couponInfo;
    }

    private CouponMemberRel stateHandler(CouponMemberRel rel) {
        if (rel == null) {
            throw new ServiceException("优惠券记录不存在");
        }
        if (rel.getVerificateState() != VerificateStateEnum.CONSUMED && rel.getVerificateState() != VerificateStateEnum.REVOKED) {
            if (rel.getVerificateState() == VerificateStateEnum.NOTCONSUME
                    && DateTime.now().isAfter(rel.getEffectiveEndTime())) {
                rel.setVerificateState(VerificateStateEnum.EXPIRED);
            }
        }
        return rel;
    }

    @Override
    public Integer getTotalReceiveCount(Long couponId, Boolean... reload) {
        String lock = "couponTotalReceiveCountLock" + couponId;
        String key = "couponTotalReceiveCount" + couponId;
        if (reload.length > 0 && reload[0]) {
            RedisUtil.redis().delete(key);
        }
        return RedisUtil.syncValueGet(lock, key, () -> couponMemberRelMapper.selectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getCouponId, couponId)
        ));
    }

    private Integer increaseTotalReceiveCount(Long couponId, Integer count) {
        String key = "couponTotalReceiveCount" + couponId;
        Integer total = getTotalReceiveCount(couponId);
        RedisUtil.valueRedis().set(key, total + count);
        return RedisUtil.<Integer>valueRedis().get(key);
    }

    private void decreaseTotalReceiveCount(Long couponId, Integer count) {
        String key = "couponTotalReceiveCount" + couponId;
        Integer total = getTotalReceiveCount(couponId);
        RedisUtil.valueRedis().set(key, NumberUtil.max(total - count, 0));
    }

    private Integer getTotalVerificateCount(Long couponId, Boolean... reload) {
        String lock = "couponTotalVerificateCountLock" + couponId;
        String key = "couponTotalVerificateCount" + couponId;
        if (reload.length > 0 && reload[0]) {
            RedisUtil.redis().delete(key);
        }
        return RedisUtil.syncValueGet(lock, key, () -> couponMemberRelMapper.selectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getCouponId, couponId)
                .eq(CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
        ));
    }

    private void increaseTotalVerificateCount(Long couponId, Integer count) {
        String key = "couponTotalVerificateCount" + couponId;
        Integer total = getTotalVerificateCount(couponId);
        RedisUtil.valueRedis().set(key, total + count);
    }

    private void updateCount(Long couponId, Boolean consumed) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            RedisUtil.syncLoad("updateCouponCountLock" + couponId, () -> {
                Integer count = couponMemberRelMapper.normalSelectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                        .eq(CouponMemberRel::getCouponId, couponId)
                        .eq(consumed, CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED));
                couponInfoMapper.normalUpdate(null, Wrappers.<CouponInfo>lambdaUpdate()
                        .set(!consumed, CouponInfo::getReceiveCount, count)
                        .set(consumed, CouponInfo::getVerificateCount, count)
                        .eq(CouponInfo::getId, couponId)
                );
                return null;
            });
        });
    }

    @Override
    public IPage<CouponResult> pageCoupon(CouponPageParam param) {
        List<Long> couponIdList = null;
        if (param.getCatalogId() != null) {
            couponIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                    .select(CatalogRel::getBusinessId)
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.COUPON)
                    .eq(CatalogRel::getCatalogId, param.getCatalogId())
            ).stream().map(CatalogRel::getBusinessId).collect(Collectors.toList());
        }
        DateTime now = DateTime.now();
        if (param.getValid() == WhetherEnum.YES) {
            param.setState(null);
        }
        CouponStateEnum state = param.getState();
        return couponInfoMapper.selectPage(param.page(), new QueryWrapper<CouponInfo>()
                .and(param.getValid() == WhetherEnum.YES, i -> i
                        .eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-receive_count", 0)
                        .lambda()
                        .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                        .nested(j -> j.isNull(CouponInfo::getReceiveEndTime).or()
                                .ge(CouponInfo::getReceiveEndTime, now)))
                .and(state == CouponStateEnum.NORMAL, i -> i
                        .eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-receive_count", 0)
                        .lambda()
                        .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                        .le(CouponInfo::getReleaseTime, now)
                        .nested(j -> j.isNull(CouponInfo::getEffectiveStartTime).or()
                                .le(CouponInfo::getEffectiveStartTime, now)
                        )
                        .nested(j -> j.isNull(CouponInfo::getReceiveEndTime).or()
                                .gt(CouponInfo::getReceiveEndTime, now.offsetNew(DateField.DAY_OF_YEAR, 3))
                        ))
                .and(state == CouponStateEnum.SOLDOUT, i -> i
                        .eq("state", CouponStateEnum.NORMAL)
                        .le("inventory-receive_count", 0)
                        .lambda()
                        .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE))
                .and(state == CouponStateEnum.NOTISSUED, i -> i
                        .eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-receive_count", 0)
                        .lambda()
                        .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                        .eq(CouponInfo::getReleaseType, ReleaseTypeEnum.REGULAR)
                        .gt(CouponInfo::getReleaseTime, now))
                .and(state == CouponStateEnum.NOTRECEIVE, i -> i
                        .eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-receive_count", 0)
                        .lambda()
                        .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                        .gt(CouponInfo::getReceiveStartTime, now))
                .and(state == CouponStateEnum.EXPIRED, i -> i
                        .eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-receive_count", 0)
                        .lambda()
                        .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                        .lt(CouponInfo::getReceiveEndTime, now))
                .and(state == CouponStateEnum.EXPIRING, i -> i
                        .eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-receive_count", 0)
                        .lambda()
                        .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                        .ge(CouponInfo::getReceiveEndTime, now)
                        .le(CouponInfo::getReceiveEndTime, now.offsetNew(DateField.DAY_OF_YEAR, 3)))
                .lambda()
                .like(StrUtil.isNotBlank(param.getCouponCode()), CouponInfo::getCouponCode, param.getCouponCode())
                .like(StrUtil.isNotBlank(param.getCouponName()), CouponInfo::getCouponName, param.getCouponName())
                .like(StrUtil.isNotBlank(param.getCreatorName()), CouponInfo::getCreatorName, param.getCreatorName())
                .like(StrUtil.isNotBlank(param.getDesignateOrgNames()), CouponInfo::getDesignateOrgNames, param.getDesignateOrgNames())
                .ge(param.getCreateTimeStart() != null, CouponInfo::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, CouponInfo::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getCouponType() != null, CouponInfo::getCouponType, param.getCouponType())
                .eq(CouponInfo::getCreateSource, CreateSourceEnum.NORMAL)
                .eq(param.getCatalogId() != null && CollUtil.isEmpty(couponIdList), CouponInfo::getId, param.getCatalogId())
                .in(CollUtil.isNotEmpty(couponIdList), CouponInfo::getId, couponIdList)
                .eq(state == CouponStateEnum.PENDING, CouponInfo::getAuditState, AuditStateEnum.PENDING)
                .eq(state == CouponStateEnum.REJECT, CouponInfo::getAuditState, AuditStateEnum.REJECT)
                .eq(state == CouponStateEnum.UNACTIVATION, CouponInfo::getState, CouponStateEnum.UNACTIVATION)
                .and(state == CouponStateEnum.DISABLE, i -> i
                        .eq(CouponInfo::getState, CouponStateEnum.DISABLE)
                        .or().lt(CouponInfo::getDisableTime, now)
                )
                .like(ObjectUtil.isNotNull(param.getRelationId()), CouponInfo::getDesignateOrgIds, param.getRelationId())
                .orderByDesc(CouponInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(couponInfo -> {
            couponInfo = stateHandler(couponInfo);
            CouponResult result = new CouponResult();
            BeanUtil.copyProperties(couponInfo, result);
            List<Long> catalogIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                    .select(CatalogRel::getCatalogId)
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.COUPON)
                    .eq(CatalogRel::getBusinessId, couponInfo.getId())
            ).stream().map(CatalogRel::getCatalogId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(catalogIdList)) {
                List<CatalogInfo> catalogList = catalogInfoMapper.selectList(Wrappers.<CatalogInfo>lambdaQuery()
                        .in(CatalogInfo::getId, catalogIdList)
                );
                result.setCatalogList(catalogList);
            }
            result.setReceiveCount(getTotalReceiveCount(couponInfo.getId()));
            result.setVerificateCount(getTotalVerificateCount(couponInfo.getId()));
            return result;
        });
    }

    @Override
    public CouponInfo addCoupon(CouponAddParam param, CreateSourceEnum createSource, CouponModeEnum couponMode) {
        CouponInfo couponInfo = new CouponInfo();
        BeanUtil.copyProperties(param, couponInfo);
        couponInfo.setCouponCode(MdcUtil.getTenantIncIdStr("couponCode", DateTime.now().toString("yyMMdd"), 4));
        if (param.getReleaseType() == ReleaseTypeEnum.IMMEDIATELY) {
            couponInfo.setReleaseTime(DateTime.now());
        }
        if (param.getCouponType().getParent() != couponMode.getType()) {
            throw new ServiceException("优惠券模式和类型有误");
        }
        switch (param.getCouponType()) {
            case DISCOUNT: {
                if (param.getCouponValue().compareTo(BigDecimal.TEN) >= 0) {
                    throw new ServiceException("折扣值必须小于10");
                }
                break;
            }
            case REDUCTION: {
                if (param.getTargetPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ServiceException("满减券门槛金额不能小于0");
                }
                break;
            }
            case UNLIMITED: {
                couponInfo.setTargetPrice(BigDecimal.ZERO);
                break;
            }
            case OTHER: {
                couponInfo.setCouponValue(null);
                break;
            }
            default:
        }
        if (param.getEffectiveType() == EffectiveTypeEnum.DATERANGE) {
            if (DateTime.now().isAfterOrEquals(param.getEffectiveEndTime())) {
                throw new ServiceException("有效时间必须大于当前时间");
            }
            couponInfo.setEffectiveDays(null);
        } else if (param.getEffectiveType() == EffectiveTypeEnum.AFTERDAYS) {
            couponInfo.setEffectiveStartTime(null);
            couponInfo.setEffectiveEndTime(null);
        }
        if (param.getReceiveType() == ReceiveTypeEnum.NONE) {
            couponInfo.setReceiveStartTime(null);
            couponInfo.setReceiveEndTime(null);
        }
        if (param.getDisableType() == DisableTypeEnum.MANUAL) {
            couponInfo.setDisableTime(null);
        }
        if (StrUtil.isBlank(param.getDesignateOrgIds())) {
            couponInfo.setDesignateOrgIds(null);
            couponInfo.setDesignateOrgNames(null);
        }
        couponInfo.setCreateSource(createSource);
        couponInfo.setReceiveCount(0);
        couponInfo.setVerificateCount(0);
        couponInfo.setCouponMode(couponMode);
        if (couponMode == CouponModeEnum.ALIPAY) {
            couponInfo.setState(CouponStateEnum.UNACTIVATION);
        } else {
            couponInfo.setState(CouponStateEnum.NORMAL);
        }
        MdcUtil.setOrganInfo(couponInfo);
        int count = couponInfoMapper.insert(couponInfo);
        if (count <= 0) {
            throw new ServiceException("新增优惠券失败");
        }
        return couponInfo;
    }

    @Transactional
    @Override
    public void addCoupon(CouponAddParam param) {
        if (param.getCouponType() == CouponTypeEnum.ALIPAYCASHVOUCHER) {
            throw new ServiceException("优惠券类型有误");
        }
        CouponInfo couponInfo = addCoupon(param, CreateSourceEnum.NORMAL, CouponModeEnum.NORMAL);
        //关联优惠券分类
        List<CatalogRel> relList = new ArrayList<>();
        for (Long catalogId : param.getCatalogIdList()) {
            CatalogRel rel = new CatalogRel();
            rel.setBusinessId(couponInfo.getId());
            rel.setBusinessType(CatalogBusinessTypeEnum.COUPON);
            rel.setCatalogId(catalogId);
            relList.add(rel);
        }
        int relCount = catalogRelMapper.insertAllBatch(relList);
        if (relCount <= 0) {
            throw new ServiceException("关联优惠券分类失败");
        }
        RedisUtil.zSetRedis().add("couponInventory", couponInfo.getId(), couponInfo.getInventory());
        RedisUtil.redis().expire("couponInventory", 30, TimeUnit.DAYS);
    }

    @Override
    public CouponInfo editCoupon(CouponEditParam param, CreateSourceEnum createSource, CouponModeEnum couponMode) {
        CouponInfo couponInfo = couponInfoMapper.selectOne(Wrappers.<CouponInfo>lambdaQuery()
                .eq(CouponInfo::getId, param.getId())
                .eq(CouponInfo::getCreateSource, createSource)
        );
        if (couponInfo == null) {
            throw new ServiceException("优惠券不存在");
        }
        if (couponInfo.getCouponMode() != couponMode) {
            throw new ServiceException("该优惠券模式有误");
        }
        CouponInfo update = new CouponInfo();
        BeanUtil.copyProperties(param, update);
        update.setId(null);
        if (MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE) {
            if (!StrUtil.containsAny(couponInfo.getCreatorOrgIds(), StrUtil.split(MdcUtil.getCurrentOrgIds(), ","))) {
                throw new ServiceException("商户不可修改指定优惠券");
            }
            update.setAuditState(AuditStateEnum.PENDING);
        }
        LambdaUpdateWrapper<CouponInfo> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CouponInfo::getId, param.getId());
        switch (couponInfo.getCouponType()) {
            case DISCOUNT: {
                if (param.getCouponValue() != null && param.getCouponValue().compareTo(BigDecimal.TEN) >= 0) {
                    throw new ServiceException("折扣值必须小于10");
                }
                break;
            }
            case REDUCTION: {
                if (param.getTargetPrice() != null && param.getTargetPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new ServiceException("满减券门槛金额不能小于0");
                }
                break;
            }
            case UNLIMITED: {
                update.setTargetPrice(BigDecimal.ZERO);
                break;
            }
            case OTHER: {
                updateWrapper.set(CouponInfo::getCouponValue, null);
                break;
            }
            default:
        }
        if (param.getEffectiveType() == EffectiveTypeEnum.DATERANGE) {
            if (DateTime.now().isAfterOrEquals(param.getEffectiveEndTime())) {
                throw new ServiceException("有效时间必须大于当前时间");
            }
            updateWrapper.set(CouponInfo::getEffectiveDays, null);
        } else if (param.getEffectiveType() == EffectiveTypeEnum.AFTERDAYS) {
            updateWrapper.set(CouponInfo::getEffectiveStartTime, null);
            updateWrapper.set(CouponInfo::getEffectiveEndTime, null);
        }
        DateTime now = DateTime.now();
        if (param.getReleaseType() == ReleaseTypeEnum.IMMEDIATELY && couponInfo.getReleaseType() != ReleaseTypeEnum.IMMEDIATELY) {
            if (now.isBefore(couponInfo.getReleaseTime())) {
                update.setReleaseTime(now);
            } else {
                update.setReleaseTime(null);
            }
        }
        if (param.getReleaseType() == ReleaseTypeEnum.REGULAR && couponInfo.getReleaseType() != ReleaseTypeEnum.REGULAR
                && couponInfo.getState() == CouponStateEnum.DISABLE) {
            update.setState(CouponStateEnum.NORMAL);
        }
        if (param.getReceiveType() == ReceiveTypeEnum.NONE) {
            updateWrapper.set(CouponInfo::getReceiveStartTime, null);
            updateWrapper.set(CouponInfo::getReceiveEndTime, null);
        }
        if (param.getDisableType() == DisableTypeEnum.MANUAL) {
            updateWrapper.set(CouponInfo::getDisableTime, null);
        }
        int surplusInventory = couponInfo.getInventory() - getTotalReceiveCount(couponInfo.getId());
        if (param.getInventoryIncrease() != null && param.getInventoryIncrease() > 0) {
            surplusInventory = surplusInventory + param.getInventoryIncrease();
            if (surplusInventory < 0) {
                throw new ServiceException("剩余库存不能小于0");
            }
            update.setInventory(couponInfo.getInventory() + param.getInventoryIncrease());
        }
        if (param.getDesignateOrgIds() != null && !StrUtil.equals(param.getDesignateOrgIds(), couponInfo.getDesignateOrgIds())) {
            //若designateOrgIds为空字符串,则设置为null
            if (StrUtil.isBlank(param.getDesignateOrgIds())) {
                updateWrapper.set(CouponInfo::getDesignateOrgIds, null);
                updateWrapper.set(CouponInfo::getDesignateOrgNames, null);
                couponMemberRelMapper.update(new CouponMemberRel(), Wrappers.<CouponMemberRel>lambdaUpdate()
                        .eq(CouponMemberRel::getCouponId, param.getId())
                        .ne(CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
                        .set(CouponMemberRel::getDesignateOrgIds, couponInfo.getCreatorOrgIds())
                        .set(CouponMemberRel::getDesignateOrgNames, couponInfo.getCreatorOrgNames())
                );
            } else {
                //设置部门信息
                MdcUtil.setOrganInfo(update);
                CouponMemberRel relUpdate = new CouponMemberRel();
                relUpdate.setDesignateOrgIds(param.getDesignateOrgIds());
                MdcUtil.setOrganInfo(relUpdate);
                couponMemberRelMapper.update(relUpdate, Wrappers.<CouponMemberRel>lambdaUpdate()
                        .eq(CouponMemberRel::getCouponId, param.getId())
                        .ne(CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
                );
            }
        } else if (param.getDesignateOrgIds() == null) {
            update.setDesignateOrgIds(null);
            update.setDesignateOrgNames(null);
        }
        int count = couponInfoMapper.update(update, updateWrapper);
        if (count <= 0) {
            throw new ServiceException("修改优惠券失败");
        }
        return couponInfoMapper.selectById(param.getId());
    }

    @Transactional
    @Override
    public void editCoupon(CouponEditParam param) {
        RedisUtil.transactionalLock("changeCoupon" + param.getId());
        CouponInfo couponInfo = editCoupon(param, CreateSourceEnum.NORMAL, CouponModeEnum.NORMAL);
        if (CollUtil.isNotEmpty(param.getCatalogIdList())) {
            catalogRelMapper.delete(Wrappers.<CatalogRel>lambdaQuery()
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.COUPON)
                    .eq(CatalogRel::getBusinessId, param.getId())
            );
            List<CatalogRel> relList = new ArrayList<>();
            for (Long catalogId : param.getCatalogIdList()) {
                CatalogRel rel = new CatalogRel();
                rel.setBusinessId(param.getId());
                rel.setBusinessType(CatalogBusinessTypeEnum.COUPON);
                rel.setCatalogId(catalogId);
                relList.add(rel);
            }
            int relCount = catalogRelMapper.insertAllBatch(relList);
            if (relCount <= 0) {
                throw new ServiceException("关联优惠券分类失败");
            }
        }
        int surplusInventory = couponInfo.getInventory() - getTotalReceiveCount(couponInfo.getId());
        RedisUtil.zSetRedis().add("couponInventory", couponInfo.getId(), surplusInventory);
        RedisUtil.redis().expire("couponInventory", 30, TimeUnit.DAYS);
    }

    @Transactional
    @Override
    public void addCoupon(AlipayCouponAddParam param) {
        CouponAddParam addParam = new CouponAddParam();
        BeanUtil.copyProperties(param, addParam);
        addParam.setReceiveType(ReceiveTypeEnum.CUSTOM);
        addParam.setCouponRemark(JSONUtil.toJsonStr(param.getCouponRemarkList()));
        if (addParam.getCouponType() != CouponTypeEnum.ALIPAYCASHVOUCHER) {
            throw new ServiceException("优惠券类型有误");
        }
        CouponInfo couponInfo = addCoupon(addParam, CreateSourceEnum.NORMAL, CouponModeEnum.ALIPAY);
        //关联优惠券分类
        List<CatalogRel> relList = new ArrayList<>();
        for (Long catalogId : param.getCatalogIdList()) {
            CatalogRel rel = new CatalogRel();
            rel.setBusinessId(couponInfo.getId());
            rel.setBusinessType(CatalogBusinessTypeEnum.COUPON);
            rel.setCatalogId(catalogId);
            relList.add(rel);
        }
        int relCount = catalogRelMapper.insertAllBatch(relList);
        if (relCount <= 0) {
            throw new ServiceException("关联优惠券分类失败");
        }
        RedisUtil.zSetRedis().add("couponInventory", couponInfo.getId(), couponInfo.getInventory());
        RedisUtil.redis().expire("couponInventory", 30, TimeUnit.DAYS);
    }

    @Transactional
    @Override
    public void editCoupon(AlipayCouponEditParam param) {
        RedisUtil.transactionalLock("changeCoupon" + param.getId());
        CouponEditParam editParam = new CouponEditParam();
        BeanUtil.copyProperties(param, editParam);
        editParam.setCouponRemark(CollUtil.isNotEmpty(param.getCouponRemarkList()) ? JSONUtil.toJsonStr(param.getCouponRemarkList()) : null);
        CouponInfo couponInfo = couponInfoMapper.selectById(param.getId());
        couponInfo = stateHandler(couponInfo);
        if (couponInfo.getState() != CouponStateEnum.UNACTIVATION) {
            if (couponInfo.getState() == CouponStateEnum.EXPIRED) {
                throw new ServiceException("优惠券已结束,不可修改");
            }
            Map<String, Object> paramMap = BeanUtil.beanToMap(param, false, true);
            for (String key : paramMap.keySet()) {
                if (StrUtil.equalsAny(key, "id", "receiveEndTime")) {
                    continue;
                }
                if (!StrUtil.equalsAny(key, "id", "receiveEndTime", "catalogIdList", "couponRemarkList") ||
                        (StrUtil.equals(key, "catalogIdList") && CollUtil.isNotEmpty(param.getCatalogIdList()))
                        || (StrUtil.equals(key, "couponRemarkList") && CollUtil.isNotEmpty(param.getCouponRemarkList()))) {
                    throw new ServiceException("激活状态只可更改领取结束时间");
                }
            }
        }
        couponInfo = editCoupon(editParam, CreateSourceEnum.NORMAL, CouponModeEnum.ALIPAY);
        if (CollUtil.isNotEmpty(param.getCatalogIdList())) {
            catalogRelMapper.delete(Wrappers.<CatalogRel>lambdaQuery()
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.COUPON)
                    .eq(CatalogRel::getBusinessId, param.getId())
            );
            List<CatalogRel> relList = new ArrayList<>();
            for (Long catalogId : param.getCatalogIdList()) {
                CatalogRel rel = new CatalogRel();
                rel.setBusinessId(param.getId());
                rel.setBusinessType(CatalogBusinessTypeEnum.COUPON);
                rel.setCatalogId(catalogId);
                relList.add(rel);
            }
            int relCount = catalogRelMapper.insertAllBatch(relList);
            if (relCount <= 0) {
                throw new ServiceException("关联优惠券分类失败");
            }
        }
        VoucherTemplateModifyRequest request = new VoucherTemplateModifyRequest();
        CouponAlipayRecord record = couponAlipayRecordMapper.selectOne(Wrappers.<CouponAlipayRecord>lambdaQuery()
                .eq(CouponAlipayRecord::getTemplateId, couponInfo.getCouponThirdId())
        );
        if (record != null) {
            request.setAppId(record.getAppId());
            request.setOutBizNo(MdcUtil.getSnowflakeIdStr());
            request.setPublishStartTime(param.getReceiveStartTime() != null ? DateUtil.date(param.getReceiveStartTime()).toString() : DateUtil.date(couponInfo.getReceiveStartTime()).toString());
            request.setPublishEndTime(param.getReceiveEndTime() != null ? DateUtil.date(param.getReceiveEndTime()).toString() : DateUtil.date(couponInfo.getReceiveEndTime()).toString());
            request.setSlogan(StrUtil.isNotBlank(param.getCouponName()) ? param.getCouponName() : couponInfo.getCouponName());
            request.setTemplateId(couponInfo.getCouponThirdId());
            EffectiveTypeEnum effectiveType = param.getEffectiveType();
            if (effectiveType == null) {
                effectiveType = couponInfo.getEffectiveType();
            }
            JSONObject validPeriod = new JSONObject();
            if (effectiveType == EffectiveTypeEnum.DATERANGE) {
                validPeriod.set("type", "ABSOLUTE");
                validPeriod.set("start", DateUtil.date(param.getEffectiveStartTime() != null ? param.getEffectiveStartTime() : couponInfo.getEffectiveStartTime()));
                validPeriod.set("end", DateUtil.date(param.getEffectiveEndTime() != null ? param.getEffectiveEndTime() : couponInfo.getEffectiveEndTime()));
            } else if (effectiveType == EffectiveTypeEnum.AFTERDAYS) {
                validPeriod.set("type", "RELATIVE");
                validPeriod.set("unit", "DAY");
                validPeriod.set("duration", param.getEffectiveDays() != null ? param.getEffectiveDays() : couponInfo.getEffectiveDays());
            }
            request.setVoucherValidPeriod(validPeriod.toString());
            AlipayMarketingCashvoucherTemplateModifyResponse response = alipayOpenPlatformService.modifyVoucherTemplate(request);
            if (!StrUtil.equalsAny(response.getStatus(), "I", "S")) {
                throw new ServiceException("修改支付宝优惠券失败");
            }
            CouponAlipayRecord update = new CouponAlipayRecord();
            update.setId(record.getId());
            update.setPublishStartTime(DateUtil.parse(request.getPublishStartTime()));
            update.setPublishEndTime(DateUtil.parse(request.getPublishEndTime()));
            update.setVoucherValidPeriod(request.getVoucherValidPeriod());
            couponAlipayRecordMapper.updateById(update);
        }
        int surplusInventory = couponInfo.getInventory() - getTotalReceiveCount(couponInfo.getId());
        RedisUtil.zSetRedis().add("couponInventory", couponInfo.getId(), surplusInventory);
        RedisUtil.redis().expire("couponInventory", 30, TimeUnit.DAYS);
    }

    @Transactional
    @Override
    public BaseResult<String> activeCoupon(AlipayCouponActiveParam param) {
        CouponInfo couponInfo = couponInfoMapper.selectById(param.getCouponId());
        if (couponInfo == null) {
            throw new ServiceException("优惠券不存在");
        }
        if (couponInfo.getCouponMode() != CouponModeEnum.ALIPAY) {
            throw new ServiceException("优惠券不是支付宝优惠券");
        }
        if (couponInfo.getState() != CouponStateEnum.UNACTIVATION) {
            throw new ServiceException("优惠券已激活");
        }
        VoucherTemplateCreateRequest request = new VoucherTemplateCreateRequest();
        request.setAmount(couponInfo.getCouponValue().toString());
        request.setBrandName(couponInfo.getCouponName());
        request.setFloorAmount(couponInfo.getTargetPrice().toString());
        request.setFundAccount(param.getFundAccount());
        String notifyUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/alipay/cashVoucherTemplate/notify").toString();
        request.setNotifyUri(notifyUrl);
        request.setOutBizNo(MdcUtil.getSnowflakeIdStr());
        request.setPublishEndTime(DateUtil.date(couponInfo.getReceiveEndTime()).toString());
        request.setPublishStartTime(DateUtil.date(couponInfo.getReceiveStartTime()).toString());
        String redirectUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/alipay/cashVoucherTemplate/pay").toString();
        request.setRedirectUri(redirectUrl);
        if (StrUtil.isNotBlank(couponInfo.getAlipayPid())) {
            JSONObject pid = new JSONObject();
            pid.set("PID", couponInfo.getAlipayPid());
            request.setRuleConf(pid.toString());
        }
        request.setVoucherDescription(couponInfo.getCouponRemark());
        request.setVoucherQuantity(couponInfo.getInventory().longValue());
        request.setVoucherType("FIX_VOUCHER");
        request.setVoucherUseScene("ALIPAY_COMMON");
        JSONObject validPeriod = new JSONObject();
        if (couponInfo.getEffectiveType() == EffectiveTypeEnum.DATERANGE) {
            validPeriod.set("type", "ABSOLUTE");
            validPeriod.set("start", DateUtil.date(couponInfo.getEffectiveStartTime()).toString());
            validPeriod.set("end", DateUtil.date(couponInfo.getEffectiveEndTime()).toString());
        } else if (couponInfo.getEffectiveType() == EffectiveTypeEnum.AFTERDAYS) {
            validPeriod.set("type", "RELATIVE");
            validPeriod.set("unit", "DAY");
            validPeriod.set("duration", couponInfo.getEffectiveDays());
        }
        request.setVoucherValidPeriod(validPeriod.toString());
        request.setAppId(param.getAppId());
        AlipayMarketingCashvoucherTemplateCreateResponse response = alipayOpenPlatformService.createVoucherTemplate(request);
        CouponAlipayRecord record = new CouponAlipayRecord();
        BeanUtil.copyProperties(request, record);
        BeanUtil.copyProperties(response, record);
        record.setCouponId(couponInfo.getId());
        couponAlipayRecordMapper.insert(record);
        return new BaseResult<String>().setResult(response.getConfirmUri());
    }

    @Override
    public Long updateThirdCouponStatus(AlipayCashVoucherPay notify) {
        CouponAlipayRecord record = couponAlipayRecordMapper.normalSelectOne(Wrappers.<CouponAlipayRecord>lambdaQuery()
                .eq(CouponAlipayRecord::getFundOrderNo, notify.getAuthNo())
                .eq(CouponAlipayRecord::getOutBizNo, notify.getOutRequestNo())
        );
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, record.getTenantId());
        ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, record.getSubTenantId());
        Long couponId = null;
        if (record != null) {
            couponId = record.getCouponId();
            CouponInfo couponInfo = couponInfoMapper.selectById(couponId);
            updateThirdCouponStatus(couponInfo);
        }
        return couponId;
    }

    @Override
    public void updateThirdCouponStatus(CouponInfo couponInfo) {
        if (couponInfo == null) {
            throw new ServiceException("优惠券不存在");
        }
        if (couponInfo.getCouponMode() == CouponModeEnum.ALIPAY) {
            List<CouponAlipayRecord> recordList = couponAlipayRecordMapper.selectList(Wrappers.<CouponAlipayRecord>lambdaQuery()
                    .eq(CouponAlipayRecord::getCouponId, couponInfo.getId())
            );
            AlipayMarketingVoucherTemplatedetailQueryResponse response;
            String templateId = null;
            for (CouponAlipayRecord record : recordList) {
                VoucherTemplateDetailQueryRequest request = new VoucherTemplateDetailQueryRequest();
                request.setAppId(record.getAppId());
                request.setTemplateId(record.getTemplateId());
                response = alipayOpenPlatformService.queryVoucherTemplate(request);
                couponAlipayRecordMapper.update(null, Wrappers.<CouponAlipayRecord>lambdaUpdate()
                        .set(CouponAlipayRecord::getStatus, response.getStatus())
                        .set(CouponAlipayRecord::getVoucherName, response.getVoucherName())
                        .set(CouponAlipayRecord::getTotalAmount, response.getTotalAmount())
                        .set(CouponAlipayRecord::getPublishCount, response.getPublishCount())
                        .set(CouponAlipayRecord::getPublishAmount, response.getPublishAmount())
                        .set(CouponAlipayRecord::getUsedCount, response.getUsedCount())
                        .set(CouponAlipayRecord::getUsedAmount, response.getUsedAmount())
                        .set(CouponAlipayRecord::getRecycleAmount, response.getRecycleAmount())
                        .eq(CouponAlipayRecord::getId, record.getId())
                );
                if (StrUtil.equalsAny(response.getStatus(), "S", "E")) {
                    templateId = record.getTemplateId();
                }
            }
            if (StrUtil.isNotBlank(templateId)) {
                couponInfoMapper.update(null, Wrappers.<CouponInfo>lambdaUpdate()
                        .set(CouponInfo::getCouponThirdId, templateId)
                        .set(CouponInfo::getState, CouponStateEnum.NORMAL)
                        .eq(CouponInfo::getId, couponInfo.getId())
                );
            }
        }
    }

    @Override
    public void updateThirdCouponRelStatus(AlipayCashVoucherNotify notify) {
        CouponMemberRel rel = couponMemberRelMapper.normalSelectOne(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getRelThirdId, notify.getVoucherId())
        );
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, rel.getTenantId());
        ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, rel.getSubTenantId());
        couponMemberRelMapper.update(null, Wrappers.<CouponMemberRel>lambdaUpdate()
                .set(CouponMemberRel::getVerificateMode, VerificateModeEnum.ALIPAY)
                .set(CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
                .set(CouponMemberRel::getVerificateTime, notify.getBizTime())
                .set(CouponMemberRel::getVerificaterThirdId, notify.getPartnerId())
                .set(CouponMemberRel::getVerificaterThirdName, notify.getPartnerName())
                .eq(CouponMemberRel::getRelThirdId, notify.getVoucherId())
        );
        //更新优惠券数量
        updateCount(rel.getCouponId(), true);
        RedisUtil.syncLoad("changeCoupon" + rel.getCouponId(), () -> {
            increaseTotalVerificateCount(rel.getCouponId(), 1);
            return null;
        });
        //更新优惠券领取缓存
        CouponMemberRel newRel = couponMemberRelMapper.selectById(rel.getId());
        changeMemberRelList(newRel.getCouponId(), newRel.getMemberId(), newRel);
    }

    @Override
    public void toggleState(CouponGetParam param) {
        RedisUtil.syncLoad("changeCoupon" + param.getId(), () -> {
            CouponInfo couponInfo = couponInfoMapper.selectOne(Wrappers.<CouponInfo>lambdaQuery()
                    .eq(CouponInfo::getId, param.getId())
                    .eq(CouponInfo::getCreateSource, CreateSourceEnum.NORMAL)
            );
            if (couponInfo == null) {
                throw new ServiceException("优惠券不存在");
            }
            CouponInfo update = new CouponInfo();
            update.setId(param.getId());
            update.setState(couponInfo.getState() == CouponStateEnum.NORMAL ? CouponStateEnum.DISABLE : CouponStateEnum.NORMAL);
            if (MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE) {
                update.setAuditState(AuditStateEnum.PENDING);
            }
            int count = couponInfoMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("操作失败");
            }
            return null;
        });
    }

    @Override
    public CouponInfo getCoupon(Long couponId) {
        CouponInfo couponInfo = couponInfoMapper.selectById(couponId);
        return stateHandler(couponInfo);
    }

    @Override
    public CouponResult getCoupon(CouponGetParam param) {
        CouponInfo couponInfo = couponInfoMapper.selectOne(Wrappers.<CouponInfo>lambdaQuery()
                .eq(CouponInfo::getId, param.getId())
        );
        couponInfo = stateHandler(couponInfo);
        CouponResult result = new CouponResult();
        BeanUtil.copyProperties(couponInfo, result);
        //第三方优惠券更新状态
        if (couponInfo.getCouponMode() != CouponModeEnum.NORMAL && couponInfo.getState() == CouponStateEnum.UNACTIVATION
                && StrUtil.isBlank(couponInfo.getCouponThirdId())) {
            updateThirdCouponStatus(couponInfo);
        }
        List<Long> catalogIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                .select(CatalogRel::getCatalogId)
                .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.COUPON)
                .eq(CatalogRel::getBusinessId, couponInfo.getId())
        ).stream().map(CatalogRel::getCatalogId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(catalogIdList)) {
            List<CatalogInfo> catalogList = catalogInfoMapper.selectList(Wrappers.<CatalogInfo>lambdaQuery()
                    .in(CatalogInfo::getId, catalogIdList)
            );
            result.setCatalogList(catalogList);
        }
        Integer totalReceiveCount = getTotalReceiveCount(couponInfo.getId(), true);
        result.setReceiveCount(totalReceiveCount);
        result.setVerificateCount(getTotalVerificateCount(couponInfo.getId(), true));
        int expiredCount = couponMemberRelMapper.selectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getCouponId, couponInfo.getId())
                .eq(CouponMemberRel::getVerificateState, VerificateStateEnum.NOTCONSUME)
                .lt(CouponMemberRel::getEffectiveEndTime, DateTime.now())
        );
        result.setExpiredCount(expiredCount);
        int surplusInventory = couponInfo.getInventory() - totalReceiveCount;
        result.setUnReceivedCount(surplusInventory);
        RedisUtil.zSetRedis().add("couponInventory", couponInfo.getId(), surplusInventory);
        RedisUtil.redis().expire("couponInventory", 30, TimeUnit.DAYS);
        return result;
    }

    @Transactional
    @Override
    public void auditCoupon(CouponAuditParam param) {
        CouponInfo couponInfo = couponInfoMapper.selectById(param.getId());
        stateHandler(couponInfo);
        if (couponInfo.getAuditState() == AuditStateEnum.PENDING) {
            CouponInfo update = new CouponInfo();
            update.setId(param.getId());
            update.setAuditState(param.getIsApprove() ? AuditStateEnum.APPROVE : AuditStateEnum.REJECT);
            update.setAuditRemark(param.getAuditRemark());
            int count = couponInfoMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("审核失败");
            }
            if (couponInfo.getCreateSource() == CreateSourceEnum.EXCHANGE) {
                GoodsAuditParam auditParam = new GoodsAuditParam();
                BeanUtil.copyProperties(param, auditParam);
                exchangeService.auditGoods(auditParam, GoodsTypeEnum.COUPON, param.getId());
            }
        } else {
            throw new ServiceException("该优惠券不需审核");
        }
    }

    @Override
    public StatisticsResult getStatistics(AuthParam param) {
        return couponInfoMapper.selectMaps(new QueryWrapper<CouponInfo>()
                .select("count(*) couponCount," +
                        "coalesce(sum(case when audit_state = 1 then 1 else 0 end),0) pendingCount," +
                        "coalesce(sum(receive_count),0) receiveCount," +
                        "coalesce(sum(verificate_count),0) verificateCount," +
                        "coalesce(sum(case when coupon_scope = 2 then 1 else 0 end),0) shopCouponCount")
                .lambda()
                .eq(CouponInfo::getCreateSource, CreateSourceEnum.NORMAL)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).stream().map(map -> BeanUtil.fillBeanWithMap(map, new StatisticsResult(), true,
                CopyOptions.create().ignoreError())).collect(Collectors.toList()).get(0);
    }

    @Override
    public IPage<CouponMemberRel> pageCouponRel(CouponRelPageParam param) {
        return couponMemberRelMapper.selectPage(param.page(), Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(param.getCouponId() != null, CouponMemberRel::getCouponId, param.getCouponId())
                .like(StrUtil.isNotBlank(param.getCouponCode()), CouponMemberRel::getCouponCode, param.getCouponCode())
                .like(StrUtil.isNotBlank(param.getCouponName()), CouponMemberRel::getCouponName, param.getCouponName())
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), CouponMemberRel::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), CouponMemberRel::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getVerificaterName()), CouponMemberRel::getVerificaterName, param.getVerificaterName())
                .like(StrUtil.isNotBlank(param.getVerificaterOrgNames()), CouponMemberRel::getVerificaterOrgNames, param.getVerificaterOrgNames())
                .eq(param.getCouponType() != null, CouponMemberRel::getCouponType, param.getCouponType())
                .eq(param.getCreateSource() != null, CouponMemberRel::getCreateSource, param.getCreateSource())
                .eq(param.getVerificateMode() != null, CouponMemberRel::getVerificateMode, param.getVerificateMode())
                .and(param.getVerificateState() == VerificateStateEnum.EXPIRED,
                        i -> i.eq(CouponMemberRel::getVerificateState, VerificateStateEnum.NOTCONSUME)
                                .lt(CouponMemberRel::getEffectiveEndTime, DateTime.now()))
                .and(param.getVerificateState() == VerificateStateEnum.NOTCONSUME,
                        i -> i.eq(CouponMemberRel::getVerificateState, VerificateStateEnum.NOTCONSUME)
                                .ge(CouponMemberRel::getEffectiveEndTime, DateTime.now()))
                .eq(param.getVerificateState() == VerificateStateEnum.CONSUMED, CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
                .eq(param.getVerificateState() == VerificateStateEnum.REVOKED, CouponMemberRel::getVerificateState, VerificateStateEnum.REVOKED)
                .ge(param.getReceiveTimeStart() != null, CouponMemberRel::getReceiveTime, param.getReceiveTimeStart())
                .le(param.getReceiveTimeEnd() != null, CouponMemberRel::getReceiveTime, param.getReceiveTimeEnd())
                .ge(param.getVerificateTimeStart() != null, CouponMemberRel::getVerificateTime, param.getVerificateTimeStart())
                .le(param.getVerificateTimeEnd() != null, CouponMemberRel::getVerificateTime, param.getVerificateTimeEnd())
                .eq(param.getVerificateState() == VerificateStateEnum.CONSUMED
                        && MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE, CouponMemberRel::getVerificaterOrgIds, MdcUtil.getCurrentOrgIds())
                .orderByDesc(!param.getOrderByVerificateTime(), CouponMemberRel::getReceiveTime)
                .orderByDesc(param.getOrderByVerificateTime(), CouponMemberRel::getVerificateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(rel -> {
            rel = stateHandler(rel);
            if (param.getVerificateState() != VerificateStateEnum.CONSUMED) {
                rel.setVerificateCode(null);
            }
            return rel;
        });
    }

    @Override
    public CouponMemberRel getCouponRel(CouponRelGetParam param) {
        CouponMemberRel rel;
        if (param.getId() != null) {
            rel = couponMemberRelMapper.selectById(param.getId());
        } else {
            rel = couponMemberRelMapper.normalSelectOne(Wrappers.<CouponMemberRel>lambdaQuery()
                    .eq(CouponMemberRel::getVerificateCode, param.getVerificateCode())
                    .eq(CouponMemberRel::getTenantId, MdcUtil.getCurrentTenantId())
            );
        }
        return stateHandler(rel);
    }

    private CouponMemberRel calculatePrice(CouponMemberRel rel, BigDecimal consumePrice) {
        CouponMemberRel newRel = new CouponMemberRel();
        if (consumePrice == null) {
            return newRel;
        }
        BigDecimal couponValue = rel.getCouponValue();
        BigDecimal discountPrice = BigDecimal.ZERO;
        newRel.setConsumePrice(consumePrice);
        switch (rel.getCouponType()) {
            case DISCOUNT: {
                discountPrice = NumberUtil.mul(consumePrice, NumberUtil.sub(1, NumberUtil.div(couponValue, 10, 2)));
                break;
            }
            case REDUCTION: {
                BigDecimal targetPrice = rel.getTargetPrice();
                if (consumePrice.compareTo(targetPrice) < 0) {
                    throw new ServiceException("未达到门槛金额");
                }
                discountPrice = couponValue;
                break;
            }
            case UNLIMITED: {
                if (consumePrice.compareTo(couponValue) < 0) {
                    discountPrice = couponValue;
                } else {
                    discountPrice = consumePrice;
                }
                break;
            }
            default:
        }
        discountPrice = NumberUtil.roundDown(discountPrice, 2);
        newRel.setDiscountPrice(discountPrice);
        newRel.setActuallyPrice(NumberUtil.max(consumePrice.subtract(discountPrice), BigDecimal.ZERO));
        return newRel;
    }

    @Transactional
    @Override
    public CouponMemberRel useCouponRel(CouponRelUseParam param) {
        return useCouponRel(param.getVerificateCode(), param.getConsumePrice(), null, VerificateModeEnum.CONSUMECODE);
    }

    private CouponMemberRel judgeCouponRel(String code, BigDecimal price, String orgId) {
        CouponMemberRel couponMemberRel = couponMemberRelMapper.normalSelectOne(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getVerificateCode, code)
                .eq(CouponMemberRel::getTenantId, MdcUtil.getCurrentTenantId())
        );
        couponMemberRel = stateHandler(couponMemberRel);
        if (couponMemberRel.getCouponMode() != CouponModeEnum.NORMAL) {
            throw new ServiceException(couponMemberRel.getCouponMode().getName() + "不能核销");
        }
        if (MdcUtil.getCurrentUser() != null) {
            if (StrUtil.isNotBlank(couponMemberRel.getDesignateOrgIds())
                    && !StrUtil.containsAny(couponMemberRel.getDesignateOrgIds(), StrUtil.split(MdcUtil.getCurrentOrgIds(), ","))) {
                throw new ServiceException("优惠券不属于本部门不可核销");
            }
        } else {
            if (orgId == null || (StrUtil.isNotBlank(couponMemberRel.getDesignateOrgIds())
                    && !StrUtil.contains(couponMemberRel.getDesignateOrgIds(), orgId))) {
                throw new ServiceException("优惠券不属于本部门不可核销");
            }
        }
        if (couponMemberRel.getVerificateState() != VerificateStateEnum.NOTCONSUME) {
            throw new ServiceException("优惠券" + couponMemberRel.getVerificateState().getName());
        }
        DateTime now = DateTime.now();
        if (!DateUtil.isIn(now, couponMemberRel.getEffectiveStartTime(), couponMemberRel.getEffectiveEndTime())) {
            throw new ServiceException("优惠券不在核销日期内");
        }
        if (couponMemberRel.getVerificateStartTime() != null && couponMemberRel.getVerificateEndTime() != null) {
            DateTime effectNow = DateUtil.parseTime(now.toTimeStr());
            DateTime start = DateUtil.dateNew(couponMemberRel.getVerificateStartTime());
            DateTime end = DateUtil.dateNew(couponMemberRel.getVerificateEndTime());
            if (!DateUtil.isIn(effectNow, start, end)) {
                throw new ServiceException("优惠券不在核销时间内");
            }
        }
        if (couponMemberRel.getTargetPrice() != null && price != null
                && NumberUtil.isLess(price, couponMemberRel.getTargetPrice())) {
            throw new ServiceException("核销金额未满足使用门槛");
        }
        return couponMemberRel;
    }

    @Override
    public CouponMemberRel useCouponRel(String code, BigDecimal price, String orgId, VerificateModeEnum verificateMode) {
        RedisUtil.transactionalLock("verificateCoupon" + code);
        CouponMemberRel rel = judgeCouponRel(code, price, orgId);
        MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("更新优惠券核销库存_" + rel.getCouponId(), () -> {
            updateCount(rel.getCouponId(), true);
            RedisUtil.syncLoad("changeCoupon" + rel.getCouponId(), () -> {
                increaseTotalVerificateCount(rel.getCouponId(), 1);
                return null;
            });
        }));
        CouponMemberRel update = calculatePrice(rel, price);
        update.setId(rel.getId());
        update.setVerificateMode(verificateMode);
        update.setVerificateTime(DateTime.now());
        update.setVerificateState(VerificateStateEnum.CONSUMED);
        update.setVerificaterId(MdcUtil.getCurrentUserId());
        update.setVerificaterName(MdcUtil.getCurrentUserName());
        update.setVerificaterOrgIds(MdcUtil.getCurrentOrgIds());
        update.setVerificaterOrgNames(MdcUtil.getCurrentOrgNames());
        int count = couponMemberRelMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("核销优惠券失败");
        }
        if (rel.getCreateSource() == CreateSourceEnum.EXCHANGE) {
            //更新兑换订单状态
            exchangeService.updateSuccessState(rel.getReceiveSourceId());
        }
        CouponMemberRel newRel = couponMemberRelMapper.selectById(rel.getId());
        changeMemberRelList(newRel.getCouponId(), newRel.getMemberId(), newRel);
        return newRel;
    }

    @Override
    public List<RelAnalysisResult> getRelAnalysis(CouponRelAnalysisGetParam param) {
        Map<Date, RelAnalysisResult> analysis = couponMemberRelMapper.selectMaps(new QueryWrapper<CouponMemberRel>()
                .select("date_format(verificate_time,'%Y-%m-%d') as verificate_date, count(1) AS verificate_count")
                .groupBy("verificate_date")
                .lambda()
                .ge(CouponMemberRel::getVerificateTime, param.getVerificateDateStart())
                .le(CouponMemberRel::getVerificateTime, param.getVerificateDateEnd())
                .eq(CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
                .eq(param.getId() != null, CouponMemberRel::getCouponId, param.getId())
        ).stream().map((map) -> BeanUtil.fillBeanWithMap(map, new RelAnalysisResult(), true, CopyOptions.create().ignoreError()))
                .collect(Collectors.toMap(RelAnalysisResult::getVerificateDate, result -> result));
        return DateUtil.rangeToList(param.getVerificateDateStart(), param.getVerificateDateEnd(), DateField.DAY_OF_YEAR).stream().map(dateTime -> {
            RelAnalysisResult result = analysis.get(dateTime);
            if (result == null) {
                result = new RelAnalysisResult();
                result.setVerificateDate(dateTime);
                result.setVerificateCount(0L);
            }
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<CouponInfoResult> pageCouponInfo(CouponInfoPageParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        List<Long> couponIdList = null;
        if (param.getCatalogId() != null) {
            couponIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                    .select(CatalogRel::getBusinessId)
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.COUPON)
                    .eq(CatalogRel::getCatalogId, param.getCatalogId())
            ).stream().map(CatalogRel::getBusinessId).collect(Collectors.toList());
        }
        DateTime now = DateTime.now();
        return couponInfoMapper.selectPage(param.page(), new QueryWrapper<CouponInfo>()
                .gt("inventory-receive_count", 0)
                .lambda()
                .eq(param.getCatalogId() != null && CollUtil.isEmpty(couponIdList), CouponInfo::getId, param.getCatalogId())
                .in(CollUtil.isNotEmpty(couponIdList), CouponInfo::getId, couponIdList)
                .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                .eq(CouponInfo::getCanShow, WhetherEnum.YES)
                .eq(CouponInfo::getCreateSource, CreateSourceEnum.NORMAL)
                .eq(CouponInfo::getState, CouponStateEnum.NORMAL)
                .le(CouponInfo::getReleaseTime, now)
                .and(j -> j.isNull(CouponInfo::getReceiveEndTime).or()
                        .ge(CouponInfo::getReceiveEndTime, now))
                .and(j -> j.isNull(CouponInfo::getEffectiveEndTime).or()
                        .ge(CouponInfo::getEffectiveEndTime, now))
                .orderByDesc(CouponInfo::getCreateTime)
        ).convert(couponInfo -> {
            couponInfo = stateHandler(couponInfo);
            CouponInfoResult result = new CouponInfoResult();
            BeanUtil.copyProperties(couponInfo, result);
            List<CouponMemberRel> relList = getMemberRelList(couponInfo.getId(), memberId);
            result.setMemberReceiveCount(relList.size());
            List<String> unIds = relList.stream().filter(rel -> rel.getVerificateState() == VerificateStateEnum.NOTCONSUME
                    && DateTime.now().isBeforeOrEquals(rel.getEffectiveEndTime()))
                    .map(rel -> Convert.toStr(rel.getId())).collect(Collectors.toList());
            List<String> ids = relList.stream().filter(rel -> rel.getVerificateState() == VerificateStateEnum.CONSUMED)
                    .map(rel -> Convert.toStr(rel.getId())).collect(Collectors.toList());
            result.setMemberUnVerificateCount(unIds.size());
            result.setMemberVerificateCount(ids.size());
            result.setMemberUnVerificateRelIdList(unIds);
            result.setMemberVerificateRelIdList(ids);
            result.setReceiveCount(getTotalReceiveCount(couponInfo.getId()));
            result.setVerificateCount(getTotalVerificateCount(couponInfo.getId()));
            return result;
        });
    }

    @Override
    public CouponInfoResult getCouponInfo(CouponGetParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        CouponInfo couponInfo = couponInfoMapper.selectOne(Wrappers.<CouponInfo>lambdaQuery()
                .eq(CouponInfo::getId, param.getId())
                .eq(CouponInfo::getCreateSource, CreateSourceEnum.NORMAL)
        );
        couponInfo = stateHandler(couponInfo);
        CouponInfoResult result = new CouponInfoResult();
        BeanUtil.copyProperties(couponInfo, result);
        List<CouponMemberRel> relList = getMemberRelList(couponInfo.getId(), memberId);
        result.setMemberReceiveCount(relList.size());
        List<String> unIds = relList.stream().filter(rel -> rel.getVerificateState() == VerificateStateEnum.NOTCONSUME
                && DateTime.now().isBeforeOrEquals(rel.getEffectiveEndTime()))
                .map(rel -> Convert.toStr(rel.getId())).collect(Collectors.toList());
        List<String> ids = relList.stream().filter(rel -> rel.getVerificateState() == VerificateStateEnum.CONSUMED)
                .map(rel -> Convert.toStr(rel.getId())).collect(Collectors.toList());
        result.setMemberUnVerificateCount(unIds.size());
        result.setMemberVerificateCount(ids.size());
        result.setMemberUnVerificateRelIdList(unIds);
        result.setMemberVerificateRelIdList(ids);
        result.setReceiveCount(getTotalReceiveCount(couponInfo.getId()));
        result.setVerificateCount(getTotalVerificateCount(couponInfo.getId()));
        return result;
    }

    private List<CouponMemberRel> getMemberRelList(Long couponId, Long memberId) {
        List<CouponMemberRel> relList = new ArrayList<>();
        if (memberId != null) {
            String key = "couponMemberRecord_" + couponId + memberId;
            if (RedisUtil.redis().hasKey(key)) {
                relList = RedisUtil.<Long, CouponMemberRel>hashRedis().values(key);
            } else {
                relList = couponMemberRelMapper.selectList(Wrappers.<CouponMemberRel>lambdaQuery()
                        .eq(CouponMemberRel::getMemberId, memberId)
                        .eq(CouponMemberRel::getCouponId, couponId)
                        .orderByAsc(CouponMemberRel::getCreateTime)
                );
                if (relList.size() > 0) {
                    Map<Long, CouponMemberRel> relMap = relList.stream().collect(Collectors.toMap(CouponMemberRel::getId, rel -> rel));
                    RedisUtil.<Long, CouponMemberRel>hashRedis().putAll(key, relMap);
                }
            }
            RedisUtil.redis().expire(key, 1, TimeUnit.DAYS);
        }
        return relList;
    }

    private void changeMemberRelList(Long couponId, Long memberId, CouponMemberRel rel) {
        String key = "couponMemberRecord_" + couponId + memberId;
        getMemberRelList(couponId, memberId);
        RedisUtil.<Long, CouponMemberRel>hashRedis().put(key, rel.getId(), rel);
    }

    @Override
    public String addRel(CouponGetParam param, Integer receiveCount, MemberInfo memberInfo, ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource) {
        //预先增加领取数(预扣库存)
        RedisUtil.syncLoad("changeCoupon" + param.getId(), () -> increaseTotalReceiveCount(param.getId(), receiveCount));
        RedisUtil.transactionalLock("changeCoupon" + param.getId() + memberInfo.getId());
        //事务commit后更新优惠券信息表的派发数
        MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("更新优惠券库存_" + param.getId(), () -> updateCount(param.getId(), false)));
        //事务rollback时把预先增加的领取数还原(恢复库存)
        MdcUtil.publishTransactionalEvent(AfterRollbackEvent.build("恢复优惠券库存缓存", () -> decreaseTotalReceiveCount(param.getId(), receiveCount)));
        CouponInfo couponInfo = couponInfoMapper.selectOne(Wrappers.<CouponInfo>lambdaQuery()
                .eq(CouponInfo::getId, param.getId())
                .eq(CouponInfo::getCreateSource, createSource)
        );
        couponInfo = stateHandler(couponInfo, receiveCount);
        if (couponInfo.getState() != CouponStateEnum.NORMAL
                && couponInfo.getState() != CouponStateEnum.EXPIRING) {
            throw new ServiceException("优惠券" + couponInfo.getState().getName() + ",领取失败");
        }
        if (couponInfo.getInventory() < getTotalReceiveCount(couponInfo.getId())) {
            throw new ServiceException("优惠券库存不足");
        }
        if (couponInfo.getReceiveLimit() > 0) {
            int relCount = couponMemberRelMapper.selectCount(Wrappers.<CouponMemberRel>lambdaQuery()
                    .eq(CouponMemberRel::getMemberId, memberInfo.getId())
                    .eq(CouponMemberRel::getCouponId, param.getId())
            );
            if (relCount + receiveCount > couponInfo.getReceiveLimit()) {
                throw new ServiceException("领取超过限制");
            }
        }
        List<CouponMemberRel> relList = new ArrayList<>();
        String appId = null;
        if (couponInfo.getCouponMode() == CouponModeEnum.ALIPAY) {
            if (StrUtil.isBlank(memberInfo.getUserId())) {
                throw new ServiceException("该会员不能领取支付宝优惠券");
            }
            CouponAlipayRecord record = couponAlipayRecordMapper.selectOne(Wrappers.<CouponAlipayRecord>lambdaQuery()
                    .eq(CouponAlipayRecord::getTemplateId, couponInfo.getCouponThirdId())
            );
            if (record == null) {
                throw new ServiceException("支付宝优惠券信息有误");
            }
            appId = record.getAppId();
        }
        for (int i = 0; i < receiveCount; i++) {
            CouponMemberRel rel = new CouponMemberRel();
            BeanUtil.copyProperties(couponInfo, rel, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
            if (StrUtil.isBlank(couponInfo.getDesignateOrgIds())) {
                rel.setDesignateOrgIds(couponInfo.getCreatorOrgIds());
                rel.setDesignateOrgNames(couponInfo.getCreatorOrgNames());
            }
            MdcUtil.setMemberInfo(rel, memberInfo);
            rel.setCouponId(couponInfo.getId());
            DateTime now = DateTime.now();
            rel.setReceiveTime(now);
            if (couponInfo.getEffectiveType() == EffectiveTypeEnum.AFTERDAYS) {
                rel.setEffectiveStartTime(now);
                rel.setEffectiveEndTime(now.offsetNew(DateField.DAY_OF_YEAR, couponInfo.getEffectiveDays()));
            }
            rel.setReceiveSource(receiveSource);
            rel.setReceiveSourceId(receiveSourceId);
            if (rel.getCouponMode() == CouponModeEnum.ALIPAY && StrUtil.isAllNotBlank(appId, memberInfo.getUserId())) {
                VoucherSendRequest request = new VoucherSendRequest();
                request.setAppId(appId);
                request.setOutBizNo(MdcUtil.getSnowflakeIdStr());
                request.setTemplateId(rel.getCouponThirdId());
                request.setUserId(memberInfo.getUserId());
                request.setMemo(rel.getCouponName());
                AlipayMarketingVoucherSendResponse response = alipayOpenPlatformService.sendVoucher(request);
                rel.setRelThirdId(response.getVoucherId());
            } else if (rel.getCouponMode() == CouponModeEnum.NORMAL) {
                String time = Convert.toStr(DateTime.now().getTime());
                String random = RandomUtil.randomNumbers(4);
                String memberIdStr = Convert.toStr(memberInfo.getId());
                String verificateCode = StrUtil.subSuf(time, time.length() - 6) + random + StrUtil.subSuf(memberIdStr, memberIdStr.length() - 4);
                rel.setVerificateCode(verificateCode);
            }
            rel.setVerificateState(VerificateStateEnum.NOTCONSUME);
            relList.add(rel);
        }
        int count = couponMemberRelMapper.insertAllBatch(relList);
        if (count != receiveCount) {
            throw new ServiceException("领取失败");
        }
        for (CouponMemberRel rel : relList) {
            changeMemberRelList(param.getId(), memberInfo.getId(), rel);
        }
        String relId;
        if (relList.get(0).getCouponMode() == CouponModeEnum.NORMAL) {
            relId = relList.get(0).getId().toString();
        } else {
            relId = relList.get(0).getRelThirdId();
        }
        return relId;
    }

    @Transactional
    @Override
    public BaseResult<String> addRel(CouponGetParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        int count = 1;
        double d = RedisUtil.zSetRedis().incrementScore("couponInventory", param.getId(), -count);
        if (d < 0) {
            RedisUtil.zSetRedis().incrementScore("couponInventory", param.getId(), count);
            throw new ServiceException("手慢了,已被领完了");
        }
        RedisUtil.redis().expire("couponInventory", 30, TimeUnit.DAYS);
        try {
            String relId = addRel(param, count, memberInfo, ReceiveSourceEnum.COUPON, null, CreateSourceEnum.NORMAL);
            return new BaseResult<String>().setResult(relId);
        } catch (Exception e) {
            RedisUtil.zSetRedis().incrementScore("couponInventory", param.getId(), count);
            throw e;
        }

    }

    @Override
    public void revokedRel(ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource, boolean enforce) {
        List<CouponMemberRel> relList = couponMemberRelMapper.selectList(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getReceiveSourceId, receiveSourceId)
                .eq(CouponMemberRel::getReceiveSource, receiveSource)
                .eq(CouponMemberRel::getCreateSource, createSource)
        );
        for (CouponMemberRel rel : relList) {
            rel = stateHandler(rel);
            if (!enforce) {
                if (rel.getVerificateState() == VerificateStateEnum.CONSUMED) {
                    throw new ServiceException("此优惠券不能撤回");
                }
            }
            if (rel.getVerificateState() != VerificateStateEnum.REVOKED) {
                CouponMemberRel update = new CouponMemberRel();
                update.setId(rel.getId());
                update.setVerificateState(VerificateStateEnum.REVOKED);
                int count = couponMemberRelMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("撤回优惠券失败");
                }
            }
        }
    }

    @Override
    public IPage<CouponMemberRel> pageCouponInfoRel(CouponInfoRelPageParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return couponMemberRelMapper.selectPage(param.page(), Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getMemberId, memberId)
                .and(param.getVerificateState() == VerificateStateEnum.EXPIRED,
                        i -> i.eq(CouponMemberRel::getVerificateState, VerificateStateEnum.NOTCONSUME)
                                .lt(CouponMemberRel::getEffectiveEndTime, DateTime.now()))
                .and(param.getVerificateState() == VerificateStateEnum.NOTCONSUME,
                        i -> i.eq(CouponMemberRel::getVerificateState, VerificateStateEnum.NOTCONSUME)
                                .ge(CouponMemberRel::getEffectiveEndTime, DateTime.now()))
                .eq(param.getVerificateState() == VerificateStateEnum.CONSUMED, CouponMemberRel::getVerificateState, VerificateStateEnum.CONSUMED)
                .orderByDesc(CouponMemberRel::getCreateTime)
        ).convert(this::stateHandler);
    }

    @Override
    public CouponMemberRel getCouponInfoRel(CouponRelGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        CouponMemberRel rel = couponMemberRelMapper.selectOne(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(StrUtil.isBlank(param.getVerificateCode()) || param.getId() != null, CouponMemberRel::getId, param.getId())
                .eq(StrUtil.isNotBlank(param.getVerificateCode()) || param.getId() == null, CouponMemberRel::getVerificateCode, param.getVerificateCode())
                .eq(CouponMemberRel::getMemberId, memberId)
        );
        return stateHandler(rel);
    }

    @Transactional
    @Override
    public CouponMemberRel useCouponRel(CouponMemberRelUseParam param) {
        return useCouponRel(param.getVerificateCode(), param.getConsumePrice(), Convert.toStr(param.getOrgId()), VerificateModeEnum.POINTOFSALES);
    }

    @Override
    public IPage<CouponInfoResult> pageShopCoupon(CouponInfoShopPageParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        List<Long> couponIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                .select(CatalogRel::getBusinessId)
                .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.COUPON)
        ).stream().map(CatalogRel::getBusinessId).collect(Collectors.toList());

        DateTime now = DateTime.now();
        return couponInfoMapper.selectPage(param.page(), new QueryWrapper<CouponInfo>()
                .gt("inventory-receive_count", 0)
                .lambda()
                .like(CouponInfo::getDesignateOrgIds, param.getRelationId())
                .in(CollUtil.isNotEmpty(couponIdList), CouponInfo::getId, couponIdList)
                .eq(CouponInfo::getAuditState, AuditStateEnum.APPROVE)
                .eq(CouponInfo::getCanShow, WhetherEnum.YES)
                .eq(CouponInfo::getCreateSource, CreateSourceEnum.NORMAL)
                .eq(CouponInfo::getState, CouponStateEnum.NORMAL)
                .le(CouponInfo::getReleaseTime, now)
                .and(j -> j.isNull(CouponInfo::getEffectiveEndTime).or()
                        .ge(CouponInfo::getEffectiveEndTime, now))
                .orderByDesc(CouponInfo::getCreateTime)
        ).convert(couponInfo -> {
            couponInfo = stateHandler(couponInfo);
            CouponInfoResult result = new CouponInfoResult();
            BeanUtil.copyProperties(couponInfo, result);
            List<CouponMemberRel> relList = getMemberRelList(couponInfo.getId(), memberId);
            result.setMemberReceiveCount(relList.size());
            List<String> unIds = relList.stream().filter(rel -> rel.getVerificateState() == VerificateStateEnum.NOTCONSUME
                    && DateTime.now().isBeforeOrEquals(rel.getEffectiveEndTime()))
                    .map(rel -> Convert.toStr(rel.getId())).collect(Collectors.toList());
            List<String> ids = relList.stream().filter(rel -> rel.getVerificateState() == VerificateStateEnum.CONSUMED)
                    .map(rel -> Convert.toStr(rel.getId())).collect(Collectors.toList());
            result.setMemberUnVerificateCount(unIds.size());
            result.setMemberVerificateCount(ids.size());
            result.setMemberUnVerificateRelIdList(unIds);
            result.setMemberVerificateRelIdList(ids);
            result.setReceiveCount(getTotalReceiveCount(couponInfo.getId()));
            result.setVerificateCount(getTotalVerificateCount(couponInfo.getId()));
            return result;
        });
    }
}
