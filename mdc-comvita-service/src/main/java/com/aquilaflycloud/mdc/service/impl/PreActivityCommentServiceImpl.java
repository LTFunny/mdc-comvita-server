package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.mdc.param.pre.PreCommentAuditParam;
import com.aquilaflycloud.mdc.param.pre.PreCommentChangViewStateParam;
import com.aquilaflycloud.mdc.param.pre.PreCommentGetParam;
import com.aquilaflycloud.mdc.param.pre.PreCommentPageParam;
import com.aquilaflycloud.mdc.result.pre.PreCommentPageResult;
import com.aquilaflycloud.mdc.service.PreActivityCommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * PreActivityCommentServiceImpl
 * @author linkq
 */
@Slf4j
@Service
public class PreActivityCommentServiceImpl implements PreActivityCommentService {



    @Override
    public void changeViewState(PreCommentChangViewStateParam param) {

    }

    @Override
    public void audit(PreCommentAuditParam param) {

    }

    @Override
    public PreCommentPageResult get(PreCommentGetParam param) {
        return null;
    }

    @Override
    public IPage<PreCommentPageResult> page(PreCommentPageParam param) {
        return null;
    }
}
