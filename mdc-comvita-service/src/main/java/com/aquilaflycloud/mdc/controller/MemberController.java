package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.wechat.WechatFansInfo;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.MemberDetailResult;
import com.aquilaflycloud.mdc.service.MemberService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberController
 *
 * @author star
 * @date 2019-09-20
 */
@RestController
@Api(tags = "会员管理")
public class MemberController {

    @Resource
    private MemberService memberService;

    @ApiOperation(value = "查询会员列表(分页)", notes = "根据条件查询会员列表(分页)")
    @PreAuthorize("hasAuthority('mdc:member:list')")
    @ApiMapping(value = "backend.comvita.member.info.page", method = RequestMethod.POST, permission = true)
    public IPage<MemberInfo> pageMemberInfo(MemberPageParam param) {
        return memberService.pageMemberInfo(param);
    }

    @ApiOperation(value = "查询会员详情", notes = "查询会员详情")
    @PreAuthorize("hasAuthority('mdc:member:get')")
    @ApiMapping(value = "backend.comvita.member.info.get", method = RequestMethod.POST, permission = true)
    public MemberDetailResult getMemberDetailInfo(MemberGetParam param) {
        return memberService.getMemberDetailInfo(param);
    }

    @ApiOperation(value = "新增会员信息", notes = "新增会员信息")
    @PreAuthorize("hasAuthority('mdc:member:add')")
    @ApiMapping(value = "backend.comvita.member.info.add", method = RequestMethod.POST, permission = true)
    public void addMemberInfo(MemberAddParam param) {
        memberService.addMemberInfo(param);
    }

    @ApiOperation(value = "查询公众号粉丝列表(分页)", notes = "根据条件查询公众号粉丝列表(分页)")
    @PreAuthorize("hasAuthority('mdc:member:list')")
    @ApiMapping(value = "backend.comvita.member.wechatFans.page", method = RequestMethod.POST, permission = true)
    public IPage<WechatFansInfo> pageWechatFans(WechatFansPageParam param) {
        return memberService.pageWechatFans(param);
    }

    @ApiOperation(value = "批量导入会员信息", notes = "批量导入会员信息")
    @PreAuthorize("hasAuthority('mdc:member:batchAdd')")
    @ApiMapping(value = "backend.comvita.member.info.batchAdd", method = RequestMethod.POST, permission = true)
    public void batchAddMemberInfo(MemberBatchAddParam param) {
        memberService.batchAddMemberInfo(param);
    }
}
