package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.member.MemberSignRecord;
import com.aquilaflycloud.mdc.result.member.MemberSignContinueResult;
import com.aquilaflycloud.mdc.result.member.MemberSignInfoResult;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberSignRecordMapper extends AfcBaseMapper<MemberSignRecord> {
    IPage<MemberSignInfoResult> selectPageByMemberParam(IPage page, @Param("limitCount") int limitCount, @Param(Constants.WRAPPER) Wrapper wrapper);

    @InterceptorIgnore(tenantLine = "true")
    List<MemberSignContinueResult> selectContinueSignCount(@Param("record") MemberSignRecord record, @Param(Constants.WRAPPER) Wrapper wrapper);
}
