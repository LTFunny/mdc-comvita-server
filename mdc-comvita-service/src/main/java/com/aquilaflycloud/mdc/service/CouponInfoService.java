package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.coupon.CouponModeEnum;
import com.aquilaflycloud.mdc.enums.coupon.ReceiveSourceEnum;
import com.aquilaflycloud.mdc.enums.coupon.VerificateModeEnum;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.param.coupon.*;
import com.aquilaflycloud.mdc.result.coupon.CouponInfoResult;
import com.aquilaflycloud.mdc.result.coupon.CouponResult;
import com.aquilaflycloud.mdc.result.coupon.RelAnalysisResult;
import com.aquilaflycloud.mdc.result.coupon.StatisticsResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.math.BigDecimal;
import java.util.List;

public interface CouponInfoService {

    Integer getTotalReceiveCount(Long couponId, Boolean... reload);

    IPage<CouponResult> pageCoupon(CouponPageParam param);

    CouponInfo addCoupon(CouponAddParam param, CreateSourceEnum createSource, CouponModeEnum couponMode);

    void addCoupon(CouponAddParam param);

    CouponInfo editCoupon(CouponEditParam param, CreateSourceEnum createSource, CouponModeEnum couponModeEnum);

    void editCoupon(CouponEditParam param);

    void toggleState(CouponGetParam param);

    CouponInfo getCoupon(Long couponId);

    CouponResult getCoupon(CouponGetParam param);

    void auditCoupon(CouponAuditParam param);

    StatisticsResult getStatistics(AuthParam param);

    IPage<CouponMemberRel> pageCouponRel(CouponRelPageParam param);

    CouponMemberRel getCouponRel(CouponRelGetParam param);

    CouponMemberRel useCouponRel(CouponRelUseParam param);

    CouponMemberRel useCouponRel(String code, BigDecimal price, String orgId, VerificateModeEnum verificateMode);

    List<RelAnalysisResult> getRelAnalysis(CouponRelAnalysisGetParam param);

    IPage<CouponInfoResult> pageCouponInfo(CouponInfoPageParam param);

    CouponInfoResult getCouponInfo(CouponGetParam param);

    String addRel(CouponGetParam param, Integer receiveCount, MemberInfo memberInfo, ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource);

    BaseResult<String> addRel(CouponGetParam param);

    void revokedRel(ReceiveSourceEnum receiveSource, Long receiveSourceId, CreateSourceEnum createSource, boolean enforce);

    IPage<CouponMemberRel> pageCouponInfoRel(CouponInfoRelPageParam param);

    CouponMemberRel getCouponInfoRel(CouponRelGetParam param);

    CouponMemberRel useCouponRel(CouponMemberRelUseParam param);

    IPage<CouponInfoResult> pageShopCoupon(CouponInfoShopPageParam param);
}
