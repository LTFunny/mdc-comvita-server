package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.member.MemberFeedbackInfo;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackEditParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackGetParam;
import com.aquilaflycloud.mdc.param.member.MemberFeedbackPageParam;
import com.aquilaflycloud.mdc.service.MemberFeedbackService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PlayStrategyController
 *
 * @author zengqingjie
 * @date 2020-06-23
 */
@RestController
@Api(tags = "会员意见反馈")
public class MemberFeedbackController {
    @Resource
    private MemberFeedbackService memberFeedbackService;

    @ApiOperation(value = "分页查询意见反馈", notes = "分页查询意见反馈")
    @PreAuthorize("hasAuthority('mdc:memberFeedback:page')")
    @ApiMapping(value = "backend.mdc.member.feedback.page", method = RequestMethod.POST, permission = true)
    public IPage<MemberFeedbackInfo> page(MemberFeedbackPageParam param) {
        return memberFeedbackService.page(param);
    }

    @ApiOperation(value = "查询意见反馈详情", notes = "查询意见反馈详情")
    @PreAuthorize("hasAuthority('mdc:memberFeedback:get')")
    @ApiMapping(value = "backend.mdc.member.feedback.get", method = RequestMethod.POST, permission = true)
    public MemberFeedbackInfo get(MemberFeedbackGetParam param) {
        return memberFeedbackService.get(param);
    }

    @ApiOperation(value = "更新反馈备注", notes = "更新反馈备注")
    @PreAuthorize("hasAuthority('mdc:memberFeedback:edit')")
    @ApiMapping(value = "backend.mdc.member.feedback.edit", method = RequestMethod.POST, permission = true)
    public void edit(MemberFeedbackEditParam param) {
        memberFeedbackService.edit(param);
    }
}
