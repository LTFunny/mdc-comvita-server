package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilafly.easypay.enums.FrpCodeEnum;
import com.aquilaflycloud.ajbcloud.req.*;
import com.aquilaflycloud.ajbcloud.resp.*;
import com.aquilaflycloud.ajbcloud.util.AjbCloudUtil;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.easypay.OrderTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.*;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.parking.*;
import com.aquilaflycloud.mdc.param.easypay.OrderParam;
import com.aquilaflycloud.mdc.param.easypay.RefundParam;
import com.aquilaflycloud.mdc.param.parking.*;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderApiResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderChargeResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderPayResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderResult;
import com.aquilaflycloud.mdc.service.EasyPayService;
import com.aquilaflycloud.mdc.service.MemberService;
import com.aquilaflycloud.mdc.service.ParkingCouponService;
import com.aquilaflycloud.mdc.service.ParkingOrderService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * ParkingOrderServiceImpl
 *
 * @author star
 * @date 2020-02-01
 */
@Slf4j
@Service
public class ParkingOrderServiceImpl implements ParkingOrderService {
    @Resource
    private EasyPayService easyPayService;
    @Resource
    private ParkingCouponService parkingCouponService;
    @Resource
    private ParkingOrderMapper parkingOrderMapper;
    @Resource
    private ParkingOrderInvoiceMapper parkingOrderInvoiceMapper;
    @Resource
    private ParkingCouponMemberRelMapper parkingCouponMemberRelMapper;
    @Resource
    private ParkingUnlicensedCarRecordMapper parkingUnlicensedCarRecordMapper;
    @Resource
    private ParkingMemberCarMapper parkingMemberCarMapper;
    @Resource
    private EasypayPaymentRecordMapper easypayPaymentRecordMapper;
    @Resource
    private EasypayRefundRecordMapper easypayRefundRecordMapper;
    @Resource
    private MemberService memberService;

    @Override
    @Transactional
    public void scanIn(UnlicensedCarParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        String[] params = param.getScanParam().split(",");
        String doorName = params[0];
        String cardId = params[1];
        String time = params[2];
        String mac = params[3];
        String vCode = params[4];
        //验证安居宝扫码参数是否正确
        if (!vCode.equals(xor(cardId + "," + time + "," + mac + ","))) {
            throw new ServiceException("扫码参数有误");
        }
        ParkingUnlicensedCarRecord record = new ParkingUnlicensedCarRecord();
        if (StrUtil.isBlank(param.getCarNo())) {
            int flag = parkingUnlicensedCarRecordMapper.selectCount(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                    .eq(ParkingUnlicensedCarRecord::getCardId, cardId)
            );
            if (flag > 0) {
                throw new ServiceException("车卡号已被使用,请重新扫码");
            }
            record.setShortCardId(MdcUtil.getTenantIncIdStr("parkingShortCardId", DateTime.now().toString("yy"), 5));
        } else {
            CardInfoGetResp cardInfoGetResp = AjbCloudUtil.CardInfo.getCardInfoList(MdcUtil.getCurrentTenantId().toString(), new CardInfoGetReq().setCarNo(param.getCarNo()));
            if (cardInfoGetResp.getStatus() && cardInfoGetResp.getData().getCount() > 0) {
                for (CardInfoGetResp.Result result : cardInfoGetResp.getData().getResult()) {
                    if (StrUtil.equals(result.getCardType(), "月租卡")) {
                        record.setCarNo(param.getCarNo());
                        break;
                    }
                }
            }
            if (StrUtil.isBlank(record.getCarNo())) {
                throw new ServiceException(param.getCarNo() + "不是月租车");
            }
        }
        record.setCardId(cardId);
        record.setInScanParam(param.getScanParam());
        record.setCarState(CarStateEnum.SCAN);
        MdcUtil.setMemberInfo(record, memberInfo);
        int count = parkingUnlicensedCarRecordMapper.insertIgnoreAllBatch(CollUtil.newArrayList(record));
        if (count > 0) {
            AjbCloudResp resp;
            if (StrUtil.isBlank(param.getCarNo())) {
                long enterTime = time.length() <= 10 ? Convert.toLong(time) * 1000 : Convert.toLong(time);
                resp = AjbCloudUtil.Scan.scanCodeEnter(MdcUtil.getCurrentTenantId().toString(), new ScanCodeEnterReq().setCardId(cardId)
                        .setDoorName(doorName).setEnterTime(DateTime.of(enterTime).toString()).setUserID(Convert.toStr(memberInfo.getId())));
            } else {
                //无牌月租车(输入临时牌)
                resp = AjbCloudUtil.Scan.scanCodeEnterOrExit(MdcUtil.getCurrentTenantId().toString(), new ScanCodeEnterOrExitReq().setCardNo(param.getCarNo())
                        .setDoorName(doorName).setType("0").setUserID(Convert.toStr(memberInfo.getId())));
            }
            if (!resp.getStatus()) {
                throw new ServiceException("安居宝报错:" + resp.getMsg());
            }
        } else {
            throw new ServiceException("无牌车扫码入场失败");
        }
    }

    private String xor(String str) {
        str = HexUtil.encodeHexStr(str);
        String[] array = StrUtil.split(str, 2);
        String code = array[0];
        for (int i = 1; i < array.length; i++) {
            int x = Integer.valueOf(code, 16);
            int y = Integer.valueOf(array[i], 16);
            code = Integer.toHexString(x ^ y);
        }
        return code.toUpperCase();
    }

