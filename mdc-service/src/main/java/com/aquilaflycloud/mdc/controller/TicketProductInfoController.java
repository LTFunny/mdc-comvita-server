package com.aquilaflycloud.mdc.controller;


import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.ProductInfoResult;
import com.aquilaflycloud.mdc.result.ticket.ProductInfoSaleOrderSumResult;
import com.aquilaflycloud.mdc.result.ticket.TicketProductInfoDetailByIdResult;
import com.aquilaflycloud.mdc.result.ticket.TicketProductSaleResult;
import com.aquilaflycloud.mdc.service.TicketProductInfoNormalService;
import com.aquilaflycloud.mdc.service.TicketProductInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 产品信息控制器
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@RestController
@Api(tags = "产品信息控制器")
public class TicketProductInfoController {
    @Resource
    private TicketProductInfoService ticketProductInfoService;

    @ApiOperation("同步产品信息")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:pull')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.pull", method = RequestMethod.POST, permission = true)
    public void pullProductInfo() {
        ticketProductInfoService.getInterfaceProductInfo();
    }

    @ApiOperation("任务同步产品信息")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.normalPull", method = RequestMethod.POST)
    public void pullNormalProductInfo() {
        ticketProductInfoService.getNormalInterfaceProductInfo();
    }

    @ApiOperation("获取产品信息列表")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:page')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.page", method = RequestMethod.POST, permission = true)
    public IPage<ProductInfoResult> listProductInfo(ProductInfoListParam param) {
        return ticketProductInfoService.listProductInfo(param);
    }

    @ApiOperation("最新活动获取产品信息列表")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:page')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.activityList", method = RequestMethod.POST)
    public IPage<ProductInfoResult> listActivityProductInfo(ProductInfoActivityListParam param) {
        return ticketProductInfoService.listActivityProductInfo(param);
    }

    @ApiOperation("更新产品信息中是否推荐")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:save')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.updateRecommend", method = RequestMethod.POST, permission = true)
    public void updateProductInfoRecommend(ProductInfoUpdateRecommendParam param) {
        ticketProductInfoService.updateProductInfoRecommend(param);
    }

    @ApiOperation("更新产品信息中是否置顶")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:save')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.updateTop", method = RequestMethod.POST, permission = true)
    public void updateProductInfoTop(ProductInfoUpdateTopParam param) {
        ticketProductInfoService.updateProductInfoTop(param);
    }

    @ApiOperation("更新产品信息中指定游玩日期")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:save')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.updatePlayTime", method = RequestMethod.POST, permission = true)
    public void updateProductInfoPlayTime(ProductInfoUpdatePlayTimeParam param) {
        ticketProductInfoService.updateProductInfoPlayTime(param);
    }

    @ApiOperation("更新产品信息是否联票")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:save')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.updateComposite", method = RequestMethod.POST, permission = true)
    public void updateProductInfoComposite(ProductInfoUpdateCompositeParam param) {
        ticketProductInfoService.updateProductInfoComposite(param);
    }

    @ApiOperation("更新产品是否上架")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:save')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.updateUpper", method = RequestMethod.POST, permission = true)
    public void updateProductInfoUpper(ProductInfoUpdateUpperParam param) {
        ticketProductInfoService.updateProductInfoUpper(param);
    }

    @ApiOperation("更新产品信息购买须知")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:save')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.updateIntroduce", method = RequestMethod.POST, permission = true)
    public void updateProductInfoBuyIntroduce(ProductInfoUpdateBuyIntroduceParam param) {
        ticketProductInfoService.updateProductInfoBuyIntroduce(param);
    }

    @ApiOperation("根据id获取产品详情")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:get')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.get", method = RequestMethod.POST, permission = true)
    public TicketProductInfoDetailByIdResult getProductInfo(ProductInfoGetParam param) {
        return ticketProductInfoService.getProductInfo(param);
    }

    /*@ApiOperation("定时器获取产品信息")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.getproduct", method = RequestMethod.POST)
    public void getproduct() {
        ticketProductInfoNormalService.getInterfaceProductInfo();
    }*/

    //产品售卖分析
    @ApiOperation("产品售卖分析-统计折线数据")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:sales')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.sales", method = RequestMethod.POST, permission = true)
    public TicketProductSaleResult salesProductInfo(ProductInfoSalesParam param) {
        return ticketProductInfoService.salesProductInfo(param);
    }

    @ApiOperation("产品售卖分析-订单统计详情")
    @PreAuthorize("hasAuthority('mdc:ticket:productInfo:sales')")
    @ApiMapping(value = "backend.mdc.ticket.productInfo.salesOrder", method = RequestMethod.POST, permission = true)
    public IPage<ProductInfoSaleOrderSumResult> salesProductOrderInfo(ProductInfoSalesOrderParam param) {
        return ticketProductInfoService.salesProductOrderInfo(param);
    }

}
