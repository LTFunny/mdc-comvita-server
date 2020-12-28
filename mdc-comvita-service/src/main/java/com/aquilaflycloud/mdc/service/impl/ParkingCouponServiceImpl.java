package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.component.event.AfterCommitEvent;
import com.aquilaflycloud.mdc.component.event.AfterRollbackEvent;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.*;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.message.MemberErrorEnum;
import com.aquilaflycloud.mdc.message.ParkingErrorEnum;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.parking.*;
import com.aquilaflycloud.mdc.param.exchange.GoodsAuditParam;
import com.aquilaflycloud.mdc.param.parking.*;
import com.aquilaflycloud.mdc.result.parking.*;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.aquilaflycloud.mdc.service.ParkingCouponService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.ajbcloud.req.CouponsAddReq;
import com.aquilaflycloud.ajbcloud.resp.AjbCloudResp;
import com.aquilaflycloud.ajbcloud.util.AjbCloudUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * ParkingCouponServiceImpl
 *
 * @author star
 * @date 2020-01-13
 */
@Slf4j
@Service
public class ParkingCouponServiceImpl implements ParkingCouponService {
    @Resource
    private ParkingCouponOrderRecordMapper parkingCouponOrderRecordMapper;
    @Resource
    private ParkingCouponMapper parkingCouponMapper;
    @Resource
    private ParkingCouponMemberRelMapper parkingCouponMemberRelMapper;
    @Resource
    private ParkingUnlicensedCarRecordMapper parkingUnlicensedCarRecordMapper;
    @Resource
    private ParkingMemberCarMapper parkingMemberCarMapper;
    @Resource
    private ParkingAjbEnterRecordMapper parkingAjbEnterRecordMapper;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private ExchangeService exchangeService;

    private ParkingCoupon stateHandler(ParkingCoupon parkingCoupon) {
        if (parkingCoupon == null) {
            throw new ServiceException("停车券不存在");
        }
        DateTime now = DateTime.now();
        if (parkingCoupon.getState() == CouponStateEnum.NORMAL) {
            if (parkingCoupon.getInventoryType() == InventoryTypeEnum.QUOTA) {
                if (getTotalDistributeWorth(parkingCoupon.getId()).compareTo(parkingCoupon.getInventory()) >= 0) {
                    parkingCoupon.setState(CouponStateEnum.SOLDOUT);
                } else if (parkingCoupon.getEffectiveType() == EffectiveTypeEnum.CUSTOM) {
                    if (parkingCoupon.getEffectiveEndTime() != null && now.isAfter(parkingCoupon.getEffectiveEndTime())) {
                        parkingCoupon.setState(CouponStateEnum.EXPIRED);
                    }
                }
            } else if (parkingCoupon.getInventoryType() == InventoryTypeEnum.AMOUNT) {
                if (getTotalDistributeCount(parkingCoupon.getId()).compareTo(parkingCoupon.getInventory()) >= 0) {
                    parkingCoupon.setState(CouponStateEnum.SOLDOUT);
                } else if (parkingCoupon.getEffectiveType() == EffectiveTypeEnum.CUSTOM) {
                    if (parkingCoupon.getEffectiveEndTime() != null && now.isAfter(parkingCoupon.getEffectiveEndTime())) {
                        parkingCoupon.setState(CouponStateEnum.EXPIRED);
                    }
                }
            }
        }
        return parkingCoupon;
    }

    private ParkingCouponMemberRel stateHandler(ParkingCouponMemberRel rel) {
        if (rel == null) {
            throw new ServiceException("停车券记录不存在");
        }
        DateTime now = DateTime.now();
        if (rel.getConsumeState() != ConsumeStateEnum.CONSUMED && rel.getConsumeState() != ConsumeStateEnum.REVOKED) {
            if (rel.getEffectiveType() != EffectiveTypeEnum.EVERLASTING) {
                if (rel.getEffectiveStartTime() != null && now.before(rel.getEffectiveStartTime())) {
                    rel.setConsumeState(ConsumeStateEnum.NOTACTIVE);
                }
                if (rel.getEffectiveEndTime() != null && now.isAfter(rel.getEffectiveEndTime())) {
                    rel.setConsumeState(ConsumeStateEnum.EXPIRED);
                }
            }
        }
        return rel;
    }

    @Override
    public BigDecimal getTotalDistributeWorth(Long couponId, Boolean... reload) {
        String lock = "parkingCouponTotalDistributeWorthLock" + couponId;
        String key = "parkingCouponTotalDistributeWorth" + couponId;
        if (reload.length > 0 && reload[0]) {
            RedisUtil.redis().delete(key);
        }
        return RedisUtil.syncValueGet(lock, key, () -> parkingCouponMemberRelMapper.selectMaps(new QueryWrapper<ParkingCouponMemberRel>()
                        .select("coalesce(sum(coupon_total_worth), 0) distribute_worth")
                        .lambda()
                        .eq(ParkingCouponMemberRel::getCouponId, couponId)
                ).stream().map(map -> Convert.toBigDecimal(map.get("distribute_worth"))).collect(Collectors.toList()).get(0)
        );
    }

    @Override
    public BigDecimal getTotalDistributeCount(Long couponId, Boolean... reload) {
        String lock = "parkingCouponTotalDistributeCountLock" + couponId;
        String key = "parkingCouponTotalDistributeCount" + couponId;
        if (reload.length > 0 && reload[0]) {
            RedisUtil.redis().delete(key);
        }
        return RedisUtil.syncValueGet(lock, key, () -> parkingCouponMemberRelMapper.selectMaps(new QueryWrapper<ParkingCouponMemberRel>()
                        .select("coalesce(sum(coupon_count), 0) distribute_count")
                        .lambda()
                        .eq(ParkingCouponMemberRel::getCouponId, couponId)
                ).stream().map(map -> Convert.toBigDecimal(map.get("distribute_count"))).collect(Collectors.toList()).get(0)
        );
    }

    private BigDecimal increaseTotalDistributeWorth(Long couponId, BigDecimal increase) {
        String key = "parkingCouponTotalDistributeWorth" + couponId;
        BigDecimal total = getTotalDistributeWorth(couponId);
        BigDecimal result = NumberUtil.add(total, increase);
        RedisUtil.valueRedis().set(key, result);
        return result;
    }

    private BigDecimal increaseTotalDistributeCount(Long couponId, BigDecimal increase) {
        String key = "parkingCouponTotalDistributeCount" + couponId;
        BigDecimal total = getTotalDistributeCount(couponId);
        BigDecimal result = NumberUtil.add(total, increase);
        RedisUtil.valueRedis().set(key, result);
        return result;
    }

    private void decreaseTotalDistributeWorth(Long couponId, BigDecimal decrease) {
        String key = "parkingCouponTotalDistributeWorth" + couponId;
        BigDecimal total = getTotalDistributeWorth(couponId);
        RedisUtil.valueRedis().set(key, NumberUtil.sub(total, decrease));
    }

    private void decreaseTotalDistributeCount(Long couponId, BigDecimal decrease) {
        String key = "parkingCouponTotalDistributeCount" + couponId;
        BigDecimal total = getTotalDistributeCount(couponId);
        RedisUtil.valueRedis().set(key, NumberUtil.sub(total, decrease));
    }

