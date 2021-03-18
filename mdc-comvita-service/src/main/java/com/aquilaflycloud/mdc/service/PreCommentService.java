package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreCommentPageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreCommentService {

    void commentInfo(CommentParam param);

    IPage<PreCommentInfo> pageComment(CommentPageParam param);

    PreCommentInfo detailsComment(CommentDetailsParam param);

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
}
