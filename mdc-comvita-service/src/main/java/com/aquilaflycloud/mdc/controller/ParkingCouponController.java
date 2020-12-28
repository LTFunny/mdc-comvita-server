package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.parking.ParkingCouponMemberRel;
import com.aquilaflycloud.mdc.param.parking.*;
import com.aquilaflycloud.mdc.result.parking.*;
import com.aquilaflycloud.mdc.service.ParkingCouponService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ParkingCouponController
 *
 * @author star
 * @date 2020-01-13
 */
@RestController
@Api(tags = "停车券管理")
public class ParkingCouponController {

    @Resource
    private ParkingCouponService parkingCouponService;

    @ApiOperation(value = "查询停车券购买记录", notes = "查询停车券购买记录")
    @PreAuthorize("hasAuthority('mdc:parkingCouponRecord:list')")
    @ApiMapping(value = "backend.comvita.parking.couponRecord.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingCouponOrderRecordResult> pageCouponRecord(CouponRecordPageParam param) {
        return parkingCouponService.pageCouponRecord(param);
    }

    @ApiOperation(value = "新增停车券购买记录", notes = "新增停车券购买记录")
    @PreAuthorize("hasAuthority('mdc:parkingCouponRecord:add')")
    @ApiMapping(value = "backend.comvita.parking.couponRecord.add", method = RequestMethod.POST, permission = true)
    public void addCouponRecord(CouponRecordAddParam param) {
        parkingCouponService.addCouponRecord(param);
    }

    @ApiOperation(value = "新增停车券增加库存购买记录", notes = "新增停车券增加库存购买记录")
    @PreAuthorize("hasAuthority('mdc:parkingCouponRecord:add')")
    @ApiMapping(value = "backend.comvita.parking.couponRecordInventory.add", method = RequestMethod.POST, permission = true)
    public void addInventory(CouponRecordInventoryAddParam param) {
        parkingCouponService.addInventory(param);
    }

    @ApiOperation(value = "审核停车券购买记录", notes = "审核停车券购买记录")
    @PreAuthorize("hasAuthority('mdc:parkingCouponRecord:audit')")
    @ApiMapping(value = "backend.comvita.parking.couponRecord.audit", method = RequestMethod.POST, permission = true)
    public void auditCouponRecord(CouponRecordParam param) {
        parkingCouponService.auditCouponRecord(param);
    }

    @ApiOperation(value = "查询停车券记录", notes = "查询停车券记录")
    @PreAuthorize("hasAuthority('mdc:parkingCoupon:list')")
    @ApiMapping(value = "backend.comvita.parking.coupon.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingCouponResult> pageCoupon(CouponPageParam param) {
        return parkingCouponService.pageCoupon(param);
    }

    @ApiOperation(value = "获取停车券详情", notes = "获取停车券详情")
    @PreAuthorize("hasAuthority('mdc:parkingCoupon:get')")
    @ApiMapping(value = "backend.comvita.parking.coupon.get", method = RequestMethod.POST, permission = true)
    public ParkingCouponResult getCoupon(CouponGetParam param) {
        return parkingCouponService.getCoupon(param);
    }

    @ApiOperation(value = "更改停车券状态", notes = "更改停车券状态")
    @PreAuthorize("hasAuthority('mdc:parkingCoupon:edit')")
    @ApiMapping(value = "backend.comvita.parking.coupon.toggle", method = RequestMethod.POST, permission = true)
    public void toggleCoupon(CouponGetParam param) {
        parkingCouponService.toggleCoupon(param);
    }

    @ApiOperation(value = "编辑停车券", notes = "编辑停车券")
    @PreAuthorize("hasAuthority('mdc:parkingCoupon:edit')")
    @ApiMapping(value = "backend.comvita.parking.coupon.edit", method = RequestMethod.POST, permission = true)
    public void editCoupon(CouponEditParam param) {
        parkingCouponService.editCoupon(param);
    }

    @ApiOperation(value = "删除停车券", notes = "删除停车券(状态为停用时可删除)")
    @PreAuthorize("hasAuthority('mdc:parkingCoupon:delete')")
    @ApiMapping(value = "backend.comvita.parking.coupon.delete", method = RequestMethod.POST, permission = true)
    public void deleteCoupon(CouponGetParam param) {
        parkingCouponService.deleteCoupon(param);
    }

    @ApiOperation(value = "派发停车券", notes = "派发停车券")
    @PreAuthorize("hasAuthority('mdc:parkingCouponMemberRel:add')")
    @ApiMapping(value = "backend.comvita.parking.couponMemberRel.add", method = RequestMethod.POST, permission = true)
    public void addCouponMemberRel(CouponMemberRelAddParam param) {
        parkingCouponService.addCouponMemberRel(param);
    }

    @ApiOperation(value = "查询派发停车券记录", notes = "查询派发停车券记录")
    @PreAuthorize("hasAuthority('mdc:parkingCouponMemberRel:list')")
    @ApiMapping(value = "backend.comvita.parking.couponMemberRel.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingCouponMemberRel> pageCouponMemberRel(CouponMemberRelPageParam param) {
        return parkingCouponService.pageCouponMemberRel(param);
    }

    @ApiOperation(value = "获取派发停车券记录概况统计", notes = "获取派发停车券记录概况统计")
    @PreAuthorize("hasAuthority('mdc:parkingCouponMemberRel:list')")
    @ApiMapping(value = "backend.comvita.parking.relStatistics.get", method = RequestMethod.POST, permission = true)
    public RelStatisticsResult getStatistics(StatisticsGetParam param) {
        return parkingCouponService.getStatistics(param);
    }

    @ApiOperation(value = "获取派发停车券记录明细分析统计", notes = "获取派发停车券记录明细分析统计")
    @PreAuthorize("hasAuthority('mdc:parkingCouponMemberRel:list')")
    @ApiMapping(value = "backend.comvita.parking.relDetailAnalysis.page", method = RequestMethod.POST, permission = true)
    public IPage<RelDetailAnalysisResult> pageDetailAnalysis(RelTimePageParam param) {
        return parkingCouponService.pageDetailAnalysis(param);
    }

    @ApiOperation(value = "获取派发停车券记录汇总分析统计", notes = "获取派发停车券记录汇总分析统计")
    @PreAuthorize("hasAuthority('mdc:parkingCouponMemberRel:list')")
    @ApiMapping(value = "backend.comvita.parking.relSummaryAnalysis.page", method = RequestMethod.POST, permission = true)
    public IPage<RelSummaryAnalysisResult> pageSummaryAnalysis(RelTimePageParam param) {
        return parkingCouponService.pageSummaryAnalysis(param);
    }

    @ApiOperation(value = "获取派发停车券记录名称金额分析统计", notes = "获取派发停车券记录名称金额分析统计")
    @PreAuthorize("hasAuthority('mdc:parkingCouponMemberRel:list')")
    @ApiMapping(value = "backend.comvita.parking.relRecordAnalysis.page", method = RequestMethod.POST, permission = true)
    public IPage<RelRecordAnalysisResult> pageRecordAnalysis(RelTimePageParam param) {
        return parkingCouponService.pageRecordAnalysis(param);
    }
}
