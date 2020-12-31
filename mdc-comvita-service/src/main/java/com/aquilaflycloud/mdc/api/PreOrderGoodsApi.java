package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.param.pre.PreOrderGoodsPageParam;
import com.aquilaflycloud.mdc.param.pre.PreReservationOrderGoodsParam;
import com.aquilaflycloud.mdc.service.PreOrderGoodsService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 14:34
 * @Version 1.0
 */
@RestController
@Api(tags = "预约接口")
public class PreOrderGoodsApi {

    @Resource
    private PreOrderGoodsService orderGoodsService;

    @ApiOperation(value = "预约自提列表", notes = "预约自提列表")
    @ApiMapping(value = "comvita.order.goods.page", method = RequestMethod.POST)
    public void pagePreOrderGoods(PreOrderGoodsPageParam param) {
        orderGoodsService.pagePreOrderGoods(param);
    }

    @ApiOperation(value = "预约自提", notes = "预约自提")
    @ApiMapping(value = "comvita.order.goods.reservation", method = RequestMethod.POST)
    public void reservationOrderGoods(PreReservationOrderGoodsParam param) {
        orderGoodsService.reservationOrderGoods(param);
    }


}
