package com.aquilaflycloud.mdc.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface SqlInjectorMapper extends BaseMapper {
    @Select("${selectSql}")
    IPage<Map<String, Object>> selectDataPage(IPage page, @Param("selectSql") String selectSql);

    @Select("${selectSql}")
    List<Map<String, Object>> selectDataList(@Param("selectSql") String selectSql);

    @Update("${updateSql}")
    void updateData(@Param("updateSql") String updateSql);

    @InterceptorIgnore(tenantLine = "true")
    @Select("${selectSql}")
    IPage<Map<String, Object>> normalSelectDataPage(IPage page, @Param("selectSql") String selectSql);

    @InterceptorIgnore(tenantLine = "true")
    @Select("${selectSql}")
    List<Map<String, Object>> normalSelectDataList(@Param("selectSql") String selectSql);

    @InterceptorIgnore(tenantLine = "true")
    @Update("${updateSql}")
    void normalUpdateData(@Param("updateSql") String updateSql);
}