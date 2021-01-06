package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.mdc.mapper.PreRuleInfoMapper;
import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.PreRuleAddParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleIdParam;
import com.aquilaflycloud.mdc.param.pre.PreRulePageParam;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.aquilaflycloud.mdc.service.PreRuleService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * PreRuleServiceImpl
 * @author linkq
 */
@Slf4j
@Service
public class PreRuleServiceImpl implements PreRuleService {

    @Resource
    private PreRuleInfoMapper preRuleInfoMapper;

//    @Override
//    public IPage<PreRuleInfo> pagePreRule(PreRulePageParam param) {
//        return null;
//        return preRuleInfoMapper.selectPage(param.page(), new QueryWrapper<Recommendation>()
//                .orderByDesc("is_top", "case when is_top = 1 then set_top_time else create_time end")
//    }

    @Override
    public IPage<PreRuleInfo> page(PreRulePageParam param) {
        return null;
    }

    @Override
    @Transactional
    public void add(PreRuleAddParam param) {
        Long tenantId = MdcUtil.getCurrentTenantId();


    }

    @Override
    public void update(PreRuleIdParam param) {

    }

    @Override
    public void stop(PreRuleIdParam param) {

    }

    @Override
    public void setDefault(PreRuleIdParam param) {

    }

    @Override
    public List<PreEnableRuleResult> getEnableRules() {
        return null;
    }
}
