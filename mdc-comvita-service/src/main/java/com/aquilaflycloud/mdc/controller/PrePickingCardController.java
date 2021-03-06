package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardBatchAddParam;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardPageParam;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardUpdateParam;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardValidationParam;
import com.aquilaflycloud.mdc.result.pre.PrePickingCardAnalysisResult;
import com.aquilaflycloud.mdc.service.PrePickingCardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PrePickingCardController
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
@RestController
@Api(tags = "配送卡")
public class PrePickingCardController {
    @Resource
    private PrePickingCardService prePickingCardService;

    @ApiOperation(value = "配送卡分页信息", notes = "配送卡分页信息")
    @PreAuthorize("hasAuthority('mdc:pre:picking:card:page')")
    @ApiMapping(value = "backend.comvita.pre.picking.card.page", method = RequestMethod.POST, permission = true)
    public IPage<PrePickingCard> page(PrePickingCardPageParam param) {
        return prePickingCardService.page(param);
    }

    @ApiOperation(value = "配送卡新增", notes = "配送卡新增")
    @PreAuthorize("hasAuthority('mdc:pre:picking:card:batchAdd')")
    @ApiMapping(value = "backend.comvita.pre.picking.card.batchAdd", method = RequestMethod.POST, permission = true)
    public void batchAdd(PrePickingCardBatchAddParam param) {
        prePickingCardService.batchAdd(param);
    }

    @ApiOperation(value = "配送卡作废", notes = "配送卡作废")
    @PreAuthorize("hasAuthority('mdc:pre:picking:card:update')")
    @ApiMapping(value = "backend.comvita.pre.picking.card.update", method = RequestMethod.POST, permission = true)
    public void update(PrePickingCardUpdateParam param) {
        prePickingCardService.update(param);
    }

    @ApiOperation(value = "配送卡概况信息", notes = "配送卡概况信息")
    @PreAuthorize("hasAuthority('mdc:pre:picking:card:page')")
    @ApiMapping(value = "backend.comvita.pre.picking.card.analysis", method = RequestMethod.POST, permission = true)
    public PrePickingCardAnalysisResult analysis() {
        return prePickingCardService.analysis();
    }

    @ApiOperation(value = "验证提货卡", notes = "验证提货卡")
//    @PreAuthorize("hasAuthority('mdc:pre:picking:card:validation')")
    @ApiMapping(value = "backend.comvita.picking.card.validation", method = RequestMethod.POST, permission = true)
    public void addStatConfirmOrder(PrePickingCardValidationParam param) {
         prePickingCardService.validationPickingCard(param);
    }
}
