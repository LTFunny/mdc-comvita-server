package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.member.MemberFace;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.param.system.ClientFileUploadParam;
import com.aquilaflycloud.mdc.result.member.MemberOtherResult;
import com.aquilaflycloud.mdc.service.MemberChannelService;
import com.aquilaflycloud.mdc.service.MemberService;
import com.aquilaflycloud.mdc.service.SystemFileService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberApi
 *
 * @author star
 * @date 2019-09-20
 */
@RestController
@Api(tags = "会员相关接口")
public class MemberApi {
    @Resource
    private MemberService memberService;
    @Resource
    private MemberChannelService memberChannelService;
    @Resource
    private SystemFileService systemFileService;

    @ApiOperation(value = "获取会员信息", notes = "根据会员码获取会员信息")
    @ApiMapping(value = "mdc.member.info.get", method = RequestMethod.POST)
    public MemberInfo getMemberInfo(MemberInfoGetParam param) {
        return memberService.getMemberInfo(param);
    }

    @ApiOperation(value = "查询当前登录会员信息", notes = "查询当前登录会员信息")
    @ApiMapping(value = "mdc.member.current.get", method = RequestMethod.POST)
    public MemberInfo getCurrentMember() {
        return MdcUtil.getRequireCurrentMember();
    }

    @ApiOperation(value = "查询登录会员其它信息", notes = "查询登录会员其它信息")
    @ApiMapping(value = "mdc.member.other.get", method = RequestMethod.POST)
    public MemberOtherResult getMemberOther(MemberOtherGetParam param) {
        return memberService.getMemberOther(param);
    }

    @ApiOperation(value = "保存会员额外信息", notes = "保存会员额外信息")
    @ApiMapping(value = "mdc.member.extra.save", method = RequestMethod.POST)
    public void saveMemberExtra(MemberExtraSaveParam param) {
        memberService.saveMemberExtra(param);
    }

    @ApiOperation(value = "会员注册", notes = "注册成功返回memberSession")
    @ApiMapping(value = "mdc.member.info.register", method = RequestMethod.POST)
    public BaseResult<String> register(MemberRegisterParam param) {
        return memberService.register(param);
    }

    @ApiOperation(value = "会员登录", notes = "登录成功返回memberSession")
    @ApiMapping(value = "mdc.member.info.login", method = RequestMethod.POST)
    public BaseResult<String> login(MemberLoginParam param) {
        return memberService.login(param);
    }

    @ApiOperation(value = "会员更新信息", notes = "会员更新信息")
    @ApiMapping(value = "mdc.member.info.edit", method = RequestMethod.POST)
    public void edit(MemberEditParam param) {
        memberService.edit(param);
    }

    @ApiOperation(value = "会员更新手机", notes = "会员更新手机")
    @ApiMapping(value = "mdc.member.phone.edit", method = RequestMethod.POST)
    public void editPhone(MemberPhoneEditParam param) {
        memberService.editPhone(param);
    }

    @ApiOperation(value = "会员上传自拍人脸", notes = "会员上传自拍人脸")
    @ApiMapping(value = "mdc.member.face.add", method = RequestMethod.POST)
    public MemberFace addFace(MemberFaceAddParam param) {
        return memberService.addFace(param);
    }

    @ApiOperation(value = "会员关联渠道", notes = "会员关联渠道")
    @ApiMapping(value = "mdc.member.channelRel.add", method = RequestMethod.POST)
    public void addChannelRel(RegisterChannelGetParam param) {
        memberChannelService.addChannelRel(param);
    }

    @ApiOperation(value = "小程序文件上传", notes = "小程序文件上传,返回文件路径")
    @ApiMapping(value = "mdc.member.file.upload", method = RequestMethod.POST)
    public BaseResult<String> uploadFile(ClientFileUploadParam param) {
        return systemFileService.uploadFile(param);
    }
}
