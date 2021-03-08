package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.pre.FlashConfirmOrderParam;
import com.aquilaflycloud.mdc.param.pre.FlashWriteOffOrderParam;
import com.aquilaflycloud.mdc.service.FlashOrderService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author zouliyong
 */
@RestController
@Api(tags = "快闪活动订单接口")
public class FlashOrderInfoApi {

    @Resource
    private FlashOrderService flashOrderService;

    @ApiOperation(value = "下订单", notes = "下订单")
    @ApiMapping(value = "backend.comvita.order.info.flash.order", method = RequestMethod.POST, permission = true)
    public String registgetFlashOrderInfoer(FlashConfirmOrderParam param) {
        return flashOrderService.getFlashOrderInfo(param);
    }

}
