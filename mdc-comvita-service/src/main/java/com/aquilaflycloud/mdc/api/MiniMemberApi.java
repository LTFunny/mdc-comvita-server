package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.service.MemberService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MiniMemberApi
 *
 * @author star
 * @date 2019-09-20
 */
@RestController
@Api(tags = "微信小程序会员相关接口")
public class MiniMemberApi {
    @Resource
    private MemberService memberService;

    @ApiOperation(value = "小程序会员登录", notes = "登录成功返回memberSession")
    @ApiMapping(value = "comvita.mini.member.login", method = RequestMethod.POST)
    public BaseResult<String> loginMiniMember(MiniMemberLoginParam param) {
        return memberService.loginMiniMember(param);
    }

    @ApiOperation(value = "小程序会员授权", notes = "小程序会员授权后更新信息")
    @ApiMapping(value = "comvita.mini.member.authorize", method = RequestMethod.POST)
    public void authorizeMiniMember(MiniMemberAuthorizeParam param) {
        memberService.authorizeMiniMember(param);
    }

    @ApiOperation(value = "小程序手机号获取", notes = "小程序会员授权后获取手机号")
    @ApiMapping(value = "comvita.mini.phone.get", method = RequestMethod.POST)
    public BaseResult<String> getMiniPhone(MiniPhoneGetParam param) {
        return memberService.getMiniPhone(param);
    }

    @ApiOperation(value = "小程序手机号获取并修改", notes = "小程序手机号获取并修改")
    @ApiMapping(value = "comvita.mini.phone.edit", method = RequestMethod.POST)
    public BaseResult<String> editMiniPhone(MiniPhoneGetParam param) {
        return memberService.editMiniPhone(param);
    }

    @ApiOperation(value = "小程序插件会员新增", notes = "登录成功返回插件会员pluginMemberId")
    @ApiMapping(value = "comvita.mini.pluginMember.add", method = RequestMethod.POST)
    public BaseResult<Long> addMiniPluginMember(MiniPluginMemberAddParam param) {
        return memberService.addMiniPluginMember(param);
    }

    @ApiOperation(value = "小程序插件会员注册", notes = "登录成功返回memberSession")
    @ApiMapping(value = "comvita.mini.pluginMember.register", method = RequestMethod.POST)
    public BaseResult<String> registerMiniPluginMember(MiniPluginMemberRegisterParam param) {
        return memberService.registerMiniPluginMember(param);
    }

    @ApiOperation(value = "小程序系统信息保存", notes = "保存小程序wx.getSystemInfoSync返回信息")
    @ApiMapping(value = "comvita.mini.device.save", method = RequestMethod.POST)
    public void saveMiniDevice(MiniDeviceSaveParam param) {
        memberService.saveMiniDevice(param);
    }
}
