package com.aquilaflycloud.mdc.api;


import com.aquilaflycloud.mdc.param.ticket.ScenicSpotInfoGetParam;
import com.aquilaflycloud.mdc.result.ticket.TicketScenicSpotInfoResult;
import com.aquilaflycloud.mdc.service.TicketScenicSpotInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 景区信息控制器
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@RestController
@Api(tags = "景区信息控制器")
public class TicketScenicSpotInfoApi {
    @Resource
    private TicketScenicSpotInfoService ticketScenicSpotInfoService;

    @ApiOperation(value = "小程序根据类型获取景区信息", notes = "小程序根据类型获取景区信息")
    @ApiMapping(value = "mdc.ticket.scenicspotinfo.get", method = RequestMethod.POST)
    public TicketScenicSpotInfoResult getScenicSpotInfo(ScenicSpotInfoGetParam param) {
        return ticketScenicSpotInfoService.getScenicSpotInfo(param);
    }

}