    @Override
    public void updateDistributeCount(Long couponId, InventoryTypeEnum inventoryType) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            RedisUtil.syncLoad("updateExchangeCountLock" + couponId, () -> {
                BigDecimal distribute = BigDecimal.ZERO;
                if (inventoryType == InventoryTypeEnum.QUOTA) {
                    distribute = parkingCouponMemberRelMapper.normalSelectMaps(new QueryWrapper<ParkingCouponMemberRel>()
                            .select("coalesce(sum(coupon_total_worth), 0) distribute_worth")
                            .lambda()
                            .eq(ParkingCouponMemberRel::getCouponId, couponId)
                    ).stream().map(map -> Convert.toBigDecimal(map.get("distribute_worth"))).collect(Collectors.toList()).get(0);
                } else if (inventoryType == InventoryTypeEnum.AMOUNT) {
                    distribute = parkingCouponMemberRelMapper.normalSelectMaps(new QueryWrapper<ParkingCouponMemberRel>()
                            .select("coalesce(sum(coupon_count), 0) distribute_count")
                            .lambda()
                            .eq(ParkingCouponMemberRel::getCouponId, couponId)
                    ).stream().map(map -> Convert.toBigDecimal(map.get("distribute_count"))).collect(Collectors.toList()).get(0);
                }
                parkingCouponMapper.normalUpdate(null, Wrappers.<ParkingCoupon>lambdaUpdate()
                        .set(ParkingCoupon::getDistribute, distribute)
                        .eq(ParkingCoupon::getId, couponId)
                );
                return null;
            });
        });
    }

    @Override
    public IPage<ParkingCouponOrderRecordResult> pageCouponRecord(CouponRecordPageParam param) {
        return parkingCouponOrderRecordMapper.selectPage(param.page(), Wrappers.<ParkingCouponOrderRecord>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getCouponCode()), ParkingCouponOrderRecord::getCouponCode, param.getCouponCode())
                .like(StrUtil.isNotBlank(param.getCouponName()), ParkingCouponOrderRecord::getCouponName, param.getCouponName())
                .like(StrUtil.isNotBlank(param.getDesignateOrgNames()), ParkingCouponOrderRecord::getDesignateOrgNames, param.getDesignateOrgNames())
                .eq(param.getCouponType() != null, ParkingCouponOrderRecord::getCouponType, param.getCouponType())
                .eq(param.getCreateSource() != null, ParkingCouponOrderRecord::getCreateSource, param.getCreateSource())
                .orderByDesc(ParkingCouponOrderRecord::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(record -> {
            ParkingCouponOrderRecordResult result = Convert.convert(ParkingCouponOrderRecordResult.class, record);
            result.setReceiveLimit(JSONUtil.toBean(record.getReceiveLimitContent(), CouponLimit.class));
            result.setUseLimit(JSONUtil.toBean(record.getUseLimitContent(), CouponLimit.class));
            return result;
        });
    }

    @Override
    public Long addCouponRecord(CouponRecordAddParam param, CreateSourceEnum createSource) {
        if (param.getCouponType() == CouponTypeEnum.FREE) {
            if (param.getInventoryType() != InventoryTypeEnum.AMOUNT) {
                throw new ServiceException("全免类型的库存类型只能为数量");
            }
        } else if (param.getCouponType() != CouponTypeEnum.FREE && param.getInventoryType() == InventoryTypeEnum.AMOUNT
                && param.getCouponWorth() == null) {
            throw new ServiceException("优惠值不能为空");
        }
        ParkingCouponOrderRecord record = new ParkingCouponOrderRecord();
        BeanUtil.copyProperties(param, record);
        MdcUtil.setOrganInfo(record);
        if (param.getCouponType() == CouponTypeEnum.FREE || param.getInventoryType() == InventoryTypeEnum.QUOTA) {
            record.setCouponWorth(null);
        }
        if (param.getReceiveLimitType() == LimitTypeEnum.UNLIMITED) {
            record.setReceiveLimitContent(null);
        }
        if (param.getUseLimitType() == LimitTypeEnum.UNLIMITED) {
            record.setUseLimitContent(null);
        }
        if (param.getEffectiveType() != EffectiveTypeEnum.CUSTOM) {
            record.setEffectiveStartTime(null);
            record.setEffectiveEndTime(null);
        }
        record.setCouponId(MdcUtil.getSnowflakeId());
        record.setCreateSource(createSource);
        record.setAuditState(AuditStateEnum.PENDING);
        record.setCouponCode(MdcUtil.getTenantIncIdStr("parkingCoupon", DateTime.now().toString("yyMMdd"), 4));
        record.setReceiveLimitContent(JSONUtil.toJsonStr(param.getReceiveLimitContent()));
        record.setUseLimitContent(JSONUtil.toJsonStr(param.getUseLimitContent()));
        int count = parkingCouponOrderRecordMapper.insert(record);
        if (count <= 0) {
            throw new ServiceException("新增停车券记录失败");
        }
        return record.getCouponId();
    }

    @Override
    public void addCouponRecord(CouponRecordAddParam param) {
        addCouponRecord(param, CreateSourceEnum.NORMAL);
    }

    @Override
    public void addInventory(CouponRecordInventoryAddParam param, CreateSourceEnum createSource) {
        ParkingCoupon parkingCoupon = parkingCouponMapper.selectById(param.getCouponId());
        if (parkingCoupon == null && createSource == CreateSourceEnum.EXCHANGE) {
            throw new ServiceException("停车券还未审核创建,不能修改库存");
        }
        stateHandler(parkingCoupon);
        ParkingCouponOrderRecord record = new ParkingCouponOrderRecord();
        BeanUtil.copyProperties(parkingCoupon, record, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        record.setAuditState(AuditStateEnum.PENDING);
        record.setCouponId(parkingCoupon.getId());
        record.setInventoryIncrease(param.getInventoryIncrease());
        int count = parkingCouponOrderRecordMapper.insert(record);
        if (count <= 0) {
            throw new ServiceException("新增停车券增加库存记录失败");
        }
    }

    @Override
    public void addInventory(CouponRecordInventoryAddParam param) {
        addInventory(param, CreateSourceEnum.NORMAL);
    }

    @Override
    @Transactional
    public void auditCouponRecord(CouponRecordParam param) {
        RedisUtil.transactionalLock("auditParkingCoupon" + param.getId());
        ParkingCouponOrderRecord record = parkingCouponOrderRecordMapper.selectById(param.getId());
        if (record == null) {
            throw new ServiceException("停车券记录不存在");
        }
        RedisUtil.transactionalLock("changeParkingCoupon" + record.getCouponId());
        if (record.getAuditState() == AuditStateEnum.PENDING) {
            ParkingCouponOrderRecord update = new ParkingCouponOrderRecord();
            update.setId(param.getId());
            update.setAuditState(param.getIsApprove() ? AuditStateEnum.APPROVE : AuditStateEnum.REJECT);
            update.setAuditRemark(param.getAuditRemark());
            if (param.getIsApprove()) {
                ParkingCoupon parkingCoupon = parkingCouponMapper.selectById(record.getCouponId());
                int count;
                if (parkingCoupon != null) {
                    ParkingCoupon updateCoupon = new ParkingCoupon();
                    updateCoupon.setId(parkingCoupon.getId());
                    updateCoupon.setInventory(NumberUtil.add(parkingCoupon.getInventory(), record.getInventoryIncrease()));
                    count = parkingCouponMapper.updateById(updateCoupon);
                } else {
                    parkingCoupon = new ParkingCoupon();
                    BeanUtil.copyProperties(record, parkingCoupon, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
                    parkingCoupon.setId(record.getCouponId());
                    parkingCoupon.setState(CouponStateEnum.NORMAL);
                    parkingCoupon.setInventory(record.getInventoryIncrease());
                    parkingCoupon.setDistribute(BigDecimal.ZERO);
                    parkingCoupon.setCreatorId(record.getCreatorId());
                    parkingCoupon.setCreatorName(record.getCreatorName());
                    parkingCoupon.setCreatorOrgIds(record.getCreatorOrgIds());
                    parkingCoupon.setCreatorOrgNames(record.getCreatorOrgNames());
                    /*String[] designateOrgIds = StrUtil.split(parkingCoupon.getDesignateOrgIds(), ",");
                    String[] designateOrgNames = StrUtil.split(parkingCoupon.getDesignateOrgNames(), ",");
                    String[] designateIds = StrUtil.split(record.getCreatorOrgIds(), ",");
                    String[] designateNames = StrUtil.split(record.getCreatorOrgNames(), ",");
                    if (!ArrayUtil.containsAny(designateOrgIds, designateIds)) {
                        String oldDesignateOrgIds = parkingCoupon.getDesignateOrgIds();
                        parkingCoupon.setDesignateOrgIds(StrUtil.isBlank(oldDesignateOrgIds) ?
                                record.getCreatorOrgIds() : oldDesignateOrgIds + "," + record.getCreatorOrgIds());
                    }
                    if (!ArrayUtil.containsAny(designateOrgNames, designateNames)) {
                        String oldDesignateOrgNames = parkingCoupon.getDesignateOrgNames();
                        parkingCoupon.setDesignateOrgNames(StrUtil.isBlank(oldDesignateOrgNames) ?
                                record.getCreatorOrgNames() : oldDesignateOrgNames + "," + record.getCreatorOrgNames());
                    }*/
                    parkingCoupon.setTenantId(record.getTenantId());
                    parkingCoupon.setSubTenantId(record.getSubTenantId());
                    count = parkingCouponMapper.insert(parkingCoupon);
                }
                if (count <= 0) {
                    throw new ServiceException("审核创券失败");
                }
            }
            int count = parkingCouponOrderRecordMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("审核失败");
            }
            if (record.getCreateSource() == CreateSourceEnum.EXCHANGE) {
                GoodsAuditParam auditParam = new GoodsAuditParam();
                BeanUtil.copyProperties(param, auditParam);
                exchangeService.auditGoods(auditParam, GoodsTypeEnum.PARKING, record.getCouponId());
            }
        } else {
            throw new ServiceException("该记录不需审核");
        }
    }

    @Override
    public IPage<ParkingCouponResult> pageCoupon(CouponPageParam param) {
        DateTime now = DateTime.now();
        return parkingCouponMapper.selectPage(param.page(), new QueryWrapper<ParkingCoupon>()
                .and(param.getState() == CouponStateEnum.NORMAL, i -> i.eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-distribute", 0)
                        .lambda()
                        .and(j -> j.and(k -> k.ge(ParkingCoupon::getEffectiveEndTime, now)
                                .eq(ParkingCoupon::getEffectiveType, EffectiveTypeEnum.CUSTOM))
                                .or().ne(ParkingCoupon::getEffectiveType, EffectiveTypeEnum.CUSTOM)))
                .and(param.getState() == CouponStateEnum.SOLDOUT, i -> i.eq("state", CouponStateEnum.NORMAL)
                        .le("inventory-distribute", 0))
                .and(param.getState() == CouponStateEnum.EXPIRED, i -> i.eq("state", CouponStateEnum.NORMAL)
                        .gt("inventory-distribute", 0)
                        .lambda()
                        .eq(ParkingCoupon::getEffectiveType, EffectiveTypeEnum.CUSTOM)
                        .lt(ParkingCoupon::getEffectiveEndTime, now))
                .lambda()
                .eq(StrUtil.isNotBlank(param.getCouponCode()), ParkingCoupon::getCouponCode, param.getCouponCode())
                .like(StrUtil.isNotBlank(param.getCouponName()), ParkingCoupon::getCouponName, param.getCouponName())
                .like(StrUtil.isNotBlank(param.getDesignateOrgNames()), ParkingCoupon::getDesignateOrgNames, param.getDesignateOrgNames())
                .eq(param.getCouponType() != null, ParkingCoupon::getCouponType, param.getCouponType())
                .eq(ParkingCoupon::getCreateSource, CreateSourceEnum.NORMAL)
                .ge(param.getCreateTimeStart() != null, ParkingCoupon::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ParkingCoupon::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getState() == CouponStateEnum.DISABLE, ParkingCoupon::getState, CouponStateEnum.DISABLE)
                .orderByDesc(ParkingCoupon::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(record -> {
            record = stateHandler(record);
            ParkingCouponResult result = Convert.convert(ParkingCouponResult.class, record);
            result.setReceiveLimit(JSONUtil.toBean(record.getReceiveLimitContent(), CouponLimit.class));
            result.setUseLimit(JSONUtil.toBean(record.getUseLimitContent(), CouponLimit.class));
            if (record.getInventoryType() == InventoryTypeEnum.QUOTA) {
                result.setDistribute(getTotalDistributeWorth(record.getId()));
            } else if (record.getInventoryType() == InventoryTypeEnum.AMOUNT) {
                result.setDistribute(getTotalDistributeCount(record.getId()));
            }
            return result;
        });
    }

    @Override
    public ParkingCoupon getCoupon(Long couponId) {
        ParkingCoupon parkingCoupon = parkingCouponMapper.selectById(couponId);
        return stateHandler(parkingCoupon);
    }

    @Override
    public ParkingCouponResult getCoupon(CouponGetParam param) {
        ParkingCoupon parkingCoupon = parkingCouponMapper.selectById(param.getId());
        parkingCoupon = stateHandler(parkingCoupon);
        ParkingCouponResult result = Convert.convert(ParkingCouponResult.class, parkingCoupon);
        result.setReceiveLimit(JSONUtil.toBean(parkingCoupon.getReceiveLimitContent(), CouponLimit.class));
        result.setUseLimit(JSONUtil.toBean(parkingCoupon.getUseLimitContent(), CouponLimit.class));
        if (parkingCoupon.getInventoryType() == InventoryTypeEnum.QUOTA) {
            result.setDistribute(getTotalDistributeWorth(parkingCoupon.getId(), true));
        } else if (parkingCoupon.getInventoryType() == InventoryTypeEnum.AMOUNT) {
            result.setDistribute(getTotalDistributeCount(parkingCoupon.getId(), true));
        }
        return result;
    }

    @Override
    public void toggleCoupon(CouponGetParam param) {
        RedisUtil.syncLoad("changeParkingCoupon" + param.getId(), () -> {
            ParkingCoupon parkingCoupon = parkingCouponMapper.selectOne(Wrappers.<ParkingCoupon>lambdaQuery()
                    .eq(ParkingCoupon::getId, param.getId())
                    .eq(ParkingCoupon::getCreateSource, CreateSourceEnum.NORMAL)
            );
            if (parkingCoupon == null) {
                throw new ServiceException("停车券不存在");
            }
            ParkingCoupon update = new ParkingCoupon();
            update.setId(param.getId());
            update.setState(parkingCoupon.getState() == CouponStateEnum.NORMAL ? CouponStateEnum.DISABLE : CouponStateEnum.NORMAL);
            int count = parkingCouponMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("操作失败");
            }
            return null;
        });
    }

    @Override
    public void editCoupon(CouponEditParam param, CreateSourceEnum createSource) {
        RedisUtil.transactionalLock("changeParkingCoupon" + param.getId());
        ParkingCoupon parkingCoupon = parkingCouponMapper.selectOne(Wrappers.<ParkingCoupon>lambdaQuery()
                .eq(ParkingCoupon::getId, param.getId())
                .eq(ParkingCoupon::getCreateSource, createSource)
        );
        if (parkingCoupon == null && createSource == CreateSourceEnum.EXCHANGE) {
            ParkingCouponOrderRecord record = parkingCouponOrderRecordMapper.selectOne(Wrappers.<ParkingCouponOrderRecord>lambdaQuery()
                    .eq(ParkingCouponOrderRecord::getCouponId, param.getId())
                    .orderByDesc(ParkingCouponOrderRecord::getCreateTime)
                    .last("limit 1")
            );
            if (record.getAuditState() == AuditStateEnum.REJECT) {
                throw new ServiceException("停车券审核不通过,不能修改");
            }
            if (record.getAuditState() == AuditStateEnum.PENDING) {
                ParkingCouponOrderRecord update = new ParkingCouponOrderRecord();
                BeanUtil.copyProperties(param, update);
                MdcUtil.setOrganInfo(update);
                update.setId(record.getId());
                update.setReceiveLimitContent(JSONUtil.toJsonStr(param.getReceiveLimitContent()));
                update.setUseLimitContent(JSONUtil.toJsonStr(param.getUseLimitContent()));
                parkingCouponOrderRecordMapper.updateById(update);
                return;
            }
        }
        stateHandler(parkingCoupon);
        LambdaUpdateWrapper<ParkingCoupon> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ParkingCoupon::getId, parkingCoupon.getId());
        ParkingCoupon update = new ParkingCoupon();
        BeanUtil.copyProperties(param, update);
        update.setId(null);
        if (param.getReceiveLimitType() == LimitTypeEnum.UNLIMITED) {
            updateWrapper.set(ParkingCoupon::getReceiveLimitContent, null);
        } else {
            update.setReceiveLimitContent(JSONUtil.toJsonStr(param.getReceiveLimitContent()));
        }
        if (param.getUseLimitType() == LimitTypeEnum.UNLIMITED) {
            updateWrapper.set(ParkingCoupon::getUseLimitContent, null);
        } else {
            update.setUseLimitContent(JSONUtil.toJsonStr(param.getUseLimitContent()));
        }
        if (param.getEffectiveType() != EffectiveTypeEnum.CUSTOM) {
            updateWrapper.set(ParkingCoupon::getEffectiveStartTime, null);
            updateWrapper.set(ParkingCoupon::getEffectiveEndTime, null);
        }
        if (param.getDesignateOrgIds() != null && !StrUtil.equals(param.getDesignateOrgIds(), parkingCoupon.getDesignateOrgIds())) {
            //若designateOrgIds为空字符串,则设置为null
            if (StrUtil.isBlank(param.getDesignateOrgIds())) {
                updateWrapper.set(ParkingCoupon::getDesignateOrgIds, null);
                updateWrapper.set(ParkingCoupon::getDesignateOrgNames, null);
                parkingCouponMemberRelMapper.update(new ParkingCouponMemberRel(), Wrappers.<ParkingCouponMemberRel>lambdaUpdate()
                        .eq(ParkingCouponMemberRel::getCouponId, param.getId())
                        .ne(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                        .set(ParkingCouponMemberRel::getDesignateOrgIds, parkingCoupon.getCreatorOrgIds())
                        .set(ParkingCouponMemberRel::getDesignateOrgNames, parkingCoupon.getCreatorOrgNames())
                );
            } else {
                MdcUtil.setOrganInfo(update);
                ParkingCouponMemberRel relUpdate = new ParkingCouponMemberRel();
                relUpdate.setDesignateOrgIds(param.getDesignateOrgIds());
                MdcUtil.setOrganInfo(relUpdate);
                parkingCouponMemberRelMapper.update(relUpdate, Wrappers.<ParkingCouponMemberRel>lambdaUpdate()
                        .eq(ParkingCouponMemberRel::getCouponId, param.getId())
                        .ne(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                );
            }
        } else if (param.getDesignateOrgIds() == null) {
            update.setDesignateOrgIds(null);
            update.setDesignateOrgNames(null);
        }
        int count = parkingCouponMapper.update(update, updateWrapper);
        if (count <= 0) {
            throw new ServiceException("编辑停车券失败");
        }
    }

    @Transactional
    @Override
    public void editCoupon(CouponEditParam param) {
        editCoupon(param, CreateSourceEnum.NORMAL);
    }

    @Override
    public void deleteCoupon(CouponGetParam param) {
        RedisUtil.syncLoad("changeParkingCoupon" + param.getId(), () -> {
            ParkingCoupon parkingCoupon = parkingCouponMapper.selectOne(Wrappers.<ParkingCoupon>lambdaQuery()
                    .eq(ParkingCoupon::getId, param.getId())
                    .eq(ParkingCoupon::getCreateSource, CreateSourceEnum.NORMAL)
            );
            parkingCoupon = stateHandler(parkingCoupon);
            if (parkingCoupon.getState() != CouponStateEnum.DISABLE) {
                throw new ServiceException("只能删除停用停车券");
            }
            int count = parkingCouponMapper.deleteById(param.getId());
            if (count <= 0) {
                throw new ServiceException("删除停车券失败");
            }
            return null;
        });
    }

    @Transactional
    @Override
    public void addCouponMemberRel(CouponMemberRelAddParam param) {
        addCouponMemberRel(param, ReceiveSourceEnum.BACKEND, null, CreateSourceEnum.NORMAL);
    }

    @Override
    public ParkingCouponMemberRel addCouponMemberRel(CouponMemberRelAddParam param, ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource) {
        RedisUtil.transactionalLock("changeParkingCoupon" + param.getCouponId());
        if (param.getAutoConsume()) {
            if (StrUtil.isAllBlank(param.getCardId(), param.getCarNo(), param.getShortCardId())) {
                throw new ServiceException("自动核销停车券时,车辆信息不能为空");
            }
        }
        //短车卡号转换安居宝车卡号
        if (StrUtil.isNotBlank(param.getShortCardId()) && StrUtil.isBlank(param.getCardId())) {
            List<ParkingUnlicensedCarRecord> recordList = parkingUnlicensedCarRecordMapper.selectList(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                    .eq(ParkingUnlicensedCarRecord::getShortCardId, param.getShortCardId())
                    .orderByDesc(ParkingUnlicensedCarRecord::getCreateTime)
                    .last("limit 1")
            );
            if (recordList.size() > 0) {
                param.setCardId(recordList.get(0).getCardId());
            } else {
                throw new ServiceException("短车卡号有误");
            }
        }
        ParkingCoupon coupon = parkingCouponMapper.selectOne(Wrappers.<ParkingCoupon>lambdaQuery()
                .eq(ParkingCoupon::getId, param.getCouponId())
                .eq(ParkingCoupon::getCreateSource, createSource)
        );
        coupon = stateHandler(coupon);
        Long couponId = coupon.getId();
        InventoryTypeEnum inventoryType = coupon.getInventoryType();
        if (MdcUtil.getCurrentUser() != null) {
            if ((StrUtil.isBlank(coupon.getDesignateOrgIds())
                    && !StrUtil.containsAny(coupon.getCreatorOrgIds(), StrUtil.split(MdcUtil.getCurrentOrgIds(), ",")))
                    || (StrUtil.isNotBlank(coupon.getDesignateOrgIds())
                    && !StrUtil.containsAny(coupon.getDesignateOrgIds(), StrUtil.split(MdcUtil.getCurrentOrgIds(), ",")))) {
                throw new ServiceException("停车券券不属于本部门不可派发");
            }
        }
        if (coupon.getState() != CouponStateEnum.NORMAL) {
            throw new ServiceException("停车券已" + coupon.getState().getName() + "不能派发");
        }
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal worth = BigDecimal.ZERO;
        BigDecimal count = BigDecimal.ZERO;
        if (inventoryType == InventoryTypeEnum.QUOTA) {
            if (param.getValue() == null) {
                throw new ServiceException("派发金额不能为空");
            }
            worth = param.getValue();
            count = BigDecimal.ONE;
            //预先扣除库存缓存
            total = increaseTotalDistributeWorth(couponId, worth);
            increaseTotalDistributeCount(couponId, count);
        } else if (inventoryType == InventoryTypeEnum.AMOUNT) {
            if (param.getCount() == null) {
                throw new ServiceException("派发数不能为空");
            }
            count = NumberUtil.toBigDecimal(param.getCount());
            worth = NumberUtil.mul(coupon.getCouponWorth(), count);
            //预先扣除库存缓存
            total = increaseTotalDistributeCount(couponId, count);
            increaseTotalDistributeWorth(couponId, worth);
        }
        //事务commit后更新停车券库存
        MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("更新停车券库存_" + couponId, () -> updateDistributeCount(couponId, inventoryType)));
        //事务rollback时把预先增加的领取数还原(恢复库存)
        BigDecimal finalWorth = worth;
        BigDecimal finalCount = count;
        MdcUtil.publishTransactionalEvent(AfterRollbackEvent.build("恢复停车券库存缓存", () -> {
            decreaseTotalDistributeWorth(couponId, finalWorth);
            decreaseTotalDistributeCount(couponId, finalCount);
        }));
        if (NumberUtil.isLess(coupon.getInventory(), total)) {
            throw new ServiceException("停车券库存不足");
        }
        ParkingCouponMemberRel rel = receiveCoupon(param, coupon, receiveSource, receiveSourceId);
        if (param.getAutoConsume()) {
            useCoupon(param, rel, null);
        }
        return rel;
    }

    private ParkingCouponMemberRel receiveCoupon(CouponMemberRelAddParam param, ParkingCoupon parkingCoupon, ReceiveSourceEnum receiveSource, Long receiveSourceId) {
        ParkingCouponMemberRel rel = new ParkingCouponMemberRel();
        BeanUtil.copyProperties(param, rel);
        if (StrUtil.isBlank(parkingCoupon.getDesignateOrgIds())) {
            rel.setDesignateOrgIds(parkingCoupon.getCreatorOrgIds());
            rel.setDesignateOrgNames(parkingCoupon.getCreatorOrgNames());
        }
        rel.setRecordName(StrUtil.emptyToNull(rel.getRecordName()));
        BeanUtil.copyProperties(parkingCoupon, rel, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        if (param.getMemberId() != null || StrUtil.isNotBlank(param.getMemberCode())) {
            MemberInfo memberInfo = memberInfoMapper.selectOne(Wrappers.<MemberInfo>lambdaQuery()
                    .eq(param.getMemberId() != null, MemberInfo::getId, param.getMemberId())
                    .eq(StrUtil.isNotBlank(param.getMemberCode()), MemberInfo::getMemberCode, param.getMemberCode())
            );
            if (memberInfo == null) {
                throw MemberErrorEnum.MEMBER_ERROR_10002.getErrorMeta().getException();
            }
            MdcUtil.setMemberInfo(rel, memberInfo);
        }
        rel.setConsumeState(ConsumeStateEnum.NOTCONSUME);
        if (parkingCoupon.getInventoryType() == InventoryTypeEnum.QUOTA) {
            rel.setCouponWorth(param.getValue());
            rel.setCouponCount(1);
            rel.setCouponTotalWorth(param.getValue());
        } else if (parkingCoupon.getInventoryType() == InventoryTypeEnum.AMOUNT) {
            rel.setCouponCount(param.getCount());
            rel.setCouponTotalWorth(NumberUtil.mul(parkingCoupon.getCouponWorth(), rel.getCouponCount()));
        }
        //检验停车券领取限制
        if (!param.getCancelLimit() && parkingCoupon.getCouponType() != CouponTypeEnum.FREE) {
            validateLimit(false, rel.getCouponId(), rel.getCouponTotalWorth(), rel.getCouponCount(), parkingCoupon.getReceiveLimitType(),
                    parkingCoupon.getReceiveLimitContent(), param.getMemberId(), param.getCarNo(), param.getCardId(), param.getShortCardId());
        }
        rel.setReceiveSource(receiveSource);
        rel.setReceiveSourceId(receiveSourceId);
        DateTime now = DateTime.now();
        rel.setReceiveTime(now);
        DateTime start = null, end = null;
        switch (parkingCoupon.getEffectiveType()) {
            case TWENTYFOUR: {
                start = getStartTime(param.getCarNo(), param.getCardId(), param.getShortCardId());
                end = now.offsetNew(DateField.HOUR_OF_DAY, 24);
                break;
            }
            case TODAY: {
                start = getStartTime(param.getCarNo(), param.getCardId(), param.getShortCardId());
                end = DateUtil.endOfDay(now);
                break;
            }
            case CUSTOM: {
                start = new DateTime(parkingCoupon.getEffectiveStartTime());
                end = new DateTime(parkingCoupon.getEffectiveEndTime());
                break;
            }
            default:
        }
        rel.setEffectiveStartTime(start);
        rel.setEffectiveEndTime(end);
        parkingCouponMemberRelMapper.insert(rel);
        return rel;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void useCoupon(CouponMemberRelAddParam param, ParkingCouponMemberRel rel, Long orderId) {
        //检验停车券状态
        if (rel.getConsumeState() == ConsumeStateEnum.CONSUMED) {
            throw new ServiceException("停车券已使用");
        }
        if (rel.getEffectiveType() != EffectiveTypeEnum.EVERLASTING) {
            if (rel.getEffectiveEndTime() != null && DateTime.now().isAfter(rel.getEffectiveEndTime())) {
                throw new ServiceException("停车券已过期");
            }
        }
        //检验停车券使用限制
        if (!param.getCancelLimit() && rel.getCouponType() != CouponTypeEnum.FREE) {
            rel = stateHandler(rel);
            if (rel.getConsumeState() != ConsumeStateEnum.NOTCONSUME) {
                throw new ServiceException(rel.getCouponName() + "停车券" + rel.getConsumeState().getName());
            }
            validateLimit(true, rel.getCouponId(), rel.getCouponTotalWorth(), rel.getCouponCount(), rel.getUseLimitType(), rel.getUseLimitContent(),
                    rel.getMemberId(), param.getCarNo(), param.getCardId(), param.getShortCardId());
        }
        //检验停车券绑定车辆信息是否一致
        if (!StrUtil.isAllBlank(rel.getCarNo(), rel.getCardId())) {
            if (StrUtil.isNotBlank(rel.getCarNo())) {
                if (!StrUtil.equals(rel.getCarNo(), param.getCarNo())) {
                    throw new ServiceException("车牌号不一致,使用失败");
                }
            } else if (StrUtil.isNotBlank(rel.getCardId())) {
                if (!StrUtil.equals(rel.getCardId(), param.getCardId())) {
                    throw new ServiceException("车卡号不一致,使用失败");
                }
            }
        }
        ParkingCouponMemberRel update = new ParkingCouponMemberRel();
        update.setId(rel.getId());
        update.setOrderId(orderId);
        update.setConsumeState(ConsumeStateEnum.CONSUMED);
        update.setConsumeTime(DateTime.now());
        update.setCardId(param.getCardId());
        update.setCarNo(param.getCarNo());
        update.setShortCardId(param.getShortCardId());
        int count = parkingCouponMemberRelMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("使用停车券失败");
        }
        if (rel.getCreateSource() == CreateSourceEnum.EXCHANGE) {
            //更新兑换订单状态
            exchangeService.updateSuccessState(rel.getReceiveSourceId());
        }
        CouponsAddReq req = new CouponsAddReq();
        req.setBarCode(Convert.toStr(rel.getId()));
        req.setCardId(param.getCardId());
        req.setCarNo(param.getCarNo());
        switch (rel.getCouponType()) {
            case MONEY: {
                req.setType("1");
                req.setValue(rel.getCouponTotalWorth().toString());
                break;
            }
            case TIME: {
                req.setType("2");
                req.setTime(Convert.toStr(NumberUtil.mul(rel.getCouponTotalWorth(), 60).intValue()));
                break;
            }
            case FREE: {
                req.setType("6");
                break;
            }
            default:
        }
        DateTime start, end, now = DateTime.now();
        if (rel.getEffectiveStartTime() == null || rel.getEffectiveEndTime() == null) {
            start = now.offsetNew(DateField.YEAR, -1);
            end = now.offsetNew(DateField.YEAR, 1);
        } else {
            start = DateUtil.dateNew(rel.getEffectiveStartTime());
            end = DateUtil.dateNew(rel.getEffectiveEndTime());
        }
        req.setStartTime(start.toString());
        req.setEndTime(end.toString());
        AjbCloudResp resp = AjbCloudUtil.Coupon.addCoupons(MdcUtil.getCurrentTenantId().toString(), req);
        if (!resp.getStatus()) {
            throw new ServiceException("安居宝报错:" + resp.getMsg());
        }
    }

    private DateTime getStartTime(String carNo, String cardId, String shortCardId) {
        DateTime startTime;
        if (StrUtil.isNotBlank(shortCardId)) {
            startTime = getCarInTime(shortCardId, true);
        } else if (StrUtil.isNotBlank(cardId)) {
            startTime = getCarInTime(cardId, false);
        } else {
            startTime = getCarInTime(carNo, false);
        }
        return startTime;
    }

    private DateTime getCarInTime(String car, boolean isShort) {
        DateTime time = DateTime.now().offset(DateField.YEAR, -1);
        if (isShort) {
            List<ParkingUnlicensedCarRecord> recordList = parkingUnlicensedCarRecordMapper.selectList(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                    .eq(ParkingUnlicensedCarRecord::getShortCardId, car)
                    .orderByDesc(ParkingUnlicensedCarRecord::getCreateTime)
                    .last("limit 1")
            );
            if (recordList.size() > 0) {
                time = new DateTime(recordList.get(0).getInTime() == null ? recordList.get(0).getCreateTime() : recordList.get(0).getInTime());
            }
        } else {
            List<ParkingAjbEnterRecord> recordList = parkingAjbEnterRecordMapper.selectList(Wrappers.<ParkingAjbEnterRecord>lambdaQuery()
                    .eq(ParkingAjbEnterRecord::getCardId, car)
                    .or().eq(ParkingAjbEnterRecord::getCarNo, car)
                    .orderByDesc(ParkingAjbEnterRecord::getAccTime)
                    .last("limit 1")
            );
            if (recordList.size() > 0) {
                time = DateUtil.parse(recordList.get(0).getAccTime());
            }
        }
        return time;
    }

    @Override
    public void validateLimit(boolean isUsed, Long couponId, BigDecimal couponWorth, Integer couponCount, LimitTypeEnum limitType,
                              String limitContent, Long memberId, String carNo, String cardId, String shortCardId) {
        if (limitType == LimitTypeEnum.UNLIMITED) {
            return;
        }
        CouponLimit limit = JSONUtil.toBean(limitContent, CouponLimit.class);
        if (limit.getLimitType() == CouponLimit.LimitType.CAR && StrUtil.isAllBlank(carNo, cardId, shortCardId)) {
            return;
        }
        if (limit.getLimitType() == CouponLimit.LimitType.PERSON && memberId == null) {
            return;
        }
        DateTime start, end, now = DateTime.now();
        switch (limit.getLimitRange()) {
            case DAY: {
                start = DateUtil.beginOfDay(now);
                end = DateUtil.endOfDay(now);
                break;
            }
            case WEEK: {
                start = DateUtil.beginOfWeek(now);
                end = DateUtil.endOfWeek(now);
                break;
            }
            case MONTH: {
                start = DateUtil.beginOfMonth(now);
                end = DateUtil.endOfMonth(now);
                break;
            }
            case YEAR: {
                start = DateUtil.beginOfYear(now);
                end = DateUtil.endOfYear(now);
                break;
            }
            case LASTING: {
                start = null;
                end = null;
                break;
            }
            default:
                return;
        }
        QueryWrapper<ParkingCouponMemberRel> wrapper = new QueryWrapper<>();
        BigDecimal couponValue;
        if (limit.getLimitCountType() == CouponLimit.LimitCountType.AMOUNT) {
            wrapper.select("coalesce(sum(coupon_count), 0) total");
            couponValue = NumberUtil.toBigDecimal(couponCount);
        } else if (limit.getLimitCountType() == CouponLimit.LimitCountType.QUOTA) {
            wrapper.select("coalesce(sum(coupon_total_worth), 0) total");
            couponValue = couponWorth;
        } else {
            return;
        }
        BigDecimal limitCount = parkingCouponMemberRelMapper.selectMaps(wrapper.lambda()
                .eq(ParkingCouponMemberRel::getCouponId, couponId)
                .and(start != null && end != null && !isUsed, i -> i.ge(ParkingCouponMemberRel::getReceiveTime, start)
                        .le(ParkingCouponMemberRel::getReceiveTime, end))
                .and(start != null && end != null && isUsed, i -> i.ge(ParkingCouponMemberRel::getConsumeTime, start)
                        .le(ParkingCouponMemberRel::getConsumeTime, end))
                .eq(limit.getLimitType() == CouponLimit.LimitType.PERSON, ParkingCouponMemberRel::getMemberId, memberId)
                .and(limit.getLimitType() == CouponLimit.LimitType.CAR, i -> i.eq(ParkingCouponMemberRel::getCarNo, carNo)
                        .or().eq(ParkingCouponMemberRel::getCardId, cardId)
                        .or().eq(ParkingCouponMemberRel::getShortCardId, shortCardId)
                )
                .eq(isUsed, ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
        ).stream().map(map -> Convert.toBigDecimal(map.get("total"))).collect(toList()).get(0);
        if (limitCount == null) {
            return;
        }
        if (limitCount.add(couponValue).compareTo(limit.getLimitCount()) > 0) {
            if (isUsed) {
                throw ParkingErrorEnum.PARKING_ERROR_10502.getErrorMeta().getException();
            } else {
                throw ParkingErrorEnum.PARKING_ERROR_10501.getErrorMeta().getException();
            }
        }
    }

    @Override
    public IPage<ParkingCouponMemberRel> pageCouponMemberRel(CouponMemberRelPageParam param) {
        DateTime now = DateTime.now();
        return parkingCouponMemberRelMapper.selectPage(param.page(), Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                .and(StrUtil.isNotBlank(param.getCarNo()), i -> i.like(ParkingCouponMemberRel::getCarNo, param.getCarNo())
                        .or().like(ParkingCouponMemberRel::getCardId, param.getCarNo())
                        .or().like(ParkingCouponMemberRel::getShortCardId, param.getCarNo()))
                .eq(StrUtil.isNotBlank(param.getCouponCode()), ParkingCouponMemberRel::getCouponCode, param.getCouponCode())
                .like(StrUtil.isNotBlank(param.getCouponName()), ParkingCouponMemberRel::getCouponName, param.getCouponName())
                .like(StrUtil.isNotBlank(param.getCreatorName()), ParkingCouponMemberRel::getCreatorName, param.getCreatorName())
                .like(StrUtil.isNotBlank(param.getDesignateOrgNames()), ParkingCouponMemberRel::getDesignateOrgNames, param.getDesignateOrgNames())
                .eq(param.getCouponType() != null, ParkingCouponMemberRel::getCouponType, param.getCouponType())
                .eq(param.getCreateSource() != null, ParkingCouponMemberRel::getCreateSource, param.getCreateSource())
                .ge(param.getReceiveTimeStart() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeStart())
                .le(param.getReceiveTimeEnd() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeEnd())
                .ge(param.getConsumeTimeStart() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeStart())
                .le(param.getConsumeTimeEnd() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeEnd())
                .and(param.getConsumeState() == ConsumeStateEnum.NOTCONSUME, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.gt(ParkingCouponMemberRel::getEffectiveEndTime, now)
                                .or().eq(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .eq(param.getConsumeState() == ConsumeStateEnum.CONSUMED, ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                .and(param.getConsumeState() == ConsumeStateEnum.EXPIRED, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.le(ParkingCouponMemberRel::getEffectiveEndTime, now)
                                .ne(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .and(param.getConsumeState() == ConsumeStateEnum.NOTACTIVE, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.ge(ParkingCouponMemberRel::getEffectiveStartTime, now)
                                .ne(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .orderByDesc(ParkingCouponMemberRel::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(this::stateHandler);
    }

    private LambdaQueryWrapper<ParkingCouponMemberRel> buildWrapper(QueryWrapper<ParkingCouponMemberRel> queryWrapper, StatisticsGetParam param) {
        DateTime now = DateTime.now();
        return queryWrapper.lambda()
                .and(StrUtil.isNotBlank(param.getCarNo()), i -> i.like(ParkingCouponMemberRel::getCarNo, param.getCarNo())
                        .or().like(ParkingCouponMemberRel::getCardId, param.getCarNo())
                        .or().like(ParkingCouponMemberRel::getShortCardId, param.getCarNo()))
                .eq(StrUtil.isNotBlank(param.getCouponCode()), ParkingCouponMemberRel::getCouponCode, param.getCouponCode())
                .like(StrUtil.isNotBlank(param.getCouponName()), ParkingCouponMemberRel::getCouponName, param.getCouponName())
                .like(StrUtil.isNotBlank(param.getCreatorName()), ParkingCouponMemberRel::getCreatorName, param.getCreatorName())
                .like(StrUtil.isNotBlank(param.getDesignateOrgNames()), ParkingCouponMemberRel::getDesignateOrgNames, param.getDesignateOrgNames())
                .eq(param.getCouponType() != null, ParkingCouponMemberRel::getCouponType, param.getCouponType())
                .eq(param.getCreateSource() != null, ParkingCouponMemberRel::getCreateSource, param.getCreateSource())
                .ge(param.getReceiveTimeStart() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeStart())
                .le(param.getReceiveTimeEnd() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeEnd())
                .ge(param.getConsumeTimeStart() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeStart())
                .le(param.getConsumeTimeEnd() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeEnd())
                .and(param.getConsumeState() == ConsumeStateEnum.NOTCONSUME, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.gt(ParkingCouponMemberRel::getEffectiveEndTime, now)
                                .or().eq(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .eq(param.getConsumeState() == ConsumeStateEnum.CONSUMED, ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                .and(param.getConsumeState() == ConsumeStateEnum.EXPIRED, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.le(ParkingCouponMemberRel::getEffectiveEndTime, now)
                                .ne(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .and(param.getConsumeState() == ConsumeStateEnum.NOTACTIVE, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.ge(ParkingCouponMemberRel::getEffectiveStartTime, now)
                                .ne(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams());
    }

    @Override
    public RelStatisticsResult getStatistics(StatisticsGetParam param) {
        RelStatisticsResult result = parkingCouponMemberRelMapper.selectMaps(buildWrapper(new QueryWrapper<ParkingCouponMemberRel>()
                .select("coalesce(sum(coupon_count), 0) distribute_count," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.MONEY.getType() + " then coupon_total_worth else 0 end), 0) distribute_amount," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.TIME.getType() + " then coupon_total_worth else 0 end), 0) distribute_time," +
                        "coalesce(sum(coupon_count), 0) consume_count," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.MONEY.getType() +
                        " and consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_total_worth else 0 end), 0) consume_amount," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.TIME.getType() +
                        " and consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_total_worth else 0 end), 0) consume_time"), param)
        ).stream().map((map) -> BeanUtil.fillBeanWithMap(map, new RelStatisticsResult(), true,
                CopyOptions.create().ignoreError())).collect(toList()).get(0);
        RelStatisticsResult statisticsResult = parkingCouponMemberRelMapper.selectMaps(buildWrapper(new QueryWrapper<ParkingCouponMemberRel>()
                .select("count(distinct car_no) car_count," +
                        "count(distinct member_id) member_count"), param)
        ).stream().map((map) -> BeanUtil.fillBeanWithMap(map, new RelStatisticsResult(), true,
                CopyOptions.create().ignoreError())).collect(toList()).get(0);
        BeanUtil.copyProperties(statisticsResult, result, CopyOptions.create().ignoreNullValue());
        return result;
    }

    @Override
    public IPage<RelDetailAnalysisResult> pageDetailAnalysis(RelTimePageParam param) {
        return parkingCouponMemberRelMapper.selectMapsPage(param.page(), new QueryWrapper<ParkingCouponMemberRel>()
                .select("coupon_name," +
                        "designate_org_names," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.MONEY.getType() + " then coupon_total_worth else 0 end), 0) distribute_amount," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.TIME.getType() + " then coupon_total_worth else 0 end), 0) distribute_time," +
                        "coalesce(sum(coupon_count), 0) distribute_count," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.MONEY.getType() +
                        " and consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_total_worth else 0 end), 0) consume_amount," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.TIME.getType() +
                        " and consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_total_worth else 0 end), 0) consume_time," +
                        "coalesce(sum(case when consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_count else 0 end), 0) consume_count")
                .lambda()
                .ge(param.getReceiveTimeStart() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeStart())
                .le(param.getReceiveTimeEnd() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeEnd())
                .ge(param.getConsumeTimeStart() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeStart())
                .le(param.getConsumeTimeEnd() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeEnd())
                .groupBy(ParkingCouponMemberRel::getDesignateOrgIds, ParkingCouponMemberRel::getCouponId)
                .orderByDesc(ParkingCouponMemberRel::getId)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams()))
                .convert(map -> BeanUtil.fillBeanWithMap(map, new RelDetailAnalysisResult(), true,
                        CopyOptions.create().ignoreError()));
    }

    @Override
    public IPage<RelSummaryAnalysisResult> pageSummaryAnalysis(RelTimePageParam param) {
        return parkingCouponMemberRelMapper.selectMapsPage(param.page(), new QueryWrapper<ParkingCouponMemberRel>()
                .select("designate_org_names," +
                        "date_format(receive_time,'%Y-%m-%d') analysis_date," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.MONEY.getType() + " then coupon_total_worth else 0 end), 0) total_amount," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.TIME.getType() + " then coupon_total_worth else 0 end), 0) total_time," +
                        "max(receive_time) last_time")
                .groupBy("analysis_date")
                .orderByDesc("analysis_date")
                .lambda()
                .ge(param.getReceiveTimeStart() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeStart())
                .le(param.getReceiveTimeEnd() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeEnd())
                .ge(param.getConsumeTimeStart() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeStart())
                .le(param.getConsumeTimeEnd() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeEnd())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams()))
                .convert(map -> BeanUtil.fillBeanWithMap(map, new RelSummaryAnalysisResult(), true,
                        CopyOptions.create().ignoreError()));
    }

    @Override
    public IPage<RelRecordAnalysisResult> pageRecordAnalysis(RelTimePageParam param) {
        return parkingCouponMemberRelMapper.selectMapsPage(param.page(), new QueryWrapper<ParkingCouponMemberRel>()
                .select("record_name," +
                        "coalesce(sum(record_amount), 0) record_total_amount," +
                        "coalesce(sum(case when member_id is not null then 1 else 0 end), 0) member_count," +
                        "coalesce(sum(case when member_id is null then 1 else 0 end), 0) non_member_count," +
                        "count(1) record_count," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.MONEY.getType() + " then coupon_total_worth else 0 end), 0) distribute_amount," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.TIME.getType() + " then coupon_total_worth else 0 end), 0) distribute_time," +
                        "coalesce(sum(coupon_count), 0) distribute_count," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.MONEY.getType() + " and consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_total_worth else 0 end), 0) consume_amount," +
                        "coalesce(sum(case when coupon_type=" + CouponTypeEnum.TIME.getType() + " and consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_total_worth else 0 end), 0) consume_time," +
                        "coalesce(sum(case when consume_state=" + ConsumeStateEnum.CONSUMED.getType() + " then coupon_count else 0 end), 0) consume_count")
                .lambda()
                .ge(param.getReceiveTimeStart() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeStart())
                .le(param.getReceiveTimeEnd() != null, ParkingCouponMemberRel::getReceiveTime, param.getReceiveTimeEnd())
                .ge(param.getConsumeTimeStart() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeStart())
                .le(param.getConsumeTimeEnd() != null, ParkingCouponMemberRel::getConsumeTime, param.getConsumeTimeEnd())
                .groupBy(ParkingCouponMemberRel::getRecordName)
                .orderByDesc(ParkingCouponMemberRel::getId)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams()))
                .convert(map -> BeanUtil.fillBeanWithMap(map, new RelRecordAnalysisResult(), true,
                        CopyOptions.create().ignoreError()));
    }

    @Override
    public void revokedRel(ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource) {
        List<ParkingCouponMemberRel> relList = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                .eq(ParkingCouponMemberRel::getReceiveSourceId, receiveSourceId)
                .eq(ParkingCouponMemberRel::getReceiveSource, receiveSource)
                .eq(ParkingCouponMemberRel::getCreateSource, createSource)
        );
        for (ParkingCouponMemberRel rel : relList) {
            rel = stateHandler(rel);
            if (rel.getConsumeState() == ConsumeStateEnum.CONSUMED) {
                throw new ServiceException("此停车券不能撤回");
            }
            if (rel.getConsumeState() != ConsumeStateEnum.REVOKED) {
                ParkingCouponMemberRel update = new ParkingCouponMemberRel();
                update.setId(rel.getId());
                update.setConsumeState(ConsumeStateEnum.REVOKED);
                int count = parkingCouponMemberRelMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("撤回停车券失败");
                }
            }
        }
    }

    /***** api接口使用 *****/

    @Override
    public IPage<ParkingCouponMemberRel> pageCouponRel(CouponRelPageParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        List<String> carNoList = parkingMemberCarMapper.selectList(Wrappers.<ParkingMemberCar>lambdaQuery()
                .eq(ParkingMemberCar::getMemberId, memberId)
        ).stream().map(ParkingMemberCar::getCarNo).collect(toList());
        List<String> cardIdList = parkingUnlicensedCarRecordMapper.selectList(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                .eq(ParkingUnlicensedCarRecord::getMemberId, memberId)
        ).stream().map(ParkingUnlicensedCarRecord::getCardId).collect(toList());
        DateTime now = DateTime.now();
        return parkingCouponMemberRelMapper.selectPage(param.page(), Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                .and(StrUtil.isAllBlank(param.getCardId(), param.getCarNo()), i -> i.eq(ParkingCouponMemberRel::getMemberId, memberId)
                        .or().in(carNoList.size() > 0, ParkingCouponMemberRel::getCarNo, carNoList)
                        .or().in(cardIdList.size() > 0, ParkingCouponMemberRel::getCardId, cardIdList))
                .and(!StrUtil.isAllBlank(param.getCardId(), param.getCarNo()), i -> i.eq(ParkingCouponMemberRel::getMemberId, memberId)
                        .or().eq(ParkingCouponMemberRel::getCarNo, param.getCarNo())
                        .or().eq(ParkingCouponMemberRel::getCardId, param.getCardId()))
                .and(param.getConsumeState() == ConsumeStateEnum.NOTCONSUME, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.gt(ParkingCouponMemberRel::getEffectiveEndTime, now)
                                .or().eq(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .eq(param.getConsumeState() == ConsumeStateEnum.CONSUMED, ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                .and(param.getConsumeState() == ConsumeStateEnum.EXPIRED, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.le(ParkingCouponMemberRel::getEffectiveEndTime, now)
                                .ne(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .and(param.getConsumeState() == ConsumeStateEnum.NOTACTIVE, i -> i
                        .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                        .and(j -> j.ge(ParkingCouponMemberRel::getEffectiveStartTime, now)
                                .ne(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING)))
                .orderByDesc(ParkingCouponMemberRel::getCreateTime)
        ).convert(this::stateHandler);
    }

    @Override
    public ParkingCouponMemberRel getCouponRel(CouponRelGetParam param) {
        ParkingCouponMemberRel rel = parkingCouponMemberRelMapper.selectById(param.getId());
        return stateHandler(rel);
    }

    @Override
    public void judgeCouponRel(ParkingCouponMemberRel rel, Long memberId, String carNo, String cardId) {
        if (!memberId.equals(rel.getId()) && !StrUtil.equals(rel.getCarNo(), carNo)
                && !StrUtil.equals(rel.getCardId(), cardId)) {
            throw new ServiceException(rel.getCouponName() + "停车券不能使用");
        }
        rel = stateHandler(rel);
        if (rel.getConsumeState() != ConsumeStateEnum.NOTCONSUME) {
            throw new ServiceException(rel.getCouponName() + "停车券" + rel.getConsumeState().getName());
        }
    }

    @Override
    public void validateUseCouponLimit(Map<String, List<ParkingCouponMemberRel>> relMap, Long memberId, String carNo, String cardId) {
        ALL:
        for (Map.Entry<String, List<ParkingCouponMemberRel>> entry : relMap.entrySet()) {
            BigDecimal couponWorth = BigDecimal.ZERO, noMemberCouponWorth = BigDecimal.ZERO;
            Integer couponCount = 0, noMemberCouponCount = 0;
            for (ParkingCouponMemberRel rel : entry.getValue()) {
                judgeCouponRel(rel, memberId, carNo, cardId);
                if (rel.getCouponType() == CouponTypeEnum.FREE) {
                    //全免券标识
                    if (entry.getValue().size() > 1 || relMap.size() > 1) {
                        throw ParkingErrorEnum.PARKING_ERROR_10504.getErrorMeta().getException();
                    }
                    break ALL;
                }
                if (rel.getMemberId() != null) {
                    couponWorth = couponWorth.add(rel.getCouponTotalWorth());
                    couponCount += rel.getCouponCount();
                } else {
                    noMemberCouponWorth = noMemberCouponWorth.add(rel.getCouponTotalWorth());
                    noMemberCouponCount += rel.getCouponCount();
                }
            }
            ParkingCouponMemberRel rel = entry.getValue().get(0);
            LimitTypeEnum limitType = rel.getUseLimitType();
            String limitContent = rel.getUseLimitContent();
            if (couponWorth.compareTo(BigDecimal.ZERO) > 0 || couponCount > 0) {
                validateLimit(true, rel.getCouponId(), couponWorth, couponCount, limitType, limitContent,
                        memberId, carNo, cardId, null);
            }
            if (noMemberCouponWorth.compareTo(BigDecimal.ZERO) > 0 || noMemberCouponCount > 0) {
                validateLimit(true, rel.getCouponId(), noMemberCouponWorth, noMemberCouponCount, limitType, limitContent,
                        null, carNo, cardId, null);
            }
        }
    }

    @Override
    public void judgeCouponRel(CouponRelJudgeParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        if (CollUtil.isNotEmpty(param.getCouponMemberRelIds())) {
            Set<Long> relIds = new HashSet<>(param.getCouponMemberRelIds());
            Map<String, List<ParkingCouponMemberRel>> relMap = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                    .in(ParkingCouponMemberRel::getId, relIds)
                    .orderByDesc(ParkingCouponMemberRel::getReceiveTime)
            ).stream().collect(Collectors.groupingBy(ParkingCouponMemberRel::getCouponCode));
            validateUseCouponLimit(relMap, memberId, param.getCarNo(), param.getCardId());
        }
    }

    @Transactional
    @Override
    public void useCouponRel(CouponRelUseParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        ParkingCouponMemberRel rel = parkingCouponMemberRelMapper.selectById(param.getId());
        if (StrUtil.isBlank(param.getCardId()) && StrUtil.isAllBlank(rel.getCarNo(), param.getCarNo())) {
            throw new ServiceException("请指定车牌号");
        } else if (StrUtil.isBlank(param.getCarNo()) && StrUtil.isAllBlank(rel.getCardId(), param.getCardId())) {
            throw new ServiceException("请指定车卡号");
        }
        CouponMemberRelAddParam addParam = new CouponMemberRelAddParam();
        BeanUtil.copyProperties(param, addParam);
        addParam.setMemberId(memberId);
        if (StrUtil.isBlank(addParam.getCarNo())) {
            addParam.setCarNo(rel.getCarNo());
        }
        if (StrUtil.isBlank(addParam.getCardId())) {
            addParam.setCardId(rel.getCardId());
        }
        useCoupon(addParam, rel, null);
    }

}
