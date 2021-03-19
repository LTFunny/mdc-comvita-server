package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.member.MemberInteractionPageParam;
import com.aquilaflycloud.mdc.param.member.MemberInteractionParam;
import com.aquilaflycloud.mdc.result.member.MemberInteractionResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * MemberInteractionService
 *
 * @author star
 * @date 2021/3/18
 */
public interface MemberInteractionService {
    /**
     * 获取互动次数
     * @param param 互动入参
     * @return 互动次数
     */
    Long getInteractionNum(MemberInteractionParam param);

    /**
     * 记录新增或取消互动
     * @param param 互动入参
     * @return 互动次数
     */
    Long toggleInteractionNum(MemberInteractionParam param);

    IPage<MemberInteractionResult> pageMemberInteraction(MemberInteractionPageParam param);
}