    @Override
    public ParkingOrderChargeResult getOrderCharge(OrderChargeGetParam param) {
        if (!param.getIsScan() && StrUtil.isAllBlank(param.getCardId(), param.getCarNo())) {
            throw new ServiceException("车牌号,车卡号不能同时为空");
        }
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        Long memberId = memberInfo.getId();
        ParkingUnlicensedCarRecord record = null;
        if (param.getIsScan()) {
            record = parkingUnlicensedCarRecordMapper.selectOne(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                    .eq(ParkingUnlicensedCarRecord::getMemberId, memberId)
                    .and(i -> i.eq(ParkingUnlicensedCarRecord::getCarState, CarStateEnum.IN)
                            .or(j -> j.eq(ParkingUnlicensedCarRecord::getCarState, CarStateEnum.SCAN)
                                    .gt(ParkingUnlicensedCarRecord::getCreateTime, DateTime.now().offset(DateField.HOUR, -2))))
                    .orderByDesc(ParkingUnlicensedCarRecord::getCreateTime)
                    .last("limit 1")
            );
            if (record == null) {
                throw new ServiceException("没有无牌车需要缴费");
            } else if (StrUtil.isBlank(record.getCarNo())) {
                param.setCardId(record.getCardId());
            } else {
                param.setCarNo(record.getCarNo());
            }
        }
        BigDecimal discountTime = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        DateTime now = DateTime.now();
        List<ParkingCouponMemberRel> relList = new ArrayList<>();
        Set<Long> relIds = new HashSet<>(CollUtil.emptyIfNull(param.getCouponMemberRelIds()));
        if (CollUtil.isNotEmpty(param.getCouponMemberRelIds())) {
            Map<String, List<ParkingCouponMemberRel>> relMap = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                    .in(ParkingCouponMemberRel::getId, relIds)
                    .orderByDesc(ParkingCouponMemberRel::getReceiveTime)
            ).stream().collect(Collectors.groupingBy(ParkingCouponMemberRel::getCouponCode));
            parkingCouponService.validateUseCouponLimit(relMap, memberId, param.getCarNo(), param.getCardId());
            ALL:
            for (Map.Entry<String, List<ParkingCouponMemberRel>> entry : relMap.entrySet()) {
                for (ParkingCouponMemberRel rel : entry.getValue()) {
                    if (rel.getCouponType() == CouponTypeEnum.TIME) {
                        discountTime = discountTime.add(rel.getCouponTotalWorth());
                    } else if (rel.getCouponType() == CouponTypeEnum.MONEY) {
                        discountAmount = discountAmount.add(rel.getCouponTotalWorth());
                    } else if (rel.getCouponType() == CouponTypeEnum.FREE) {
                        //全免券标识
                        discountAmount = null;
                        relIds.clear();
                        relIds.add(rel.getId());
                        relList.clear();
                        relList.add(rel);
                        break ALL;
                    }
                    relList.add(rel);
                }
            }
        }
        CostExtendGetReq req = new CostExtendGetReq().setCardId(param.getCardId()).setCarNo(param.getCarNo())
                .setDiscountTime(Convert.toStr(NumberUtil.mul(discountTime, 60).intValue()));
        if (discountAmount != null) {
            req.setDiscountAmount(discountAmount.toString());
        } else {
            //全免券
            req.setDiscountAmount("999999.99");
        }
        GetCostResp resp = AjbCloudUtil.Cost.getCostExtend(MdcUtil.getCurrentTenantId().toString(), req);
        List<ParkingCouponMemberRel> usedRelList = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                .and(i -> i.eq(ParkingCouponMemberRel::getCarNo, param.getCarNo())
                        .or().eq(ParkingCouponMemberRel::getCardId, param.getCardId()))
                .and(i -> i.gt(ParkingCouponMemberRel::getEffectiveEndTime, now)
                        .or().eq(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING))
                .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                .isNull(ParkingCouponMemberRel::getOrderId)
        );
        relList.addAll(usedRelList);
        if (resp.getStatus()) {
            ParkingOrderChargeResult result = new ParkingOrderChargeResult();
            BeanUtil.copyProperties(resp.getData(), result);
            result.setTotalPrice(NumberUtil.toBigDecimal(resp.getData().getTotalCharge()));
            result.setActualPrice(NumberUtil.toBigDecimal(resp.getData().getPayCharge()));
            if (discountAmount != null) {
                result.setDiscountAmount(discountAmount);
            } else {
                //全免券
                result.setDiscountAmount(result.getTotalPrice().subtract(result.getActualPrice()));
            }
            result.setDiscountTime(discountTime.intValue());
            result.setChargeCode(resp.getData().getCode());
            result.setChargeState(EnumUtil.likeValueOf(ChargeStateEnum.class, resp.getData().getCode()));
            result.setRelList(relList);
            List<Map<String, Object>> countResult = parkingCouponMemberRelMapper.selectMaps(new QueryWrapper<ParkingCouponMemberRel>()
                    .select("coalesce(sum(coupon_count), 0) total_count")
                    .lambda()
                    .and(i -> i.eq(ParkingCouponMemberRel::getCarNo, param.getCarNo())
                            .or().eq(ParkingCouponMemberRel::getCardId, param.getCardId())
                            .or().eq(ParkingCouponMemberRel::getMemberId, memberId))
                    .and(i -> i.gt(ParkingCouponMemberRel::getEffectiveEndTime, now)
                            .or().eq(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING))
                    .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.NOTCONSUME)
                    .notIn(CollUtil.isNotEmpty(relIds), ParkingCouponMemberRel::getId, relIds)
            );
            result.setRelCount(Convert.toInt(countResult.get(0).get("total_count")));
            if (StrUtil.isBlank(result.getCarNo())) {
                if (record != null) {
                    result.setShortCardId(record.getShortCardId());
                }
            }
            return result;
        } else {
            throw new ServiceException("安居宝报错:" + resp.getMsg());
        }
    }

    @Override
    public List<ParkingOrderChargeResult> batchGetOrderCharge(OrderChargeBatchGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        String lockName = "batchGetOrderChargeLock" + memberId;
        String key = "batchGetOrderCharge" + memberId;
        ParkingOrderChargeResult[] results = RedisUtil.syncLoad(lockName, () -> {
            List<ParkingOrderChargeResult> orderList = RedisUtil.<ParkingOrderChargeResult>listRedis().range(key, 0, -1);
            if (CollUtil.isNotEmpty(orderList) && !param.getRefresh()) {
                return orderList.toArray(new ParkingOrderChargeResult[]{});
            }
            List<String> carNoList = parkingMemberCarMapper.selectList(Wrappers.<ParkingMemberCar>lambdaQuery()
                    .eq(ParkingMemberCar::getMemberId, memberId)
            ).stream().map(ParkingMemberCar::getCarNo).collect(Collectors.toList());
            List<ParkingUnlicensedCarRecord> recordList = parkingUnlicensedCarRecordMapper.selectList(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                    .eq(ParkingUnlicensedCarRecord::getMemberId, memberId)
                    .isNull(ParkingUnlicensedCarRecord::getCarNo)
                    .and(i -> i.eq(ParkingUnlicensedCarRecord::getCarState, CarStateEnum.IN)
                            .or(j -> j.eq(ParkingUnlicensedCarRecord::getCarState, CarStateEnum.SCAN)
                                    .gt(ParkingUnlicensedCarRecord::getCreateTime, DateTime.now().offset(DateField.HOUR, -2))))

            );
            List<String> shortCardIdList = recordList.stream().map(ParkingUnlicensedCarRecord::getShortCardId).collect(Collectors.toList());
            List<Future<GetCostResp>> futureList = new ArrayList<>();
            for (String carNo : carNoList) {
                Future<GetCostResp> future = MdcUtil.getTtlExecutorService().submit(
                        () -> AjbCloudUtil.Cost.getCost(MdcUtil.getCurrentTenantId().toString(), new CostGetReq().setCarNo(carNo)));
                futureList.add(future);
            }
            for (ParkingUnlicensedCarRecord record : recordList) {
                Future<GetCostResp> future = MdcUtil.getTtlExecutorService().submit(
                        () -> AjbCloudUtil.Cost.getCost(MdcUtil.getCurrentTenantId().toString(), new CostGetReq().setCardId(record.getCardId())));
                futureList.add(future);
            }
            orderList = new ArrayList<>();
            int size = carNoList.size();
            carNoList.addAll(shortCardIdList);
            for (int i = 0; i < carNoList.size(); i++) {
                try {
                    GetCostResp resp = futureList.get(i).get();
                    ParkingOrderChargeResult result = new ParkingOrderChargeResult();
                    if (resp.getStatus()) {
                        BeanUtil.copyProperties(resp.getData(), result);
                        result.setTotalPrice(NumberUtil.toBigDecimal(resp.getData().getTotalCharge()));
                        result.setActualPrice(NumberUtil.toBigDecimal(resp.getData().getPayCharge()));
                        result.setChargeCode(resp.getData().getCode());
                        result.setChargeState(EnumUtil.likeValueOf(ChargeStateEnum.class, resp.getData().getCode()));
                        if (i >= size) {
                            result.setShortCardId(carNoList.get(i));
                        }
                    } else if (StrUtil.equals(resp.getCode(), "0179")) {
                        if (i < size) {
                            result.setCarNo(carNoList.get(i));
                        } else {
                            result.setShortCardId(carNoList.get(i));
                        }
                        result.setChargeState(ChargeStateEnum.NOTINPARK);
                    } else if (StrUtil.equals(resp.getCode(), "0180")) {
                        if (i < size) {
                            result.setCarNo(carNoList.get(i));
                        } else {
                            result.setShortCardId(carNoList.get(i));
                        }
                        result.setChargeState(ChargeStateEnum.NOTINPARK);
                    } else if (StrUtil.equals(resp.getCode(), "0190")) {
                        if (i < size) {
                            result.setCarNo(carNoList.get(i));
                        } else {
                            result.setShortCardId(carNoList.get(i));
                        }
                        result.setChargeState(ChargeStateEnum.FREEPAY);
                    } else {
                        continue;
                    }
                    orderList.add(result);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("批量获取停车缴费信息失败", e);
                }
            }
            CollUtil.sort(orderList, (o1, o2) -> {
                String code1 = o1.getChargeCode();
                String code2 = o2.getChargeCode();
                if (StrUtil.isBlank(code1) && StrUtil.isNotBlank(code2)) {
                    return 1;
                } else if (StrUtil.isNotBlank(code1) && StrUtil.isBlank(code2)) {
                    return -1;
                } else {
                    return 0;
                }
            });
            if (CollUtil.isNotEmpty(orderList)) {
                RedisUtil.redis().delete(key);
                RedisUtil.<ParkingOrderChargeResult>listRedis().leftPushAll(key, orderList);
                RedisUtil.redis().expire(key, 30, TimeUnit.MINUTES);
            }
            return orderList.toArray(new ParkingOrderChargeResult[]{});
        });
        return CollUtil.toList(results);
    }

    @Transactional
    @Override
    public ParkingOrderPayResult addOrder(OrderAddParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        Long memberId = memberInfo.getId();
        RedisUtil.transactionalLock("parkingOrderLock" + memberId);
        BigDecimal discountTime = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        List<ParkingOrder> orderList = parkingOrderMapper.selectList(Wrappers.<ParkingOrder>lambdaQuery()
                .and(i -> i.eq(ParkingOrder::getCarNo, param.getCarNo()).or().eq(ParkingOrder::getCardId, param.getCardId()))
                .eq(ParkingOrder::getOrderState, OrderStateEnum.NOTPAY)
                .last("limit 1")
        );
        Long orderId = MdcUtil.getSnowflakeId();
        ParkingOrder parkingOrder = null;
        if (orderList.size() > 0) {
            parkingOrder = orderList.get(0);
            orderId = parkingOrder.getId();
        }
        //无牌车信息
        String shortCardId = null, cardNo = null, scanParam = null;
        //获取无牌车短车卡号
        ParkingUnlicensedCarRecord record = null;
        if (StrUtil.isNotBlank(param.getScanParam()) && parkingOrder == null) {
            record = parkingUnlicensedCarRecordMapper.selectOne(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                    .eq(StrUtil.isNotBlank(param.getCarNo()), ParkingUnlicensedCarRecord::getCarNo, param.getCarNo())
                    .eq(StrUtil.isNotBlank(param.getCardId()), ParkingUnlicensedCarRecord::getCardId, param.getCardId())
                    .last("limit 1")
            );
            if (record != null) {
                scanParam = record.getInScanParam();
                if (StrUtil.isBlank(record.getCarNo())) {
                    shortCardId = record.getShortCardId();
                } else {
                    cardNo = record.getCarNo();
                }
            }
        }
        if (CollUtil.isNotEmpty(param.getCouponMemberRelIds())) {
            Set<Long> relIds = new HashSet<>(param.getCouponMemberRelIds());
            Map<String, List<ParkingCouponMemberRel>> relMap = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                    .in(ParkingCouponMemberRel::getId, relIds)
                    .orderByDesc(ParkingCouponMemberRel::getReceiveTime)
            ).stream().collect(Collectors.groupingBy(ParkingCouponMemberRel::getCouponCode));
            //验证使用停车券限制
            parkingCouponService.validateUseCouponLimit(relMap, memberId, param.getCarNo(), param.getCardId());
            for (List<ParkingCouponMemberRel> relList : relMap.values()) {
                for (ParkingCouponMemberRel rel : relList) {
                    if (rel.getCouponType() == CouponTypeEnum.TIME) {
                        discountTime = discountTime.add(rel.getCouponTotalWorth());
                    } else if (rel.getCouponType() == CouponTypeEnum.MONEY) {
                        discountAmount = discountAmount.add(rel.getCouponTotalWorth());
                    }
                    CouponMemberRelAddParam relAddParam = new CouponMemberRelAddParam();
                    BeanUtil.copyProperties(param, relAddParam);
                    relAddParam.setCancelLimit(true);
                    try {
                        parkingCouponService.useCoupon(relAddParam, rel, orderId);
                    } catch (ServiceException e) {
                        log.info("使用停车券失败", e);
                    }
                }
            }
        }
        GetCostResp resp = AjbCloudUtil.Cost.getCost(MdcUtil.getCurrentTenantId().toString(), new CostGetReq().setCardId(param.getCardId()).setCarNo(param.getCarNo()));
        if (resp.getStatus()) {
            boolean insert = false;
            if (parkingOrder == null) {
                insert = true;
            }
            parkingOrder = new ParkingOrder();
            BeanUtil.copyProperties(resp.getData(), parkingOrder);
            if (StrUtil.isNotBlank(param.getScanParam())) {
                String[] params = param.getScanParam().split(",");
                //String doorName = params[0];
                String orderNo = params[1];
                String time = params[2];
                String mac = params[3];
                String vCode = params[4];
                //验证安居宝扫码参数是否正确
                if (!vCode.equals(xor(orderNo + "," + time + "," + mac + ","))) {
                    throw new ServiceException("扫码参数有误");
                }
                parkingOrder.setShortCardId(shortCardId);
                parkingOrder.setInScanParam(scanParam);
                parkingOrder.setOutScanParam(param.getScanParam());
            }
            parkingOrder.setId(orderId);
            parkingOrder.setTotalPrice(NumberUtil.toBigDecimal(resp.getData().getTotalCharge()));
            parkingOrder.setActualPrice(NumberUtil.toBigDecimal(resp.getData().getPayCharge()));
            parkingOrder.setDiscountAmount(discountAmount);
            parkingOrder.setDiscountTime(discountTime.intValue());
            parkingOrder.setOrderState(OrderStateEnum.NOTPAY);
            MdcUtil.setMemberInfo(parkingOrder, memberInfo);
            int count;
            if (insert) {
                count = parkingOrderMapper.insert(parkingOrder);
            } else {
                count = parkingOrderMapper.updateById(parkingOrder);
            }
            if (count > 0) {
                if (parkingOrder.getActualPrice().compareTo(BigDecimal.ZERO) <= 0 && StrUtil.isNotBlank(cardNo)) {
                    //无牌月租车(临时牌),不需要查费,直接出场
                    String[] params = param.getScanParam().split(",");
                    AjbCloudResp enterOrExitResp = AjbCloudUtil.Scan.scanCodeEnterOrExit(MdcUtil.getCurrentTenantId().toString(), new ScanCodeEnterOrExitReq()
                            .setCardNo(cardNo).setDoorName(params[0]).setType("1").setUserID(Convert.toStr(memberInfo.getId())));
                    if (enterOrExitResp.getStatus()) {
                        ParkingUnlicensedCarRecord recordUpdate = new ParkingUnlicensedCarRecord();
                        recordUpdate.setId(record.getId());
                        recordUpdate.setOutScanParam(parkingOrder.getOutScanParam());
                        parkingUnlicensedCarRecordMapper.updateById(recordUpdate);
                        ParkingOrder update = new ParkingOrder();
                        update.setId(orderId);
                        update.setOrderState(OrderStateEnum.PAID);
                        update.setPayTime(DateTime.now());
                        parkingOrderMapper.updateById(update);
                        return new ParkingOrderPayResult().setId(orderId);
                    }
                }
                String orderNo = MdcUtil.getSnowflakeIdStr();
                AjbCloudResp orderResp = AjbCloudUtil.Order.orderPlace(MdcUtil.getCurrentTenantId().toString(),
                        new OrderPlaceReq().setCarNo(param.getCarNo()).setCardId(param.getCardId())
                                .setTradeNo(orderNo).setValue(parkingOrder.getActualPrice().toString()));
                if (orderResp.getStatus()) {
                    if (parkingOrder.getActualPrice().compareTo(BigDecimal.ZERO) > 0) {
                        OrderParam orderParam = new OrderParam().setId(orderId).setOrderNo(orderNo)
                                .setAmount(parkingOrder.getActualPrice()).setFrpCode(FrpCodeEnum.APPLET_PAY)
                                .setProductName("停车缴费_" + StrUtil.nullToEmpty(param.getCarNo()) + StrUtil.nullToEmpty(param.getCardId()))
                                .setTimeExpire(2);
                        switch (param.getPayType()) {
                            case WECHAT_MINI: {
                                orderParam.setFrpCode(FrpCodeEnum.APPLET_PAY).setAppId(memberInfo.getWxAppId()).setOpenId(memberInfo.getOpenId());
                                break;
                            }
                            case ALIPAY: {
                                orderParam.setFrpCode(FrpCodeEnum.ALIPAY_NATIVE).setAppId(memberInfo.getAliAppId()).setUserId(memberInfo.getUserId());
                                break;
                            }
                            default:
                        }
                        String payParam = easyPayService.orderParking(orderParam);
                        return new ParkingOrderPayResult().setId(orderId).setPayParam(payParam);
                    } else {
                        //若是不需支付直接调用安居宝接口确认订单
                        ParkingOrder update = new ParkingOrder();
                        update.setId(orderId);
                        OrderConfirmReq req = new OrderConfirmReq();
                        DateTime now = DateTime.now();
                        //若是无牌车
                        if (record != null && StrUtil.isNotBlank(param.getScanParam())) {
                            String[] params = parkingOrder.getOutScanParam().split(",");
                            req.setLeaveType("1").setDoorName(params[0]).setScanCodeOrder(params[1])
                                    .setUserID(Convert.toStr(parkingOrder.getMemberId())).setPayType("6");
                        }
                        req.setTradeNo(orderNo).setPayTime(now.toString()).setPayType("6");
                        AjbCloudResp confirmResp = AjbCloudUtil.Order.orderConfirm(MdcUtil.getCurrentTenantId().toString(), req);
                        if (confirmResp.getStatus()) {
                            update.setOrderState(OrderStateEnum.PAID);
                            update.setPayTime(now);
                            //若是无牌车
                            if (record != null && StrUtil.isNotBlank(param.getScanParam())) {
                                ParkingUnlicensedCarRecord carRecord = new ParkingUnlicensedCarRecord();
                                carRecord.setId(record.getId());
                                carRecord.setOutScanParam(parkingOrder.getOutScanParam());
                                parkingUnlicensedCarRecordMapper.updateById(carRecord);
                            }
                            //更新使用的停车券的订单id
                            List<Long> idList = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                                    .eq(ParkingCouponMemberRel::getCardId, parkingOrder.getCardId())
                                    .and(i -> i.gt(ParkingCouponMemberRel::getEffectiveEndTime, DateTime.now())
                                            .or().eq(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING))
                                    .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                                    .isNull(ParkingCouponMemberRel::getOrderId)
                            ).stream().map(ParkingCouponMemberRel::getId).collect(Collectors.toList());
                            if (idList.size() > 0) {
                                ParkingCouponMemberRel updateRel = new ParkingCouponMemberRel();
                                MdcUtil.setMemberInfo(updateRel, memberInfo);
                                parkingCouponMemberRelMapper.update(null, Wrappers.<ParkingCouponMemberRel>lambdaUpdate()
                                        .set(ParkingCouponMemberRel::getMemberId, updateRel.getMemberId())
                                        .set(ParkingCouponMemberRel::getPhoneNumber, updateRel.getPhoneNumber())
                                        .set(ParkingCouponMemberRel::getAppId, updateRel.getAppId())
                                        .set(ParkingCouponMemberRel::getUserId, updateRel.getUserId())
                                        .set(ParkingCouponMemberRel::getOpenId, updateRel.getOpenId())
                                        .set(ParkingCouponMemberRel::getNickName, updateRel.getNickName())
                                        .set(ParkingCouponMemberRel::getAvatarUrl, updateRel.getAvatarUrl())
                                        .set(ParkingCouponMemberRel::getOrderId, orderId)
                                        .in(ParkingCouponMemberRel::getId, idList));
                            }
                        } else {
                            throw new ServiceException("安居宝报错:" + confirmResp.getMsg());
                        }
                        parkingOrderMapper.updateById(update);
                        return new ParkingOrderPayResult().setId(orderId);
                    }
                } else {
                    throw new ServiceException("安居宝报错:" + orderResp.getMsg());
                }
            } else {
                throw new ServiceException("保存订单失败");
            }
        } else {
            throw new ServiceException("安居宝报错:" + resp.getMsg());
        }
    }

    @Transactional
    @Override
    public void finishOrder(Boolean isSuccess, PaymentTypeEnum paymentType, EasypayPaymentRecord record) {
        if (isSuccess) {
            ParkingOrder parkingOrder = parkingOrderMapper.selectById(record.getOrderId());
            DateTime now = DateTime.now();
            ParkingOrder update = new ParkingOrder();
            update.setId(record.getOrderId());
            OrderConfirmReq req = new OrderConfirmReq();
            //是否无牌车标识
            boolean flag = false;
            //无牌车确认订单
            if (StrUtil.isNotBlank(parkingOrder.getOutScanParam()) && StrUtil.isNotBlank(parkingOrder.getCardId())) {
                String[] params = parkingOrder.getOutScanParam().split(",");
                req.setLeaveType("1").setDoorName(params[0]).setScanCodeOrder(params[1])
                        .setUserID(Convert.toStr(parkingOrder.getMemberId()));
                flag = true;
            }
            req.setTradeNo(record.getOrderNo()).setPayTime(now.toString());
            switch (paymentType) {
                case WECHAT: {
                    req.setPayType("1");
                    break;
                }
                case ALIPAY: {
                    req.setPayType("4");
                    break;
                }
                default:
            }
            AjbCloudResp resp = AjbCloudUtil.Order.orderConfirm(MdcUtil.getCurrentTenantId().toString(), req);
            if (resp.getStatus()) {
                update.setOrderState(OrderStateEnum.PAID);
                update.setPayTime(now);
                parkingOrderMapper.updateById(update);
                try {
                    if (flag) {
                        //若是无牌车,更新无牌车记录表
                        ParkingUnlicensedCarRecord carRecord = new ParkingUnlicensedCarRecord();
                        carRecord.setCardId(parkingOrder.getCardId());
                        carRecord.setInScanParam(parkingOrder.getInScanParam());
                        parkingUnlicensedCarRecordMapper.updateById(carRecord);
                    }
                    //更新使用的停车券的订单id
                    List<Long> idList = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                            .and(i -> i.eq(ParkingCouponMemberRel::getCarNo, parkingOrder.getCarNo())
                                    .or().eq(ParkingCouponMemberRel::getCardId, parkingOrder.getCardId()))
                            .and(i -> i.gt(ParkingCouponMemberRel::getEffectiveEndTime, now)
                                    .or().eq(ParkingCouponMemberRel::getEffectiveType, EffectiveTypeEnum.EVERLASTING))
                            .eq(ParkingCouponMemberRel::getConsumeState, ConsumeStateEnum.CONSUMED)
                            .isNull(ParkingCouponMemberRel::getOrderId)
                    ).stream().map(ParkingCouponMemberRel::getId).collect(Collectors.toList());
                    if (idList.size() > 0) {
                        MemberInfo memberInfo = memberService.getUnifiedMember(Wrappers.<MemberInfo>lambdaQuery()
                                .eq(StrUtil.isNotBlank(parkingOrder.getUserId()), MemberInfo::getAliAppId, parkingOrder.getAppId())
                                .eq(StrUtil.isNotBlank(parkingOrder.getOpenId()), MemberInfo::getWxAppId, parkingOrder.getAppId())
                                .eq(StrUtil.isAllBlank(parkingOrder.getUserId(), parkingOrder.getOpenId()), MemberInfo::getId, parkingOrder.getMemberId())
                        );
                        ParkingCouponMemberRel updateRel = new ParkingCouponMemberRel();
                        MdcUtil.setMemberInfo(updateRel, memberInfo);
                        parkingCouponMemberRelMapper.update(null, Wrappers.<ParkingCouponMemberRel>lambdaUpdate()
                                .set(ParkingCouponMemberRel::getMemberId, updateRel.getMemberId())
                                .set(ParkingCouponMemberRel::getPhoneNumber, updateRel.getPhoneNumber())
                                .set(ParkingCouponMemberRel::getAppId, updateRel.getAppId())
                                .set(ParkingCouponMemberRel::getUserId, updateRel.getUserId())
                                .set(ParkingCouponMemberRel::getOpenId, updateRel.getOpenId())
                                .set(ParkingCouponMemberRel::getNickName, updateRel.getNickName())
                                .set(ParkingCouponMemberRel::getAvatarUrl, updateRel.getAvatarUrl())
                                .set(ParkingCouponMemberRel::getOrderId, record.getOrderId())
                                .in(ParkingCouponMemberRel::getId, idList));
                    }
                } catch (Exception e) {
                    log.error("更新停车订单额外信息失败", e);
                }
            } else {
                //安居宝确认订单失败才需要退款
                log.info("安居宝确认订单报错:{}-{}", resp.getCode(), resp.getMsg());
                //不主动发起退款
                /*try {
                    easyPayService.refundParkingOrder(new RefundParam().setPayType(record.getPayType()).setId(parkingOrder.getId()).setOrderNo(record.getOrderNo())
                            .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount()).setRefundReason(resp.getMsg()));
                } catch (ServiceException e) {
                    log.error("发起停车缴费退款失败", e);
                }*/
                update.setRefundReason(resp.getMsg());
                update.setOrderState(OrderStateEnum.REFUNDCONFIRM);
                parkingOrderMapper.updateById(update);
            }
        } else {
            log.error("支付回调通知失败");
        }
    }

    @Override
    public void finishRefund(Boolean isSuccess, EasypayRefundRecord record) {
        ParkingOrder update = new ParkingOrder();
        update.setId(record.getOrderId());
        if (isSuccess) {
            update.setRefundTime(DateTime.now());
            update.setOrderState(OrderStateEnum.REFUNDSUCC);
        } else {
            update.setOrderState(OrderStateEnum.REFUNDFAIL);
        }
        parkingOrderMapper.updateById(update);
    }

    @Override
    public IPage<ParkingOrderApiResult> pageOrder(PageParam<ParkingOrder> param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return parkingOrderMapper.selectPage(param.page(), Wrappers.<ParkingOrder>lambdaQuery()
                .eq(ParkingOrder::getMemberId, memberId)
                .orderByDesc(ParkingOrder::getLastUpdateTime)
        ).convert(order -> {
            ParkingOrderApiResult result = Convert.convert(ParkingOrderApiResult.class, order);
            ParkingOrderInvoice invoice = parkingOrderInvoiceMapper.selectOne(Wrappers.<ParkingOrderInvoice>lambdaQuery()
                    .select(ParkingOrderInvoice::getId)
                    .eq(ParkingOrderInvoice::getOrderId, result.getId())
                    .eq(ParkingOrderInvoice::getMemberId, memberId)
                    .last("limit 1")
            );
            if (invoice != null) {
                result.setInvoiceId(invoice.getId());
            }
            return result;
        });
    }

    @Override
    public ParkingOrderResult getOrder(OrderGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        ParkingOrder parkingOrder = parkingOrderMapper.selectOne(Wrappers.<ParkingOrder>lambdaQuery()
                .eq(ParkingOrder::getId, param.getId())
                .eq(ParkingOrder::getMemberId, memberId)
        );
        if (parkingOrder == null) {
            throw new ServiceException("订单不存在");
        }
        ParkingOrderResult result = new ParkingOrderResult();
        BeanUtil.copyProperties(parkingOrder, result);
        List<ParkingCouponMemberRel> relList = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                .eq(ParkingCouponMemberRel::getOrderId, parkingOrder.getId())
        );
        result.setCouponRelList(relList);
        return result;
    }

    @Override
    public void refundOrder(OrderGetParam param) {
        ParkingOrder parkingOrder = parkingOrderMapper.selectById(param.getId());
        if (parkingOrder.getOrderState() != OrderStateEnum.REFUNDFAIL && parkingOrder.getOrderState() != OrderStateEnum.REFUNDCONFIRM) {
            throw new ServiceException("此订单不能退款");
        }
        EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.PARKING)
                .eq(EasypayPaymentRecord::getOrderId, parkingOrder.getId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
                .last("limit 1")
        );
        if (record == null) {
            throw new ServiceException("此订单信息有误");
        }
        //发起退款
        easyPayService.refundParkingOrder(new RefundParam().setAppId(record.getAppId()).setPayType(record.getPayType()).setId(parkingOrder.getId()).setOrderNo(record.getOrderNo())
                .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount()).setRefundReason(parkingOrder.getRefundReason()));
        ParkingOrder update = new ParkingOrder();
        update.setId(parkingOrder.getId());
        update.setOrderState(OrderStateEnum.REFUNDING);
        parkingOrderMapper.updateById(update);
    }

    @Transactional
    @Override
    public void addInvoice(InvoiceAddParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        RedisUtil.transactionalLock("addInvoiceLock" + param.getOrderId());
        ParkingOrder parkingOrder = parkingOrderMapper.selectOne(Wrappers.<ParkingOrder>lambdaQuery()
                .eq(ParkingOrder::getMemberId, memberInfo.getId())
                .eq(ParkingOrder::getId, param.getOrderId())
        );
        if (parkingOrder.getOrderState() != OrderStateEnum.PAID) {
            throw new ServiceException("此订单不能申请开发票");
        }
        DateTime payTime = DateTime.of(parkingOrder.getPayTime());
        EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.PARKING)
                .eq(EasypayPaymentRecord::getOrderId, parkingOrder.getId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
                .last("limit 1")
        );
        if (record == null) {
            throw new ServiceException("此订单信息有误");
        }
        ParkingOrderInvoice invoice = new ParkingOrderInvoice();
        BeanUtil.copyProperties(param, invoice);
        MdcUtil.setMemberInfo(invoice, memberInfo);
        invoice.setCardId(parkingOrder.getCardId());
        invoice.setCarNo(parkingOrder.getCarNo());
        invoice.setOrderId(record.getOrderId());
        invoice.setOrderNo(record.getOrderNo());
        invoice.setTotalFee(record.getAmount());
        invoice.setPayTime(payTime);
        invoice.setPayType(PayTypeEnum.TEMPPARKING);
        int count = parkingOrderInvoiceMapper.insert(invoice);
        if (count <= 0) {
            throw new ServiceException("新增发票失败");
        }
        InvoiceAddReq req = new InvoiceAddReq();
        BeanUtil.copyProperties(param, req);
        req.setCardId(invoice.getCardId());
        req.setCarNo(invoice.getCarNo());
        req.setTradeNo(invoice.getOrderNo());
        req.setInvoiceType(Convert.toStr(invoice.getInvoiceType().getType()));
        req.setPayType(Convert.toStr(invoice.getPayType().getType()));
        req.setPayTime(DateUtil.dateNew(invoice.getPayTime()).toString());
        req.setTotalFee(invoice.getTotalFee().toString());
        AjbCloudResp resp = AjbCloudUtil.Invoice.addInvoice(MdcUtil.getCurrentTenantId().toString(), req);
        if (!resp.getStatus()) {
            if ("0319".equals(resp.getCode())) {
                DateTime newPayTime = DateTime.of(payTime).offsetNew(DateField.SECOND, -1);
                parkingOrderMapper.update(null, Wrappers.<ParkingOrder>lambdaUpdate()
                        .set(ParkingOrder::getPayTime, newPayTime)
                        .eq(ParkingOrder::getId, parkingOrder.getId())
                );
                parkingOrderInvoiceMapper.update(null, Wrappers.<ParkingOrderInvoice>lambdaUpdate()
                        .set(ParkingOrderInvoice::getPayTime, newPayTime)
                        .eq(ParkingOrderInvoice::getId, invoice.getId())
                );
                req.setPayTime(DateUtil.dateNew(newPayTime).toString());
                resp = AjbCloudUtil.Invoice.addInvoice(MdcUtil.getCurrentTenantId().toString(), req);
                if (!resp.getStatus()) {
                    throw new ServiceException("安居宝报错:" + resp.getMsg());
                }
            } else {
                throw new ServiceException("安居宝报错:" + resp.getMsg());
            }
        }
    }

    @Override
    public IPage<ParkingOrderInvoice> pageInvoice(InvoicePageParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return parkingOrderInvoiceMapper.selectPage(param.page(), Wrappers.<ParkingOrderInvoice>lambdaQuery()
                .eq(ParkingOrderInvoice::getMemberId, memberId)
                .eq(param.getInvoiceType() != null, ParkingOrderInvoice::getInvoiceType, param.getInvoiceType())
                .like(StrUtil.isNotBlank(param.getBuyerNo()), ParkingOrderInvoice::getBuyerNo, param.getBuyerNo())
                .like(StrUtil.isNotBlank(param.getBuyerAddress()), ParkingOrderInvoice::getBuyerAddress, param.getBuyerAddress())
                .like(StrUtil.isNotBlank(param.getBuyerPhone()), ParkingOrderInvoice::getBuyerPhone, param.getBuyerPhone())
                .like(StrUtil.isNotBlank(param.getBuyerBank()), ParkingOrderInvoice::getBuyerBank, param.getBuyerBank())
                .like(StrUtil.isNotBlank(param.getBuyerAccount()), ParkingOrderInvoice::getBuyerAccount, param.getBuyerAccount())
                .like(StrUtil.isNotBlank(param.getBuyerName()), ParkingOrderInvoice::getBuyerName, param.getBuyerName())
                .orderByDesc(ParkingOrderInvoice::getCreateTime)
        );
    }

    @Override
    public ParkingOrderInvoice getInvoice(InvoiceGetParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        ParkingOrderInvoice invoice = parkingOrderInvoiceMapper.selectOne(Wrappers.<ParkingOrderInvoice>lambdaQuery()
                .eq(ParkingOrderInvoice::getMemberId, memberId)
                .eq(ParkingOrderInvoice::getId, param.getId())
        );
        //若发票路径为空,向安居宝请求发票下载地址
        if (StrUtil.isBlank(invoice.getInvoiceUrl())) {
            InvoiceUrlGetResp resp = AjbCloudUtil.Invoice.getInvoiceUrl(MdcUtil.getCurrentTenantId().toString(),
                    new InvoiceUrlGetReq().setTradeNo(invoice.getOrderNo()));
            if (resp.getStatus()) {
                invoice.setInvoiceUrl(resp.getData());
                parkingOrderInvoiceMapper.update(null, Wrappers.<ParkingOrderInvoice>lambdaUpdate()
                        .set(ParkingOrderInvoice::getInvoiceUrl, resp.getData())
                        .eq(ParkingOrderInvoice::getId, param.getId())
                );
            }
        }
        return invoice;
    }

    @Override
    public IPage<String> pageCarNo(CarNoPageParam param) {
        DateTime now = DateTime.now();
        CarInfoGetResp resp = AjbCloudUtil.Info.getCarInfo(MdcUtil.getCurrentTenantId().toString(),
                new CarInfoGetReq().setIsLike("1").setCarNo(param.getCarNo())
                        .setStartTime(now.offsetNew(DateField.YEAR, -1).toString()).setEndTime(now.toString()));
        IPage<String> page = param.page();
        if (resp.getStatus()) {
            PageResp<CarInfoGetResp.Result> pageResp = resp.getData();
            page.setTotal(pageResp.getCount()).setRecords(pageResp.getResult().stream().map(CarInfoGetResp.Result::getCarNo).collect(Collectors.toList()));
        } else {
            page.setSize(0).setTotal(0);
        }
        return page;
    }

    @Override
    public IPage<ParkingUnlicensedCarRecord> pageUnlicensedCar(ParkingUnlicensedPageParam param) {
        return parkingUnlicensedCarRecordMapper.selectPage(param.page(), Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), ParkingUnlicensedCarRecord::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), ParkingUnlicensedCarRecord::getNickName, param.getNickName())
                .eq(StrUtil.isNotBlank(param.getShortCardId()), ParkingUnlicensedCarRecord::getShortCardId, param.getShortCardId())
                .ge(param.getCreateTimeStart() != null, ParkingUnlicensedCarRecord::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ParkingUnlicensedCarRecord::getCreateTime, param.getCreateTimeEnd())
                .orderByDesc(ParkingUnlicensedCarRecord::getCreateTime)
        );
    }

    @Override
    public ParkingOrderChargeResult getParkingOrderCharge(ParkingOrderChargeGetParam param) {
        if (StrUtil.isAllBlank(param.getCarNo(), param.getCardId()) && StrUtil.isNotBlank(param.getShortCardId())) {
            ParkingUnlicensedCarRecord record = parkingUnlicensedCarRecordMapper.selectOne(Wrappers.<ParkingUnlicensedCarRecord>lambdaQuery()
                    .eq(ParkingUnlicensedCarRecord::getShortCardId, param.getShortCardId())
            );
            if (record == null) {
                throw new ServiceException("此短车卡号不存在");
            }
            param.setCardId(record.getCardId());
        }
        BigDecimal worth = BigDecimal.ZERO;
        CostExtendGetReq req = new CostExtendGetReq();
        ParkingCoupon coupon = null;
        if (param.getParkingCouponId() != null) {
            coupon = parkingCouponService.getCoupon(new CouponGetParam().setId(param.getParkingCouponId()));
            if (coupon.getState() != CouponStateEnum.NORMAL) {
                throw new ServiceException("停车券已" + coupon.getState().getName());
            }
            BigDecimal couponWorth = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;
            if (coupon.getInventoryType() == InventoryTypeEnum.QUOTA) {
                if (param.getValue() == null) {
                    throw new ServiceException("派发金额不能为空");
                }
                worth = couponWorth = param.getValue();
                total = parkingCouponService.getTotalDistributeWorth(coupon.getId());
            } else if (coupon.getInventoryType() == InventoryTypeEnum.AMOUNT) {
                if (param.getCount() == null) {
                    throw new ServiceException("派发数不能为空");
                }
                couponWorth = NumberUtil.toBigDecimal(param.getCount());
                worth = NumberUtil.mul(coupon.getCouponWorth(), param.getCount());
                total = parkingCouponService.getTotalDistributeCount(coupon.getId());
            }
            if (NumberUtil.isLess(coupon.getInventory(), total.add(couponWorth))) {
                throw new ServiceException("停车券库存不足");
            }
            if (coupon.getCouponType() == CouponTypeEnum.MONEY) {
                req.setDiscountAmount(worth.toString());
            } else if (coupon.getCouponType() == CouponTypeEnum.TIME) {
                req.setDiscountTime(Convert.toStr(NumberUtil.mul(worth, 60).intValue()));
            } else if (coupon.getCouponType() == CouponTypeEnum.FREE) {
                //若是全免券把优惠金额调至最大integer
                req.setDiscountAmount("999999.99");
            }
        }
        GetCostResp resp = AjbCloudUtil.Cost.getCostExtend(MdcUtil.getCurrentTenantId().toString(), req.setCardId(param.getCardId()).setCarNo(param.getCarNo()));
        if (resp.getStatus()) {
            ParkingOrderChargeResult result = new ParkingOrderChargeResult();
            BeanUtil.copyProperties(resp.getData(), result);
            result.setTotalPrice(NumberUtil.toBigDecimal(resp.getData().getTotalCharge()));
            result.setActualPrice(NumberUtil.toBigDecimal(resp.getData().getPayCharge()));
            result.setDiscountAmount(BigDecimal.ZERO);
            result.setDiscountTime(0);
            if (coupon != null) {
                if (coupon.getCouponType() == CouponTypeEnum.MONEY) {
                    result.setDiscountAmount(worth);
                } else if (coupon.getCouponType() == CouponTypeEnum.TIME) {
                    result.setDiscountTime(worth.intValue());
                    result.setDiscountAmount(result.getTotalPrice().subtract(result.getActualPrice()));
                } else if (coupon.getCouponType() == CouponTypeEnum.FREE) {
                    result.setDiscountAmount(result.getTotalPrice());
                }
            }
            result.setChargeCode(resp.getData().getCode());
            result.setChargeState(EnumUtil.likeValueOf(ChargeStateEnum.class, resp.getData().getCode()));
            result.setShortCardId(param.getShortCardId());
            return result;
        } else {
            throw new ServiceException("安居宝报错:" + resp.getMsg());
        }
    }

    @Override
    public IPage<ParkingOrder> pageParkingOrder(ParkingOrderPageParam param) {
        return parkingOrderMapper.selectPage(param.page(), Wrappers.<ParkingOrder>lambdaQuery()
                .eq(param.getOrderState() != null, ParkingOrder::getOrderState, param.getOrderState())
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), ParkingOrder::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), ParkingOrder::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getCarNo()), ParkingOrder::getCarNo, param.getCarNo())
                .like(StrUtil.isNotBlank(param.getCardId()), ParkingOrder::getCardId, param.getCardId())
                .like(StrUtil.isNotBlank(param.getShortCardId()), ParkingOrder::getShortCardId, param.getShortCardId())
                .orderByDesc(ParkingOrder::getCreateTime)
        );
    }

    @Override
    public ParkingOrderResult getParkingOrder(OrderGetParam param) {
        ParkingOrder parkingOrder = parkingOrderMapper.selectById(param.getId());
        ParkingOrderResult result = new ParkingOrderResult();
        BeanUtil.copyProperties(parkingOrder, result);
        List<ParkingCouponMemberRel> relList = parkingCouponMemberRelMapper.selectList(Wrappers.<ParkingCouponMemberRel>lambdaQuery()
                .eq(ParkingCouponMemberRel::getOrderId, parkingOrder.getId())
        );
        List<EasypayPaymentRecord> paymentRecordList = easypayPaymentRecordMapper.selectList(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderId, parkingOrder.getId())
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.PARKING)
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
        );
        List<EasypayRefundRecord> refundRecordList = easypayRefundRecordMapper.selectList(Wrappers.<EasypayRefundRecord>lambdaQuery()
                .eq(EasypayRefundRecord::getOrderId, parkingOrder.getId())
                .eq(EasypayRefundRecord::getOrderType, OrderTypeEnum.PARKING)
                .orderByDesc(EasypayRefundRecord::getCreateTime)
        );
        result.setCouponRelList(relList);
        result.setPaymentRecordList(paymentRecordList);
        result.setRefundRecordList(refundRecordList);
        return result;
    }

    @Override
    public void refundParkingOrder(ParkingOrderRefundParam param) {
        ParkingOrder parkingOrder = parkingOrderMapper.selectById(param.getId());
        if (parkingOrder.getOrderState() == OrderStateEnum.NOTPAY || parkingOrder.getOrderState() == OrderStateEnum.REFUNDING
                || parkingOrder.getOrderState() == OrderStateEnum.REFUNDSUCC) {
            throw new ServiceException("此订单不能退款");
        }
        EasypayPaymentRecord record = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.PARKING)
                .eq(EasypayPaymentRecord::getOrderId, parkingOrder.getId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                .orderByDesc(EasypayPaymentRecord::getCreateTime)
                .last("limit 1")
        );
        if (record == null) {
            throw new ServiceException("此订单信息有误");
        }
        //发起退款
        easyPayService.refundParkingOrder(new RefundParam().setAppId(record.getAppId()).setPayType(record.getPayType()).setId(parkingOrder.getId()).setOrderNo(record.getOrderNo())
                .setRefundOrderNo(MdcUtil.getSnowflakeIdStr()).setRefundAmount(record.getAmount()).setRefundReason(param.getRefundReason()));
        ParkingOrder update = new ParkingOrder();
        update.setId(parkingOrder.getId());
        update.setOrderState(OrderStateEnum.REFUNDING);
        parkingOrderMapper.updateById(update);
    }

    @Override
    public IPage<ParkingOrderInvoice> pageParkingInvoice(ParkingInvoicePageParam param) {
        return parkingOrderInvoiceMapper.selectPage(param.page(), Wrappers.<ParkingOrderInvoice>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getPhoneNumber()), ParkingOrderInvoice::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), ParkingOrderInvoice::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getCarNo()), ParkingOrderInvoice::getCarNo, param.getCarNo())
                .like(StrUtil.isNotBlank(param.getCardId()), ParkingOrderInvoice::getCardId, param.getCardId())
                .ge(param.getPayTimeStart() != null, ParkingOrderInvoice::getPayTime, param.getPayTimeStart())
                .le(param.getPayTimeEnd() != null, ParkingOrderInvoice::getPayTime, param.getPayTimeEnd())
                .eq(param.getInvoiceType() != null, ParkingOrderInvoice::getInvoiceType, param.getInvoiceType())
                .like(StrUtil.isNotBlank(param.getBuyerNo()), ParkingOrderInvoice::getBuyerNo, param.getBuyerNo())
                .like(StrUtil.isNotBlank(param.getBuyerAddress()), ParkingOrderInvoice::getBuyerAddress, param.getBuyerAddress())
                .like(StrUtil.isNotBlank(param.getBuyerPhone()), ParkingOrderInvoice::getBuyerPhone, param.getBuyerPhone())
                .like(StrUtil.isNotBlank(param.getBuyerBank()), ParkingOrderInvoice::getBuyerBank, param.getBuyerBank())
                .like(StrUtil.isNotBlank(param.getBuyerAccount()), ParkingOrderInvoice::getBuyerAccount, param.getBuyerAccount())
                .like(StrUtil.isNotBlank(param.getBuyerName()), ParkingOrderInvoice::getBuyerName, param.getBuyerName())
                .ge(param.getCreateTimeStart() != null, ParkingOrderInvoice::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, ParkingOrderInvoice::getCreateTime, param.getCreateTimeEnd())
                .orderByDesc(ParkingOrderInvoice::getCreateTime)
        );
    }
}
