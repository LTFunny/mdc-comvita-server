package com.aquilaflycloud.mdc.api;


import com.aquilaflycloud.mdc.param.ticket.ProductInfoGetByIdParam;
import com.aquilaflycloud.mdc.param.ticket.WechatGetProductInfoParam;
import com.aquilaflycloud.mdc.result.ticket.TicketProductInfoDetailResult;
import com.aquilaflycloud.mdc.result.ticket.WechatGetProductInfoResult;
import com.aquilaflycloud.mdc.service.TicketProductInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 产品信息相关接口
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@RestController
@Api(tags = "产品信息相关接口")
public class TicketProductApi {
    @Resource
    private TicketProductInfoService ticketProductInfoService;

    @ApiOperation(value = "微信小程序购票页面获取所有产品信息", notes = "微信小程序购票页面获取所有产品信息")
    @ApiMapping(value = "comvita.ticket.productInfo.get", method = RequestMethod.POST)
    public WechatGetProductInfoResult wechatGetProductInfo(WechatGetProductInfoParam param) {
        return ticketProductInfoService.wechatGetProductInfo(param);
    }

    @ApiOperation(value = "通过id获取产品详情", notes = "通过id获取产品详情")
    @ApiMapping(value = "comvita.ticket.productInfo.getById", method = RequestMethod.POST)
    public TicketProductInfoDetailResult wechatGetProductInfoById(ProductInfoGetByIdParam param) {
        return ticketProductInfoService.wechatGetProductInfoById(param);
    }
}
