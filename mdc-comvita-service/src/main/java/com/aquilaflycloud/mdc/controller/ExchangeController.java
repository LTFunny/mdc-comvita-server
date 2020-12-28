package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.exchange.ExchangeOrder;
import com.aquilaflycloud.mdc.param.exchange.*;
import com.aquilaflycloud.mdc.result.exchange.*;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ExchangeController
 *
 * @author star
 * @date 2020-03-15
 */
@RestController
@Api(tags = "兑换商城管理")
public class ExchangeController {

    @Resource
    private ExchangeService exchangeService;

    @ApiOperation(value = "兑换商品列表查询(分页)", notes = "兑换商品列表查询(分页)")
    @PreAuthorize("hasAuthority('mdc:exchangeGoods:list')")
    @ApiMapping(value = "backend.comvita.exchange.goods.page", method = RequestMethod.POST, permission = true)
    public IPage<GoodsResult> pageGoods(GoodsPageParam param) {
        return exchangeService.pageGoods(param);
    }

    @ApiOperation(value = "新增兑换商品", notes = "新增兑换商品")
    @PreAuthorize("hasAuthority('mdc:exchangeGoods:add')")
    @ApiMapping(value = "backend.comvita.exchange.goods.add", method = RequestMethod.POST, permission = true)
    public void addGoods(GoodsAddParam param) {
        exchangeService.addGoods(param);
    }

    @ApiOperation(value = "修改兑换商品", notes = "修改兑换商品")
    @PreAuthorize("hasAuthority('mdc:exchangeGoods:edit')")
    @ApiMapping(value = "backend.comvita.exchange.goods.edit", method = RequestMethod.POST, permission = true)
    public void editGoods(GoodsEditParam param) {
        exchangeService.editGoods(param);
    }

    @ApiOperation(value = "上架/下架兑换商品", notes = "上架/下架兑换商品")
    @PreAuthorize("hasAuthority('mdc:exchangeGoods:edit')")
    @ApiMapping(value = "backend.comvita.exchange.goods.toggle", method = RequestMethod.POST, permission = true)
    public void toggleGoods(GoodsGetParam param) {
        exchangeService.toggleGoods(param);
    }

    @ApiOperation(value = "审核物流商品", notes = "审核物流商品")
    @PreAuthorize("hasAuthority('mdc:exchangeGoods:audit')")
    @ApiMapping(value = "backend.comvita.exchange.goods.audit", method = RequestMethod.POST, permission = true)
    public void auditGoods(GoodsAuditParam param) {
        exchangeService.auditGoods(param);
    }

    @ApiOperation(value = "获取兑换商品", notes = "获取兑换商品")
    @PreAuthorize("hasAuthority('mdc:exchangeGoods:get')")
    @ApiMapping(value = "backend.comvita.exchange.goods.get", method = RequestMethod.POST, permission = true)
    public GoodsResult getGoods(GoodsGetParam param) {
        return exchangeService.getGoods(param);
    }

    @ApiOperation(value = "获取兑换商品销售排名", notes = "获取兑换商品销售排名")
    @PreAuthorize("hasAuthority('mdc:exchangeGoods:list')")
    @ApiMapping(value = "backend.comvita.exchange.goodsRank.get", method = RequestMethod.POST, permission = true)
    public GoodsRankResult getGoodsRank(GoodsRankGetParam param) {
        return exchangeService.getGoodsRank(param);
    }

    @ApiOperation(value = "获取兑换订单概况统计", notes = "获取兑换订单概况统计")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:list')")
    @ApiMapping(value = "backend.comvita.exchange.orderStatistics.get", method = RequestMethod.POST, permission = true)
    public OrderStatisticsResult getOrderStatistics(OrderStatisticsGetParam param) {
        return exchangeService.getOrderStatistics(param);
    }

    @ApiOperation(value = "获取兑换订单分析统计", notes = "获取兑换订单分析统计")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:list')")
    @ApiMapping(value = "backend.comvita.exchange.orderAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<OrderAnalysisResult> getOrderAnalysis(OrderAnalysisGetParam param) {
        return exchangeService.getOrderAnalysis(param);
    }

    @ApiOperation(value = "兑换订单列表查询(分页)", notes = "兑换订单列表查询(分页)")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:list')")
    @ApiMapping(value = "backend.comvita.exchange.order.page", method = RequestMethod.POST, permission = true)
    public IPage<ExchangeOrder> pageOrder(OrderPageParam param) {
        return exchangeService.pageOrder(param);
    }

    @ApiOperation(value = "获取兑换订单", notes = "获取兑换订单")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:get')")
    @ApiMapping(value = "backend.comvita.exchange.order.get", method = RequestMethod.POST, permission = true)
    public OrderResult pageOrder(OrderGetParam param) {
        return exchangeService.getOrder(param);
    }

    @ApiOperation(value = "修改订单收货信息", notes = "修改订单收货信息")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:edit')")
    @ApiMapping(value = "backend.comvita.exchange.orderReceive.edit", method = RequestMethod.POST, permission = true)
    public void editOrderReceive(OrderReceiveEditParam param) {
        exchangeService.editOrderReceive(param);
    }

    @ApiOperation(value = "修改订单信息", notes = "修改订单信息(暂只支持修改退款类型)")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:edit')")
    @ApiMapping(value = "backend.comvita.exchange.order.edit", method = RequestMethod.POST, permission = true)
    public void editOrder(OrderEditParam param) {
        exchangeService.editOrder(param);
    }

    @ApiOperation(value = "完成订单信息", notes = "完成订单信息,把支付失败的订单查询支付记录完成订单")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:finish')")
    @ApiMapping(value = "backend.comvita.exchange.order.finish", method = RequestMethod.POST, permission = true)
    public void finishExchangeOrder(OrderGetParam param) {
        exchangeService.finishExchangeOrder(param);
    }

    @ApiOperation(value = "订单确认退款", notes = "订单确认退款(交易失败或退款失败时可用)")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:refund')")
    @ApiMapping(value = "backend.comvita.exchange.order.refund", method = RequestMethod.POST, permission = true)
    public void refundOrder(OrderGetParam param) {
        exchangeService.refundOrder(param);
    }

    @ApiOperation(value = "订单确认", notes = "订单确认")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:edit')")
    @ApiMapping(value = "backend.comvita.exchange.order.confirm", method = RequestMethod.POST, permission = true)
    public void confirmOrder(OrderGetParam param) {
        exchangeService.confirmOrder(param);
    }

    @ApiOperation(value = "订单发货", notes = "订单发货")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:edit')")
    @ApiMapping(value = "backend.comvita.exchange.order.deliver", method = RequestMethod.POST, permission = true)
    public void deliverOrder(OrderDeliverParam param) {
        exchangeService.deliverOrder(param);
    }

    @ApiOperation(value = "订单退款审核", notes = "订单退款审核")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:refund')")
    @ApiMapping(value = "backend.comvita.exchange.orderRefund.audit", method = RequestMethod.POST, permission = true)
    public void auditOrderRefund(OrderRefundAuditParam param) {
        exchangeService.auditOrderRefund(param);
    }

    @ApiOperation(value = "订单强制退款", notes = "订单强制退款(忽略订单状态和商品状态,强制退款)")
    @PreAuthorize("hasAuthority('mdc:exchangeOrder:enforceRefund')")
    @ApiMapping(value = "backend.comvita.exchange.order.enforceRefund", method = RequestMethod.POST, permission = true)
    public void enforceRefundOrder(OrderEnforceRefundParam param) {
        exchangeService.enforceRefundOrder(param);
    }
}
