package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.member.MemberHealthRecord;
import com.aquilaflycloud.mdc.param.member.HealthAddParam;
import com.aquilaflycloud.mdc.param.member.HealthQuickAddParam;

/**
 * MemberHealthService
 *
 * @author star
 * @date 2020-04-19
 */
public interface MemberHealthService {
    MemberHealthRecord getHealth();

    void addHealth(HealthAddParam param);

    void quickAddHealth(HealthQuickAddParam param);
}

