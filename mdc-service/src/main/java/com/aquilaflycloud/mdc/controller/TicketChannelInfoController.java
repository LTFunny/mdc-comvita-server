package com.aquilaflycloud.mdc.controller;


import com.aquilaflycloud.mdc.model.ticket.TicketChannelInfo;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.TicketChannelInfoByIdResult;
import com.aquilaflycloud.mdc.result.ticket.TicketChannelSaleResult;
import com.aquilaflycloud.mdc.result.ticket.TicketOrderInfoSalesResult;
import com.aquilaflycloud.mdc.service.TicketChannelInfoService;
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
 * 渠道信息控制器
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@RestController
@Api(tags = "渠道信息控制器")
public class TicketChannelInfoController {
    @Resource
    private TicketChannelInfoService ticketChannelInfoService;

    @ApiOperation("获取渠道列表")
    @PreAuthorize("hasAuthority('mdc:ticket:channelinfo:list')")
    @ApiMapping(value = "backend.mdc.ticket.channelinfo.page", method = RequestMethod.POST, permission = true)
    public IPage<TicketChannelInfo> pageChannelInfo(ChannelInfoPageParam param) {
        return ticketChannelInfoService.pageChannelInfo(param);
    }

    @ApiOperation("获取渠道列表-不分页")
    @PreAuthorize("hasAuthority('mdc:ticket:channelinfo:list')")
    @ApiMapping(value = "backend.mdc.ticket.channelinfo.list", method = RequestMethod.POST, permission = true)
    public List<TicketChannelInfo> listChannelInfo(ChannelInfoListParam param) {
        return ticketChannelInfoService.listChannelInfo(param);
    }

    @ApiOperation("添加渠道信息")
    @PreAuthorize("hasAuthority('mdc:ticket:channelinfo:add')")
    @ApiMapping(value = "backend.mdc.ticket.channelinfo.add", method = RequestMethod.POST, permission = true)
    public TicketChannelInfo addChannelInfo(ChannelInfoAddParam param) {
        return ticketChannelInfoService.addChannelInfo(param);
    }

    @ApiOperation("更新状态")
    @PreAuthorize("hasAuthority('mdc:ticket:channelinfo:edit')")
    @ApiMapping(value = "backend.mdc.ticket.channelinfo.editState", method = RequestMethod.POST, permission = true)
    public void updateChannelInfo(ChannelInfoUpdateParam param) {
        ticketChannelInfoService.updateChannelInfo(param);
    }

    @ApiOperation("获取渠道详情")
    @PreAuthorize("hasAuthority('mdc:ticket:channelinfo:get')")
    @ApiMapping(value = "backend.mdc.ticket.channelinfo.get", method = RequestMethod.POST, permission = true)
    public TicketChannelInfoByIdResult getChannelInfo(ChannelInfoGetParam param) {
        return ticketChannelInfoService.getChannelInfo(param);
    }

    @ApiOperation("渠道销售概况")
    @PreAuthorize("hasAuthority('mdc:ticket:channelinfo:sales')")
    @ApiMapping(value = "backend.mdc.ticket.channelinfo.sales", method = RequestMethod.POST, permission = true)
    public TicketChannelSaleResult channelSales(ChannelSalesParam param) {
        return ticketChannelInfoService.channelSales(param);
    }

    @ApiOperation("渠道销售概况-订单详情")
    @PreAuthorize("hasAuthority('mdc:ticket:channelinfo:sales')")
    @ApiMapping(value = "backend.mdc.ticket.channelinfo.salesOrder", method = RequestMethod.POST, permission = true)
    public IPage<TicketOrderInfoSalesResult> channelSalesOrderInfo(ChannelSalesOrderInfoParam param) {
        return ticketChannelInfoService.channelSalesOrderInfo(param);
    }
}
