package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface PreCommentService {

    void commentInfo(CommentParam param);

    IPage<PreCommentInfo> pageComment(CommentPageParam param);

    PreCommentInfo detailsComment(CommentDetailsParam param);
}
