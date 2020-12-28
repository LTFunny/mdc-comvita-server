package com.aquilaflycloud.mdc.controller;


import com.aquilaflycloud.mdc.model.ticket.TicketScenicSpotInfo;
import com.aquilaflycloud.mdc.param.ticket.ScenicSpotInfoGetParam;
import com.aquilaflycloud.mdc.param.ticket.ScenicSpotInfoListParam;
import com.aquilaflycloud.mdc.param.ticket.ScenicSpotInfoSaveParam;
import com.aquilaflycloud.mdc.param.ticket.ScenicSpotInfoUpdateByIdParam;
import com.aquilaflycloud.mdc.result.ticket.TicketScenicSpotInfoResult;
import com.aquilaflycloud.mdc.service.TicketScenicSpotInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class TicketScenicSpotInfoController {
    @Resource
    private TicketScenicSpotInfoService ticketScenicSpotInfoService;

    @ApiOperation("获取景区信息列表")
    @PreAuthorize("hasAuthority('mdc:ticket:scenicspotinfo:page')")
    @ApiMapping(value = "backend.comvita.ticket.scenicspotinfo.page", method = RequestMethod.POST, permission = true)
    public IPage<TicketScenicSpotInfo> listScenicSpotInfo(ScenicSpotInfoListParam param) {
        return ticketScenicSpotInfoService.listScenicSpotInfo(param);
    }

    @ApiOperation("获取景区信息")
    @PreAuthorize("hasAuthority('mdc:ticket:scenicspotinfo:get')")
    @ApiMapping(value = "backend.comvita.ticket.scenicspotinfo.get", method = RequestMethod.POST, permission = true)
    public TicketScenicSpotInfoResult getScenicSpotInfo(ScenicSpotInfoGetParam param) {
        return ticketScenicSpotInfoService.getScenicSpotInfo(param);
    }

    @ApiOperation("保存景区信息")
    @PreAuthorize("hasAuthority('mdc:ticket:scenicspotinfo:save')")
    @ApiMapping(value = "backend.comvita.ticket.scenicspotinfo.save", method = RequestMethod.POST, permission = true)
    public TicketScenicSpotInfo saveScenicSpotInfo(ScenicSpotInfoSaveParam param) {
        return ticketScenicSpotInfoService.saveScenicSpotInfo(param);
    }

    @ApiOperation("根据景区id更新指定可看部门ids和账号")
    @PreAuthorize("hasAuthority('mdc:ticket:scenicspotinfo:updateById')")
    @ApiMapping(value = "backend.comvita.ticket.scenicspotinfo.updateById", method = RequestMethod.POST, permission = true)
    public int updateById(ScenicSpotInfoUpdateByIdParam param) {
        return ticketScenicSpotInfoService.updateById(param);
    }

}
