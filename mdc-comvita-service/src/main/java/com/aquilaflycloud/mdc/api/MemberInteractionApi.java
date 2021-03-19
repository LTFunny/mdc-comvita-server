package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.member.InteractionBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionTypeEnum;
import com.aquilaflycloud.mdc.param.member.MemberInteractionParam;
import com.aquilaflycloud.mdc.param.member.MemberInteractionToggleParam;
import com.aquilaflycloud.mdc.service.MemberInteractionService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberInteractionApi
 *
 * @author star
 * @date 2021/3/18
 */
@RestController
@Api(tags = "会员互动记录接口")
public class MemberInteractionApi {

    @Resource
    private MemberInteractionService memberInteractionService;

    @ApiOperation(value = "评论点赞/取消点赞", notes = "评论点赞/取消点赞,返回点赞数")
    @ApiMapping(value = "comvita.member.commentLike.toggle", method = RequestMethod.POST)
    public BaseResult<Long> addAdShare(MemberInteractionToggleParam param) {
        MemberInteractionParam eventParam = new MemberInteractionParam();
        eventParam.setBusinessId(param.getBusinessId());
        eventParam.setBusinessType(InteractionBusinessTypeEnum.COMMENT);
        eventParam.setInteractionType(InteractionTypeEnum.LIKE);
        return BaseResult.buildResult(memberInteractionService.toggleInteractionNum(eventParam));
    }
}
