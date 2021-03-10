package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface PreActivityInfoMapper extends BaseMapper<PreActivityInfo> {

    List<Map<String, Object>> getFolksonomy(Long id);

    /**
     * 获取活动关联门店以及会员的信息
     * @return
     */
    List<Map<String, Object>> getShopAndMember();

    /**
     * 获取预售活动关联门店以及会员的信息
     * @return
     */
    List<Map<String, Object>> getPreActivityRefShopAndMember();



}