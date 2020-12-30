package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface PreOrderInfoMapper extends AfcBaseMapper<PreOrderInfo> {

    String getMaxOrderCode();
}