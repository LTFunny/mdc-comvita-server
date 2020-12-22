package com.aquilaflycloud.mdc.controller;

import cn.hutool.json.JSONObject;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.parking.*;
import com.aquilaflycloud.mdc.param.parking.*;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderChargeResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderResult;
import com.aquilaflycloud.mdc.service.ParkingOrderService;
import com.aquilaflycloud.mdc.service.ParkingService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.ajbcloud.util.AjbCloudUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * ParkingController
 *
 * @author star
 * @date 2020-01-08
 */
@RestController
@Api(tags = "停车系统管理")
public class ParkingController {
    @Resource
    private ParkingOrderService parkingOrderService;
    @Resource
    private ParkingService parkingService;

    @ApiOperation(value = "安居宝接口调用", notes = "安居宝接口调用")
    @PreAuthorize("hasAuthority('mdc:parking:manage')")
    @ApiMapping(value = "backend.mdc.parking.ajbCloud.call", method = RequestMethod.POST, permission = true)
    public BaseResult<JSONObject> callAjbCloud(@RequestBody JSONObject param) {
        return new BaseResult<JSONObject>().setResult(AjbCloudUtil.Common.sendRequest(
                MdcUtil.getCurrentTenantId().toString(), param.getJSONObject("param")));
    }

    @ApiOperation(value = "安居宝收费记录推送列表", notes = "安居宝收费记录推送列表")
    @PreAuthorize("hasAuthority('mdc:parking:manage')")
    @ApiMapping(value = "backend.mdc.parking.ajbCharge.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingAjbChargeRecord> pageCharge(PageParam param) {
        return parkingService.pageCharge(param);
    }

    @ApiOperation(value = "安居宝进离记录推送列表", notes = "安居宝进离记录推送列表")
    @PreAuthorize("hasAuthority('mdc:parking:manage')")
    @ApiMapping(value = "backend.mdc.parking.ajbEnter.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingAjbEnterRecord> pageEnter(PageParam param) {
        return parkingService.pageEnter(param);
    }

    @ApiOperation(value = "安居宝锁车记录推送列表", notes = "安居宝锁车记录推送列表")
    @PreAuthorize("hasAuthority('mdc:parking:manage')")
    @ApiMapping(value = "backend.mdc.parking.ajbLock.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingAjbLockRecord> pageLock(PageParam param) {
        return parkingService.pageLock(param);
    }

    @ApiOperation(value = "安居宝锁车异常推送列表", notes = "安居宝锁车异常推送列表")
    @PreAuthorize("hasAuthority('mdc:parking:manage')")
    @ApiMapping(value = "backend.mdc.parking.ajbLockErr.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingAjbLockErrRecord> pageLockErr(PageParam param) {
        return parkingService.pageLockErr(param);
    }

    @ApiOperation(value = "安居宝优惠核销推送列表", notes = "安居宝优惠核销推送列表")
    @PreAuthorize("hasAuthority('mdc:parking:manage')")
    @ApiMapping(value = "backend.mdc.parking.ajbCoupon.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingAjbCouponRecord> pageCoupon(PageParam param) {
        return parkingService.pageCoupon(param);
    }

    @ApiOperation(value = "安居宝接口错误记录列表", notes = "安居宝接口错误记录列表")
    @PreAuthorize("hasAuthority('mdc:parking:manage')")
    @ApiMapping(value = "backend.mdc.parking.ajbInterface.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingAjbInterfaceRecord> pageInterface(PageParam param) {
        return parkingService.pageInterface(param);
    }

    @ApiOperation(value = "车牌号模糊查询", notes = "车牌号模糊查询")
    @PreAuthorize("hasAuthority('mdc:parkingCar:list')")
    @ApiMapping(value = "backend.mdc.parking.carNo.page", method = RequestMethod.POST, permission = true)
    public IPage<String> pageCarNo(CarNoPageParam param) {
        return parkingOrderService.pageCarNo(param);
    }

    @ApiOperation(value = "查询无牌车记录列表(分页)", notes = "查询无牌车记录列表(分页)")
    @PreAuthorize("hasAuthority('mdc:parkingCar:list')")
    @ApiMapping(value = "backend.mdc.parking.unlicensedCar.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingUnlicensedCarRecord> pageUnlicensedCar(ParkingUnlicensedPageParam param) {
        return parkingOrderService.pageUnlicensedCar(param);
    }

    @ApiOperation(value = "获取停车计费信息", notes = "获取停车计费信息")
    @PreAuthorize("hasAuthority('mdc:parkingOrder:list')")
    @ApiMapping(value = "backend.mdc.parking.orderCharge.get", method = RequestMethod.POST, permission = true)
    public ParkingOrderChargeResult getParkingOrderCharge(ParkingOrderChargeGetParam param) {
        return parkingOrderService.getParkingOrderCharge(param);
    }

    @ApiOperation(value = "查询停车订单列表(分页)", notes = "查询停车订单列表(分页)")
    @PreAuthorize("hasAuthority('mdc:parkingOrder:list')")
    @ApiMapping(value = "backend.mdc.parking.order.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingOrder> pageParkingOrder(ParkingOrderPageParam param) {
        return parkingOrderService.pageParkingOrder(param);
    }

    @ApiOperation(value = "获取停车订单详情", notes = "获取停车订单详情与支付记录")
    @PreAuthorize("hasAuthority('mdc:parkingOrder:get')")
    @ApiMapping(value = "backend.mdc.parking.order.get", method = RequestMethod.POST, permission = true)
    public ParkingOrderResult getParkingOrder(OrderGetParam param) {
        return parkingOrderService.getParkingOrder(param);
    }

    @ApiOperation(value = "发起订单退款", notes = "发起订单退款")
    @PreAuthorize("hasAuthority('mdc:parkingOrder:refund')")
    @ApiMapping(value = "backend.mdc.parking.order.refund", method = RequestMethod.POST, permission = true)
    public void refundParkingOrder(ParkingOrderRefundParam param) {
        parkingOrderService.refundParkingOrder(param);
    }

    @ApiOperation(value = "查询停车发票列表(分页)", notes = "查询停车发票列表(分页)")
    @PreAuthorize("hasAuthority('mdc:parkingOrder:list')")
    @ApiMapping(value = "backend.mdc.parking.invoice.page", method = RequestMethod.POST, permission = true)
    public IPage<ParkingOrderInvoice> pageParkingInvoice(ParkingInvoicePageParam param) {
        return parkingOrderService.pageParkingInvoice(param);
    }
}
