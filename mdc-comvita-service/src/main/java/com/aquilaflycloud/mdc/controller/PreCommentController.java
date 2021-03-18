package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.service.PreActivityCommentService;
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
    private PreActivityCommentService preActivityCommentService;

    /**
     * 分页获取点评信息
     * @param param
     * @return
     */
    @ApiOperation(value = "活动点评分页信息", notes = "活动点评分页信息")
    @ApiMapping(value = "backend.comvita.comment.page", method = RequestMethod.POST, permission = true)
    public IPage<PreCommentPageResult> page(PreCommentPageParam param) {
        return preActivityCommentService.page(param);
    }

    /**
     * 获取点评详情
     * @param param
     * @return
     */
    @ApiOperation(value = "活动点评详情", notes = "活动点评详情")
    @ApiMapping(value = "backend.comvita.comment.get", method = RequestMethod.POST, permission = true)
    public PreCommentPageResult get(PreCommentGetParam param) {
        return preActivityCommentService.get(param);
    }

    /**
     * 审核
     * @param param
     */
    @ApiOperation(value = "活动点评审核", notes = "活动点评审核")
    @ApiMapping(value = "backend.comvita.comment.audit", method = RequestMethod.POST, permission = true)
    public void audit(PreCommentAuditParam param) {
        preActivityCommentService.audit(param);
    }


    /**
     * 改变展示状态
     * @param param
     */
    @ApiOperation(value = "活动点评隐藏(公开)", notes = "活动点评隐藏(公开)")
    @ApiMapping(value = "backend.comvita.comment.view.state.change", method = RequestMethod.POST, permission = true)
    public void changeViewState(PreCommentChangViewStateParam param) {
        preActivityCommentService.changeViewState(param);
    }

}
