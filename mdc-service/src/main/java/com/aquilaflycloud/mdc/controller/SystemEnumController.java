package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.system.SystemEnum;
import com.aquilaflycloud.mdc.param.system.EnumAddParam;
import com.aquilaflycloud.mdc.param.system.EnumListParam;
import com.aquilaflycloud.mdc.service.SystemEnumService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * SystemEnumController
 *
 * @author star
 * @date 2019-11-04
 */
@RestController
@Api(tags = "动态字典枚举接口")
public class SystemEnumController {

    @Resource
    private SystemEnumService systemEnumService;

    @ApiOperation(value = "查询字典枚举(分页)", notes = "查询字典枚举(分页)")
    @PreAuthorize("hasAuthority('mdc:system:enumList')")
    @ApiMapping(value = "backend.mdc.enum.info.page", method = RequestMethod.POST, permission = true)
    public IPage<SystemEnum> page(EnumListParam param) {
        return systemEnumService.page(param);
    }

    @ApiOperation(value = "增加字典枚举", notes = "增加字典枚举")
    @PreAuthorize("hasAuthority('mdc:system:enumAdd')")
    @ApiMapping(value = "backend.mdc.enum.info.add", method = RequestMethod.POST, permission = true)
    public void add(EnumAddParam param) {
        systemEnumService.add(param);
    }
}
