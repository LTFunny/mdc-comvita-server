package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PrePickingCardPageParam;
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
@Api(tags = "提货卡")
public class PrePickingCardController {
    @Resource
    private PrePickingCardService prePickingCardService;

    @ApiOperation(value = "提货卡分页信息", notes = "提货卡分页信息")
    @PreAuthorize("hasAuthority('mdc:pre:picking:card:page')")
    @ApiMapping(value = "backend.comvita.pre.picking.card.page", method = RequestMethod.POST, permission = true)
    public IPage<PrePickingCard> page(PrePickingCardPageParam param) {
        return prePickingCardService.page(param);
    }


}
