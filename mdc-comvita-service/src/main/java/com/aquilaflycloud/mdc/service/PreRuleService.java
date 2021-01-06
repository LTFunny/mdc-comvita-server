package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.PreRuleAddParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleIdParam;
import com.aquilaflycloud.mdc.param.pre.PreRulePageParam;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PreRuleService
 * @author linkq
 */
public interface PreRuleService {

    IPage<PreRuleInfo> page(PreRulePageParam param);

    @Transactional
    void add(PreRuleAddParam param);

    void update(PreRuleIdParam param);

    /**
     * 停用
     * @param param
     */
    void stop(PreRuleIdParam param);

    /**
     * 设为默认
     * @param param
     */
    void setDefault(PreRuleIdParam param);

    List<PreEnableRuleResult> getEnableRules();
}
