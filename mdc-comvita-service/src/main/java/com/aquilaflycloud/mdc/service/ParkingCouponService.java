package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.parking.InventoryTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.LimitTypeEnum;
import com.aquilaflycloud.mdc.enums.parking.ReceiveSourceEnum;
import com.aquilaflycloud.mdc.model.parking.ParkingCoupon;
import com.aquilaflycloud.mdc.model.parking.ParkingCouponMemberRel;
import com.aquilaflycloud.mdc.param.parking.*;
import com.aquilaflycloud.mdc.result.parking.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ParkingCouponService
 *
 * @author star
 * @date 2020-01-13
 */
public interface ParkingCouponService {
    BigDecimal getTotalDistributeWorth(Long couponId, Boolean... reload);

    BigDecimal getTotalDistributeCount(Long couponId, Boolean... reload);

    void updateDistributeCount(Long couponId, InventoryTypeEnum inventoryType);

    IPage<ParkingCouponOrderRecordResult> pageCouponRecord(CouponRecordPageParam param);

    Long addCouponRecord(CouponRecordAddParam param, CreateSourceEnum createSource);

    void addCouponRecord(CouponRecordAddParam param);

    void addInventory(CouponRecordInventoryAddParam param, CreateSourceEnum createSource);

    void addInventory(CouponRecordInventoryAddParam param);

    void auditCouponRecord(CouponRecordParam param);

    IPage<ParkingCouponResult> pageCoupon(CouponPageParam param);

    ParkingCoupon getCoupon(Long couponId);

    ParkingCouponResult getCoupon(CouponGetParam param);

    void toggleCoupon(CouponGetParam param);

    void editCoupon(CouponEditParam param, CreateSourceEnum createSource);

    void editCoupon(CouponEditParam param);

    void deleteCoupon(CouponGetParam param);

    void addCouponMemberRel(CouponMemberRelAddParam param);

    ParkingCouponMemberRel addCouponMemberRel(CouponMemberRelAddParam param, ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource);

    void useCoupon(CouponMemberRelAddParam param, ParkingCouponMemberRel rel, Long orderId);

    void validateLimit(boolean isUsed, Long couponId, BigDecimal couponWorth, Integer couponCount, LimitTypeEnum limitType, String limitContent,
                       Long memberId, String carNo, String cardId, String shortCardId);

    IPage<ParkingCouponMemberRel> pageCouponMemberRel(CouponMemberRelPageParam param);

    RelStatisticsResult getStatistics(StatisticsGetParam param);

    IPage<RelDetailAnalysisResult> pageDetailAnalysis(RelTimePageParam param);

    IPage<RelSummaryAnalysisResult> pageSummaryAnalysis(RelTimePageParam param);

    IPage<RelRecordAnalysisResult> pageRecordAnalysis(RelTimePageParam param);

    void revokedRel(ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource);

    IPage<ParkingCouponMemberRel> pageCouponRel(CouponRelPageParam param);

    ParkingCouponMemberRel getCouponRel(CouponRelGetParam param);

    void judgeCouponRel(ParkingCouponMemberRel rel, Long memberId, String carNo, String cardId);

    void validateUseCouponLimit(Map<String, List<ParkingCouponMemberRel>> relMap, Long memberId, String carNo, String cardId);

    void judgeCouponRel(CouponRelJudgeParam param);

    void useCouponRel(CouponRelUseParam param);
}

