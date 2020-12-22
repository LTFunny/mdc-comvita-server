package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.param.coupon.*;
import com.aquilaflycloud.mdc.result.coupon.CouponResult;
import com.aquilaflycloud.mdc.result.coupon.RelAnalysisResult;
import com.aquilaflycloud.mdc.result.coupon.StatisticsResult;
import com.aquilaflycloud.mdc.service.CouponInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * CouponInfoController
 *
 * @author star
 * @date 2020-03-08
 */
@RestController
@Api(tags = "优惠券管理")
public class CouponInfoController {

    @Resource
    private CouponInfoService couponInfoService;

    @ApiOperation(value = "获取优惠券列表(分页)", notes = "获取优惠券列表(分页)")
    @PreAuthorize("hasAuthority('mdc:coupon:list')")
    @ApiMapping(value = "backend.mdc.coupon.info.page", method = RequestMethod.POST, permission = true)
    public IPage<CouponResult> pageCoupon(CouponPageParam param) {
        return couponInfoService.pageCoupon(param);
    }

    @ApiOperation(value = "新增优惠券", notes = "新增优惠券")
    @PreAuthorize("hasAuthority('mdc:coupon:add')")
    @ApiMapping(value = "backend.mdc.coupon.info.add", method = RequestMethod.POST, permission = true)
    public void addCoupon(CouponAddParam param) {
        couponInfoService.addCoupon(param);
    }

    @ApiOperation(value = "编辑优惠券", notes = "编辑优惠券")
    @PreAuthorize("hasAuthority('mdc:coupon:edit')")
    @ApiMapping(value = "backend.mdc.coupon.info.edit", method = RequestMethod.POST, permission = true)
    public void editCoupon(CouponEditParam param) {
        couponInfoService.editCoupon(param);
    }

    @ApiOperation(value = "新增支付宝优惠券", notes = "新增支付宝优惠券")
    @PreAuthorize("hasAuthority('mdc:alipayCoupon:add')")
    @ApiMapping(value = "backend.mdc.coupon.alipay.add", method = RequestMethod.POST, permission = true)
    public void addAlipayCoupon(AlipayCouponAddParam param) {
        couponInfoService.addCoupon(param);
    }

    @ApiOperation(value = "编辑支付宝优惠券", notes = "编辑支付宝优惠券")
    @PreAuthorize("hasAuthority('mdc:alipayCoupon:edit')")
    @ApiMapping(value = "backend.mdc.coupon.alipay.edit", method = RequestMethod.POST, permission = true)
    public void editAlipayCoupon(AlipayCouponEditParam param) {
        couponInfoService.editCoupon(param);
    }

    @ApiOperation(value = "激活支付宝优惠券", notes = "编辑支付宝优惠券")
    @PreAuthorize("hasAuthority('mdc:alipayCoupon:active')")
    @ApiMapping(value = "backend.mdc.coupon.alipay.active", method = RequestMethod.POST, permission = true)
    public BaseResult<String> activeAlipayCoupon(AlipayCouponActiveParam param) {
        return couponInfoService.activeCoupon(param);
    }

    @ApiOperation(value = "启用/停用优惠券", notes = "启用/停用优惠券")
    @PreAuthorize("hasAuthority('mdc:coupon:edit')")
    @ApiMapping(value = "backend.mdc.coupon.info.toggle", method = RequestMethod.POST, permission = true)
    public void toggleState(CouponGetParam param) {
        couponInfoService.toggleState(param);
    }

    @ApiOperation(value = "获取优惠券", notes = "获取优惠券")
    @PreAuthorize("hasAuthority('mdc:coupon:get')")
    @ApiMapping(value = "backend.mdc.coupon.info.get", method = RequestMethod.POST, permission = true)
    public CouponResult getCoupon(CouponGetParam param) {
        return couponInfoService.getCoupon(param);
    }

    @ApiOperation(value = "审核优惠券", notes = "审核优惠券")
    @PreAuthorize("hasAuthority('mdc:coupon:audit')")
    @ApiMapping(value = "backend.mdc.coupon.info.audit", method = RequestMethod.POST, permission = true)
    public void auditCoupon(CouponAuditParam param) {
        couponInfoService.auditCoupon(param);
    }

    @ApiOperation(value = "获取优惠券概况统计", notes = "获取优惠券概况统计")
    @PreAuthorize("hasAuthority('mdc:coupon:list')")
    @ApiMapping(value = "backend.mdc.coupon.statistics.get", method = RequestMethod.POST, permission = true)
    public StatisticsResult getStatistics(AuthParam param) {
        return couponInfoService.getStatistics(param);
    }

    @ApiOperation(value = "会员优惠券记录列表(分页)", notes = "会员优惠券记录列表(分页)")
    @PreAuthorize("hasAuthority('mdc:couponRel:list')")
    @ApiMapping(value = "backend.mdc.coupon.rel.page", method = RequestMethod.POST, permission = true)
    public IPage<CouponMemberRel> pageCouponRel(CouponRelPageParam param) {
        return couponInfoService.pageCouponRel(param);
    }

    @ApiOperation(value = "获取会员优惠券记录", notes = "获取会员优惠券记录")
    @PreAuthorize("hasAuthority('mdc:couponRel:get')")
    @ApiMapping(value = "backend.mdc.coupon.rel.get", method = RequestMethod.POST, permission = true)
    public CouponMemberRel getCouponRel(CouponRelGetParam param) {
        return couponInfoService.getCouponRel(param);
    }

    @ApiOperation(value = "核销会员优惠券记录(核销码核销)", notes = "核销会员优惠券记录(核销码核销)")
    @PreAuthorize("hasAuthority('mdc:couponRel:use')")
    @ApiMapping(value = "backend.mdc.coupon.rel.use", method = RequestMethod.POST, permission = true)
    public CouponMemberRel useCoupon(CouponRelUseParam param) {
        return couponInfoService.useCouponRel(param);
    }

    @ApiOperation(value = "会员优惠券核销分析", notes = "会员优惠券核销分析")
    @PreAuthorize("hasAuthority('mdc:couponRel:list')")
    @ApiMapping(value = "backend.mdc.coupon.relAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<RelAnalysisResult> getRelAnalysis(CouponRelAnalysisGetParam param) {
        return couponInfoService.getRelAnalysis(param);
    }
}
