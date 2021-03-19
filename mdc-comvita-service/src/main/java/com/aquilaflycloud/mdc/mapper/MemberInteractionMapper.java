package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.result.member.MemberInteractionResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

public interface MemberInteractionMapper extends BaseMapper<MemberInteraction> {
    IPage<PreCommentInfo> selectInteractionCommentPage(IPage<MemberInteraction> page, @Param(Constants.WRAPPER) Wrapper<MemberInteraction> wrapper);

    IPage<MemberInteractionResult> selectInteractionPage(IPage<MemberInteraction> page, @Param(Constants.WRAPPER) Wrapper<MemberInteraction> wrapper);
}
