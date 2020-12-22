package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.member.MemberAttributesAnalysisParam;
import com.aquilaflycloud.mdc.result.member.MemberAttributesAnalysisResult;
import com.aquilaflycloud.mdc.service.MemberService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberAttributesAnalysisController
 *
 * @author Zengqingjie
 * @date 2020-05-27
 */
@RestController
@Api(tags = "会员属性分析")
public class MemberAttributesAnalysisController {
    @Resource
    private MemberService memberService;

    @ApiOperation(value = "会员属性分析", notes = "会员属性分析")
    @PreAuthorize("hasAuthority('mdc:memberAttr:analysis')")
    @ApiMapping(value = "backend.mdc.member.attributes.analysis", method = RequestMethod.POST, permission = true)
    public MemberAttributesAnalysisResult memberAttributesAnalysis(MemberAttributesAnalysisParam param) {
        return memberService.memberAttributesAnalysis(param);
    }
}
