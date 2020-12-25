package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramCode;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniAccountResult;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniProgramPageResult;
import com.aquilaflycloud.mdc.service.WechatMiniProgramCodeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.open.bean.ma.WxOpenMaCategory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * WechatMiniProgramCodeController
 *
 * @author star
 * @date 2019-10-09
 */
@Api(tags = "微信小程序代码接口")
@RestController
public class WechatMiniProgramCodeController {

    @Resource
    private WechatMiniProgramCodeService wechatMiniProgramCodeService;

    @ApiOperation(value = "查询小程序代码列表(分页)", notes = "查询小程序代码列表(分页)")
    @PreAuthorize("hasAuthority('mdc:wechat:codeList')")
    @ApiMapping(value = "backend.comvita.wechat.code.page", method = RequestMethod.POST, permission = true)
    public IPage<WechatMiniProgramCode> pageCode(CodeListParam param) {
        return wechatMiniProgramCodeService.pageCode(param);
    }

    @ApiOperation(value = "查询小程序账号列表(分页)", notes = "查询小程序账号列表(分页)")
    @PreAuthorize("hasAuthority('mdc:wechat:codeList')")
    @ApiMapping(value = "backend.comvita.wechat.codeAccount.page", method = RequestMethod.POST, permission = true)
    public IPage<WechatMiniAccountResult> pageAccount(CodeAccountListParam param) {
        return wechatMiniProgramCodeService.pageAccount(param);
    }

    @ApiOperation(value = "获取体验版代码", notes = "获取体验版代码")
    @PreAuthorize("hasAuthority('mdc:wechat:codeList')")
    @ApiMapping(value = "backend.comvita.wechat.miniDevCode.get", method = RequestMethod.POST, permission = true)
    public WechatMiniProgramCode getDevCode(CodeGetParam param) {
        return wechatMiniProgramCodeService.getDevCode(param);
    }

    @ApiOperation(value = "获取审核版代码", notes = "获取审核版代码")
    @PreAuthorize("hasAuthority('mdc:wechat:codeList')")
    @ApiMapping(value = "backend.comvita.wechat.miniAuditCode.get", method = RequestMethod.POST, permission = true)
    public WechatMiniProgramCode getAuditCode(CodeGetParam param) {
        return wechatMiniProgramCodeService.getAuditCode(param);
    }

    @ApiOperation(value = "获取线上版代码", notes = "获取线上版代码")
    @PreAuthorize("hasAuthority('mdc:wechat:codeList')")
    @ApiMapping(value = "backend.comvita.wechat.miniProdCode.get", method = RequestMethod.POST, permission = true)
    public WechatMiniProgramCode getProdCode(CodeGetParam param) {
        return wechatMiniProgramCodeService.getProdCode(param);
    }

    @ApiOperation(value = "提交代码", notes = "根据模板提交代码")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.code.commit", method = RequestMethod.POST, permission = true)
    public void commit(CodeCommitParam param) {
        wechatMiniProgramCodeService.commit(param);
    }

    @ApiOperation(value = "创建代码", notes = "创建代码")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.code.add", method = RequestMethod.POST, permission = true)
    public void add(CodeAddParam param) {
        wechatMiniProgramCodeService.add(param);
    }

    @ApiOperation(value = "获取小程序体验二维码", notes = "获取小程序体验二维码")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.codeQrcode.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getQrcode(CodeQrCodeGetParam param) {
        return new BaseResult<String>().setResult(wechatMiniProgramCodeService.getQrcode(param));
    }

    @ApiOperation(value = "获取小程序可选类目", notes = "获取小程序可选类目")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.codeCategory.get", method = RequestMethod.POST, permission = true)
    public List<WxOpenMaCategory> getCategory(CodeGetParam param) {
        return wechatMiniProgramCodeService.getCategory(param);
    }

    @ApiOperation(value = "获取小程序可选页面路径", notes = "获取小程序可选页面路径")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.codePage.get", method = RequestMethod.POST, permission = true)
    public WechatMiniProgramPageResult getPage(CodeGetParam param) {
        return wechatMiniProgramCodeService.getPage(param);
    }

    @ApiOperation(value = "代码提交审核", notes = "代码提交审核")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.code.submit", method = RequestMethod.POST, permission = true)
    public void submit(CodeSubmitParam param) {
        wechatMiniProgramCodeService.submit(param);
    }

    @ApiOperation(value = "代码审核撤回", notes = "代码审核撤回")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.code.undo", method = RequestMethod.POST, permission = true)
    public void undo(CodeGetParam param) {
        wechatMiniProgramCodeService.undo(param);
    }

    @ApiOperation(value = "代码更新审核状态", notes = "代码更新审核状态")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.codeAudit.update", method = RequestMethod.POST, permission = true)
    public void updateAudit(CodeAuditUpdateParam param) {
        wechatMiniProgramCodeService.updateAudit(param);
    }

    @ApiOperation(value = "发布审核通过代码", notes = "发布审核通过代码")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.code.release", method = RequestMethod.POST, permission = true)
    public void release(CodeGetParam param) {
        wechatMiniProgramCodeService.release(param);
    }

    @ApiOperation(value = "代码回退", notes = "代码回退")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.code.revert", method = RequestMethod.POST, permission = true)
    public void revert(CodeGetParam param) {
        wechatMiniProgramCodeService.revert(param);
    }

    @ApiOperation(value = "设置线上版是否可访问", notes = "设置线上版是否可访问")
    @PreAuthorize("hasAuthority('mdc:wechat:codeSave')")
    @ApiMapping(value = "backend.comvita.wechat.codeVisit.change", method = RequestMethod.POST, permission = true)
    public void changeVisit(CodeVisitChangeParam param) {
        wechatMiniProgramCodeService.changeVisit(param);
    }
}
