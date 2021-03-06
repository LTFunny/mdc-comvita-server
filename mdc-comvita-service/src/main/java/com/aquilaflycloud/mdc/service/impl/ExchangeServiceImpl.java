package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.*;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.auth.enums.UserTypeEnum;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.component.event.AfterCommitEvent;
import com.aquilaflycloud.mdc.component.event.AfterRollbackEvent;
import com.aquilaflycloud.mdc.enums.catalog.CatalogBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.*;
import com.aquilaflycloud.mdc.enums.easypay.FrpCodeEnum;
import com.aquilaflycloud.mdc.enums.easypay.OrderTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.PayType;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.*;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.model.catalog.CatalogRel;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.exchange.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.param.coupon.CouponAddParam;
import com.aquilaflycloud.mdc.param.coupon.CouponEditParam;
import com.aquilaflycloud.mdc.param.coupon.CouponGetParam;
import com.aquilaflycloud.mdc.param.easypay.OrderParam;
import com.aquilaflycloud.mdc.param.easypay.RefundParam;
import com.aquilaflycloud.mdc.param.exchange.*;
import com.aquilaflycloud.mdc.result.exchange.*;
import com.aquilaflycloud.mdc.service.*;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.aquilaflycloud.util.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.map.event.EntryExpiredListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * ExchangeServiceImpl
 *
 * @author star
 * @date 2020-03-15
 */
@Slf4j
@Service
public class ExchangeServiceImpl implements ExchangeService {
    @Resource
    private CatalogInfoMapper catalogInfoMapper;
    @Resource
    private CatalogRelMapper catalogRelMapper;
    @Resource
    private ExchangeGoodsMapper exchangeGoodsMapper;
    @Resource
    private ExchangeOrderMapper exchangeOrderMapper;
    @Resource
    private ExchangeRecommendRelMapper exchangeRecommendRelMapper;
    @Resource
    private ExchangeOrderOperateRecordMapper exchangeOrderOperateRecordMapper;
    @Resource
    private MemberService memberService;
    @Resource
    private EasypayPaymentRecordMapper easypayPaymentRecordMapper;
    @Resource
    private EasypayRefundRecordMapper easypayRefundRecordMapper;
    @Resource
    private CouponMemberRelMapper couponMemberRelMapper;
    @Resource
    private CouponInfoService couponInfoService;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private EasyPayService easyPayService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private ExchangeGoodsSpecValueInfoMapper exchangeGoodsSpecValueInfoMapper;
    @Resource
    private ExchangeGoodsSkuInfoMapper exchangeGoodsSkuInfoMapper;
    @Resource
    private ExchangeSpecValueInfoMapper exchangeSpecValueInfoMapper;

    private final static int OVERTIME = 15;
    private final static int OVER_CONFIRM_DAY = 15;
    private final static int OVER_REFUND_DAY = 7;

    private int getOvertime() {
        Integer time = clientConfigService.getItemByName(null, "exchangeOrderOvertimeToPay");
        return time == null ? OVERTIME : time;
    }

    private int getOverConfirmDay() {
        Integer time = clientConfigService.getItemByName(null, "exchangeOrderOverConfirmDay");
        return time == null ? OVER_CONFIRM_DAY : time;
    }

    private int getOverRefundDay() {
        Integer time = clientConfigService.getItemByName(null, "exchangeOrderOverRefundDay");
        return time == null ? OVER_REFUND_DAY : time;
    }

    private Integer getCacheCount(Long goodsId, Long skuId) {
        RMapCache<Long, Integer> map = RedisUtil.redisson().getMapCache("exchangeGoodsCount_" + goodsId + (ObjectUtil.isNotNull(skuId) ? skuId : ""));
        int total = 0;
        for (Integer count : map.values()) {
            total += count;
        }
        return total;
    }

