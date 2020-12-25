package com.aquilaflycloud.mdc.controller;


import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.*;
import com.aquilaflycloud.mdc.service.TicketOrderInfoService;
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
 * <p>
 * 订单信息控制器
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@RestController
@Api(tags = "订单信息控制器")
public class TicketOrderInfoController {
    @Resource
    private TicketOrderInfoService ticketOrderInfoService;

    @ApiOperation("购票订单分页")
    @PreAuthorize("hasAuthority('mdc:ticket:orderinfo:page')")
    @ApiMapping(value = "backend.comvita.ticket.orderinfo.page", method = RequestMethod.POST, permission = true)
    public IPage<TicketOrderInfo> pageOrderInfo(OrderInfoPageParam param) {
        return ticketOrderInfoService.pageOrderInfo(param);
    }

    @ApiOperation("购票订单详情")
    @PreAuthorize("hasAuthority('mdc:ticket:orderinfo:page')")
    @ApiMapping(value = "backend.comvita.ticket.orderinfo.get", method = RequestMethod.POST, permission = true)
    public TicketOrderInfoResult getOrderInfo(OrderInfoGetParam param) {
        return ticketOrderInfoService.getOrderInfo(param);
    }

    @ApiOperation("查询退款记录列表")
    @PreAuthorize("hasAuthority('mdc:ticket:refund:list')")
    @ApiMapping(value = "backend.comvita.ticket.refund.list", method = RequestMethod.POST, permission = true)
    public IPage<TicketOrderResult> pageRefundOrder(OrderRefundPageParam param) {
        return ticketOrderInfoService.pageRefundOrder(param);
    }

    @ApiOperation("获取退款详情")
    @PreAuthorize("hasAuthority('mdc:ticket:refund:get')")
    @ApiMapping(value = "backend.comvita.ticket.refund.get", method = RequestMethod.POST, permission = true)
    public TicketOrderResult getRefundDetail(OrderRefundGetParam param) {
        return ticketOrderInfoService.getRefundDetail(param);
    }

    @ApiOperation("核销曲线图")
    @PreAuthorize("hasAuthority('mdc:ticket:verificate:list')")
    @ApiMapping(value = "backend.comvita.ticket.verificate.chart", method = RequestMethod.POST, permission = true)
    public List<TicketVerificateChartResult> getUseCntByDate(TicketVerificatePageParam param) {
        return ticketOrderInfoService.getUseCntByDate(param);
    }

    @ApiOperation("获取核销详情")
    @PreAuthorize("hasAuthority('mdc:ticket:verificate:get')")
    @ApiMapping(value = "backend.comvita.ticket.verificate.get", method = RequestMethod.POST, permission = true)
    public TicketVerificateResult getVerificatedTicket(OrderInfoGetParam param) {
        return ticketOrderInfoService.getVerificatedTicket(param);
    }

    @ApiOperation("核销概况")
    @PreAuthorize("hasAuthority('mdc:ticket:verificate:list')")
    @ApiMapping(value = "backend.comvita.ticket.verificate.statistics", method = RequestMethod.POST, permission = true)
    public TicketStatisticResult verificateStatistic(TicketVerificatePageParam param) {
        return ticketOrderInfoService.verificateStatistic(param);
    }

    @ApiOperation("核销记录列表")
    @PreAuthorize("hasAuthority('mdc:ticket:verificate:list')")
    @ApiMapping(value = "backend.comvita.ticket.verificate.page", method = RequestMethod.POST, permission = true)
    public IPage<TicketVerificateOrderResult> pageVerificatedOrder(OrderVerificatePageParam param) {
        return ticketOrderInfoService.pageVerificatedOrder(param);
    }
}
