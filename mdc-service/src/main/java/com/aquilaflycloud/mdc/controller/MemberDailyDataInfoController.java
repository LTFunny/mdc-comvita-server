package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.member.MemberGrowParam;
import com.aquilaflycloud.mdc.param.member.MemberVisitTimesParam;
import com.aquilaflycloud.mdc.result.member.MemberGrowResult;
import com.aquilaflycloud.mdc.result.member.MemberVisitTimesResult;
import com.aquilaflycloud.mdc.service.MemberDailyDataInfoService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * AlipayMemberDailyDataInfoController
 *
 * @author Zengqingjie
 * @date 2020-05-26
 */
@Api(tags = "会员分析")
@RestController
public class MemberDailyDataInfoController {
    @Resource
    private MemberDailyDataInfoService memberDailyDataInfoService;

    @ApiOperation(value = "会员增长分析", notes = "会员增长分析")
    @PreAuthorize("hasAuthority('mdc:memberDaily:grow')")
    @ApiMapping(value = "backend.mdc.member.daily.grow", method = RequestMethod.POST, permission = true)
    public MemberGrowResult memberDailyGrow(MemberGrowParam param) {
        return memberDailyDataInfoService.memberGrow(param);
    }

    @ApiOperation(value = "用户访问分析", notes = "用户访问分析")
    @PreAuthorize("hasAuthority('mdc:memberDaily:times')")
    @ApiMapping(value = "backend.mdc.member.daily.times", method = RequestMethod.POST, permission = true)
    public MemberVisitTimesResult memberDailyVisitTimes(MemberVisitTimesParam param) {
        return memberDailyDataInfoService.memberVisitTimes(param);
    }
}
