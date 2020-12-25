package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.IsvAppInfo;
import com.aquilaflycloud.mdc.param.isv.IsvGetParam;
import com.aquilaflycloud.mdc.param.isv.IsvListParam;
import com.aquilaflycloud.mdc.param.isv.IsvSaveParam;
import com.aquilaflycloud.mdc.service.IsvAppInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * IsvAppInfoController
 *
 * @author star
 * @date 2019-12-17
 */
@RestController
@Api(tags = "ISV信息管理")
public class IsvAppInfoController {

    @Resource
    private IsvAppInfoService isvAppInfoService;

    @ApiOperation(value = "查询isv列表(分页)", notes = "查询isv列表(分页)")
    @PreAuthorize("hasAuthority('mdc:isv:list')")
    @ApiMapping(value = "backend.comvita.isv.info.list", method = RequestMethod.POST, permission = true)
    public IPage<IsvAppInfo> page(IsvListParam param) {
        return isvAppInfoService.pageIsvInfo(param);
    }

    @ApiOperation(value = "获取isv信息", notes = "获取isv信息")
    @PreAuthorize("hasAuthority('mdc:isv:get')")
    @ApiMapping(value = "backend.comvita.isv.info.get", method = RequestMethod.POST, permission = true)
    public IsvAppInfo get(IsvGetParam param) {
        return isvAppInfoService.get(param);
    }

    @ApiOperation(value = "保存isv信息", notes = "保存isv信息")
    @PreAuthorize("hasAuthority('mdc:isv:save')")
    @ApiMapping(value = "backend.comvita.isv.info.save", method = RequestMethod.POST, permission = true)
    public void save(IsvSaveParam param) {
        isvAppInfoService.save(param);
    }

    @ApiOperation(value = "删除isv信息", notes = "删除isv信息")
    @PreAuthorize("hasAuthority('mdc:isv:delete')")
    @ApiMapping(value = "backend.comvita.isv.info.delete", method = RequestMethod.POST, permission = true)
    public void delete(IsvGetParam param) {
        isvAppInfoService.delete(param);
    }
}
