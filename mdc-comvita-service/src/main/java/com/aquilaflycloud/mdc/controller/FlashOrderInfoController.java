package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PreFlashOrderInfo;
import com.aquilaflycloud.mdc.param.pre.FlashPageParam;
import com.aquilaflycloud.mdc.service.FlashOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
@Api(tags = "快闪活动后端")
public class FlashOrderInfoController {

    @Resource
    private FlashOrderService flashOrderService;

  /* @ApiOperation(value = "活动概况", notes = "活动概况")
    @ApiMapping(value = "backend.comvita.order.info.flash.survey", method = RequestMethod.POST, permission = true)
    public void getFlashOrderInfoSurvey(FlashConfirmOrderParam param) {
        flashOrderService.getFlashOrderInfo(param);
    }*/
    @ApiOperation(value = "领取明细", notes = "领取明细")
    @ApiMapping(value = "backend.comvita.order.info.flash.detailed", method = RequestMethod.POST, permission = true)
    public IPage<PreFlashOrderInfo> getFlashOrderInfoDetailed(FlashPageParam param) {
       return flashOrderService.pageFlashOrderInfo(param);
    }
}
