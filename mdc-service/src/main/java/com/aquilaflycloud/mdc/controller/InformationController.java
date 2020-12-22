package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.information.Information;
import com.aquilaflycloud.mdc.param.information.*;
import com.aquilaflycloud.mdc.service.InformationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * InformationController
 *
 * @author star
 * @date 2020-03-07
 */
@RestController
@Api(tags = "资讯公告管理")
public class InformationController {
    @Resource
    private InformationService informationService;

    @ApiOperation(value = "获取资讯列表(分页)", notes = "获取资讯列表(分页)")
    @PreAuthorize("hasAuthority('mdc:info:list')")
    @ApiMapping(value = "backend.mdc.information.info.page", method = RequestMethod.POST, permission = true)
    public IPage<Information> pageInfo(InfoPageParam param) {
        return informationService.pageInfo(param);
    }

    @ApiOperation(value = "获取资讯", notes = "获取资讯")
    @PreAuthorize("hasAuthority('mdc:info:get')")
    @ApiMapping(value = "backend.mdc.information.info.get", method = RequestMethod.POST, permission = true)
    public Information getInfo(InfoGetParam param) {
        return informationService.getInfo(param);
    }

    @ApiOperation(value = "获取单个资讯", notes = "获取单个资讯")
    @PreAuthorize("hasAuthority('mdc:info:get')")
    @ApiMapping(value = "backend.mdc.information.importantest.get", method = RequestMethod.POST, permission = true)
    public Information getInfoOne(InfoOneGetParam param) {
        return informationService.getInfo(param);
    }

    @ApiOperation(value = "新增资讯", notes = "新增资讯")
    @PreAuthorize("hasAuthority('mdc:info:add')")
    @ApiMapping(value = "backend.mdc.information.info.add", method = RequestMethod.POST, permission = true)
    public void addInfo(InfoAddParam param) {
        informationService.addInfo(param);
    }

    @ApiOperation(value = "编辑资讯", notes = "编辑资讯")
    @PreAuthorize("hasAuthority('mdc:info:edit')")
    @ApiMapping(value = "backend.mdc.information.info.edit", method = RequestMethod.POST, permission = true)
    public void editInfo(InfoEditParam param) {
        informationService.editInfo(param);
    }

    @ApiOperation(value = "启用/停用资讯", notes = "启用/停用资讯")
    @PreAuthorize("hasAuthority('mdc:info:edit')")
    @ApiMapping(value = "backend.mdc.information.info.toggle", method = RequestMethod.POST, permission = true)
    public void toggleInfo(InfoGetParam param) {
        informationService.toggleInfo(param);
    }

    @ApiOperation(value = "删除资讯", notes = "删除资讯")
    @PreAuthorize("hasAuthority('mdc:info:delete')")
    @ApiMapping(value = "backend.mdc.information.info.delete", method = RequestMethod.POST, permission = true)
    public void deleteInfo(InfoGetParam param) {
        informationService.deleteInfo(param);
    }
}
