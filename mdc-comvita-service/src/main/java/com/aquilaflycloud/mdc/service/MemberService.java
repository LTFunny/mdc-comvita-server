package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.member.MemberFace;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.wechat.WechatFansInfo;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.MemberAttributesAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberDetailResult;
import com.aquilaflycloud.mdc.result.member.MemberOtherResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * MemberService
 *
 * @author star
 * @date 2019-10-28
 */
public interface MemberService {
    IPage<MemberInfo> pageMemberInfo(MemberPageParam param);

    MemberDetailResult getMemberDetailInfo(MemberGetParam param);

    MemberInfo getMemberInfo(MemberInfoGetParam param);

    IPage<WechatFansInfo> pageWechatFans(WechatFansPageParam param);

    MemberOtherResult getMemberOther(MemberOtherGetParam param);

    void saveMemberExtra(MemberExtraSaveParam param);

    MemberInfo getUnifiedMember(LambdaQueryWrapper<MemberInfo> wrappers, Boolean... isNormalSelect);

    BaseResult<String> register(MemberRegisterParam param);

    BaseResult<String> login(MemberLoginParam param);

    void edit(MemberEditParam param);

    void editPhone(MemberInfo member, String phoneNumber);

    void editPhone(MemberPhoneEditParam param);

    MemberFace addFace(MemberFaceAddParam param);

    BaseResult<String> loginMiniMember(MiniMemberLoginParam param);

    void authorizeMiniMember(MiniMemberAuthorizeParam param);

    void editMiniMember(MiniMemberEditParam param);

    BaseResult<String> getMiniPhone(MiniPhoneGetParam param);

    BaseResult<String> editMiniPhone(MiniPhoneGetParam param);

    void saveMiniDevice(MiniDeviceSaveParam param);

    MemberAttributesAnalysisResult memberAttributesAnalysis(MemberAttributesAnalysisParam param);

    void addMemberInfo(MemberAddParam param);

    void batchAddMemberInfo(MemberBatchAddParam param);
}

