package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.param.member.MemberFeedbackAddParam;
import com.aquilaflycloud.mdc.service.MemberFeedbackService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PlayStrategyApi
 *
 * @author zengqingjie
 * @date 2020-06-28
 */
@RestController
@Api(tags = "会员意见反馈")
public class MemberFeedbackApi {
    @Resource
    private MemberFeedbackService memberFeedbackService;

    @ApiOperation(value = "添加意见反馈", notes = "添加意见反馈")
    @ApiMapping(value = "comvita.feedback.info.add", method = RequestMethod.POST)
    public void add(MemberFeedbackAddParam param) {
        memberFeedbackService.add(param);
    }
}
