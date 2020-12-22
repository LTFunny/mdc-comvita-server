package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.parking.ParkingCouponMemberRel;
import com.aquilaflycloud.mdc.model.parking.ParkingMemberCar;
import com.aquilaflycloud.mdc.model.parking.ParkingOrder;
import com.aquilaflycloud.mdc.model.parking.ParkingOrderInvoice;
import com.aquilaflycloud.mdc.param.parking.*;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderApiResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderChargeResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderPayResult;
import com.aquilaflycloud.mdc.result.parking.ParkingOrderResult;
import com.aquilaflycloud.mdc.service.ParkingCouponService;
import com.aquilaflycloud.mdc.service.ParkingMemberCarService;
import com.aquilaflycloud.mdc.service.ParkingOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ParkingApi
 *
 * @author star
 * @date 2020-02-01
 */
@RestController
@Api(tags = "停车相关接口")
public class ParkingApi {
    @Resource
    private ParkingOrderService parkingOrderService;
    @Resource
    private ParkingCouponService parkingCouponService;
    @Resource
    private ParkingMemberCarService parkingMemberCarService;

    @ApiOperation(value = "无牌车入场", notes = "无牌车入场")
    @ApiMapping(value = "mdc.parking.unlicensedCar.in", method = RequestMethod.POST)
    public void scanIn(UnlicensedCarParam param) {
        parkingOrderService.scanIn(param);
    }

    @ApiOperation(value = "获取停车计费信息", notes = "获取停车计费信息")
    @ApiMapping(value = "mdc.parking.orderCharge.get", method = RequestMethod.POST)
    public ParkingOrderChargeResult getOrderCharge(OrderChargeGetParam param) {
        return parkingOrderService.getOrderCharge(param);
    }

    @ApiOperation(value = "批量获取停车计费信息", notes = "批量获取会员绑定的车牌(包括无牌车)停车计费信息,没有优惠券信息")
    @ApiMapping(value = "mdc.parking.orderCharge.batchGet", method = RequestMethod.POST)
    public List<ParkingOrderChargeResult> batchGetOrderCharge(OrderChargeBatchGetParam param) {
        return parkingOrderService.batchGetOrderCharge(param);
    }

    @ApiOperation(value = "新增停车缴费订单", notes = "新增停车缴费订单,并返回支付信息")
    @ApiMapping(value = "mdc.parking.order.add", method = RequestMethod.POST)
    public ParkingOrderPayResult addOrder(OrderAddParam param) {
        return parkingOrderService.addOrder(param);
    }

    @ApiOperation(value = "查询缴费订单(分页)", notes = "查询缴费订单(分页)")
    @ApiMapping(value = "mdc.parking.order.page", method = RequestMethod.POST)
    public IPage<ParkingOrderApiResult> pageOrder(PageParam<ParkingOrder> param) {
        return parkingOrderService.pageOrder(param);
    }

    @ApiOperation(value = "获取缴费订单", notes = "获取缴费订单")
    @ApiMapping(value = "mdc.parking.order.get", method = RequestMethod.POST)
    public ParkingOrderResult getOrder(OrderGetParam param) {
        return parkingOrderService.getOrder(param);
    }

    @ApiOperation(value = "退款失败订单重新退款", notes = "退款失败订单重新发起退款")
    @ApiMapping(value = "mdc.parking.order.refund", method = RequestMethod.POST)
    public void refundOrder(OrderGetParam param) {
        parkingOrderService.refundOrder(param);
    }

    @ApiOperation(value = "新增电子发票", notes = "新增电子发票")
    @ApiMapping(value = "mdc.parking.invoice.add", method = RequestMethod.POST)
    public void addInvoice(InvoiceAddParam param) {
        parkingOrderService.addInvoice(param);
    }

    @ApiOperation(value = "查询电子发票记录", notes = "查询电子发票记录")
    @ApiMapping(value = "mdc.parking.invoice.page", method = RequestMethod.POST)
    public IPage<ParkingOrderInvoice> pageInvoice(InvoicePageParam param) {
        return parkingOrderService.pageInvoice(param);
    }

    @ApiOperation(value = "获取电子发票详情", notes = "获取电子发票详情")
    @ApiMapping(value = "mdc.parking.invoice.get", method = RequestMethod.POST)
    public ParkingOrderInvoice getInvoice(InvoiceGetParam param) {
        return parkingOrderService.getInvoice(param);
    }

    @ApiOperation(value = "查询停车券(分页)", notes = "查询停车券(分页)")
    @ApiMapping(value = "mdc.parking.coupon.page", method = RequestMethod.POST)
    public IPage<ParkingCouponMemberRel> pageCouponRel(CouponRelPageParam param) {
        return parkingCouponService.pageCouponRel(param);
    }

    @ApiOperation(value = "获取停车券", notes = "获取停车券")
    @ApiMapping(value = "mdc.parking.coupon.get", method = RequestMethod.POST)
    public ParkingCouponMemberRel getCouponRel(CouponRelGetParam param) {
        return parkingCouponService.getCouponRel(param);
    }

    @ApiOperation(value = "判断停车券是否可用", notes = "判断停车券是否可用")
    @ApiMapping(value = "mdc.parking.coupon.judge", method = RequestMethod.POST)
    public void judgeCouponRel(CouponRelJudgeParam param) {
        parkingCouponService.judgeCouponRel(param);
    }

    @ApiOperation(value = "使用停车券", notes = "使用停车券")
    @ApiMapping(value = "mdc.parking.coupon.use", method = RequestMethod.POST)
    public void useCouponRel(CouponRelUseParam param) {
        parkingCouponService.useCouponRel(param);
    }

    @ApiOperation(value = "保存车牌号", notes = "保存车牌号")
    @ApiMapping(value = "mdc.parking.car.save", method = RequestMethod.POST)
    public void saveCar(MemberCarParam param) {
        parkingMemberCarService.saveCar(param);
    }

    @ApiOperation(value = "删除车牌号", notes = "删除车牌号")
    @ApiMapping(value = "mdc.parking.car.delete", method = RequestMethod.POST)
    public void deleteCar(MemberCarParam param) {
        parkingMemberCarService.deleteCar(param);
    }

    @ApiOperation(value = "获取车牌号列表", notes = "获取车牌号列表")
    @ApiMapping(value = "mdc.parking.car.list", method = RequestMethod.POST)
    public List<ParkingMemberCar> listCar() {
        return parkingMemberCarService.listCar();
    }
}
