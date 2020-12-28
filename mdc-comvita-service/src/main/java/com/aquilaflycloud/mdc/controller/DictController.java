package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.bean.EnumProperty;
import com.aquilaflycloud.mdc.param.system.DictListParam;
import com.aquilaflycloud.mdc.param.system.DictNameParam;
import com.aquilaflycloud.mdc.service.BizEnumService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * DictController
 *
 * @author star
 * @date 2019-10-21
 */
@RestController
@Api(tags = "字典查询")
public class DictController {

    @Resource
    private BizEnumService bizEnumService;

    @ApiOperation(value = "查询字典", notes = "查询字典", response = EnumProperty.class, responseContainer = "List")
    @PreAuthorize("hasAuthority('mdc:dict:list')")
    @ApiMapping(value = "backend.mdccomvita.dict.info.list", method = RequestMethod.POST, permission = true)
    public Collection<Enum> list(DictListParam param) {
        return bizEnumService.listEnums(param);
    }

    @ApiOperation(value = "把字典加入数据权限控制", notes = "把字典加入数据权限控制")
    @PreAuthorize("hasAuthority('mdc:dict:manage')")
    @ApiMapping(value = "backend.comvita.dict.dataAuth.add", method = RequestMethod.POST, permission = true)
    public void add(DictNameParam param) {
        bizEnumService.addDataAuth(param);
    }

    @ApiOperation(value = "把字典剔除数据权限控制", notes = "把字典剔除数据权限控制")
    @PreAuthorize("hasAuthority('mdc:dict:manage')")
    @ApiMapping(value = "backend.comvita.dict.dataAuth.delete", method = RequestMethod.POST, permission = true)
    public void delete(DictNameParam param) {
        bizEnumService.deleteDataAuth(param);
    }
}
