package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoGetResult;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 10:40
 * @Version 1.0
 */
@RestController
@Api(tags = "订单接口")
public class PreOrderInfoApi {

    @Resource
    private PreOrderInfoService orderInfoService;

    @ApiOperation(value = "生成待确认订单", notes = "生成待确认订单")
    @ApiMapping(value = "comvita.order.info.statconfirm.add", method = RequestMethod.POST)
    public BaseResult addStatConfirmOrder(PreStayConfirmOrderParam param) {
        return new BaseResult<Integer>().setResult(orderInfoService.addStatConfirmOrder(param));
    }

    @ApiOperation(value = "订单确认列表", notes = "订单确认列表")
    @ApiMapping(value = "comvita.order.info.confirm.page", method = RequestMethod.POST)
    public IPage<PreOrderInfo> pagePreOrderInfo(PreOrderInfoPageParam param) {
        return orderInfoService.pagePreOrderInfo(param);
    }

    @ApiOperation(value = "订单确认详情", notes = "订单确认详情")
    @ApiMapping(value = "comvita.order.info.confirm.get", method = RequestMethod.POST)
    public PreOrderInfoGetResult getConfirmOrderInfo(PreOrderInfoGetParam param) {
        return orderInfoService.getConfirmOrderInfo(param);
    }

    @ApiOperation(value = "对订单进行确认", notes = "对订单进行确认")
    @ApiMapping(value = "comvita.order.info.confirm.validation", method = RequestMethod.POST)
    public void validationConfirmOrder(PreConfirmOrderParam param) {
         orderInfoService.validationConfirmOrder(param);
    }

    @ApiOperation(value = "预约自提", notes = "预约自提")
    @ApiMapping(value = "comvita.order.goods.reservation", method = RequestMethod.POST)
    public void reservationOrderGoods(PreReservationOrderGoodsParam param) {
        orderInfoService.reservationOrderGoods(param);
    }


}
