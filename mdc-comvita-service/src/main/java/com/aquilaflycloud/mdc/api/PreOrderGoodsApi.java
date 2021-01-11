package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.param.pre.PreOrderGoodsPageParam;
import com.aquilaflycloud.mdc.param.pre.PreReservationOrderGoodsParam;
import com.aquilaflycloud.mdc.service.PreOrderGoodsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    public IPage<PreOrderGoods> pagePreOrderGoods(PreOrderGoodsPageParam param) {
        return orderGoodsService.pagePreOrderGoods(param);
    }

    @ApiOperation(value = "预约自提详情", notes = "预约自提详情")
    @ApiMapping(value = "comvita.order.goods.get", method = RequestMethod.POST)
    public PreOrderGoods getPreOrderGoods(PreReservationOrderGoodsParam param) {
        return orderGoodsService.getPreOrderGoods(param);
    }

    @ApiOperation(value = "预约自提", notes = "预约自提")
    @ApiMapping(value = "comvita.order.goods.reservation", method = RequestMethod.POST)
    public void reservationOrderGoods(PreReservationOrderGoodsParam param) {
        orderGoodsService.reservationOrderGoods(param);
    }




}
