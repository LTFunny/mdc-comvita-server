package com.aquilaflycloud.mdc.api;


import com.aquilaflycloud.mdc.param.ticket.ChannelInfoGetParam;
import com.aquilaflycloud.mdc.param.ticket.ChannelInfoRefIdParam;
import com.aquilaflycloud.mdc.result.ticket.ChannelInfoRefIdResult;
import com.aquilaflycloud.mdc.result.ticket.TicketChannelInfoByIdResult;
import com.aquilaflycloud.mdc.service.TicketChannelInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 渠道信息相关接口
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@RestController
@Api(tags = "渠道信息相关接口")
public class TicketChannelInfoApi {
    @Resource
    private TicketChannelInfoService ticketChannelInfoService;

    @ApiOperation(value = "获取渠道信息", notes = "获取渠道信息")
    @ApiMapping(value = "comvita.ticket.channelInfo.getChannelInfo", method = RequestMethod.POST)
    public TicketChannelInfoByIdResult getChannelInfo(ChannelInfoGetParam param) {
        return ticketChannelInfoService.getChannelInfo(param);
    }

    @ApiOperation(value = "查询最新推荐id", notes = "查询最新推荐id")
    @ApiMapping(value = "comvita.ticket.channelInfo.getRefId", method = RequestMethod.POST)
    public ChannelInfoRefIdResult getRefId(ChannelInfoRefIdParam param) {
        return ticketChannelInfoService.getRefId(param);
    }
}
