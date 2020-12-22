package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.model.member.MemberGrade;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.MemberGradeResult;

import java.util.List;

/**
 * MemberGradeService
 *
 * @author star
 * @date 2020-03-05
 */
public interface MemberGradeService {
    List<MemberGradeResult> listMemberGrade(MemberGradeListParam param);

    MemberGrade getMemberGrade(MemberGradeGetParam param);

    void batchAddMemberGrade(MemberGradeBatchAddParam param);

    void addMemberGrade(MemberGradeAddParam param);

    void editMemberGrade(MemberGradeEditParam param);

    void deleteMemberGrade(MemberGradeGetParam param);

    MemberGrade getRewardGrade(String appId, RewardTypeEnum rewardType, Integer reward);
}

