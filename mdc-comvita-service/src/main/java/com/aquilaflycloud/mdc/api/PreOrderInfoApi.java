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

}
