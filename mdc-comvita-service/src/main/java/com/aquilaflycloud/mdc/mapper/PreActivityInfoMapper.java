package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface PreActivityInfoMapper extends BaseMapper<PreActivityInfo> {

    List<Map<String, Object>> getFolksonomy(Long id);

}