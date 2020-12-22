package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.mission.MissionTypeEnum;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.mission.MissionInfo;
import com.aquilaflycloud.mdc.model.mission.MissionMemberRecord;
import com.aquilaflycloud.mdc.param.mission.MissionAddParam;
import com.aquilaflycloud.mdc.param.mission.MissionEditParam;
import com.aquilaflycloud.mdc.param.mission.MissionGetParam;
import com.aquilaflycloud.mdc.result.mission.MissionInfoResult;
import com.aquilaflycloud.mdc.result.mission.MissionRecordResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * MissionService
 *
 * @author star
 * @date 2020-05-08
 */
public interface MissionService {
    List<MissionInfo> listMission();

    MissionInfoResult getMission(MissionGetParam param);

    void addMission(MissionAddParam param);

    void editMission(MissionEditParam param);

    void toggleMission(MissionGetParam param);

    IPage<MissionRecordResult> pageRecord(PageParam<MissionMemberRecord> param);

    void acceptMission(MemberInfo memberInfo);

    void checkMission(Long memberId, MissionTypeEnum missionType);
}

