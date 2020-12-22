package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.system.SystemSqlInjector;
import com.aquilaflycloud.mdc.param.system.*;
import com.aquilaflycloud.mdc.result.system.SqlResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * SystemSqlInjectorService
 *
 * @author star
 * @date 2020/8/31
 */
public interface SystemSqlInjectorService {
    IPage<SystemSqlInjector> page(SqlPageParam param);

    List<SystemSqlInjector> list(SqlPageParam param);

    void add(SqlAddParam param);

    void edit(SqlEditParam param);

    SqlResult get(SqlGetParam param);

    void delete(SqlGetParam param);

    List<Map<String, Object>> execute(SqlExecuteParam param);

    IPage<Map<String, Object>> executePage(SqlExecutePageParam param);
}
