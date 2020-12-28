package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.RegisterChannelAnalysisResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelStatisticsResult;
import com.aquilaflycloud.mdc.result.member.RegisterChannelTopResult;
import com.aquilaflycloud.mdc.service.MemberChannelService;
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
 * MemberChannelController
 *
 * @author star
 * @date 2020-02-19
 */
@RestController
@Api(tags = "会员渠道管理")
public class MemberChannelController {

    @Resource
    private MemberChannelService memberChannelService;

    @ApiOperation(value = "查询渠道列表(分页)", notes = "查询渠道列表(分页)")
    @PreAuthorize("hasAuthority('mdc:memberChannel:list')")
    @ApiMapping(value = "backend.comvita.member.channel.page", method = RequestMethod.POST, permission = true)
    public IPage<RegisterChannelResult> pageRegisterChannel(RegisterChannelPageParam param) {
        return memberChannelService.pageRegisterChannel(param);
    }

    @ApiOperation(value = "新增渠道", notes = "新增渠道")
    @PreAuthorize("hasAuthority('mdc:memberChannel:add')")
    @ApiMapping(value = "backend.comvita.member.channel.add", method = RequestMethod.POST, permission = true)
    public void addRegisterChannel(RegisterChannelAddParam param) {
        memberChannelService.addRegisterChannel(param);
    }

    @ApiOperation(value = "批量新增渠道", notes = "批量新增渠道")
    @PreAuthorize("hasAuthority('mdc:memberChannel:add')")
    @ApiMapping(value = "backend.comvita.member.channel.batchAdd", method = RequestMethod.POST, permission = true)
    public void batchAddRegisterChannel(RegisterChannelBatchAddParam param) {
        memberChannelService.batchAddRegisterChannel(param);
    }

    @ApiOperation(value = "编辑渠道", notes = "编辑渠道")
    @PreAuthorize("hasAuthority('mdc:memberChannel:edit')")
    @ApiMapping(value = "backend.comvita.member.channel.edit", method = RequestMethod.POST, permission = true)
    public void editRegisterChannel(RegisterChannelEditParam param) {
        memberChannelService.editRegisterChannel(param);
    }

    @ApiOperation(value = "更改渠道状态", notes = "更改渠道状态")
    @PreAuthorize("hasAuthority('mdc:memberChannel:edit')")
    @ApiMapping(value = "backend.comvita.member.channel.toggle", method = RequestMethod.POST, permission = true)
    public void toggleRegisterChannel(RegisterChannelGetParam param) {
        memberChannelService.toggleRegisterChannel(param);
    }

    @ApiOperation(value = "批量下载小程序码", notes = "批量下载小程序码")
    @PreAuthorize("hasAuthority('mdc:memberChannel:list')")
    @ApiMapping(value = "backend.comvita.member.channelMiniCode.download", method = RequestMethod.POST, permission = true)
    public BaseResult<String> downloadChannelMiniCode(RegisterChannelBatchGetParam param) {
        return memberChannelService.downloadChannelMiniCode(param);
    }

    @ApiOperation(value = "获取渠道概况", notes = "获取渠道概况")
    @PreAuthorize("hasAuthority('mdc:memberChannel:list')")
    @ApiMapping(value = "backend.comvita.member.channelStatistics.get", method = RequestMethod.POST, permission = true)
    public RegisterChannelStatisticsResult getChannelStatistics(RegisterChannelTimeGetParam param) {
        return memberChannelService.getChannelStatistics(param);
    }

    @ApiOperation(value = "获取渠道分析", notes = "获取渠道分析")
    @PreAuthorize("hasAuthority('mdc:memberChannel:list')")
    @ApiMapping(value = "backend.comvita.member.channelAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<RegisterChannelAnalysisResult> getChannelAnalysis(RegisterChannelTimeGetParam param) {
        return memberChannelService.getChannelAnalysis(param);
    }

    @ApiOperation(value = "获取渠道码排名(默认前10)", notes = "获取渠道码排名(默认前10)")
    @PreAuthorize("hasAuthority('mdc:memberChannel:list')")
    @ApiMapping(value = "backend.comvita.member.channelTop.List", method = RequestMethod.POST, permission = true)
    public List<RegisterChannelTopResult> listChannelTop(RegisterChannelTopListParam param) {
        return memberChannelService.listChannelTop(param);
    }
}
