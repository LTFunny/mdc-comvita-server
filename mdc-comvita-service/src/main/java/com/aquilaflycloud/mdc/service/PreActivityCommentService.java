package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.*;
import com.baomidou.mybatisplus.core.metadata.IPage;


/**
 * 活动点评
 * PreActivityCommentService
 * @author linkq
 */
public interface PreActivityCommentService {

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
