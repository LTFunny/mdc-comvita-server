package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.member.MemberSignAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberSignContinueAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberSignPageParam;
import com.aquilaflycloud.mdc.result.member.MemberSignAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberSignContinueAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberSignInfoResult;
import com.aquilaflycloud.mdc.result.member.MemberSignResult;
import com.aquilaflycloud.mdc.service.MemberSignService;
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
 * MemberSignController
 *
 * @author star
 * @date 2020-01-02
 */
@RestController
@Api(tags = "会员签到接口")
public class MemberSignController {

    @Resource
    private MemberSignService memberSignService;

    @ApiOperation(value = "查询签到记录", notes = "查询签到记录(会员去重)")
    @PreAuthorize("hasAuthority('mdc:sign:list')")
    @ApiMapping(value = "backend.mdc.sign.memberRecord.page", method = RequestMethod.POST, permission = true)
    public IPage<MemberSignInfoResult> pageMemberRecord(MemberSignPageParam param) {
        return memberSignService.pageMemberRecord(param);
    }

    @ApiOperation(value = "查询签到详细记录", notes = "查询签到详细记录")
    @PreAuthorize("hasAuthority('mdc:sign:list')")
    @ApiMapping(value = "backend.mdc.sign.record.page", method = RequestMethod.POST, permission = true)
    public IPage<MemberSignResult> pageRecord(MemberSignPageParam param) {
        return memberSignService.pageRecord(param);
    }

    @ApiOperation(value = "获取签到数分析", notes = "获取签到数分析")
    @PreAuthorize("hasAuthority('mdc:sign:list')")
    @ApiMapping(value = "backend.mdc.sign.countAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<MemberSignAnalysisResult> getSignCountAnalysis(MemberSignAnalysisParam param) {
        return memberSignService.getSignCountAnalysis(param);
    }

    @ApiOperation(value = "获取连续签到数分析", notes = "获取连续签到数分析")
    @PreAuthorize("hasAuthority('mdc:sign:list')")
    @ApiMapping(value = "backend.mdc.sign.continueAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<MemberSignContinueAnalysisResult> getContinueSignCountAnalysis(MemberSignContinueAnalysisParam param) {
        return memberSignService.getContinueSignCountAnalysis(param);
    }
}
