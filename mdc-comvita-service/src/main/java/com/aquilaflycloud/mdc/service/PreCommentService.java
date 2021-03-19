package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreCommentInfoResult;
import com.aquilaflycloud.mdc.result.pre.PreCommentPageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreCommentService {

    void commentInfo(CommentParam param);

    IPage<PreCommentInfo> pageComment(CommentPageParam param);

    PreCommentInfoResult detailsComment(CommentDetailsParam param);

    IPage<PreCommentInfo> pageLikeComment(PageParam<MemberInteraction> param);

    /**
     * 改变显示状态 隐藏/公开
     * @param param
     */
    void changeViewState(PreCommentChangViewStateParam param);

    /**
     * 审核
     * @param param
     */
    void audit(PreCommentAuditParam param);

    /**
     * 获取点评详情
     * @param param
     * @return
     */
    PreCommentPageResult get(PreCommentGetParam param);

    /**
     * 分页获取活动点评
     * @param param
     * @return
     */
    IPage<PreCommentPageResult> page(PreCommentPageParam param);

    /**
     * 回复
     * @param param
     */
    void reply(PreCommentReplyParam param);
}
