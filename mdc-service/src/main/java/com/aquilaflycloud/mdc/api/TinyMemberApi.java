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
 * TinyMemberApi
 *
 * @author star
 * @date 2020-04-07
 */
@RestController
@Api(tags = "支付宝小程序会员相关接口")
public class TinyMemberApi {
    @Resource
    private MemberService memberService;

    @ApiOperation(value = "小程序会员登录", notes = "登录成功返回memberSession")
    @ApiMapping(value = "mdc.tiny.member.login", method = RequestMethod.POST)
    public BaseResult<String> loginTinyMember(TinyMemberLoginParam param) {
        return memberService.loginTinyMember(param);
    }

    @ApiOperation(value = "小程序会员授权", notes = "小程序会员授权后更新信息")
    @ApiMapping(value = "mdc.tiny.member.authorize", method = RequestMethod.POST)
    public void authorizeTinyMember(TinyMemberAuthorizeParam param) {
        memberService.authorizeTinyMember(param);
    }

    @ApiOperation(value = "小程序运动步数获取", notes = "小程序会员授权后获取运动步数")
    @ApiMapping(value = "mdc.tiny.run.get", method = RequestMethod.POST)
    public BaseResult<String> getTinyRun(TinyEncryptionGetParam param) {
        return memberService.getTinyRun(param);
    }

    @ApiOperation(value = "小程序手机号获取", notes = "小程序会员授权后获取手机号")
    @ApiMapping(value = "mdc.tiny.phone.get", method = RequestMethod.POST)
    public BaseResult<String> getTinyPhone(TinyEncryptionGetParam param) {
        return memberService.getTinyPhone(param);
    }

    @ApiOperation(value = "小程序手机号获取并修改", notes = "小程序手机号获取并修改")
    @ApiMapping(value = "mdc.tiny.phone.edit", method = RequestMethod.POST)
    public BaseResult<String> editTinyPhone(TinyEncryptionGetParam param) {
        return memberService.editTinyPhone(param);
    }
}
