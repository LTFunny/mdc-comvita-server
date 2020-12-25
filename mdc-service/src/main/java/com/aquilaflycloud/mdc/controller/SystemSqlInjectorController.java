package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.system.SystemSqlInjector;
import com.aquilaflycloud.mdc.param.system.*;
import com.aquilaflycloud.mdc.result.system.SqlResult;
import com.aquilaflycloud.mdc.service.SystemSqlInjectorService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * SystemSqlInjectorController
 *
 * @author star
 * @date 2020/8/31
 */
@RestController
@Api(tags = "自定义sql管理")
public class SystemSqlInjectorController {
    @Resource
    private SystemSqlInjectorService systemSqlInjectorService;

    @ApiOperation(value = "获取自定义sql列表(分页)", notes = "获取自定义sql列表(分页)")
    @PreAuthorize("hasAuthority('mdc:systemSql:list')")
    @ApiMapping(value = "backend.comvita.system.sql.page", method = RequestMethod.POST, permission = true)
    public IPage<SystemSqlInjector> page(SqlPageParam param) {
        return systemSqlInjectorService.page(param);
    }

    @ApiOperation(value = "获取自定义sql列表", notes = "获取自定义sql列表")
    @PreAuthorize("hasAuthority('mdc:systemSql:list')")
    @ApiMapping(value = "backend.comvita.system.sql.list", method = RequestMethod.POST, permission = true)
    public List<SystemSqlInjector> list(SqlPageParam param) {
        return systemSqlInjectorService.list(param);
    }

    @ApiOperation(value = "新增自定义sql", notes = "新增自定义sql")
    @PreAuthorize("hasAuthority('mdc:systemSql:add')")
    @ApiMapping(value = "backend.comvita.system.sql.add", method = RequestMethod.POST, permission = true)
    public void add(SqlAddParam param) {
        systemSqlInjectorService.add(param);
    }

    @ApiOperation(value = "编辑自定义sql", notes = "编辑自定义sql")
    @PreAuthorize("hasAuthority('mdc:systemSql:edit')")
    @ApiMapping(value = "backend.comvita.system.sql.edit", method = RequestMethod.POST, permission = true)
    public void edit(SqlEditParam param) {
        systemSqlInjectorService.edit(param);
    }

    @ApiOperation(value = "获取自定义sql详情", notes = "获取自定义sql详情")
    @PreAuthorize("hasAuthority('mdc:systemSql:get')")
    @ApiMapping(value = "backend.comvita.system.sql.get", method = RequestMethod.POST, permission = true)
    public SqlResult get(SqlGetParam param) {
        return systemSqlInjectorService.get(param);
    }

    @ApiOperation(value = "删除自定义sql", notes = "删除自定义sql")
    @PreAuthorize("hasAuthority('mdc:systemSql:delete')")
    @ApiMapping(value = "backend.comvita.system.sql.delete", method = RequestMethod.POST, permission = true)
    public void delete(SqlGetParam param) {
        systemSqlInjectorService.delete(param);
    }

    @ApiOperation(value = "执行自定义sql", notes = "执行自定义sql")
    @PreAuthorize("hasAuthority('mdc:systemSql:execute')")
    @ApiMapping(value = "backend.comvita.system.sql.execute", method = RequestMethod.POST, permission = true)
    public List<Map<String, Object>> execute(SqlExecuteParam param) {
        return systemSqlInjectorService.execute(param);
    }

    @ApiOperation(value = "执行自定义sql(分页)", notes = "执行自定义sql(分页)")
    @PreAuthorize("hasAuthority('mdc:systemSql:execute')")
    @ApiMapping(value = "backend.comvita.system.sql.executePage", method = RequestMethod.POST, permission = true)
    public IPage<Map<String, Object>> executePage(SqlExecutePageParam param) {
        return systemSqlInjectorService.executePage(param);
    }
}
