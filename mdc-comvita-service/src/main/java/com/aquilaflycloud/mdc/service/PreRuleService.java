package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreRuleInfo;
import com.aquilaflycloud.mdc.param.pre.PreRuleAddParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleIdParam;
import com.aquilaflycloud.mdc.param.pre.PreRulePageParam;
import com.aquilaflycloud.mdc.param.pre.PreRuleUpdateParam;
import com.aquilaflycloud.mdc.result.pre.PreEnableRuleResult;
import com.aquilaflycloud.mdc.result.pre.PreRuleDetailResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * PreRuleService
 * @author linkq
 */
public interface PreRuleService {

    /**
     * page
     * @param param
     * @return
     */
    IPage<PreRuleDetailResult> page(PreRulePageParam param);

    /**
     * add
     * @param param
     */
    void add(PreRuleAddParam param);

    /**
     * 获取详情
     * @param param
     */
    PreRuleDetailResult get(PreRuleIdParam param);

    /**
     * update
     * @param param
     */
    void update(PreRuleUpdateParam param);

    /**
     * 停用/启用
     * @param param
     */
    void cancelStart(PreRuleIdParam param);

    /**
     * 设为默认
     * @param param
     */
    void setDefault(PreRuleIdParam param);

    /**
     * 获取已启用的规则
     * @return
     */
    List<PreEnableRuleResult> getEnableRules();
}
