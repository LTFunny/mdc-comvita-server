package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.member.MemberEventLog;
import com.aquilaflycloud.mdc.result.member.MemberEventLogResult;
import com.aquilaflycloud.mdc.result.member.MemberEventSexAndAgeResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberEventLogMapper extends AfcBaseMapper<MemberEventLog> {

    List<MemberEventLogResult> selectLogCount(@Param(Constants.WRAPPER) Wrapper wrapper);

    MemberEventSexAndAgeResult selectLogSexAndAge(@Param(Constants.WRAPPER) Wrapper wrapper);
}