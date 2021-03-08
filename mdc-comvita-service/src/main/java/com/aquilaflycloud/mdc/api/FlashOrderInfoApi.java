package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.param.pre.FlashConfirmOrderParam;
import com.aquilaflycloud.mdc.param.pre.MemberFlashPageParam;
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
@Api(tags = "快闪活动订单接口")
public class FlashOrderInfoApi {

    @Resource
    private FlashOrderService flashOrderService;

    @ApiOperation(value = "下订单", notes = "下订单")
    @ApiMapping(value = "backend.comvita.order.info.flash.order", method = RequestMethod.POST, permission = true)
    public String registgetFlashOrderInfoer(FlashConfirmOrderParam param) {
        return flashOrderService.getFlashOrderInfo(param);
    }

    @ApiOperation(value = "获取我参与的活动列表(分页)", notes = "获取我参与的活动列表(分页)")
    @ApiMapping(value = "backend.comvita.order.memberFlash.page", method = RequestMethod.POST, permission = true)
    public IPage<PreActivityInfo> pageMemberFlash(MemberFlashPageParam param) {
        return flashOrderService.pageMemberFlash(param);
    }

}
