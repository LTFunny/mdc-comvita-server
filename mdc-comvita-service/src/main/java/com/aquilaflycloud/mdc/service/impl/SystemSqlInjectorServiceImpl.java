package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.mapper.SqlInjectorMapper;
import com.aquilaflycloud.mdc.mapper.SystemSqlInjectorMapper;
import com.aquilaflycloud.mdc.model.system.SystemSqlInjector;
import com.aquilaflycloud.mdc.param.system.*;
import com.aquilaflycloud.mdc.result.system.SqlResult;
import com.aquilaflycloud.mdc.service.SystemSqlInjectorService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SystemSqlInjectorServiceImpl
 *
 * @author star
 * @date 2020/8/31
 */
@Service
public class SystemSqlInjectorServiceImpl implements SystemSqlInjectorService {
    @Resource
    private SystemSqlInjectorMapper systemSqlInjectorMapper;
    @Resource
    private SqlInjectorMapper sqlInjectorMapper;

    @Override
    public IPage<SystemSqlInjector> page(SqlPageParam param) {
        return systemSqlInjectorMapper.selectPage(param.page(), Wrappers.<SystemSqlInjector>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), SystemSqlInjector::getName, param.getName())
                .orderByDesc(SystemSqlInjector::getCreateTime)
        );
    }

    @Override
    public List<SystemSqlInjector> list(SqlPageParam param) {
        return systemSqlInjectorMapper.selectList(Wrappers.<SystemSqlInjector>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), SystemSqlInjector::getName, param.getName())
                .orderByDesc(SystemSqlInjector::getCreateTime)
        );
    }

    @Override
    public void add(SqlAddParam param) {
        List<String> paramList = ReUtil.findAll("(?<=\\{)[^}]*(?=\\})", param.getSqlWhereContent(), 0);
        if (paramList.size() == param.getParamList().size()) {
            Map<String, String> paramMap = param.getParamList().stream().collect(Collectors.toMap(SqlWhereParam::getName, SqlWhereParam::getName));
            for (String name : paramList) {
                if (paramMap.get(name) == null) {
                    throw new ServiceException("缺少参数:" + name);
                }
            }
        }
        SystemSqlInjector sqlInjector = new SystemSqlInjector();
        BeanUtil.copyProperties(param, sqlInjector);
        sqlInjector.setColumnContent(JSONUtil.toJsonStr(param.getColumnList()));
        sqlInjector.setParamContent(JSONUtil.toJsonStr(param.getParamList()));
        int count = systemSqlInjectorMapper.insert(sqlInjector);
        if (count <= 0) {
            throw new ServiceException("新增sql失败");
        }
    }

    @Override
    public void edit(SqlEditParam param) {
        SystemSqlInjector sqlInjector = systemSqlInjectorMapper.selectById(param.getId());
        if (sqlInjector == null) {
            throw new ServiceException("sql不存在");
        }
        SystemSqlInjector update = new SystemSqlInjector();
        BeanUtil.copyProperties(param, update);
        if (CollUtil.isNotEmpty(param.getColumnList())) {
            update.setColumnContent(JSONUtil.toJsonStr(param.getColumnList()));
        }
        if (CollUtil.isNotEmpty(param.getParamList())) {
            List<String> paramList = ReUtil.findAll("(?<=\\{)[^}]*(?=\\})", sqlInjector.getSqlWhereContent(), 0);
            if (paramList.size() == param.getParamList().size()) {
                Map<String, String> paramMap = param.getParamList().stream().collect(Collectors.toMap(SqlWhereParam::getName, SqlWhereParam::getName));
                for (String name : paramList) {
                    if (paramMap.get(name) == null) {
                        throw new ServiceException("缺少参数:" + name);
                    }
                }
            }
            update.setParamContent(JSONUtil.toJsonStr(param.getParamList()));
        }
        int count = systemSqlInjectorMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑sql失败");
        }
    }

    @Override
    public SqlResult get(SqlGetParam param) {
        SqlResult result = new SqlResult();
        SystemSqlInjector sqlInjector = systemSqlInjectorMapper.selectById(param.getId());
        BeanUtil.copyProperties(sqlInjector, result);
        result.setColumnList(JSONUtil.toList(JSONUtil.parseArray(sqlInjector.getColumnContent()), SqlResult.SqlColumn.class));
        result.setParamList(JSONUtil.toList(JSONUtil.parseArray(sqlInjector.getParamContent()), SqlResult.SqlParam.class));
        return result;
    }

    @Override
    public void delete(SqlGetParam param) {
        SystemSqlInjector sqlInjector = systemSqlInjectorMapper.selectById(param.getId());
        if (sqlInjector == null) {
            throw new ServiceException("sql不存在");
        }
        int count = systemSqlInjectorMapper.deleteById(param.getId());
        if (count <= 0) {
            throw new ServiceException("删除sql失败");
        }
    }

    @Override
    public List<Map<String, Object>> execute(SqlExecuteParam param) {
        SystemSqlInjector sqlInjector = systemSqlInjectorMapper.selectById(param.getId());
        if (sqlInjector == null) {
            throw new ServiceException("sql不存在");
        }
        StrBuilder sqlBuilder = new StrBuilder(sqlInjector.getSqlContent());
        if (StrUtil.isNotBlank(sqlInjector.getParamContent())) {
            List<String> paramList = ReUtil.findAll("(?<=\\{)[^}]*(?=\\})", sqlInjector.getSqlWhereContent(), 0);
            Map<String, String> paramMap = param.getParamValues().stream().collect(Collectors.toMap(SqlExecuteWhereParam::getName, SqlExecuteWhereParam::getValue));
            for (String name : paramList) {
                if (paramMap.get(name) == null) {
                    throw new ServiceException("缺少参数:" + name);
                } else {
                    paramMap.put(name, "'" + paramMap.get(name) + "'");
                }
            }
            sqlBuilder.append(" where ").append(StrUtil.format(sqlInjector.getSqlWhereContent(), paramMap));
        }
        return sqlInjectorMapper.selectDataList(sqlBuilder.toString());
    }

    @Override
    public IPage<Map<String, Object>> executePage(SqlExecutePageParam param) {
        SystemSqlInjector sqlInjector = systemSqlInjectorMapper.selectById(param.getId());
        if (sqlInjector == null) {
            throw new ServiceException("sql不存在");
        }
        StrBuilder sqlBuilder = new StrBuilder(sqlInjector.getSqlContent());
        if (StrUtil.isNotBlank(sqlInjector.getParamContent())) {
            List<String> paramList = ReUtil.findAll("(?<=\\{)[^}]*(?=\\})", sqlInjector.getSqlWhereContent(), 0);
            Map<String, String> paramMap = param.getParamValues().stream().collect(Collectors.toMap(SqlExecuteWhereParam::getName, SqlExecuteWhereParam::getValue));
            for (String name : paramList) {
                if (paramMap.get(name) == null) {
                    throw new ServiceException("缺少参数:" + name);
                } else {
                    paramMap.put(name, "'" + paramMap.get(name) + "'");
                }
            }
            sqlBuilder.append(" where ").append(StrUtil.format(sqlInjector.getSqlWhereContent(), paramMap));
        }
        return sqlInjectorMapper.selectDataPage(param.page(), sqlBuilder.toString());
    }
}