    private ExchangeGoods stateHandler(ExchangeGoods exchangeGoods, Long skuId) {
        if (exchangeGoods == null) {
            throw new ServiceException("???????????????");
        }
        if (exchangeGoods.getState() == GoodsStateEnum.NORMAL) {
            if (exchangeGoods.getAuditState() == AuditStateEnum.PENDING) {
                exchangeGoods.setState(GoodsStateEnum.PENDING);
            } else if (exchangeGoods.getAuditState() == AuditStateEnum.REJECT) {
                exchangeGoods.setState(GoodsStateEnum.REJECT);
            } else {
                if (exchangeGoods.getShelveType() == ShelveTypeEnum.REGULAR && DateTime.now().isBefore(exchangeGoods.getShelveTime())) {
                    exchangeGoods.setState(GoodsStateEnum.NOTSHELVE);
                } else {
                    /*int cacheCount = getCacheCount(exchangeGoods.getId());
                    exchangeGoods.setExchangeCount(exchangeGoods.getExchangeCount() + cacheCount);*/
                    int exchangeCount = 0;
                    if (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.COUPON)) {
                        //??????????????????????????????
                        exchangeCount = getTotalExchangeCount(exchangeGoods.getId());
                    } else if (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
                        if (ObjectUtil.isNull(skuId)) {
                            //????????????????????????sku??????
                            List<ExchangeGoodsSkuInfo> skuInfos = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                                    .eq(ExchangeGoodsSkuInfo::getGoodsId, exchangeGoods.getId())
                            );
                            exchangeCount = getPhysicalTotalExchangeCount(exchangeGoods.getId(), skuInfos);
                        } else {
                            exchangeCount = getPhysicalSkuExchangeCount(exchangeGoods.getId(), skuId);
                        }

                    }
                    if (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.COUPON)
                            || (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.PHYSICAL) && ObjectUtil.isNull(skuId))) {
                        //???????????????????????????????????????????????????
                        if (exchangeGoods.getInventory() <= exchangeCount) {
                            exchangeGoods.setState(GoodsStateEnum.SOLDOUT);
                        }
                    } else if (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.PHYSICAL) && ObjectUtil.isNotNull(skuId)) {
                        //????????????sku?????????sku?????????
                        ExchangeGoodsSkuInfo exchangeGoodsSkuInfo = exchangeGoodsSkuInfoMapper.selectById(skuId);
                        if (null == exchangeGoodsSkuInfo) {
                            throw new ServiceException("???????????????????????????????????????");
                        }
                        if (exchangeGoodsSkuInfo.getInventory() <= exchangeCount) {
                            exchangeGoods.setState(GoodsStateEnum.SOLDOUT);
                        }
                    }
                }
            }
        }
        return exchangeGoods;
    }

    private ExchangeOrder stateHandler(ExchangeOrder exchangeOrder) {
        if (exchangeOrder == null) {
            throw new ServiceException("???????????????");
        }
        DateTime now = DateTime.now();
        if (exchangeOrder.getOrderState() == OrderStateEnum.NOTPAY) {
            if (now.offsetNew(DateField.MINUTE, -getOvertime()).after(exchangeOrder.getCreateTime())) {
                exchangeOrder.setOrderState(OrderStateEnum.CLOSED);
            }
        } else if (exchangeOrder.getOrderState() == OrderStateEnum.DELIVERYING) {
            if (now.offsetNew(DateField.DAY_OF_YEAR, -getOverConfirmDay()).after(exchangeOrder.getDeliveryTime())) {
                exchangeOrder.setOrderState(OrderStateEnum.SUCCESS);
            }
        }
        if (exchangeOrder.getRefundState() == RefundStateEnum.REFUNDAUDIT) {
            exchangeOrder.setOrderState(OrderStateEnum.REFUNDAUDIT);
        }
        return exchangeOrder;
    }

    private Integer getTotalExchangeCount(Long goodsId, Boolean... reload) {
        String lock = "exchangeGoodsTotalExchangeCountLock" + goodsId;
        String key = "exchangeGoodsTotalExchangeCount" + goodsId;
        if (reload.length > 0 && reload[0]) {
            RedisUtil.redis().delete(key);
        }
        return RedisUtil.syncValueGet(lock, key, () -> exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                        .select("coalesce(sum(goods_count), 0) exchange_count")
                        .lambda()
                        .eq(ExchangeOrder::getGoodsId, goodsId)
                        .nested(i -> i.ne(ExchangeOrder::getOrderState, OrderStateEnum.CLOSED)
                                .ne(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY))
                ).stream().map(map -> Convert.toInt(map.get("exchange_count"))).collect(Collectors.toList()).get(0)
        );
    }

    private Integer getPhysicalSkuExchangeCount(Long goodsId, Long skuId, Boolean... reload) {
        String lock = "exchangeGoodsTotalExchangeCountLock" + goodsId + skuId;
        String key = "exchangeGoodsTotalExchangeCount" + goodsId + skuId;
        if (reload.length > 0 && reload[0]) {
            RedisUtil.redis().delete(key);
        }
        return RedisUtil.syncValueGet(lock, key, () -> exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                        .select("coalesce(sum(goods_count), 0) exchange_count")
                        .lambda()
                        .eq(ExchangeOrder::getGoodsId, goodsId)
                        .eq(ExchangeOrder::getSkuId, skuId)
                        .nested(i -> i.ne(ExchangeOrder::getOrderState, OrderStateEnum.CLOSED)
                                .ne(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY))
                ).stream().map(map -> Convert.toInt(map.get("exchange_count"))).collect(Collectors.toList()).get(0)
        );
    }

    private Integer getPhysicalTotalExchangeCount(Long goodsId, List<ExchangeGoodsSkuInfo> skuInfos, Boolean... reload) {
        int count = 0;
        if (null != skuInfos && skuInfos.size() > 0) {
            for (ExchangeGoodsSkuInfo item : skuInfos) {
                Integer exchangeCount = getPhysicalSkuExchangeCount(goodsId, item.getId(), reload);
                count = count + exchangeCount;
            }
        }
        return count;
    }

    private void increaseTotalExchangeCount(Long goodsId, Long skuId, Integer count) {
        String key = "exchangeGoodsTotalExchangeCount" + goodsId + (ObjectUtil.isNotNull(skuId) ? skuId : "");
        //??????skuId???????????????????????????
        Integer total = ObjectUtil.isNotNull(skuId) ? getPhysicalSkuExchangeCount(goodsId, skuId) : getTotalExchangeCount(goodsId);
        RedisUtil.valueRedis().set(key, total + count);
    }

    private void updateExchangeCount(Long goodsId, Long skuId) {
        MdcUtil.getTtlExecutorService().submit(() -> {
            RedisUtil.syncLoad("updateExchangeCountLock" + goodsId + (ObjectUtil.isNotNull(skuId) ? skuId : ""), () -> {
                Integer exchangeCount = exchangeOrderMapper.normalSelectMaps(new QueryWrapper<ExchangeOrder>()
                        .select("coalesce(sum(goods_count), 0) exchange_count")
                        .lambda()
                        .eq(ExchangeOrder::getGoodsId, goodsId)
                        .eq(ObjectUtil.isNotNull(skuId), ExchangeOrder::getSkuId, skuId)
                        .nested(i -> i.ne(ExchangeOrder::getOrderState, OrderStateEnum.CLOSED)
                                .ne(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY))
                ).stream().map(map -> Convert.toInt(map.get("exchange_count"))).collect(Collectors.toList()).get(0);

                //???????????????skuId??????????????????????????????????????????????????????????????????sku??????????????????????????????????????????????????????????????????????????????
                if (ObjectUtil.isNotNull(skuId)) {
                    ExchangeGoodsSkuInfo exchangeGoodsSkuInfo = exchangeGoodsSkuInfoMapper.normalSelectById(skuId);
                    Integer oldExchangeCount = exchangeGoodsSkuInfo.getExchangeCount();
                    //?????????????????????????????????????????????
                    Integer diff = exchangeCount - oldExchangeCount;
                    exchangeGoodsSkuInfoMapper.normalUpdate(null, Wrappers.<ExchangeGoodsSkuInfo>lambdaUpdate()
                            .set(ExchangeGoodsSkuInfo::getExchangeCount, exchangeCount)
                            .eq(ExchangeGoodsSkuInfo::getId, skuId)
                    );

                    //????????????+??????
                    ExchangeGoods exchangeGoods = exchangeGoodsMapper.normalSelectById(goodsId);
                    exchangeGoodsMapper.normalUpdate(null, Wrappers.<ExchangeGoods>lambdaUpdate()
                            .set(ExchangeGoods::getExchangeCount, exchangeGoods.getExchangeCount() + diff)
                            .eq(ExchangeGoods::getId, goodsId)
                    );

                } else {
                    exchangeGoodsMapper.normalUpdate(null, Wrappers.<ExchangeGoods>lambdaUpdate()
                            .set(ExchangeGoods::getExchangeCount, exchangeCount)
                            .eq(ExchangeGoods::getId, goodsId)
                    );
                }
                return null;
            });
        });
    }

    private void addOperateRecord(ExchangeOrder order, OrderStateEnum before, OrderStateEnum after) {
        ExchangeOrderOperateRecord record = new ExchangeOrderOperateRecord();
        record.setOrderId(order.getId());
        BeanUtil.copyProperties(order, record, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        record.setBeforeState(before);
        record.setAfterState(after);
        int count = exchangeOrderOperateRecordMapper.insert(record);
        if (count <= 0) {
            throw new ServiceException("??????????????????????????????");
        }
    }

    @Override
    public IPage<GoodsResult> pageGoods(GoodsPageParam param) {
        List<Long> goodsIdList = null;
        if (param.getCatalogId() != null) {
            goodsIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                    .select(CatalogRel::getBusinessId)
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.EXCHANGE)
                    .eq(CatalogRel::getCatalogId, param.getCatalogId())
            ).stream().map(CatalogRel::getBusinessId).collect(Collectors.toList());
        }
        DateTime now = DateTime.now();
        if (param.getValid() == WhetherEnum.YES) {
            param.setState(null);
        }
        GoodsStateEnum state = param.getState();
        return exchangeGoodsMapper.selectPage(param.page(), new QueryWrapper<ExchangeGoods>()
                .and(param.getValid() == WhetherEnum.YES, i -> i
                        .gt("inventory-exchange_count", 0)
                        .lambda()
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                        .le(ExchangeGoods::getShelveTime, now)
                )
                .and(state == GoodsStateEnum.SOLDOUT, i -> i
                        .le("inventory-exchange_count", 0)
                        .lambda()
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                        .le(ExchangeGoods::getShelveTime, now)
                )
                .and(state == GoodsStateEnum.NORMAL, i -> i
                        .gt("inventory-exchange_count", 0)
                        .lambda()
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                        .le(ExchangeGoods::getShelveTime, now)
                )
                .lambda()
                .eq(param.getGoodsType() != null, ExchangeGoods::getGoodsType, param.getGoodsType())
                .like(StrUtil.isNotBlank(param.getGoodsCode()), ExchangeGoods::getGoodsCode, param.getGoodsCode())
                .like(StrUtil.isNotBlank(param.getGoodsName()), ExchangeGoods::getGoodsName, param.getGoodsName())
                .ge(param.getShelveTimeStart() != null, ExchangeGoods::getShelveTime, param.getShelveTimeStart())
                .le(param.getShelveTimeEnd() != null, ExchangeGoods::getShelveTime, param.getShelveTimeEnd())
                .ge(param.getCreateTimeStart() != null, ExchangeGoods::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ExchangeGoods::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getCatalogId() != null && CollUtil.isEmpty(goodsIdList), ExchangeGoods::getId, param.getCatalogId())
                .in(CollUtil.isNotEmpty(goodsIdList), ExchangeGoods::getId, goodsIdList)
                .and(state == GoodsStateEnum.NOTSHELVE, i -> i
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                        .gt(ExchangeGoods::getShelveTime, now)
                )
                .and(state == GoodsStateEnum.PENDING, i -> i
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.PENDING)
                )
                .and(state == GoodsStateEnum.REJECT, i -> i
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.REJECT)
                )
                .eq(state == GoodsStateEnum.OFFSHELVE, ExchangeGoods::getState, state)
                .eq(state == GoodsStateEnum.INVALID, ExchangeGoods::getState, state)
                .like(ObjectUtil.isNotNull(param.getRelationId()), ExchangeGoods::getDesignateOrgIds, param.getRelationId())
                .orderByDesc(ExchangeGoods::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(goods -> {
            goods = stateHandler(goods, null);
            goods.setGoodsDetail(null);
            goods.setGoodsRemark(null);
            goods.setGoodsService(null);
            GoodsResult result = new GoodsResult();
            BeanUtil.copyProperties(goods, result);
            //??????????????????
            List<Long> catalogIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                    .select(CatalogRel::getCatalogId)
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.EXCHANGE)
                    .eq(CatalogRel::getBusinessId, goods.getId())
            ).stream().map(CatalogRel::getCatalogId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(catalogIdList)) {
                List<CatalogInfo> catalogList = catalogInfoMapper.selectList(Wrappers.<CatalogInfo>lambdaQuery()
                        .in(CatalogInfo::getId, catalogIdList)
                );
                result.setCatalogList(catalogList);
            }

            List<ExchangeGoodsSkuInfo> skuInfos = new ArrayList<>();
            if (goods.getGoodsType() == GoodsTypeEnum.PHYSICAL) {
                //????????????????????????
                List<ExchangeGoodsSkuInfo> dbSkuInfos = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                        .eq(ExchangeGoodsSkuInfo::getGoodsId, goods.getId())
                        .orderByAsc(ExchangeGoodsSkuInfo::getSinglePrice, ExchangeGoodsSkuInfo::getSingleReward)
                );

                if (null != dbSkuInfos && dbSkuInfos.size() > 0) {
                    skuInfos = dbSkuInfos;
                }
            }

            //?????????????????????????????????
            if (ObjectUtil.equal(result.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
                result.setExchangeCount(getPhysicalTotalExchangeCount(goods.getId(), skuInfos));
            } else {
                result.setExchangeCount(getTotalExchangeCount(goods.getId()));
            }

            //??????????????????
            if (JSONUtil.isJsonArray(goods.getGoodsImgs())) {
                result.setGoodsImgList(JSONUtil.toList(JSONUtil.parseArray(goods.getGoodsImgs()), String.class));
            }

            if (goods.getGoodsType() == GoodsTypeEnum.COUPON) {
                result.setCoupon(JSONUtil.toBean(goods.getRelContent(), GoodsCouponResult.class));
            } else if (goods.getGoodsType() == GoodsTypeEnum.PHYSICAL) {
                //???????????????????????????????????????
                if (null != skuInfos && skuInfos.size() > 0) {
                    result.setSingleBigPrice(skuInfos.get(0).getSinglePrice());
                    result.setSingleBigReward(skuInfos.get(0).getSingleReward());

                    if (skuInfos.size() == 1) {
                        result.setSingleSmallPrice(skuInfos.get(0).getSinglePrice());
                        result.setSingleSmallReward(skuInfos.get(0).getSingleReward());
                    } else if (skuInfos.size() > 1) {
                        result.setSingleSmallPrice(skuInfos.get(skuInfos.size() - 1).getSinglePrice());
                        result.setSingleSmallReward(skuInfos.get(skuInfos.size() - 1).getSingleReward());
                    }
                }

            }
            return result;
        });
    }

    @Transactional
    @Override
    public void addGoods(GoodsAddParam param) {
        //???????????????????????????
        if (param.getGoodsType().equals(GoodsTypeEnum.COUPON)) {
            if (Convert.toInt(param.getSingleReward(), 0) == 0 && param.getSinglePrice().compareTo(BigDecimal.ZERO) == 0) {
                throw new ServiceException("?????????????????????????????????????????????0");
            }

            //?????????????????????????????????0
            if (Convert.toInt(param.getInventory()) < 1) {
                throw new ServiceException("??????????????????1");
            }
        }

        //??????????????????sku???????????????????????????????????????
        if (param.getGoodsType().equals(GoodsTypeEnum.PHYSICAL)) {
            if (CollUtil.isEmpty(param.getSkuAddInfos())) {
                throw new ServiceException("????????????sku??????????????????");
            }

            if (CollUtil.isEmpty(param.getGoodsSpecValueAddInfos())) {
                throw new ServiceException("??????????????????????????????????????????");
            }
        }

        if (param.getGoodsType().equals(GoodsTypeEnum.PHYSICAL)) {
            //???????????????id???set??????????????????????????????set????????????????????????
            Set<Long> goodsSpecIdsSet = new HashSet<>();
            List<ExchangeGoodsSpecValueAddInfo> goodsSpecValueAddInfos = param.getGoodsSpecValueAddInfos();
            //?????????????????????????????????
            for (ExchangeGoodsSpecValueAddInfo goodsSpecValueAddInfo : goodsSpecValueAddInfos) {
                //????????????????????????
                goodsSpecIdsSet.add(goodsSpecValueAddInfo.getSpecId());
                String[] values = goodsSpecValueAddInfo.getSpecValueIds().split(",");
                Set<String> valuesSet = new HashSet<>(Arrays.asList(values));
                if (values.length != valuesSet.size()) {
                    throw new ServiceException("?????????????????????????????????");
                }
            }

            if (goodsSpecIdsSet.size() != goodsSpecValueAddInfos.size()) {
                throw new ServiceException("??????????????????????????????");
            }

            List<String> skuSpecValueIds = param.getSkuAddInfos().stream().map(ExchangeGoodsSkuAddInfo::getSpecValueIds).collect(Collectors.toList());
            Set<String> skuSet = new HashSet<>(skuSpecValueIds);
            if (skuSet.size() != skuSpecValueIds.size()) {
                throw new ServiceException("????????????sku???????????????????????????");
            }
        }

        //??????????????????
        if (param.getGoodsType().equals(GoodsTypeEnum.PHYSICAL)) {
            List<ExchangeGoodsSkuAddInfo> skuAddInfos = param.getSkuAddInfos();
            for (ExchangeGoodsSkuAddInfo item : skuAddInfos) {
                if (Convert.toInt(item.getSingleReward(), 0) == 0 && item.getSinglePrice().compareTo(BigDecimal.ZERO) == 0) {
                    throw new ServiceException("?????????????????????????????????????????????0");
                }
            }
        }

        Long relId = null;
        String relContent = null;
        AuditStateEnum auditState = null;
        Long goodsId = MdcUtil.getSnowflakeId();
        //??????????????????????????????????????????sku
        List<ExchangeGoodsSkuInfo> exchangeGoodsSkuInfos = new ArrayList<>();
        //????????????????????????
        int pInventory = 0;
        switch (param.getGoodsType()) {
            case COUPON: {
                if (BeanUtil.isEmpty(param.getCoupon())) {
                    throw new ServiceException("???????????????????????????");
                }
                CouponAddParam couponAddParam = new CouponAddParam();
                BeanUtil.copyProperties(param.getCoupon(), couponAddParam);
                couponAddParam.setCouponName(param.getGoodsName());
                couponAddParam.setInventory(param.getInventory());
                couponAddParam.setReceiveLimit(param.getExchangeLimit());
                couponAddParam.setCanShow(WhetherEnum.NO);
                couponAddParam.setReleaseType(ReleaseTypeEnum.IMMEDIATELY);
                couponAddParam.setDesignateOrgIds(param.getDesignateOrgIds());
                CouponInfo couponInfo = couponInfoService.addCoupon(couponAddParam, CreateSourceEnum.EXCHANGE, CouponModeEnum.NORMAL);
                relId = couponInfo.getId();
                GoodsCouponResult couponResult = new GoodsCouponResult();
                BeanUtil.copyProperties(param.getCoupon(), couponResult);
                relContent = JSONUtil.toJsonStr(couponResult);
                auditState = couponInfo.getAuditState();
                break;
            }
            case PHYSICAL: {
                //???????????????????????????
                List<ExchangeGoodsSpecValueInfo> exchangeGoodsSpecValueInfos = new ArrayList<>();
                List<ExchangeGoodsSpecValueAddInfo> goodsSpecValueAddInfos = param.getGoodsSpecValueAddInfos();
                for (ExchangeGoodsSpecValueAddInfo item : goodsSpecValueAddInfos) {
                    ExchangeGoodsSpecValueInfo info = new ExchangeGoodsSpecValueInfo();
                    BeanUtil.copyProperties(item, info);
                    info.setGoodsId(goodsId);
                    exchangeGoodsSpecValueInfos.add(info);
                }

                int goodsSpecValueCount = exchangeGoodsSpecValueInfoMapper.insertAllBatch(exchangeGoodsSpecValueInfos);
                if (goodsSpecValueCount < 0) {
                    throw new ServiceException("??????????????????????????????");
                }

                List<ExchangeGoodsSkuAddInfo> skuAddInfos = param.getSkuAddInfos();
                for (ExchangeGoodsSkuAddInfo item : skuAddInfos) {
                    ExchangeGoodsSkuInfo info = new ExchangeGoodsSkuInfo();
                    BeanUtil.copyProperties(item, info);
                    if (ObjectUtil.isNull(param.getRewardType())) {
                        param.setRewardType(item.getRewardType());
                    }
                    info.setGoodsId(goodsId);
                    info.setId(MdcUtil.getSnowflakeId());
                    info.setExchangeCount(0);
                    exchangeGoodsSkuInfos.add(info);
                    //?????????????????????
                    pInventory = pInventory + info.getInventory();
                }

                int goodsSkuCount = exchangeGoodsSkuInfoMapper.insertAllBatch(exchangeGoodsSkuInfos);
                if (goodsSkuCount < 0) {
                    throw new ServiceException("????????????sku????????????");
                }
                break;
            }
            default:
        }

        ExchangeGoods goods = new ExchangeGoods();
        BeanUtil.copyProperties(param, goods);
        goods.setId(goodsId);
        goods.setGoodsImgs(JSONUtil.toJsonStr(param.getGoodsImgList()));
        if (param.getShelveType() == ShelveTypeEnum.IMMEDIATELY) {
            goods.setShelveTime(DateTime.now());
        }
        if (param.getGoodsType() != GoodsTypeEnum.PHYSICAL) {
            goods.setExpressPrice(null);
        } else {
            goods.setRelId(null);
        }
        if (param.getSingleReward() == null) {
            goods.setRewardType(null);
        }
        if (param.getRefundType() == RefundTypeEnum.NONREFUNDABLE) {
            goods.setRefundExpired(WhetherEnum.NO);
        }
        if (ObjectUtil.equal(param.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
            //?????????????????????
            goods.setInventory(pInventory);
        }
        goods.setGoodsCode(MdcUtil.getTenantIncIdStr("goodsCode", DateTime.now().toString("yyMMdd"), 4));
        goods.setExchangeCount(0);
        goods.setRelId(relId);
        goods.setRelContent(relContent);
        goods.setAuditState(auditState);
        goods.setState(GoodsStateEnum.NORMAL);
        MdcUtil.setOrganInfo(goods);
        int count = exchangeGoodsMapper.insert(goods);
        if (count > 0) {
            //?????????????????????
            List<CatalogRel> relList = new ArrayList<>();
            for (Long catalogId : param.getCatalogIdList()) {
                CatalogRel rel = new CatalogRel();
                rel.setBusinessId(goods.getId());
                rel.setBusinessType(CatalogBusinessTypeEnum.EXCHANGE);
                rel.setCatalogId(catalogId);
                relList.add(rel);
            }
            int relCount = catalogRelMapper.insertAllBatch(relList);
            if (relCount <= 0) {
                throw new ServiceException("????????????????????????");
            }

            if (param.getGoodsType().equals(GoodsTypeEnum.COUPON)) {
                RedisUtil.zSetRedis().add("goodsInventory", goods.getId(), goods.getInventory());
                RedisUtil.redis().expire("goodsInventory", 30, TimeUnit.DAYS);
            } else if (param.getGoodsType().equals(GoodsTypeEnum.PHYSICAL)) {
                for (ExchangeGoodsSkuInfo item : exchangeGoodsSkuInfos) {
                    RedisUtil.zSetRedis().add("goodsInventory", item.getGoodsId().toString() + item.getId().toString(), item.getInventory());
                    RedisUtil.redis().expire("goodsInventory", 30, TimeUnit.DAYS);
                }
            }
        } else {
            throw new ServiceException("??????????????????");
        }
    }

    @Transactional
    @Override
    public void editGoods(GoodsEditParam param) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(param.getId());
        if (goods == null) {
            throw new ServiceException("???????????????");
        }
        if (MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE) {
            if (!StrUtil.containsAny(goods.getCreatorOrgIds(), StrUtil.split(MdcUtil.getCurrentOrgIds(), ","))) {
                throw new ServiceException("??????????????????????????????");
            }
        }

        List<ExchangeGoodsSkuEditInfo> skuEditInfos = new ArrayList<>();

        List<ExchangeGoodsSkuInfo> dbSkuInfos = new ArrayList<>();
        //????????????????????????????????????
        if (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
            List<ExchangeGoodsSkuEditInfo> skuEditInfosTemp = param.getSkuEditInfos();

            if (null != skuEditInfosTemp && skuEditInfosTemp.size() > 0) {
                skuEditInfos = skuEditInfosTemp;
            }

            //???????????????????????????sku
            List<ExchangeGoodsSkuInfo> selectSkuInfo = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                    .eq(ExchangeGoodsSkuInfo::getGoodsId, param.getId())
            );

            if (null != selectSkuInfo && selectSkuInfo.size() > 0) {
                dbSkuInfos = selectSkuInfo;
                for (ExchangeGoodsSkuInfo exchangeGoodsSkuInfo : selectSkuInfo) {
                    RedisUtil.transactionalLock("changeExchangeGoods" + param.getId() + exchangeGoodsSkuInfo.getId());
                }
            }
        } else if (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.COUPON)) {
            RedisUtil.transactionalLock("changeExchangeGoods" + param.getId());
        }

        //?????????????????????????????????????????????????????????????????????0
        if (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.COUPON)) {
            if (Convert.toInt(param.getSingleReward(), 0) == 0 && Convert.toInt(goods.getSingleReward(), 0) == 0
                    && Convert.toBigDecimal(param.getSinglePrice(), BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0
                    && Convert.toBigDecimal(goods.getSinglePrice(), BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0) {
                throw new ServiceException("?????????????????????????????????????????????0");
            }
        } else if (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
            for (ExchangeGoodsSkuEditInfo item : skuEditInfos) {
                if (Convert.toInt(item.getSingleReward(), 0) == 0 && Convert.toBigDecimal(item.getSinglePrice(), BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0) {
                    throw new ServiceException("?????????????????????????????????????????????0");
                }
            }
        }

        //????????????????????????
        if (CollUtil.isNotEmpty(param.getCatalogIdList())) {
            catalogRelMapper.delete(Wrappers.<CatalogRel>lambdaQuery()
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.EXCHANGE)
                    .eq(CatalogRel::getBusinessId, param.getId())
            );
            List<CatalogRel> relList = new ArrayList<>();
            for (Long catalogId : param.getCatalogIdList()) {
                CatalogRel rel = new CatalogRel();
                rel.setBusinessId(param.getId());
                rel.setBusinessType(CatalogBusinessTypeEnum.EXCHANGE);
                rel.setCatalogId(catalogId);
                relList.add(rel);
            }
            int relCount = catalogRelMapper.insertAllBatch(relList);
            if (relCount <= 0) {
                throw new ServiceException("????????????????????????");
            }
        }
        Long relId = goods.getRelId();
        String relContent = null;
        AuditStateEnum auditState = null;
        switch (goods.getGoodsType()) {
            case COUPON: {
                if (!BeanUtil.isEmpty(param.getCoupon()) || !StrUtil.isAllBlank(param.getGoodsName(), param.getDesignateOrgIds())
                        || param.getInventoryIncrease() != null || param.getExchangeLimit() != null) {
                    CouponEditParam couponEditParam = new CouponEditParam();
                    BeanUtil.copyProperties(param.getCoupon(), couponEditParam);
                    couponEditParam.setId(relId);
                    couponEditParam.setCouponName(param.getGoodsName());
                    couponEditParam.setInventoryIncrease(param.getInventoryIncrease());
                    couponEditParam.setReceiveLimit(param.getExchangeLimit());
                    couponEditParam.setDesignateOrgIds(param.getDesignateOrgIds());
                    CouponInfo couponInfo = couponInfoService.editCoupon(couponEditParam, CreateSourceEnum.EXCHANGE, CouponModeEnum.NORMAL);
                    auditState = couponInfo.getAuditState();
                    GoodsCouponResult couponResult = JSONUtil.toBean(goods.getRelContent(), GoodsCouponResult.class);
                    BeanUtil.copyProperties(param.getCoupon(), couponResult, CopyOptions.create().ignoreNullValue());
                    relContent = JSONUtil.toJsonStr(couponResult);
                }
                break;
            }
            case PHYSICAL: {
                if (MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE) {
                    auditState = AuditStateEnum.PENDING;
                }

                break;
            }
            default:
        }
        LambdaUpdateWrapper<ExchangeGoods> updateWrapper = Wrappers.lambdaUpdate();
        ExchangeGoods update = new ExchangeGoods();
        BeanUtil.copyProperties(param, update);
        update.setId(null);
        updateWrapper.eq(ExchangeGoods::getId, param.getId());
        if (CollUtil.isNotEmpty(param.getGoodsImgList())) {
            update.setGoodsImgs(JSONUtil.toJsonStr(param.getGoodsImgList()));
        }
        if (goods.getGoodsType() != GoodsTypeEnum.PHYSICAL) {
            update.setRelContent(relContent);
            update.setExpressPrice(null);
        } else {
            update.setRelId(null);
        }
        DateTime now = DateTime.now();
        if (param.getShelveType() == ShelveTypeEnum.IMMEDIATELY && goods.getShelveType() != ShelveTypeEnum.IMMEDIATELY) {
            if (now.isBefore(goods.getShelveTime())) {
                update.setShelveTime(now);
            } else {
                update.setShelveTime(null);
            }
        }
        if (param.getShelveType() == ShelveTypeEnum.REGULAR && goods.getShelveType() != ShelveTypeEnum.REGULAR) {
            update.setState(GoodsStateEnum.NORMAL);
        }
        if (param.getRefundType() == RefundTypeEnum.NONREFUNDABLE) {
            update.setRefundExpired(WhetherEnum.NO);
        }
        if (param.getDesignateOrgIds() != null && !StrUtil.equals(param.getDesignateOrgIds(), goods.getDesignateOrgIds())) {
            //???designateOrgIds???????????????,????????????null
            if (StrUtil.isBlank(param.getDesignateOrgIds())) {
                updateWrapper.set(ExchangeGoods::getDesignateOrgIds, null);
                updateWrapper.set(ExchangeGoods::getDesignateOrgNames, null);
                exchangeOrderMapper.update(new ExchangeOrder(), Wrappers.<ExchangeOrder>lambdaUpdate()
                        .eq(ExchangeOrder::getGoodsId, param.getId())
                        .set(ExchangeOrder::getDesignateOrgIds, goods.getCreatorOrgIds())
                        .set(ExchangeOrder::getDesignateOrgNames, goods.getCreatorOrgNames())
                );
            } else {
                MdcUtil.setOrganInfo(update);
                ExchangeOrder orderUpdate = new ExchangeOrder();
                orderUpdate.setDesignateOrgIds(param.getDesignateOrgIds());
                MdcUtil.setOrganInfo(orderUpdate);
                exchangeOrderMapper.update(orderUpdate, Wrappers.<ExchangeOrder>lambdaUpdate()
                        .eq(ExchangeOrder::getGoodsId, param.getId())
                );
            }

        } else if (param.getDesignateOrgIds() == null) {
            update.setDesignateOrgIds(null);
            update.setDesignateOrgNames(null);
        }

        switch (goods.getGoodsType()) {
            case PHYSICAL: {
                //?????????????????????(skuId, ExchangeGoodsSkuInfo)
                Map<Long, ExchangeGoodsSkuInfo> dbSkuInfosMap = new HashMap<>();
                for (ExchangeGoodsSkuInfo item : dbSkuInfos) {
                    dbSkuInfosMap.put(item.getId(), item);
                }
                Integer inventoryIncrease = 0;
                //?????????????????????
                for (ExchangeGoodsSkuEditInfo item : skuEditInfos) {
                    ExchangeGoodsSkuInfo skuInfo = dbSkuInfosMap.get(item.getId());
                    if (null == skuInfo) {
                        throw new ServiceException("????????????????????????????????????");
                    }
                    ExchangeGoodsSkuInfo info = new ExchangeGoodsSkuInfo();
                    BeanUtil.copyProperties(item, info);
                    int surplusInventory = skuInfo.getInventory() - getPhysicalSkuExchangeCount(goods.getId(), skuInfo.getId()) - getCacheCount(goods.getId(), skuInfo.getId());
                    if (item.getInventoryIncrease() != null && item.getInventoryIncrease() > 0) {
                        surplusInventory = surplusInventory + item.getInventoryIncrease();
                        if (surplusInventory < 0) {
                            throw new ServiceException("????????????????????????0");
                        }
                        info.setInventory(skuInfo.getInventory() + item.getInventoryIncrease());
                    }
                    inventoryIncrease = inventoryIncrease + (ObjectUtil.isNull(item.getInventoryIncrease()) ? 0 : item.getInventoryIncrease());
                    int skuUpdateCount = exchangeGoodsSkuInfoMapper.updateById(info);
                    if (skuUpdateCount <= 0) {
                        throw new ServiceException("??????????????????");
                    }
                    RedisUtil.zSetRedis().add("goodsInventory", goods.getId().toString() + skuInfo.getId(), surplusInventory);
                    RedisUtil.redis().expire("goodsInventory", 30, TimeUnit.DAYS);
                }
                //?????????????????????
                update.setInventory(goods.getInventory() + inventoryIncrease);
                update.setAuditState(auditState);
                int count = exchangeGoodsMapper.update(update, updateWrapper);
                if (count <= 0) {
                    throw new ServiceException("??????????????????");
                }
                break;
            }
            default:
        }
    }

    @Override
    public void toggleGoods(GoodsGetParam param) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(param.getId());
        ExchangeGoods update = new ExchangeGoods();
        update.setId(goods.getId());
        DateTime now = DateTime.now();
        if (goods.getShelveType() == ShelveTypeEnum.REGULAR && now.isBefore(goods.getShelveTime())) {
            update.setShelveTime(now);
        } else {
            update.setState(goods.getState() != GoodsStateEnum.OFFSHELVE ? GoodsStateEnum.OFFSHELVE : GoodsStateEnum.NORMAL);
        }
        int count = exchangeGoodsMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????");
        }
    }

    @Override
    public void auditGoods(GoodsAuditParam param, GoodsTypeEnum goodsType, Long relId) {
        ExchangeGoods goods = exchangeGoodsMapper.selectOne(Wrappers.<ExchangeGoods>lambdaQuery()
                .eq(ExchangeGoods::getGoodsType, goodsType)
                .eq(ExchangeGoods::getRelId, relId)
        );
        stateHandler(goods, null);
        auditGoods(param, goods);
    }

    @Override
    public void auditGoods(GoodsAuditParam param) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(param.getId());
        stateHandler(goods, null);
        if (goods.getGoodsType() != GoodsTypeEnum.PHYSICAL) {
            throw new ServiceException("???????????????????????????");
        }
        auditGoods(param, goods);
    }

    private void auditGoods(GoodsAuditParam param, ExchangeGoods goods) {
        if (goods.getAuditState() == AuditStateEnum.PENDING) {
            ExchangeGoods update = new ExchangeGoods();
            update.setId(goods.getId());
            update.setAuditState(param.getIsApprove() ? AuditStateEnum.APPROVE : AuditStateEnum.REJECT);
            update.setAuditRemark(param.getAuditRemark());
            int count = exchangeGoodsMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("????????????");
            }
        } else {
            throw new ServiceException("?????????????????????");
        }
    }

    private ExchangeGoods updateGoodsState(ExchangeGoods goods) {
        GoodsStateEnum state = goods.getState();
        //??????????????????????????????
        if (state == GoodsStateEnum.NORMAL) {
            //??????????????????????????????????????????,???????????????????????????
            switch (goods.getGoodsType()) {
                case COUPON: {
                    CouponInfo couponInfo = couponInfoService.getCoupon(goods.getRelId());
                    if (couponInfo.getState() != CouponStateEnum.NORMAL
                            && couponInfo.getState() != CouponStateEnum.EXPIRING) {
                        state = GoodsStateEnum.INVALID;
                    }
                    break;
                }
                default:
            }
            if (state == GoodsStateEnum.INVALID) {
                exchangeGoodsMapper.update(null, Wrappers.<ExchangeGoods>lambdaUpdate()
                        .set(ExchangeGoods::getState, state)
                        .eq(ExchangeGoods::getId, goods.getId())
                );
            }
        }
        goods.setState(state);
        return goods;
    }

    @Override
    public GoodsResult getGoods(GoodsGetParam param) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(param.getId());
        goods = stateHandler(goods, null);
        ExchangeGoods finalGoods = goods;
        //???????????????????????????????????????
        if (ObjectUtil.equal(GoodsTypeEnum.COUPON, goods.getGoodsType())) {
            goods = RedisUtil.syncLoad("changeExchangeGoods" + param.getId(), () -> updateGoodsState(finalGoods));
        }

        GoodsResult result = new GoodsResult();
        BeanUtil.copyProperties(goods, result);
        //????????????????????????
        List<Long> catalogIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                .select(CatalogRel::getCatalogId)
                .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.EXCHANGE)
                .eq(CatalogRel::getBusinessId, goods.getId())
        ).stream().map(CatalogRel::getCatalogId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(catalogIdList)) {
            List<CatalogInfo> catalogList = catalogInfoMapper.selectList(Wrappers.<CatalogInfo>lambdaQuery()
                    .in(CatalogInfo::getId, catalogIdList)
            );
            result.setCatalogList(catalogList);
        }
        if (JSONUtil.isJsonArray(goods.getGoodsImgs())) {
            result.setGoodsImgList(JSONUtil.toList(JSONUtil.parseArray(goods.getGoodsImgs()), String.class));
        }
        int successCount = exchangeOrderMapper.selectCount(Wrappers.<ExchangeOrder>lambdaQuery()
                .eq(ExchangeOrder::getGoodsId, goods.getId())
                .eq(ExchangeOrder::getOrderState, OrderStateEnum.SUCCESS)
        );
        if (goods.getGoodsType() == GoodsTypeEnum.COUPON) {
            result.setCoupon(JSONUtil.toBean(goods.getRelContent(), GoodsCouponResult.class));
        }
        result.setExchangeSuccessCount(successCount);
        //???????????????????????????????????????
        int totalExchangeCount = 0;
        List<ExchangeGoodsSkuInfo> skuInfos = new ArrayList<>();
        switch (goods.getGoodsType()) {
            case PHYSICAL: {
                List<ExchangeGoodsSkuInfo> dbSkuInfos = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                        .eq(ExchangeGoodsSkuInfo::getGoodsId, goods.getId())
                );

                if (null != dbSkuInfos && dbSkuInfos.size() > 0) {
                    skuInfos = dbSkuInfos;
                }

                totalExchangeCount = getPhysicalTotalExchangeCount(goods.getId(), skuInfos, true);
                break;
            }
            default:
        }
        result.setExchangeCount(totalExchangeCount);
        int surplusInventory = 0;
        switch (goods.getGoodsType()) {
            case PHYSICAL: {
                List<ExchangeGoodsSkuInfo> goodsSkuInfos = new ArrayList<>();
                if (null != skuInfos && skuInfos.size() > 0) {
                    goodsSkuInfos = skuInfos;
                    for (ExchangeGoodsSkuInfo item : skuInfos) {
                        int itemSurplusInventory = item.getInventory() - getPhysicalSkuExchangeCount(goods.getId(), item.getId(), true) - getCacheCount(goods.getId(), item.getId());
                        surplusInventory = surplusInventory + itemSurplusInventory;
                        RedisUtil.zSetRedis().add("goodsInventory", goods.getId() + item.getId().toString(), itemSurplusInventory);
                        RedisUtil.redis().expire("goodsInventory", 30, TimeUnit.DAYS);
                    }
                }

                result.setGoodsSkuInfos(goodsSkuInfos);
                break;
            }
            default:
        }
        switch (goods.getGoodsType()) {
            case COUPON: {
                couponInfoService.getTotalReceiveCount(goods.getRelId(), true);
                break;
            }
            case PHYSICAL: {
                List<GoodsSpecValueInfoResult> goodsSpecValueInfoResults = new ArrayList<>();
                //???????????????????????????????????????
                List<ExchangeGoodsSpecValueInfo> goodsSpecValueInfos = exchangeGoodsSpecValueInfoMapper.selectList(Wrappers.<ExchangeGoodsSpecValueInfo>lambdaQuery()
                        .eq(ExchangeGoodsSpecValueInfo::getGoodsId, goods.getId())
                        .orderByAsc(ExchangeGoodsSpecValueInfo::getSortNo)
                );
                if (null != goodsSpecValueInfos && goodsSpecValueInfos.size() > 0) {
                    //???????????????????????????
                    List<Long> specIds = goodsSpecValueInfos.stream().map(ExchangeGoodsSpecValueInfo::getSpecId).collect(Collectors.toList());

                    Map<Long, ExchangeSpecValueInfo> specMap = exchangeSpecValueInfoMapper.selectList(Wrappers.<ExchangeSpecValueInfo>lambdaQuery()
                            .in(ExchangeSpecValueInfo::getId, specIds)
                    ).stream().collect(Collectors.toMap(ExchangeSpecValueInfo::getId, exchangeSpecValueInfo -> exchangeSpecValueInfo));
                    for (ExchangeGoodsSpecValueInfo item : goodsSpecValueInfos) {
                        GoodsSpecValueInfoResult specValueInfoResult = new GoodsSpecValueInfoResult();
                        //??????????????????
                        BeanUtil.copyProperties(item, specValueInfoResult);

                        ExchangeSpecValueInfo info = specMap.get(specValueInfoResult.getSpecId());
                        if (null != info) {
                            specValueInfoResult.setName(info.getName());
                        }
                        //???????????????
                        String[] values = item.getSpecValueIds().split(",");
                        //???????????????
                        List<GoodsValueInfoResult> goodsValueInfoResults = new ArrayList<>();
                        List<ExchangeSpecValueInfo> valueInfos = exchangeSpecValueInfoMapper.selectList(Wrappers.<ExchangeSpecValueInfo>lambdaQuery()
                                .in(ExchangeSpecValueInfo::getId, ArrayUtil.cast(Object.class, values))
                        );
                        for (ExchangeSpecValueInfo valueInfo : valueInfos) {
                            goodsValueInfoResults.add(new GoodsValueInfoResult(valueInfo.getId(), valueInfo.getName()));
                        }
                        specValueInfoResult.setGoodsValueInfoResults(goodsValueInfoResults);
                        goodsSpecValueInfoResults.add(specValueInfoResult);
                    }
                }
                result.setGoodsSpecValueInfoResults(goodsSpecValueInfoResults);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + goods.getGoodsType());
        }
        //???????????????
        RedisUtil.zSetRedis().add("goodsInventory", goods.getId(), surplusInventory);
        RedisUtil.redis().expire("goodsInventory", 30, TimeUnit.DAYS);
        return result;
    }

    @Override
    public GoodsRankResult getGoodsRank(GoodsRankGetParam param) {
        AtomicLong i = new AtomicLong(1L);
        List<GoodsRankResult.GoodsSalesRank> countRank = exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                .select("goods_name," +
                        "coalesce(sum(case when exchange_time is not null then goods_count else 0 end), 0) value")
                .orderByDesc("value")
                .lambda()
                .eq(param.getPayMode() != null, ExchangeOrder::getPayMode, param.getPayMode())
                .ge(param.getCreateTimeStart() != null, ExchangeOrder::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ExchangeOrder::getCreateTime, param.getCreateTimeEnd())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
                .groupBy(ExchangeOrder::getGoodsId)
                .last("limit " + param.getLimit())
        ).stream().map(map -> {
            GoodsRankResult.GoodsSalesRank rank = BeanUtil.fillBeanWithMap(map, new GoodsRankResult.GoodsSalesRank(), true, CopyOptions.create().ignoreError());
            rank.setRankNo(i.getAndIncrement());
            return rank;
        }).collect(Collectors.toList());
        i.set(1L);
        List<GoodsRankResult.GoodsSalesRank> priceRank = exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                .select("goods_name," +
                        "coalesce(sum(case when exchange_time is not null then total_price else 0 end), 0) value")
                .orderByDesc("value")
                .lambda()
                .eq(param.getPayMode() != null, ExchangeOrder::getPayMode, param.getPayMode())
                .ge(param.getCreateTimeStart() != null, ExchangeOrder::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ExchangeOrder::getCreateTime, param.getCreateTimeEnd())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
                .groupBy(ExchangeOrder::getGoodsId)
                .last("limit " + param.getLimit())
        ).stream().map(map -> {
            GoodsRankResult.GoodsSalesRank rank = BeanUtil.fillBeanWithMap(map, new GoodsRankResult.GoodsSalesRank(), true, CopyOptions.create().ignoreError());
            rank.setRankNo(i.getAndIncrement());
            return rank;
        }).collect(Collectors.toList());
        GoodsRankResult result = new GoodsRankResult();
        result.setCountRank(countRank);
        result.setPriceRank(priceRank);
        return result;
    }

    @Override
    public OrderStatisticsResult getOrderStatistics(OrderStatisticsGetParam param) {
        DateTime now = DateTime.now();
        int overTime = getOvertime();
        int overConfirmDay = getOverConfirmDay();
        OrderStatisticsResult result = exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                .select("count(1) order_count," +
                        "coalesce(sum(case when order_state = 1 and create_time >= '" + now.offsetNew(DateField.MINUTE, -overTime) + "' then 1 else 0 end), 0) not_pay_count," +
                        "coalesce(sum(case when total_price > 0 and order_state != 1 and order_state != 7 and order_state != 8 then 1 else 0 end), 0) paid_count," +
                        "coalesce(sum(case when order_state = 5 and delivery_time >= '" + now.offsetNew(DateField.DAY_OF_YEAR, -overConfirmDay) +
                        "' and (refund_state is null or refund_state = 2 or refund_state = 3 ) then 1 else 0 end), 0) delivering_count," +
                        "coalesce(sum(case when (order_state = 6 or (order_state = 5 and delivery_time < '" + now.offsetNew(DateField.DAY_OF_YEAR, -overConfirmDay) +
                        "')) and refund_state is null then 1 else 0 end), 0) success_count," +
                        "coalesce(sum(case when refund_state is not null and refund_state != 2 and refund_state != 3 then 1 else 0 end), 0) refund_count," +
                        "coalesce(sum(case when exchange_time is not null then 1 else 0 end), 0) deal_count," +
                        "coalesce(sum(case when exchange_time is not null then total_price else 0 end), 0) deal_price"
                )
                .lambda()
                .eq(param.getPayMode() != null, ExchangeOrder::getPayMode, param.getPayMode())
                .ge(param.getCreateTimeStart() != null, ExchangeOrder::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ExchangeOrder::getCreateTime, param.getCreateTimeEnd())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).stream().map((map) -> BeanUtil.fillBeanWithMap(map, new OrderStatisticsResult(), true,
                CopyOptions.create().ignoreError())).collect(Collectors.toList()).get(0);
        OrderStatisticsResult statisticsResult = exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                .select("count(distinct member_id) deal_member_count")
                .lambda()
                .isNotNull(ExchangeOrder::getExchangeTime)
                .eq(param.getPayMode() != null, ExchangeOrder::getPayMode, param.getPayMode())
                .ge(param.getCreateTimeStart() != null, ExchangeOrder::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ExchangeOrder::getCreateTime, param.getCreateTimeEnd())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).stream().map((map) -> BeanUtil.fillBeanWithMap(map, new OrderStatisticsResult(), true,
                CopyOptions.create().ignoreError())).collect(Collectors.toList()).get(0);
        BeanUtil.copyProperties(statisticsResult, result, CopyOptions.create().ignoreNullValue());
        if (result.getOrderCount() == 0) {
            result.setDealRate(NumberUtil.formatPercent(0, 2));
        } else {
            result.setDealRate(NumberUtil.formatPercent(NumberUtil.div(result.getDealCount(), result.getOrderCount()).doubleValue(), 2));
        }
        if (result.getDealMemberCount() == 0) {
            result.setDealMemberPrice(BigDecimal.ZERO);
        } else {
            result.setDealMemberPrice(NumberUtil.div(result.getDealPrice(), result.getDealMemberCount(), 2));
        }
        return result;
    }

    @Override
    public List<OrderAnalysisResult> getOrderAnalysis(OrderAnalysisGetParam param) {
        Map<Date, OrderAnalysisResult> analysis = exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                .select("date_format(create_time,'%Y-%m-%d') as create_date," +
                        "coalesce(sum(case when exchange_time is not null then 1 else 0 end), 0) deal_count," +
                        "coalesce(sum(case when exchange_time is not null then total_price else 0 end), 0) deal_price"
                )
                .groupBy("create_date")
                .lambda()
                .ge(ExchangeOrder::getCreateTime, param.getCreateTimeStart())
                .le(ExchangeOrder::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getPayMode() != null, ExchangeOrder::getPayMode, param.getPayMode())
        ).stream().map((map) -> BeanUtil.fillBeanWithMap(map, new OrderAnalysisResult(), true, CopyOptions.create().ignoreError()))
                .collect(Collectors.toMap(OrderAnalysisResult::getCreateDate, result -> result));
        return DateUtil.rangeToList(param.getCreateTimeStart(), param.getCreateTimeEnd(), DateField.DAY_OF_YEAR).stream().map(dateTime -> {
            OrderAnalysisResult result = analysis.get(dateTime);
            if (result == null) {
                result = new OrderAnalysisResult();
                result.setCreateDate(dateTime);
                result.setDealCount(0);
                result.setDealPrice(BigDecimal.ZERO);
            }
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public IPage<ExchangeOrder> pageOrder(OrderPageParam param) {
        OrderStateEnum state = param.getOrderState();
        DateTime now = DateTime.now();
        return exchangeOrderMapper.selectPage(param.page(), Wrappers.<ExchangeOrder>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getOrderCode()), ExchangeOrder::getOrderCode, param.getOrderCode())
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), ExchangeOrder::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), ExchangeOrder::getNickName, param.getNickName())
                .eq(param.getGoodsId() != null, ExchangeOrder::getGoodsId, param.getGoodsId())
                .eq(param.getGoodsType() != null, ExchangeOrder::getGoodsType, param.getGoodsType())
                .like(StrUtil.isNotBlank(param.getGoodsName()), ExchangeOrder::getGoodsName, param.getGoodsName())
                .eq(param.getPayMode() != null, ExchangeOrder::getPayMode, param.getPayMode())
                .ge(param.getExchangeTimeStart() != null, ExchangeOrder::getExchangeTime, param.getExchangeTimeStart())
                .le(param.getExchangeTimeEnd() != null, ExchangeOrder::getExchangeTime, param.getExchangeTimeEnd())
                .eq(param.getDeliveryType() != null, ExchangeOrder::getDeliveryType, param.getDeliveryType())
                .like(StrUtil.isNotBlank(param.getReceiveName()), ExchangeOrder::getReceiveName, param.getReceiveName())
                .like(StrUtil.isNotBlank(param.getReceivePhone()), ExchangeOrder::getReceivePhone, param.getReceivePhone())
                .ge(param.getPayTimeStart() != null, ExchangeOrder::getPayTime, param.getPayTimeStart())
                .le(param.getPayTimeEnd() != null, ExchangeOrder::getPayTime, param.getPayTimeEnd())
                .ge(param.getCreateTimeStart() != null, ExchangeOrder::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ExchangeOrder::getCreateTime, param.getCreateTimeEnd())
                .and(state == OrderStateEnum.NOTPAY, i -> i
                        .eq(ExchangeOrder::getOrderState, param.getOrderState())
                        .ge(ExchangeOrder::getCreateTime, now.offsetNew(DateField.MINUTE, -getOvertime()))
                )
                .eq(state == OrderStateEnum.FAILED, ExchangeOrder::getOrderState, OrderStateEnum.FAILED)
                .and(state == OrderStateEnum.SUCCESS, i -> i
                        .nested(j -> j.eq(ExchangeOrder::getOrderState, OrderStateEnum.SUCCESS)
                                .or(k -> k.eq(ExchangeOrder::getOrderState, OrderStateEnum.DELIVERYING)
                                        .lt(ExchangeOrder::getDeliveryTime, now.offsetNew(DateField.DAY_OF_YEAR, -getOverConfirmDay())))
                        )
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == OrderStateEnum.NOTCONSUME, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTCONSUME)
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == OrderStateEnum.PENDING, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.PENDING)
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == OrderStateEnum.NOTDELIVERY, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTDELIVERY)
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == OrderStateEnum.DELIVERYING, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.DELIVERYING)
                        .ge(ExchangeOrder::getDeliveryTime, now.offsetNew(DateField.DAY_OF_YEAR, -getOverConfirmDay()))
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == OrderStateEnum.CLOSED, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.CLOSED).or()
                        .nested(j -> j.eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY)
                                .lt(ExchangeOrder::getCreateTime, now.offsetNew(DateField.MINUTE, -getOvertime())))
                )
                .eq(state == OrderStateEnum.REFUNDAUDIT, ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT)
                .eq(state == OrderStateEnum.REFUNDING, ExchangeOrder::getOrderState, OrderStateEnum.REFUNDING)
                .eq(state == OrderStateEnum.REFUNDSUCC, ExchangeOrder::getOrderState, OrderStateEnum.REFUNDSUCC)
                .eq(state == OrderStateEnum.REFUNDFAIL, ExchangeOrder::getOrderState, OrderStateEnum.REFUNDFAIL)
                .orderByDesc(ExchangeOrder::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(this::stateHandler);
    }

    @Override
    public OrderResult getOrder(OrderGetParam param) {
        ExchangeOrder order = exchangeOrderMapper.selectById(param.getId());
        order = stateHandler(order);
        OrderResult result = new OrderResult();
        BeanUtil.copyProperties(order, result);
        List<EasypayPaymentRecord> paymentRecordList = easypayPaymentRecordMapper.selectList(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayPaymentRecord::getOrderId, param.getId())
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
        );
        List<EasypayRefundRecord> refundRecordList = easypayRefundRecordMapper.selectList(Wrappers.<EasypayRefundRecord>lambdaQuery()
                .eq(EasypayRefundRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayRefundRecord::getOrderId, param.getId())
                .orderByDesc(EasypayRefundRecord::getCreateTime)
        );
        List<ExchangeOrderOperateRecord> operateRecordList = exchangeOrderOperateRecordMapper.selectList(Wrappers.<ExchangeOrderOperateRecord>lambdaQuery()
                .eq(ExchangeOrderOperateRecord::getOrderId, param.getId())
                .orderByDesc(ExchangeOrderOperateRecord::getCreateTime)
        );
        result.setPaymentRecordList(paymentRecordList);
        result.setRefundRecordList(refundRecordList);
        result.setOperateRecordList(operateRecordList);
        if (order.getGoodsType() == GoodsTypeEnum.COUPON) {
            result.setCoupon(JSONUtil.toBean(order.getRelContent(), GoodsCouponResult.class));
        }
        return result;
    }

    @Override
    public void editOrderReceive(OrderReceiveEditParam param) {
        ExchangeOrder order = exchangeOrderMapper.selectById(param.getId());
        order = stateHandler(order);
        if (StrUtil.isBlank(param.getOrderOperatorRemark())) {
            if (order.getGoodsType() != GoodsTypeEnum.PHYSICAL) {
                throw new ServiceException("????????????????????????,????????????????????????");
            }
            if (order.getOrderState().getType() > 4) {
                throw new ServiceException("???????????????,????????????????????????");
            }
        } else {
            if (order.getGoodsType() != GoodsTypeEnum.PHYSICAL || order.getOrderState().getType() > 4) {
                param = new OrderReceiveEditParam().setId(param.getId()).setOrderOperatorRemark(param.getOrderOperatorRemark());
            }
        }
        ExchangeOrder update = BeanUtil.copyProperties(param, ExchangeOrder.class);
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
    }

    @Override
    public void editOrder(OrderEditParam param) {
        ExchangeOrder update = new ExchangeOrder();
        BeanUtil.copyProperties(param, update);
        exchangeOrderMapper.update(update, Wrappers.<ExchangeOrder>lambdaUpdate()
                .in(ExchangeOrder::getId, param.getIdList())
        );
    }

    @Override
    public void finishExchangeOrder(OrderGetParam param) {
        ExchangeOrder order = exchangeOrderMapper.selectById(param.getId());
        order = stateHandler(order);
        if (order.getOrderState() != OrderStateEnum.NOTPAY && order.getOrderState() != OrderStateEnum.FAILED && order.getOrderState() != OrderStateEnum.CLOSED) {
            throw new ServiceException("???????????????????????????");
        }
        EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayPaymentRecord::getOrderId, order.getId())
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
                .last("limit 1")
        );
        if (record == null) {
            throw new ServiceException("?????????????????????");
        }
        boolean isPaid;
        if (record.getPayState() != WhetherEnum.YES) {
            isPaid = easyPayService.checkOrder(record);
        } else {
            isPaid = true;
        }
        RedisUtil.syncLoad("changeExchangeOrder" + param.getId(), () -> {
            finishOrder(isPaid, null, record);
            return null;
        });
    }

    @Transactional
    @Override
    public void refundOrder(OrderGetParam param) {
        RedisUtil.transactionalLock("changeExchangeOrder" + param.getId());
        ExchangeOrder order = exchangeOrderMapper.selectById(param.getId());
        order = stateHandler(order);
        OrderStateEnum originalState = order.getOrderState();
        if (originalState != OrderStateEnum.REFUNDFAIL && originalState != OrderStateEnum.FAILED
                && originalState != OrderStateEnum.NOTCONSUME && originalState != OrderStateEnum.PENDING) {
            throw new ServiceException("???????????????" + originalState.getName() + "????????????");
        }
        if (order.getRefundType() == RefundTypeEnum.NONREFUNDABLE
                && (originalState == OrderStateEnum.NOTCONSUME || originalState == OrderStateEnum.PENDING)) {
            throw new ServiceException("?????????????????????");
        }
        EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayPaymentRecord::getOrderId, order.getId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
                .last("limit 1")
        );
        if (record == null) {
            throw new ServiceException("?????????????????????");
        }
        switch (order.getGoodsType()) {
            case COUPON: {
                couponInfoService.revokedRel(ReceiveSourceEnum.EXCHANGE, order.getId(), CreateSourceEnum.EXCHANGE, false);
                break;
            }
            default:
        }
        ExchangeOrder update = new ExchangeOrder();
        update.setId(order.getId());
        update.setOrderState(OrderStateEnum.REFUNDING);
        update.setRefundState(RefundStateEnum.REFUNDING);
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????");
        }
        easyPayService.refundExchangeOrder(new RefundParam().setAppId(record.getAppId()).setPayType(record.getPayType()).setId(order.getId()).setOrderNo(record.getOrderNo())
                .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount()).setRefundReason(order.getOrderState().getName() + ",????????????"));
        addOperateRecord(order, originalState, OrderStateEnum.REFUNDING);
    }

    @Transactional
    @Override
    public void confirmOrder(OrderGetParam param) {
        RedisUtil.transactionalLock("changeExchangeOrder" + param.getId());
        ExchangeOrder order = exchangeOrderMapper.selectById(param.getId());
        order = stateHandler(order);
        if (order.getOrderState() != OrderStateEnum.PENDING) {
            throw new ServiceException("???????????????????????????");
        }
        ExchangeOrder update = new ExchangeOrder();
        update.setId(order.getId());
        update.setOrderState(OrderStateEnum.NOTDELIVERY);
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????");
        }
        addOperateRecord(order, OrderStateEnum.PENDING, OrderStateEnum.NOTDELIVERY);
    }

    @Transactional
    @Override
    public void deliverOrder(OrderDeliverParam param) {
        RedisUtil.transactionalLock("changeExchangeOrder" + param.getId());
        ExchangeOrder order = exchangeOrderMapper.selectById(param.getId());
        order = stateHandler(order);
        if (order.getOrderState() != OrderStateEnum.NOTDELIVERY) {
            throw new ServiceException("???????????????????????????");
        }
        ExchangeOrder update = new ExchangeOrder();
        BeanUtil.copyProperties(param, update);
        update.setOrderState(OrderStateEnum.DELIVERYING);
        update.setDeliveryTime(DateTime.now());
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????");
        }
        addOperateRecord(order, OrderStateEnum.NOTDELIVERY, OrderStateEnum.DELIVERYING);
    }

    @Transactional
    @Override
    public void auditOrderRefund(OrderRefundAuditParam param) {
        RedisUtil.transactionalLock("changeExchangeOrder" + param.getId());
        ExchangeOrder order = exchangeOrderMapper.selectById(param.getId());
        if (order == null) {
            throw new ServiceException("???????????????");
        }
        DateTime now = DateTime.now();
        if (order.getOrderState() == OrderStateEnum.NOTPAY) {
            if (now.offsetNew(DateField.MINUTE, -getOvertime()).after(order.getCreateTime())) {
                order.setOrderState(OrderStateEnum.CLOSED);
            }
        } else if (order.getOrderState() == OrderStateEnum.DELIVERYING) {
            if (now.offsetNew(DateField.DAY_OF_YEAR, -getOverConfirmDay()).after(order.getDeliveryTime())) {
                order.setOrderState(OrderStateEnum.SUCCESS);
            }
        }
        OrderStateEnum originalState = order.getOrderState();
        if (order.getRefundState() == RefundStateEnum.REFUNDAUDIT) {
            order.setOrderState(OrderStateEnum.REFUNDAUDIT);
        }
        if (order.getOrderState() != OrderStateEnum.REFUNDAUDIT) {
            throw new ServiceException("???????????????????????????");
        }
        EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayPaymentRecord::getOrderId, order.getId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
                .last("limit 1")
        );
        if (record == null) {
            throw new ServiceException("?????????????????????");
        }
        //?????????????????????????????????
            /*if (param.getIsApprove()) {
                switch (order.getGoodsType()) {
                    case COUPON: {
                        couponInfoService.revokedRel(ReceiveSourceEnum.EXCHANGE, order.getId(), CreateSourceEnum.EXCHANGE);
                        break;
                    }
                }
            }*/
        ExchangeOrder update = new ExchangeOrder();
        update.setId(param.getId());
        update.setOrderState(param.getIsApprove() ? OrderStateEnum.REFUNDING : null);
        update.setRefundState(param.getIsApprove() ? RefundStateEnum.REFUNDING : RefundStateEnum.REFUNDREJECT);
        update.setRefundAuditRemark(param.getAuditRemark());
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????");
        }
        if (param.getIsApprove()) {
            //??????????????????
                /*RedisUtil.syncLoad("changeExchangeGoods" + order.getGoodsId(), () -> {
                    ExchangeGoods exchangeGoods = exchangeGoodsMapper.selectById(order.getGoodsId());
                    ExchangeGoods goodsUpdate = new ExchangeGoods();
                    goodsUpdate.setId(order.getGoodsId());
                    goodsUpdate.setExchangeCount(exchangeGoods.getExchangeCount() - order.getGoodsCount());
                    int updateCount = exchangeGoodsMapper.updateById(goodsUpdate);
                    if (updateCount <= 0) {
                        throw new ServiceException("????????????????????????");
                    }
                    return null;
                });*/
            easyPayService.refundExchangeOrder(new RefundParam().setAppId(record.getAppId()).setPayType(record.getPayType()).setId(order.getId()).setOrderNo(record.getOrderNo())
                    .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount())
                    .setRefundReason(Convert.toStr(param.getAuditRemark(), "????????????")));
            addOperateRecord(order, OrderStateEnum.REFUNDAUDIT, OrderStateEnum.REFUNDING);
        } else {
            addOperateRecord(order, OrderStateEnum.REFUNDAUDIT, originalState);
        }
    }

    @Transactional
    @Override
    public void enforceRefundOrder(OrderEnforceRefundParam param) {
        RedisUtil.transactionalLock("changeExchangeOrder" + param.getId());
        ExchangeOrder order = exchangeOrderMapper.selectOne(Wrappers.<ExchangeOrder>lambdaQuery()
                .eq(ExchangeOrder::getId, param.getId()).or()
                .eq(ExchangeOrder::getOrderCode, param.getOrderCode())
        );
        order = stateHandler(order);
        OrderStateEnum originalState = order.getOrderState();
        if (originalState == OrderStateEnum.NOTPAY || originalState == OrderStateEnum.CLOSED
                || originalState == OrderStateEnum.REFUNDING || originalState == OrderStateEnum.REFUNDSUCC) {
            throw new ServiceException("???????????????" + originalState.getName() + "????????????");
        }
        EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayPaymentRecord::getOrderId, order.getId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
                .last("limit 1")
        );
        if (record == null) {
            throw new ServiceException("?????????????????????");
        }
        switch (order.getGoodsType()) {
            case COUPON: {
                couponInfoService.revokedRel(ReceiveSourceEnum.EXCHANGE, order.getId(), CreateSourceEnum.EXCHANGE, true);
                break;
            }
            default:
        }
        ExchangeOrder update = new ExchangeOrder();
        update.setId(order.getId());
        update.setOrderState(OrderStateEnum.REFUNDING);
        update.setRefundState(RefundStateEnum.REFUNDING);
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????");
        }
        easyPayService.refundExchangeOrder(new RefundParam().setAppId(record.getAppId()).setPayType(record.getPayType()).setId(order.getId()).setOrderNo(record.getOrderNo())
                .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount()).setRefundReason(order.getOrderState().getName() + ",????????????"));
        addOperateRecord(order, originalState, OrderStateEnum.REFUNDING);
    }

    @Override
    public IPage<RecommendGoodsResult> pageRecommend(RecommendGoodsPageParam param) {
        DateTime now = DateTime.now();
        GoodsStateEnum state = param.getState();
        return exchangeRecommendRelMapper.selectPageRecommend(param.page(), new QueryWrapper<ExchangeGoods>()
                .eq("a.recommend_type", param.getRecommendType())
                .orderByAsc("a.recommend_order")
                .orderByDesc("a.create_time")
                .and(state == GoodsStateEnum.SOLDOUT, i -> i
                        .le("inventory-exchange_count", 0)
                        .lambda()
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                        .le(ExchangeGoods::getShelveTime, now)
                )
                .and(state == GoodsStateEnum.NORMAL, i -> i
                        .gt("inventory-exchange_count", 0)
                        .lambda()
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                        .le(ExchangeGoods::getShelveTime, now)
                )
                .lambda()
                .eq(param.getGoodsType() != null, ExchangeGoods::getGoodsType, param.getGoodsType())
                .like(StrUtil.isNotBlank(param.getGoodsCode()), ExchangeGoods::getGoodsCode, param.getGoodsCode())
                .like(StrUtil.isNotBlank(param.getGoodsName()), ExchangeGoods::getGoodsName, param.getGoodsName())
                .ge(param.getShelveTimeStart() != null, ExchangeGoods::getShelveTime, param.getShelveTimeStart())
                .le(param.getShelveTimeEnd() != null, ExchangeGoods::getShelveTime, param.getShelveTimeEnd())
                .and(state == GoodsStateEnum.NOTSHELVE, i -> i
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                        .gt(ExchangeGoods::getShelveTime, now)
                )
                .and(state == GoodsStateEnum.PENDING, i -> i
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.PENDING)
                )
                .and(state == GoodsStateEnum.REJECT, i -> i
                        .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                        .eq(ExchangeGoods::getAuditState, AuditStateEnum.REJECT)
                )
                .eq(state == GoodsStateEnum.OFFSHELVE, ExchangeGoods::getState, state)
                .eq(state == GoodsStateEnum.INVALID, ExchangeGoods::getState, state)
        ).convert(result -> {
            switch (result.getGoodsType()) {
                case PHYSICAL: {
                    List<ExchangeGoodsSkuInfo> skuInfos = new ArrayList<>();
                    //????????????????????????
                    List<ExchangeGoodsSkuInfo> dbSkuInfos = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                            .eq(ExchangeGoodsSkuInfo::getGoodsId, result.getId())
                            .orderByAsc(ExchangeGoodsSkuInfo::getSinglePrice, ExchangeGoodsSkuInfo::getSingleReward)
                    );

                    if (null != dbSkuInfos && dbSkuInfos.size() > 0) {
                        skuInfos = dbSkuInfos;
                        result.setSingleBigPrice(skuInfos.get(0).getSinglePrice());
                        result.setSingleBigReward(skuInfos.get(0).getSingleReward());

                        if (skuInfos.size() == 1) {
                            result.setSingleSmallPrice(skuInfos.get(0).getSinglePrice());
                            result.setSingleSmallReward(skuInfos.get(0).getSingleReward());
                        } else if (skuInfos.size() > 1) {
                            result.setSingleSmallPrice(skuInfos.get(skuInfos.size() - 1).getSinglePrice());
                            result.setSingleSmallReward(skuInfos.get(skuInfos.size() - 1).getSingleReward());
                        }
                    }

                    result.setExchangeCount(getPhysicalTotalExchangeCount(result.getId(), skuInfos));
                    break;
                }
                default:
            }

            return result;
        });
    }

    @Override
    public void addRecommend(RecommendGoodsAddParam param) {
        List<ExchangeRecommendRel> relList = new ArrayList<>();
        for (Long id : param.getGoodsIdList()) {
            ExchangeRecommendRel rel = new ExchangeRecommendRel();
            rel.setGoodsId(id);
            rel.setRecommendType(param.getRecommendType());
            rel.setRecommendOrder(0);
            relList.add(rel);
        }
        int count = exchangeRecommendRelMapper.insertIgnoreAllBatch(relList);
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
    }

    @Override
    public void editRecommend(RecommendGoodsEditParam param) {
        for (RecommendGoodsEditParam.RecommendRel recommendRel : param.getRecommendRelList()) {
            ExchangeRecommendRel rel = new ExchangeRecommendRel();
            rel.setId(recommendRel.getRecommendId());
            rel.setRecommendOrder(recommendRel.getRecommendOrder());
            exchangeRecommendRelMapper.updateById(rel);
        }
    }

    @Override
    public void deleteRecommend(RecommendGoodsDeleteParam param) {
        int count = exchangeRecommendRelMapper.deleteBatchIds(param.getRecommendIdList());
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
    }

    @Override
    public IPage<ExchangeGoodsResult> pageExchangeGoods(ExchangeGoodsPageParam param) {
        List<Long> goodsIdList = null;
        if (param.getCatalogId() != null) {
            goodsIdList = catalogRelMapper.selectList(Wrappers.<CatalogRel>lambdaQuery()
                    .select(CatalogRel::getBusinessId)
                    .eq(CatalogRel::getBusinessType, CatalogBusinessTypeEnum.EXCHANGE)
                    .eq(CatalogRel::getCatalogId, param.getCatalogId())
            ).stream().map(CatalogRel::getBusinessId).collect(Collectors.toList());
        }
        return exchangeGoodsMapper.selectPage(param.page(), new QueryWrapper<ExchangeGoods>()
                .lambda()
                .eq(param.getCatalogId() != null && CollUtil.isEmpty(goodsIdList), ExchangeGoods::getId, param.getCatalogId())
                .in(CollUtil.isNotEmpty(goodsIdList), ExchangeGoods::getId, goodsIdList)
                .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                .le(ExchangeGoods::getShelveTime, DateTime.now())
                .orderByDesc(ExchangeGoods::getCreateTime)
        ).convert(goods -> {
            goods = stateHandler(goods, null);
            goods.setGoodsDetail(null);
            goods.setGoodsRemark(null);
            goods.setGoodsService(null);
            ExchangeGoodsResult result = BeanUtil.copyProperties(goods, ExchangeGoodsResult.class);
            if (JSONUtil.isJsonArray(goods.getGoodsImgs())) {
                result.setGoodsImgList(JSONUtil.toList(JSONUtil.parseArray(goods.getGoodsImgs()), String.class));
            }

            //????????????????????????
            List<ExchangeGoodsSkuInfo> skuInfos = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                    .eq(ExchangeGoodsSkuInfo::getGoodsId, goods.getId())
                    .orderByAsc(ExchangeGoodsSkuInfo::getSinglePrice, ExchangeGoodsSkuInfo::getSingleReward)
            );

            //???????????????????????????????????????????????????
            if (ObjectUtil.equal(result.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
                result.setExchangeCount(getPhysicalTotalExchangeCount(goods.getId(), skuInfos));
            } else {
                result.setExchangeCount(getTotalExchangeCount(goods.getId()));
            }

            if (goods.getGoodsType() == GoodsTypeEnum.COUPON) {
                result.setCoupon(JSONUtil.toBean(goods.getRelContent(), GoodsCouponResult.class));
            } else if (goods.getGoodsType() == GoodsTypeEnum.PHYSICAL) {
                //????????????????????????
                if (null != skuInfos && skuInfos.size() > 0) {
                    ExchangeGoodsSkuInfo exchangeGoodsSkuInfo = skuInfos.get(0);
                    result.setSingleSmallPrice(exchangeGoodsSkuInfo.getSinglePrice());
                    result.setSingleSmallReward(exchangeGoodsSkuInfo.getSingleReward());
                }
            }
            return result;
        });
    }

    @Override
    public IPage<ExchangeGoodsResult> pageRecommendGoods(RecommendPageParam param) {
        return exchangeRecommendRelMapper.selectPageRecommend(param.page(), new QueryWrapper<ExchangeGoods>()
                .eq("a.recommend_type", param.getRecommendType())
                .orderByAsc("a.recommend_order")
                .orderByDesc("a.create_time")
                .lambda()
                .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                .le(ExchangeGoods::getShelveTime, DateTime.now())
        ).convert(recommendGoods -> {
            ExchangeGoods goods = BeanUtil.copyProperties(recommendGoods, ExchangeGoods.class);
            goods = stateHandler(goods, null);
            goods.setGoodsDetail(null);
            goods.setGoodsRemark(null);
            goods.setGoodsService(null);
            ExchangeGoodsResult result = BeanUtil.copyProperties(goods, ExchangeGoodsResult.class);
            if (JSONUtil.isJsonArray(goods.getGoodsImgs())) {
                result.setGoodsImgList(JSONUtil.toList(JSONUtil.parseArray(goods.getGoodsImgs()), String.class));
            }
            //????????????????????????
            List<ExchangeGoodsSkuInfo> skuInfos = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                    .eq(ExchangeGoodsSkuInfo::getGoodsId, goods.getId())
                    .orderByAsc(ExchangeGoodsSkuInfo::getSinglePrice, ExchangeGoodsSkuInfo::getSingleReward)
            );

            //?????????????????????????????????
            result.setExchangeCount(ObjectUtil.equal(result.getGoodsType(), GoodsTypeEnum.PHYSICAL) ? getPhysicalTotalExchangeCount(goods.getId(), skuInfos) : getTotalExchangeCount(goods.getId()));
//            result.setExchangeCount(getTotalExchangeCount(goods.getId()));
            if (goods.getGoodsType() == GoodsTypeEnum.COUPON) {
                result.setCoupon(JSONUtil.toBean(goods.getRelContent(), GoodsCouponResult.class));
            } else if (goods.getGoodsType() == GoodsTypeEnum.PHYSICAL) {
                //????????????????????????
                if (null != skuInfos && skuInfos.size() > 0) {
                    ExchangeGoodsSkuInfo exchangeGoodsSkuInfo = skuInfos.get(0);
                    result.setSingleSmallPrice(exchangeGoodsSkuInfo.getSinglePrice());
                    result.setSingleSmallReward(exchangeGoodsSkuInfo.getSingleReward());
                }
            }
            return result;
        });
    }

    @Override
    public ExchangeGoodsResult getExchangeGoods(GoodsGetParam param) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(param.getId());
        goods = stateHandler(goods, null);
        ExchangeGoodsResult result = BeanUtil.copyProperties(goods, ExchangeGoodsResult.class);
        if (JSONUtil.isJsonArray(goods.getGoodsImgs())) {
            result.setGoodsImgList(JSONUtil.toList(JSONUtil.parseArray(goods.getGoodsImgs()), String.class));
        }

        //??????????????????????????????
        switch (goods.getGoodsType()) {
            case PHYSICAL: {
                List<ExchangeGoodsSkuInfo> skuInfos = exchangeGoodsSkuInfoMapper.selectList(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                        .eq(ExchangeGoodsSkuInfo::getGoodsId, goods.getId())
                        .orderByAsc(ExchangeGoodsSkuInfo::getSinglePrice, ExchangeGoodsSkuInfo::getSingleReward)
                );

                //????????????????????????
                if (null != skuInfos && skuInfos.size() > 0) {
                    ExchangeGoodsSkuInfo exchangeGoodsSkuInfo = skuInfos.get(0);
                    result.setSingleSmallPrice(exchangeGoodsSkuInfo.getSinglePrice());
                    result.setSingleSmallReward(exchangeGoodsSkuInfo.getSingleReward());
                }

                result.setExchangeCount(getPhysicalTotalExchangeCount(goods.getId(), skuInfos));
                break;
            }
            default:
        }

        if (goods.getGoodsType() == GoodsTypeEnum.COUPON) {
            result.setCoupon(JSONUtil.toBean(goods.getRelContent(), GoodsCouponResult.class));
        }
        return result;
    }

    @Override
    public ExchangeOrderPayResult addOrder(OrderAddParam param) {
        ExchangeGoods exchangeGoods = exchangeGoodsMapper.selectById(param.getGoodsId());
        //??????????????????skuId
        if (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.PHYSICAL) && ObjectUtil.isNull(param.getSkuId())) {
            throw new ServiceException("???????????????skuId????????????");
        }
        //??????????????????????????????
        if (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.COUPON)) {
            double d = RedisUtil.zSetRedis().incrementScore("goodsInventory", param.getGoodsId(), -param.getGoodsCount());
            if (d < 0) {
                RedisUtil.zSetRedis().incrementScore("goodsInventory", param.getGoodsId(), param.getGoodsCount());
                throw new ServiceException("????????????");
            }
        } else if (ObjectUtil.equal(exchangeGoods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
            ExchangeGoodsSkuInfo skuInfo = exchangeGoodsSkuInfoMapper.selectById(param.getSkuId());
            if (null == skuInfo) {
                throw new ServiceException("????????????????????????sku??????");
            }

            double d = RedisUtil.zSetRedis().incrementScore("goodsInventory", param.getGoodsId().toString() + skuInfo.getId().toString(), -param.getGoodsCount());
            if (d < 0) {
                RedisUtil.zSetRedis().incrementScore("goodsInventory", param.getGoodsId().toString() + skuInfo.getId().toString(), param.getGoodsCount());
                throw new ServiceException("????????????");
            }
        }
        ExchangeOrder exchangeOrder;
        try {
            exchangeOrder = SpringUtil.getBean(ExchangeService.class).addExchangeOrder(param);
        } catch (Exception e) {
            RedisUtil.zSetRedis().incrementScore("goodsInventory", ObjectUtil.isNotNull(param.getSkuId()) ? param.getGoodsId() + param.getSkuId().toString() : param.getGoodsId(), param.getGoodsCount());
            throw e;
        }
        ExchangeOrderPayResult result = new ExchangeOrderPayResult().setId(exchangeOrder.getId());
        try {
            if (exchangeOrder.getTotalPrice().compareTo(BigDecimal.ZERO) > 0) {
                //????????????
                String payParam = payExchangeOrder(param.getPayType(), exchangeOrder);
                result.setPayParam(payParam);
            }
        } catch (Exception e) {
            log.error("????????????????????????", e);
        }
        RedisUtil.redis().expire("goodsInventory", 30, TimeUnit.DAYS);
        return result;
    }

    @Transactional
    @Override
    public ExchangeOrder addExchangeOrder(OrderAddParam param) {
        Long orderId = MdcUtil.getSnowflakeId();
        //??????????????????skuId
        final Serializable key = ObjectUtil.isNotNull(param.getSkuId()) ? param.getGoodsId() + param.getSkuId().toString() : param.getGoodsId();

        //??????????????????????????????
        Integer totalExchangeCount = RedisUtil.syncLoad("changeExchangeGoods" + key, () -> {

            int count = ObjectUtil.isNotNull(param.getSkuId()) ? getPhysicalSkuExchangeCount(param.getGoodsId(), param.getSkuId())
                    : getTotalExchangeCount(param.getGoodsId());
            //???????????????????????????,overtime???????????????
            RMapCache<Long, Integer> map = RedisUtil.redisson().getMapCache("exchangeGoodsCount_" + key);
            if (map.size() == 0) {
                map.addListener((EntryExpiredListener<Long, Integer>) event -> {

                    Long i = RedisUtil.setRedis().add("goodsOrderIds_" + key, event.getKey());
                    if (i > 0) {
                        RedisUtil.zSetRedis().incrementScore("goodsInventory", key, event.getValue());
                        RedisUtil.redis().expire("goodsOrderIds_" + key, 30, TimeUnit.DAYS);
                    }
                });
            }
            map.put(orderId, param.getGoodsCount(), getOvertime(), TimeUnit.MINUTES);
            return count + getCacheCount(param.getGoodsId(), param.getSkuId());
        });
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        RedisUtil.transactionalLock("changeExchangeGoods" + key + memberInfo.getId());
        //??????rollback????????????????????????????????????(????????????)
        MdcUtil.publishTransactionalEvent(AfterRollbackEvent.build("????????????????????????", () -> {
            RMapCache<Long, Integer> map = RedisUtil.redisson().getMapCache("exchangeGoodsCount_" + key);
            map.remove(orderId);
        }));
        ExchangeGoods goods = exchangeGoodsMapper.selectById(param.getGoodsId());
        goods = stateHandler(goods, param.getSkuId());
        ExchangeGoodsSkuInfo exchangeGoodsSkuInfo = null;
        if (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.PHYSICAL) && ObjectUtil.isNotNull(param.getSkuId())) {
            ExchangeGoodsSkuInfo skuInfo = exchangeGoodsSkuInfoMapper.selectById(param.getSkuId());
            if (null == skuInfo) {
                throw new ServiceException("????????????????????????????????????");
            }
            exchangeGoodsSkuInfo = skuInfo;
        }
        //?????????????????????????????????
        if (goods.getState() != GoodsStateEnum.NORMAL &&
                (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.COUPON))) {
            throw new ServiceException("?????????" + goods.getState().getName() + "????????????");
        } else if (goods.getState() != GoodsStateEnum.NORMAL && ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
            throw new ServiceException("????????????????????????" + goods.getState().getName() + "????????????");
        }

        //???????????????sku
        if (goods.getState() == GoodsStateEnum.NORMAL
                && (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.COUPON))) {
            if (goods.getInventory() < totalExchangeCount) {
                throw new ServiceException("??????????????????");
            }
        } else if (goods.getState() == GoodsStateEnum.NORMAL && ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {

            if (exchangeGoodsSkuInfo.getInventory() < totalExchangeCount) {
                throw new ServiceException("?????????????????????????????????");
            }
        }

        //????????????????????????????????????????????????????????????????????????????????????
        if (goods.getGoodsType() == GoodsTypeEnum.PHYSICAL && ObjectUtil.equal(goods.getDeliveryType(), DeliveryTypeEnum.ONLINE)) {
            if (StrUtil.hasBlank(param.getReceiveName(), param.getReceivePhone(), param.getReceiveProvince(),
                    param.getReceiveCity(), param.getReceiveCounty(), param.getReceiveAddress())) {
                throw new ServiceException("????????????????????????????????????");
            }
        }

        //??????(??????????????????+????????????)>????????????
        DateTime now = DateTime.now();
        if (goods.getExchangeLimit() > 0) {
            int exchangeCount = exchangeOrderMapper.selectMaps(new QueryWrapper<ExchangeOrder>()
                    .select("coalesce(sum(goods_count), 0) exchange_count")
                    .lambda()
                    .eq(ExchangeOrder::getGoodsId, param.getGoodsId())
                    .eq(ExchangeOrder::getMemberId, memberInfo.getId())
                    .nested(i -> i.ne(ExchangeOrder::getOrderState, OrderStateEnum.CLOSED)
                            .ne(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY)
                            .or(j -> j.eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY)
                                    .ge(ExchangeOrder::getCreateTime, now.offsetNew(DateField.MINUTE, -getOvertime()))))
            ).stream().map(map -> Convert.toInt(map.get("exchange_count"))).collect(Collectors.toList()).get(0);
            if (exchangeCount + param.getGoodsCount() > goods.getExchangeLimit()) {
                throw new ServiceException("??????????????????");
            }
        }

        boolean isSkuGoods = !ObjectUtil.isNull(exchangeGoodsSkuInfo);
        //?????????????????????sku????????????????????????????????????
        RewardTypeEnum rewardType = isSkuGoods ? exchangeGoodsSkuInfo.getRewardType() : goods.getRewardType();
        int totalReward = isSkuGoods ? NumberUtil.mul(exchangeGoodsSkuInfo.getSingleReward(),
                param.getGoodsCount()).intValue() : NumberUtil.mul(goods.getSingleReward(), param.getGoodsCount()).intValue();
        BigDecimal totalPrice = isSkuGoods ?
                NumberUtil.add(goods.getExpressPrice(), NumberUtil.mul(exchangeGoodsSkuInfo.getSinglePrice(), param.getGoodsCount())) : NumberUtil.add(goods.getExpressPrice(), NumberUtil.mul(goods.getSinglePrice(), param.getGoodsCount()));
        OrderStateEnum orderState;

        //??????????????????????????????????????????????????????
        DeliveryTypeEnum deliveryType = goods.getGoodsType() == GoodsTypeEnum.PHYSICAL ? goods.getDeliveryType() : DeliveryTypeEnum.SYSTEM;
        DateTime exchangeTime = null;
        if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            //????????????
            if (totalReward > 0) {
                //?????????????????????????????????
                memberRewardService.addExchangeRewardRecord(memberInfo, rewardType, -totalReward, goods.getId());
            }
            if (goods.getGoodsType() == GoodsTypeEnum.PHYSICAL) {
                orderState = OrderStateEnum.PENDING;
            } else {
                orderState = OrderStateEnum.NOTCONSUME;
            }
            exchangeTime = now;
        } else {
            //?????????
            if (totalReward > 0) {
                //??????????????????????????????????????????
                int memberReward = memberRewardService.getMemberTotalReward(memberInfo.getId(), rewardType);
                if (memberReward - totalReward < 0) {
                    throw new ServiceException("???????????????");
                }
            }
            orderState = OrderStateEnum.NOTPAY;
        }
        ExchangeOrder order = new ExchangeOrder();
        BeanUtil.copyProperties(goods, order, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        BeanUtil.copyProperties(param, order, CopyOptions.create().ignoreNullValue());
        if (StrUtil.isBlank(goods.getDesignateOrgIds())) {
            order.setDesignateOrgIds(goods.getCreatorOrgIds());
            order.setDesignateOrgNames(goods.getCreatorOrgNames());
        }
        MdcUtil.setMemberInfo(order, memberInfo);
        String time = Convert.toStr(DateTime.now().getTime());
        String random = RandomUtil.randomNumbers(4);
        String memberIdStr = Convert.toStr(memberInfo.getId());
        String orderCode = StrUtil.subSuf(time, time.length() - 6) + random + StrUtil.subSuf(memberIdStr, memberIdStr.length() - 4);
        order.setOrderCode(orderCode);
        order.setTotalReward(totalReward);
        order.setTotalPrice(totalPrice);
        order.setDeliveryType(deliveryType);
        order.setOrderState(orderState);
        order.setExchangeTime(exchangeTime);
        //????????????sku????????????
        if (ObjectUtil.equal(goods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
            order.setSkuInfo(exchangeGoodsSkuInfo.getRewardType(), exchangeGoodsSkuInfo.getSingleReward(), exchangeGoodsSkuInfo.getSinglePrice(),
                    exchangeGoodsSkuInfo.getId(), exchangeGoodsSkuInfo.getSpecIds(), exchangeGoodsSkuInfo.getSpecValueIds(),
                    exchangeGoodsSkuInfo.getConfigureJson(), exchangeGoodsSkuInfo.getSelfCode());

        }
        PayModeEnum payMode = null;
        if (totalReward > 0 && totalPrice.compareTo(BigDecimal.ZERO) > 0) {
            payMode = PayModeEnum.MIX;
        } else if (totalReward > 0) {
            payMode = PayModeEnum.REWARD;
        } else if (totalPrice.compareTo(BigDecimal.ZERO) > 0) {
            payMode = PayModeEnum.CASH;
        }
        order.setPayMode(payMode);
        order.setId(orderId);
        int count = exchangeOrderMapper.insert(order);
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
        addOperateRecord(order, null, order.getOrderState());
        if (order.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            //????????????????????????
            switch (order.getGoodsType()) {
                case COUPON: {
                    CouponGetParam couponGetParam = new CouponGetParam();
                    couponGetParam.setId(order.getRelId());
                    couponInfoService.addRel(couponGetParam, order.getGoodsCount(), memberInfo,
                            ReceiveSourceEnum.EXCHANGE, order.getId(), CreateSourceEnum.EXCHANGE);
                    break;
                }
                default:
            }
            //??????commit??????????????????????????????
            MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("??????????????????_" + key, () -> updateExchangeCount(param.getGoodsId(), param.getSkuId())));
            RedisUtil.syncLoad("changeExchangeGoods" + key, () -> {
                RMapCache<Long, Integer> map = RedisUtil.redisson().getMapCache("exchangeGoodsCount_" + key);
                map.remove(orderId);
                increaseTotalExchangeCount(param.getGoodsId(), param.getSkuId(), param.getGoodsCount());
                return null;
            });
        }
        return order;
    }

    private String payExchangeOrder(PayType payType, ExchangeOrder exchangeOrder) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        OrderParam orderParam = new OrderParam().setId(exchangeOrder.getId()).setOrderNo(MdcUtil.getSnowflakeIdStr())
                .setAmount(exchangeOrder.getTotalPrice()).setFrpCode(FrpCodeEnum.APPLET_PAY)
                .setProductName("????????????_" + exchangeOrder.getGoodsName() + (ObjectUtil.isNotNull(exchangeOrder.getSkuId()) ? exchangeOrder.getSkuId() : ""))
                .setTimeExpire(getOvertime());
        switch (payType) {
            case WECHAT_MINI: {
                orderParam.setFrpCode(FrpCodeEnum.APPLET_PAY).setAppId(memberInfo.getWxAppId()).setOpenId(memberInfo.getOpenId());
                break;
            }
            default:
        }
        return easyPayService.orderExchange(orderParam);
    }

    @Override
    public ExchangeOrderPayResult payOrder(OrderPayParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        ExchangeOrder order = exchangeOrderMapper.selectOne(Wrappers.<ExchangeOrder>lambdaQuery()
                .eq(ExchangeOrder::getId, param.getOrderId())
                .eq(ExchangeOrder::getMemberId, memberInfo.getId())
        );
        order = stateHandler(order);
        if (order.getOrderState() == OrderStateEnum.NOTPAY) {
            EasypayPaymentRecord paymentRecord = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                    .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                    .eq(EasypayPaymentRecord::getOrderId, param.getOrderId())
                    .last("limit 1")
                    .orderByDesc(EasypayPaymentRecord::getCreateTime)
            );
            if (paymentRecord != null) {
                if (paymentRecord.getPayState() == WhetherEnum.YES) {
                    throw new ServiceException("????????????????????????");
                } else {
                    boolean isPaid = easyPayService.checkOrder(paymentRecord);
                    if (isPaid) {
                        throw new ServiceException("????????????????????????");
                    }
                }
            }
            String payParam = payExchangeOrder(param.getPayType(), order);
            return new ExchangeOrderPayResult().setId(order.getId()).setPayParam(payParam);
        } else if (order.getOrderState() == OrderStateEnum.CLOSED) {
            throw new ServiceException("?????????????????????,????????????");
        } else {
            throw new ServiceException("??????????????????,????????????");
        }
    }

    @Override
    public void finishOrder(Boolean isSuccess, PaymentTypeEnum paymentType, EasypayPaymentRecord record) {
        if (isSuccess) {
            ExchangeOrder exchangeOrder = exchangeOrderMapper.selectById(record.getOrderId());
            try {
                SpringUtil.getBean(ExchangeService.class).finishOrder(exchangeOrder);
            } catch (Exception e) {
                //???????????????????????????(???????????????)
                log.error("????????????????????????", e);
                SpringUtil.getBean(ExchangeService.class).updateFailedState(exchangeOrder, e.getMessage());
            }
        } else {
            log.error("????????????????????????");
        }
    }

    @Transactional
    @Override
    public void finishOrder(ExchangeOrder exchangeOrder) {
        //??????????????????
        ExchangeGoods exchangeGoods = exchangeGoodsMapper.selectById(exchangeOrder.getGoodsId());
        exchangeGoods = stateHandler(exchangeGoods, null);
        if (exchangeGoods.getState() != GoodsStateEnum.NORMAL) {
            throw new ServiceException("?????????" + exchangeGoods.getState().getName() + "????????????");
        }
        ExchangeOrder update = new ExchangeOrder();
        update.setId(exchangeOrder.getId());
        MemberInfo memberInfo = memberService.getUnifiedMember(Wrappers.<MemberInfo>lambdaQuery()
                .eq(StrUtil.isNotBlank(exchangeOrder.getUserId()), MemberInfo::getAliAppId, exchangeOrder.getAppId())
                .eq(StrUtil.isNotBlank(exchangeOrder.getOpenId()), MemberInfo::getWxAppId, exchangeOrder.getAppId())
                .eq(StrUtil.isAllBlank(exchangeOrder.getUserId(), exchangeOrder.getOpenId()), MemberInfo::getId, exchangeOrder.getMemberId())
        );
        switch (exchangeOrder.getGoodsType()) {
            case COUPON: {
                CouponGetParam couponGetParam = new CouponGetParam();
                couponGetParam.setId(exchangeOrder.getRelId());
                couponInfoService.addRel(couponGetParam, exchangeOrder.getGoodsCount(), memberInfo,
                        ReceiveSourceEnum.EXCHANGE, exchangeOrder.getId(), CreateSourceEnum.EXCHANGE);
                break;
            }
            default:
        }
        //???????????????
        if (exchangeOrder.getTotalReward() > 0) {
            memberRewardService.addExchangeRewardRecord(memberInfo, exchangeOrder.getRewardType(), -exchangeOrder.getTotalReward(), exchangeOrder.getGoodsId());
        }
        OrderStateEnum orderState;
        if (exchangeOrder.getGoodsType() == GoodsTypeEnum.PHYSICAL) {
            orderState = OrderStateEnum.PENDING;
        } else {
            orderState = OrderStateEnum.NOTCONSUME;
        }
        update.setOrderState(orderState);
        DateTime now = DateTime.now();
        update.setExchangeTime(now);
        update.setPayTime(now);
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
        addOperateRecord(exchangeOrder, OrderStateEnum.NOTPAY, orderState);
        Serializable key = ObjectUtil.isNotNull(exchangeOrder.getSkuId()) ? exchangeOrder.getGoodsId() + exchangeOrder.getSkuId().toString() : exchangeOrder.getGoodsId();
        MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("?????????????????????_" + key, () -> {
            updateExchangeCount(exchangeOrder.getGoodsId(), exchangeOrder.getSkuId());
            //??????????????????
            RedisUtil.syncLoad("changeExchangeGoods" + key, () -> {
                increaseTotalExchangeCount(exchangeOrder.getGoodsId(), exchangeOrder.getSkuId(), exchangeOrder.getGoodsCount());
                return null;
            });
            //????????????????????????
            RMapCache<Long, Integer> map = RedisUtil.redisson().getMapCache("exchangeGoodsCount_" + key);
            map.remove(exchangeOrder.getId());
        }));
    }

    @Transactional
    @Override
    public void updateFailedState(ExchangeOrder order, String reason) {
        /*easyPayService.refundExchangeOrder(new RefundParam().setPayType(record.getPayType()).setId(exchangeOrder.getId()).setOrderNo(record.getOrderNo())
                .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount()).setRefundReason(e.getMessage()));*/
        ExchangeOrder update = new ExchangeOrder();
        update.setId(order.getId());
        update.setOrderState(OrderStateEnum.FAILED);
        update.setRefundState(RefundStateEnum.REFUNDCONFIRM);
        update.setRefundReasonType("?????????????????????");
        update.setRefundReason(reason);
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("??????????????????????????????");
        }
        addOperateRecord(order, OrderStateEnum.NOTPAY, OrderStateEnum.FAILED);
    }

    @Override
    public void updateSuccessState(Long orderId) {
        ExchangeOrder update = new ExchangeOrder();
        update.setOrderState(OrderStateEnum.SUCCESS);
        int count = exchangeOrderMapper.update(update, Wrappers.<ExchangeOrder>lambdaUpdate()
                .eq(ExchangeOrder::getId, orderId)
                .ne(ExchangeOrder::getGoodsType, GoodsTypeEnum.PHYSICAL)
        );
        if (count <= 0) {
            throw new ServiceException("??????????????????????????????");
        }
        ExchangeOrder order = exchangeOrderMapper.selectById(orderId);
        addOperateRecord(order, OrderStateEnum.NOTCONSUME, OrderStateEnum.SUCCESS);
    }

    @Override
    public void finishRefund(Boolean isSuccess, EasypayRefundRecord record) {
        ExchangeOrder update = new ExchangeOrder();
        update.setId(record.getOrderId());
        if (isSuccess) {
            update.setOrderState(OrderStateEnum.REFUNDSUCC);
            update.setRefundTime(DateTime.now());
            update.setRefundState(RefundStateEnum.REFUNDSUCC);
        } else {
            update.setOrderState(OrderStateEnum.REFUNDFAIL);
            update.setRefundState(RefundStateEnum.REFUNDFAIL);
        }
        int count = exchangeOrderMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("????????????????????????");
        }
    }

    @Override
    public IPage<ExchangeOrderPageResult> pageExchangeOrder(ExchangeOrderPageParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        ExchangeOrderPageParam.OrderStateEnum state = param.getOrderState();
        DateTime now = DateTime.now();
        return exchangeOrderMapper.selectPage(param.page(), Wrappers.<ExchangeOrder>lambdaQuery()
                .eq(ExchangeOrder::getMemberId, memberId)
                .eq(param.getIsPhysical() != null && param.getIsPhysical(), ExchangeOrder::getGoodsType, GoodsTypeEnum.PHYSICAL)
                .ne(param.getIsPhysical() != null && !param.getIsPhysical(), ExchangeOrder::getGoodsType, GoodsTypeEnum.PHYSICAL)
                .eq(param.getGoodsType() != null, ExchangeOrder::getGoodsType, param.getGoodsType())
                .like(StrUtil.isNotBlank(param.getGoodsName()), ExchangeOrder::getGoodsName, param.getGoodsName())
                .and(state == ExchangeOrderPageParam.OrderStateEnum.NOTPAY, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY)
                        .ge(ExchangeOrder::getCreateTime, now.offsetNew(DateField.MINUTE, -getOvertime()))
                )
                .and(state == ExchangeOrderPageParam.OrderStateEnum.SUCCESS, i -> i
                        .nested(j -> j.eq(ExchangeOrder::getOrderState, OrderStateEnum.SUCCESS)
                                .or(k -> k.eq(ExchangeOrder::getOrderState, OrderStateEnum.DELIVERYING)
                                        .lt(ExchangeOrder::getDeliveryTime, now.offsetNew(DateField.DAY_OF_YEAR, -getOverConfirmDay())))
                        )
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == ExchangeOrderPageParam.OrderStateEnum.NOTCONSUME, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTCONSUME)
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == ExchangeOrderPageParam.OrderStateEnum.NOTDELIVERY, i -> i
                        .nested(j -> j.eq(ExchangeOrder::getOrderState, OrderStateEnum.PENDING).or()
                                .eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTDELIVERY))
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .and(state == ExchangeOrderPageParam.OrderStateEnum.DELIVERYING, i -> i
                        .eq(ExchangeOrder::getOrderState, OrderStateEnum.DELIVERYING)
                        .ge(ExchangeOrder::getDeliveryTime, now.offsetNew(DateField.DAY_OF_YEAR, -getOverConfirmDay()))
                        .nested(j -> j.isNull(ExchangeOrder::getRefundState).or()
                                .ne(ExchangeOrder::getRefundState, RefundStateEnum.REFUNDAUDIT))
                )
                .isNotNull(state == ExchangeOrderPageParam.OrderStateEnum.REFUND, ExchangeOrder::getRefundState)
                .orderByDesc(ExchangeOrder::getCreateTime)
        ).convert(order -> {
            order = stateHandler(order);
            ExchangeOrderPageResult result = BeanUtil.copyProperties(order, ExchangeOrderPageResult.class);
            if (JSONUtil.isJsonArray(order.getGoodsImgs())) {
                result.setGoodsImgList(JSONUtil.toList(JSONUtil.parseArray(order.getGoodsImgs()), String.class));
            }
            if (order.getGoodsType() == GoodsTypeEnum.COUPON) {
                result.setCoupon(JSONUtil.toBean(order.getRelContent(), GoodsCouponResult.class));
            }
            return result;
        });
    }

    private CouponMemberRel stateHandler(CouponMemberRel rel) {
        if (rel == null) {
            throw new ServiceException("????????????????????????");
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
    public ExchangeOrderResult getExchangeOrder(OrderGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        ExchangeOrder order = exchangeOrderMapper.selectOne(Wrappers.<ExchangeOrder>lambdaQuery()
                .eq(ExchangeOrder::getId, param.getId())
                .eq(ExchangeOrder::getMemberId, memberId)
        );
        order = stateHandler(order);
        ExchangeOrderResult result = BeanUtil.copyProperties(order, ExchangeOrderResult.class);
        if (JSONUtil.isJsonArray(order.getGoodsImgs())) {
            result.setGoodsImgList(JSONUtil.toList(JSONUtil.parseArray(order.getGoodsImgs()), String.class));
        }
        if (order.getGoodsType() == GoodsTypeEnum.COUPON) {
            result.setCoupon(JSONUtil.toBean(order.getRelContent(), GoodsCouponResult.class));
        }
        EasypayPaymentRecord paymentRecord = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayPaymentRecord::getOrderId, param.getId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                .last("limit 1")
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
        );
        EasypayRefundRecord refundRecord = easypayRefundRecordMapper.selectOne(Wrappers.<EasypayRefundRecord>lambdaQuery()
                .eq(EasypayRefundRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                .eq(EasypayRefundRecord::getOrderId, param.getId())
                .eq(EasypayRefundRecord::getRefundState, WhetherEnum.YES)
                .last("limit 1")
                .orderByDesc(EasypayRefundRecord::getCreateTime)
        );
        List<CouponMemberRel> couponRelList = couponMemberRelMapper.selectList(Wrappers.<CouponMemberRel>lambdaQuery()
                .eq(CouponMemberRel::getReceiveSource, ReceiveSourceEnum.EXCHANGE)
                .eq(CouponMemberRel::getReceiveSourceId, order.getId())
                .eq(CouponMemberRel::getCreateSource, CreateSourceEnum.EXCHANGE)
        ).stream().map(this::stateHandler).collect(Collectors.toList());
        result.setPaymentRecord(paymentRecord);
        result.setRefundRecord(refundRecord);
        result.setCouponRelList(couponRelList);
        long second = getOvertime() * 60 - DateUtil.between(DateTime.now(), order.getCreateTime(), DateUnit.SECOND, true);
        result.setEffectiveSecond(second < 0 ? 0 : second);
        return result;
    }

    @Override
    public void receipt(OrderGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        ExchangeOrder exchangeOrder = RedisUtil.syncLoad("changeExchangeOrder" + param.getId(), () -> {
            ExchangeOrder order = exchangeOrderMapper.selectOne(Wrappers.<ExchangeOrder>lambdaQuery()
                    .eq(ExchangeOrder::getId, param.getId())
                    .eq(ExchangeOrder::getMemberId, memberId)
            );
            order = stateHandler(order);
            if (order.getOrderState() != OrderStateEnum.DELIVERYING) {
                throw new ServiceException("?????????????????????????????????");
            }
            ExchangeOrder update = new ExchangeOrder();
            update.setId(order.getId());
            update.setOrderState(OrderStateEnum.SUCCESS);
            update.setReceiveTime(DateTime.now());
            int count = exchangeOrderMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("????????????");
            }
            return order;
        });
        addOperateRecord(exchangeOrder, OrderStateEnum.DELIVERYING, OrderStateEnum.SUCCESS);
    }

    @Override
    public void revokeRefund(OrderGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        RedisUtil.syncLoad("changeExchangeOrder" + param.getId(), () -> {
            ExchangeOrder order = exchangeOrderMapper.selectOne(Wrappers.<ExchangeOrder>lambdaQuery()
                    .eq(ExchangeOrder::getId, param.getId())
                    .eq(ExchangeOrder::getMemberId, memberId)
            );
            if (order == null) {
                throw new ServiceException("???????????????");
            }
            DateTime now = DateTime.now();
            if (order.getOrderState() == OrderStateEnum.NOTPAY) {
                if (now.offsetNew(DateField.MINUTE, -getOvertime()).after(order.getCreateTime())) {
                    order.setOrderState(OrderStateEnum.CLOSED);
                }
            } else if (order.getOrderState() == OrderStateEnum.DELIVERYING) {
                if (now.offsetNew(DateField.DAY_OF_YEAR, -getOverConfirmDay()).after(order.getDeliveryTime())) {
                    order.setOrderState(OrderStateEnum.SUCCESS);
                }
            }
            OrderStateEnum originalState = order.getOrderState();
            if (order.getRefundState() == RefundStateEnum.REFUNDAUDIT) {
                order.setOrderState(OrderStateEnum.REFUNDAUDIT);
            }
            if (order.getOrderState() != OrderStateEnum.REFUNDAUDIT) {
                throw new ServiceException("?????????????????????????????????");
            }
            ExchangeOrder update = new ExchangeOrder();
            update.setId(order.getId());
            update.setRefundState(RefundStateEnum.REFUNDREVOKE);
            int count = exchangeOrderMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("????????????");
            }
            addOperateRecord(order, OrderStateEnum.REFUNDAUDIT, originalState);
            return null;
        });
    }

    @Transactional
    @Override
    public void refund(OrderRefundParam param) {
        RedisUtil.transactionalLock("changeExchangeOrder" + param.getId());
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        ExchangeOrder order = exchangeOrderMapper.selectOne(Wrappers.<ExchangeOrder>lambdaQuery()
                .eq(ExchangeOrder::getId, param.getId())
                .eq(ExchangeOrder::getMemberId, memberId)
        );
        order = stateHandler(order);
        if (order.getRefundType() == RefundTypeEnum.NONREFUNDABLE) {
            throw new ServiceException("?????????????????????");
        }
        DateTime now = DateTime.now();
        RefundStateEnum refundState;
        OrderStateEnum originalState = order.getOrderState();
        OrderStateEnum afterState;
        if (originalState == OrderStateEnum.NOTCONSUME || originalState == OrderStateEnum.PENDING
                || originalState == OrderStateEnum.REFUNDFAIL) {
            //??????????????????
            EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                    .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                    .eq(EasypayPaymentRecord::getOrderId, order.getId())
                    .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                    .orderByDesc(EasypayPaymentRecord::getCreateTime)
                    .last("limit 1")
            );
            if (record == null) {
                throw new ServiceException("?????????????????????");
            }
            switch (order.getGoodsType()) {
                case COUPON: {
                    couponInfoService.revokedRel(ReceiveSourceEnum.EXCHANGE, order.getId(), CreateSourceEnum.EXCHANGE, false);
                    break;
                }
                default:
            }
            refundState = RefundStateEnum.REFUNDING;
            ExchangeOrder update = new ExchangeOrder();
            BeanUtil.copyProperties(param, update);
            afterState = OrderStateEnum.REFUNDING;
            update.setOrderState(afterState);
            update.setRefundState(refundState);
            int count = exchangeOrderMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("????????????");
            }
            //??????????????????
                /*ExchangeOrder finalOrder = order;
                RedisUtil.syncLoad("changeExchangeGoods" + order.getGoodsId(), () -> {
                    ExchangeGoods exchangeGoods = exchangeGoodsMapper.selectById(finalOrder.getGoodsId());
                    ExchangeGoods goodsUpdate = new ExchangeGoods();
                    goodsUpdate.setId(finalOrder.getGoodsId());
                    goodsUpdate.setExchangeCount(exchangeGoods.getExchangeCount() - finalOrder.getGoodsCount());
                    int updateCount = exchangeGoodsMapper.updateById(goodsUpdate);
                    if (updateCount <= 0) {
                        throw new ServiceException("????????????????????????");
                    }
                    return null;
                });*/
            easyPayService.refundExchangeOrder(new RefundParam().setAppId(record.getAppId()).setPayType(record.getPayType()).setId(order.getId()).setOrderNo(record.getOrderNo())
                    .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount())
                    .setRefundReason(param.getRefundReasonType() + "_" + param.getRefundReason()));
        } else if (order.getGoodsType() == GoodsTypeEnum.PHYSICAL && (originalState == OrderStateEnum.NOTDELIVERY
                || originalState == OrderStateEnum.DELIVERYING || originalState == OrderStateEnum.SUCCESS)) {
            if ((order.getOrderState() == OrderStateEnum.SUCCESS && now.offsetNew(DateField.DAY_OF_YEAR, -getOverRefundDay()).isAfter(order.getReceiveTime()))
                    || (order.getOrderState() == OrderStateEnum.DELIVERYING
                    && now.offsetNew(DateField.DAY_OF_YEAR, -getOverRefundDay() - getOverConfirmDay()).isAfter(order.getDeliveryTime()))) {
                throw new ServiceException("????????????7???????????????");
            }
            //??????????????????
            refundState = RefundStateEnum.REFUNDAUDIT;
            ExchangeOrder update = new ExchangeOrder();
            BeanUtil.copyProperties(param, update);
            afterState = OrderStateEnum.REFUNDAUDIT;
            //??????????????????????????????????????????,????????????????????????????????????????????????????????????
            update.setRefundState(refundState);
            int count = exchangeOrderMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("????????????");
            }
        } else {
            throw new ServiceException("???????????????????????????");
        }
        addOperateRecord(order, originalState, afterState);
    }

    @Override
    public void updateOrderState() {
        DateTime now = DateTime.now();
        List<Long> tenantIdList = exchangeOrderMapper.normalSelectList(Wrappers.<ExchangeOrder>lambdaQuery()
                .select(ExchangeOrder::getTenantId)
                .groupBy(ExchangeOrder::getTenantId)
        ).stream().map(ExchangeOrder::getTenantId).collect(Collectors.toList());
        for (Long tenantId : tenantIdList) {
            ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, tenantId);
            Integer time = clientConfigService.getItemByName(null, "exchangeOrderOvertimeToPay", tenantId);
            time = time == null ? OVERTIME : time;
            //????????????????????????????????????closed
            exchangeOrderMapper.update(null, Wrappers.<ExchangeOrder>lambdaUpdate()
                    .eq(ExchangeOrder::getOrderState, OrderStateEnum.NOTPAY)
                    .lt(ExchangeOrder::getCreateTime, now.offsetNew(DateField.MINUTE, -time))
                    .set(ExchangeOrder::getOrderState, OrderStateEnum.CLOSED)
            );
            //????????????????????????????????????success
            time = clientConfigService.getItemByName(null, "exchangeOrderOverConfirmDay", tenantId);
            time = time == null ? OVER_CONFIRM_DAY : time;
            exchangeOrderMapper.update(null, Wrappers.<ExchangeOrder>lambdaUpdate()
                    .eq(ExchangeOrder::getOrderState, OrderStateEnum.DELIVERYING)
                    .lt(ExchangeOrder::getDeliveryTime, now.offsetNew(DateField.DAY_OF_YEAR, -time))
                    .set(ExchangeOrder::getOrderState, OrderStateEnum.SUCCESS)
                    .set(ExchangeOrder::getReceiveTime, now)
            );
        }
    }

    @Transactional
    @Override
    public void autoRefundExpireOrder() {
        List<Long> orderIdList = couponMemberRelMapper.normalSelectList(Wrappers.<CouponMemberRel>lambdaQuery()
                .select(CouponMemberRel::getReceiveSourceId)
                .eq(CouponMemberRel::getVerificateState, VerificateStateEnum.NOTCONSUME)
                .lt(CouponMemberRel::getEffectiveEndTime, DateTime.now())
                .eq(CouponMemberRel::getCreateSource, CreateSourceEnum.EXCHANGE)
                .eq(CouponMemberRel::getReceiveSource, ReceiveSourceEnum.EXCHANGE)
                .isNotNull(CouponMemberRel::getReceiveSourceId)
        ).stream().map(CouponMemberRel::getReceiveSourceId).collect(Collectors.toList());
        log.info("?????????????????????????????????{}", orderIdList.size());
        List<ExchangeOrder> orderList = exchangeOrderMapper.normalSelectList(Wrappers.<ExchangeOrder>lambdaQuery()
                .in(ExchangeOrder::getId, orderIdList)
                .eq(ExchangeOrder::getRefundType, RefundTypeEnum.REFUNDANYTIME)
                .eq(ExchangeOrder::getRefundExpired, WhetherEnum.YES)
                .gt(ExchangeOrder::getTotalPrice, BigDecimal.ZERO)
        );
        log.info("??????????????????????????????????????????{}", orderList.size());
        for (ExchangeOrder order : orderList) {
            ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, order.getTenantId());
            Long orderId = order.getId();
            EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                    .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.EXCHANGE)
                    .eq(EasypayPaymentRecord::getOrderId, orderId)
                    .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                    .orderByDesc(EasypayPaymentRecord::getCreateTime)
                    .last("limit 1")
            );
            if (record == null) {
                log.info("????????????{}????????????????????????", order.getOrderCode());
                continue;
            }
            couponInfoService.revokedRel(ReceiveSourceEnum.EXCHANGE, orderId, CreateSourceEnum.EXCHANGE, false);
            RefundStateEnum refundState = RefundStateEnum.REFUNDING;
            ExchangeOrder update = new ExchangeOrder();
            update.setId(orderId);
            String reasonType = "??????????????????";
            String reason = "???????????????????????????";
            update.setRefundReasonType(reasonType);
            update.setRefundReason(reason);
            OrderStateEnum afterState = OrderStateEnum.REFUNDING;
            update.setOrderState(afterState);
            update.setRefundState(refundState);
            int count = exchangeOrderMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("????????????");
            }
            easyPayService.refundExchangeOrder(new RefundParam().setAppId(record.getAppId()).setPayType(record.getPayType())
                    .setId(orderId).setOrderNo(record.getOrderNo())
                    .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount())
                    .setRefundReason(reasonType + "_" + reason));
            addOperateRecord(order, order.getOrderState(), afterState);
        }
    }

    @Override
    public IPage<ExchangeGoods> pageShopGood(ExchangeShopGoodsPageParam param) {
        List<Long> goodsIdList = null;
        return exchangeGoodsMapper.selectPage(param.page(), new QueryWrapper<ExchangeGoods>()
                .gt("inventory-exchange_count", 0)
                .lambda()
                .like(ExchangeGoods::getDesignateOrgIds, param.getRelationId())
                .in(CollUtil.isNotEmpty(goodsIdList), ExchangeGoods::getId, goodsIdList)
                .eq(ExchangeGoods::getAuditState, AuditStateEnum.APPROVE)
                .eq(ExchangeGoods::getState, GoodsStateEnum.NORMAL)
                .le(ExchangeGoods::getShelveTime, DateTime.now())
                .orderByDesc(ExchangeGoods::getCreateTime)
        );
    }

    @Override
    public ExchangeGoodsSpecValueResult getGoodsSpecValue(GoodsGetParam param) {
        ExchangeGoods goods = exchangeGoodsMapper.selectById(param.getId());
        if (null == goods) {
            throw new ServiceException("??????????????????????????????????????????");
        }

        if (ObjectUtil.notEqual(goods.getGoodsType(), GoodsTypeEnum.PHYSICAL)) {
            throw new ServiceException("????????????????????????-????????????????????????");
        }

        List<GoodsSpecValueInfoResult> goodsSpecValueInfoResults = new ArrayList<>();
        //???????????????????????????????????????
        List<ExchangeGoodsSpecValueInfo> goodsSpecValueInfos = exchangeGoodsSpecValueInfoMapper.selectList(Wrappers.<ExchangeGoodsSpecValueInfo>lambdaQuery()
                .eq(ExchangeGoodsSpecValueInfo::getGoodsId, goods.getId())
                .orderByAsc(ExchangeGoodsSpecValueInfo::getSortNo)
        );

        if (null != goodsSpecValueInfos && goodsSpecValueInfos.size() > 0) {
            //???????????????????????????
            List<Long> specIds = goodsSpecValueInfos.stream().map(ExchangeGoodsSpecValueInfo::getSpecId).collect(Collectors.toList());

            Map<Long, ExchangeSpecValueInfo> specMap = exchangeSpecValueInfoMapper.selectList(Wrappers.<ExchangeSpecValueInfo>lambdaQuery()
                    .in(ExchangeSpecValueInfo::getId, specIds)
            ).stream().collect(Collectors.toMap(ExchangeSpecValueInfo::getId, exchangeSpecValueInfo -> exchangeSpecValueInfo));

            for (ExchangeGoodsSpecValueInfo item : goodsSpecValueInfos) {
                GoodsSpecValueInfoResult specValueInfoResult = new GoodsSpecValueInfoResult();
                //??????????????????
                BeanUtil.copyProperties(item, specValueInfoResult);

                ExchangeSpecValueInfo info = specMap.get(specValueInfoResult.getSpecId());
                if (null != info) {
                    specValueInfoResult.setName(info.getName());
                }

                //???????????????
                String[] values = item.getSpecValueIds().split(",");

                //???????????????
                List<GoodsValueInfoResult> goodsValueInfoResults = new ArrayList<>();
                List<ExchangeSpecValueInfo> valueInfos = exchangeSpecValueInfoMapper.selectList(Wrappers.<ExchangeSpecValueInfo>lambdaQuery()
                        .in(ExchangeSpecValueInfo::getId, ArrayUtil.cast(Object.class, values))
                );

                for (ExchangeSpecValueInfo valueInfo : valueInfos) {
                    goodsValueInfoResults.add(new GoodsValueInfoResult(valueInfo.getId(), valueInfo.getName()));
                }

                specValueInfoResult.setGoodsValueInfoResults(goodsValueInfoResults);
                goodsSpecValueInfoResults.add(specValueInfoResult);
            }
        }

        ExchangeGoodsSpecValueResult result = new ExchangeGoodsSpecValueResult();
        result.setGoodsSpecValueInfoResults(goodsSpecValueInfoResults);

        return result;
    }

    @Override
    public ExchangeGoodsSkuInfo getSkuByGoodsId(GoodsGetSkuInfoParam param) {
        ExchangeGoodsSkuInfo skuInfo = exchangeGoodsSkuInfoMapper.selectOne(Wrappers.<ExchangeGoodsSkuInfo>lambdaQuery()
                .eq(ExchangeGoodsSkuInfo::getGoodsId, param.getId())
                .eq(ExchangeGoodsSkuInfo::getSpecIds, param.getSpecIds())
                .eq(ExchangeGoodsSkuInfo::getSpecValueIds, param.getSpecValueIds())
        );

        //?????????????????????sku
        if (null == skuInfo) {
            throw new ServiceException("????????????????????????????????????????????????");
        }

        //????????????????????????
        skuInfo.setExchangeCount(getPhysicalSkuExchangeCount(param.getId(), skuInfo.getId()));
        return skuInfo;
    }
}
