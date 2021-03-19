package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.enums.member.InteractionBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionTypeEnum;
import com.aquilaflycloud.mdc.param.member.MemberInteractionPageParam;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberInteractionResult;
import com.aquilaflycloud.mdc.result.pre.PreCommentPageResult;
import com.aquilaflycloud.mdc.service.MemberInteractionService;
import com.aquilaflycloud.mdc.service.PreCommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PreCommentController
 * @author linkq
 */
@RestController
@Api(tags = "活动点评")
public class PreCommentController {

    @Resource
    private PreCommentService preCommentService;

    @Resource
    private MemberInteractionService memberInteractionService;

    /**
     * 分页获取点评信息
     * @param param
     * @return
     */
    @ApiOperation(value = "活动点评分页信息", notes = "活动点评分页信息")
    @ApiMapping(value = "backend.comvita.comment.page", method = RequestMethod.POST, permission = true)
    public IPage<PreCommentPageResult> page(PreCommentPageParam param) {
        return preCommentService.page(param);
    }

    /**
     * 获取点评详情
     * @param param
     * @return
     */
    @ApiOperation(value = "活动点评详情", notes = "活动点评详情")
    @ApiMapping(value = "backend.comvita.comment.get", method = RequestMethod.POST, permission = true)
    public PreCommentPageResult get(PreCommentGetParam param) {
        return preCommentService.get(param);
    }

    /**
     * 审核
     * @param param
     */
    @ApiOperation(value = "活动点评审核", notes = "活动点评审核")
    @ApiMapping(value = "backend.comvita.comment.audit", method = RequestMethod.POST, permission = true)
    public void audit(PreCommentAuditParam param) {
        preCommentService.audit(param);
    }

    /**
     * 改变展示状态
     * @param param
     */
    @ApiOperation(value = "活动点评隐藏(公开)", notes = "活动点评隐藏(公开)")
    @ApiMapping(value = "backend.comvita.comment.view.state.change", method = RequestMethod.POST, permission = true)
    public void changeViewState(PreCommentChangViewStateParam param) {
        preCommentService.changeViewState(param);
    }

    @ApiOperation(value = "活动点评点赞记录(分页)", notes = "活动点评点赞记录(分页)")
    @ApiMapping(value = "backend.comvita.commentLike.page", method = RequestMethod.POST, permission = true)
    public IPage<MemberInteractionResult> pageCommentLike(PreCommentLikePageParam param) {
        MemberInteractionPageParam pageParam = new MemberInteractionPageParam();
        pageParam.setBusinessId(param.getId());
        pageParam.setBusinessType(InteractionBusinessTypeEnum.COMMENT);
        pageParam.setInteractionType(InteractionTypeEnum.LIKE);
        pageParam.setPageNum(param.getPageNum());
        pageParam.setPageSize(param.getPageSize());
        return memberInteractionService.pageMemberInteraction(pageParam);
    }

}
