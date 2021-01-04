package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author zly
 */
@RestController
@Api(tags = "订单管理")
public class PreOrderAdministrationController {

    @Resource
    private PreOrderAdministrationService preOrderAdministrationService;


    @ApiOperation(value = "订单管理列表", notes = "订单管理列表")
    @PreAuthorize("hasAuthority('mdc:pageAdministrationList:list')")
    @ApiMapping(value = "backend.comvita.administration.page", method = RequestMethod.POST, permission = true)
    public IPage<PreOrderInfo> pageAdministrationList(AdministrationListParam param) {
        return preOrderAdministrationService.pageAdministrationList(param);
    }

}
