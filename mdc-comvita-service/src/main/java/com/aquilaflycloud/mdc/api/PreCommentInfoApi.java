package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.param.pre.CommentDetailsParam;
import com.aquilaflycloud.mdc.param.pre.CommentPageParam;
import com.aquilaflycloud.mdc.param.pre.CommentParam;
import com.aquilaflycloud.mdc.result.pre.PreCommentInfoResult;
import com.aquilaflycloud.mdc.result.pre.PreCommentLikeResult;
import com.aquilaflycloud.mdc.service.PreCommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author zouliyong
 */
@RestController
@Api(tags = "点评接口")
public class PreCommentInfoApi {

    @Resource
    private PreCommentService preCommentService;

    @ApiOperation(value = "写点评", notes = "写点评")
    @ApiMapping(value = "comvita.info.comment", method = RequestMethod.POST)
    public void commentInfo(CommentParam param) {
        preCommentService.commentInfo(param);
    }

    @ApiOperation(value = "获取我的点评列表(分页)", notes = "获取我的点评列表(分页)")
    @ApiMapping(value = "comvita.comment.page", method = RequestMethod.POST)
    public IPage<PreCommentInfo> pageComment(CommentPageParam param) {
        return preCommentService.pageComment(param);
    }

    @ApiOperation(value = "获取我的点评明细", notes = "获取我的点评明细")
    @ApiMapping(value = "comvita.comment.details", method = RequestMethod.POST)
    public PreCommentInfoResult detailsComment(CommentDetailsParam param) {
        return preCommentService.detailsComment(param);
    }

    @ApiOperation(value = "获取我的点赞评论列表(分页)", notes = "获取我的点赞评论列表(分页)")
    @ApiMapping(value = "comvita.comment.like.page", method = RequestMethod.POST)
    public IPage<PreCommentLikeResult> pageLikeComment(PageParam<MemberInteraction> param) {
        return preCommentService.pageLikeComment(param);
    }
}
