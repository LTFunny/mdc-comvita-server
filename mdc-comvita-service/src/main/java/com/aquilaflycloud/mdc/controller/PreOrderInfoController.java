package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.pre.PreConfirmOrderParam;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 14:42
 * @Version 1.0
 */
@RestController
@Api(tags = "订单接口")
public class PreOrderInfoController {

    @Resource
    private PreOrderInfoService orderInfoService;

    @ApiOperation(value = "对订单进行确认", notes = "对订单进行确认")
    @ApiMapping(value = "backend.comvita.order.info.confirm.validation", method = RequestMethod.POST)
    public void validationConfirmOrder(PreConfirmOrderParam param) {
        orderInfoService.validationConfirmOrder(param);
    }


}
