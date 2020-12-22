package com.aquilaflycloud.mdc.controller;

import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.template.WxMaPubTemplateTitleListResult;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramMessage;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * WechatMiniProgramSubscribeMessageController
 *
 * @author star
 * @date 2020-03-03
 */
@Api(tags = "微信小程序订阅消息接口")
@RestController
public class WechatMiniProgramSubscribeMessageController {

    @Resource
    private WechatMiniProgramSubscribeMessageService wechatMiniProgramSubscribeMessageService;

    @ApiOperation(value = "获取当前帐号所设置的类目信息", notes = "获取小程序帐号当前所设置的类目")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.newTmplCategory.list", method = RequestMethod.POST, permission = true)
    public List<WxMaSubscribeService.CategoryData> listCategory(WechatAuthorSiteGetParam param) {
        return wechatMiniProgramSubscribeMessageService.listCategory(param);
    }

    @ApiOperation(value = "获取模板标题列表", notes = "获取小程序订阅消息的模板库标题列表")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.pubTemplateTitle.page", method = RequestMethod.POST, permission = true)
    public IPage<WxMaPubTemplateTitleListResult.TemplateItem> pagePubTemplateTitle(MiniPubTemplateTitlePageParam param) {
        return wechatMiniProgramSubscribeMessageService.pagePubTemplateTitle(param);
    }

    @ApiOperation(value = "获取模板标题下的关键词库", notes = "获取小程序订阅消息模板库中某个模板标题下关键词库")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.pubTemplateKeyword.list", method = RequestMethod.POST, permission = true)
    public List<WxMaSubscribeService.PubTemplateKeyword> listPubTemplateKeyword(MiniPubTemplateKeywordGetParam param) {
        return wechatMiniProgramSubscribeMessageService.listPubTemplateKeyword(param);
    }

    @ApiOperation(value = "组合模板并添加到个人模板库", notes = "组合模板并添加至帐号下的个人模板库，得到用于发消息的模板")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.template.add", method = RequestMethod.POST, permission = true)
    public BaseResult<String> addTemplate(MiniTemplateAddParam param) {
        return wechatMiniProgramSubscribeMessageService.addTemplate(param);
    }

    @ApiOperation(value = "获取帐号下的模板列表", notes = "获取小程序帐号下的个人模板库中已经存在的模板列表")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.template.list", method = RequestMethod.POST, permission = true)
    public List<WxMaSubscribeService.TemplateInfo> listTemplate(WechatAuthorSiteGetParam param) {
        return wechatMiniProgramSubscribeMessageService.listTemplate(param);
    }

    @ApiOperation(value = "删除帐号下的某个模板", notes = "删除帐号下的个人模板")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.template.delete", method = RequestMethod.POST, permission = true)
    public void deleteTemplate(MiniTemplateGetParam param) {
        wechatMiniProgramSubscribeMessageService.deleteTemplate(param);
    }

    @ApiOperation(value = "保存消息模板", notes = "保存消息模板")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.message.save", method = RequestMethod.POST, permission = true)
    public void saveMessage(MiniMessageSaveParam param) {
        wechatMiniProgramSubscribeMessageService.saveMessage(param);
    }

    @ApiOperation(value = "编辑消息模板", notes = "编辑消息模板")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.message.edit", method = RequestMethod.POST, permission = true)
    public void editMessage(MiniMessageEditParam param) {
        wechatMiniProgramSubscribeMessageService.editMessage(param);
    }

    @ApiOperation(value = "启用/停用消息模板", notes = "启用/停用消息模板")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.message.toggle", method = RequestMethod.POST, permission = true)
    public void toggleMessage(MiniMessageGetParam param) {
        wechatMiniProgramSubscribeMessageService.toggleMessage(param);
    }

    @ApiOperation(value = "获取消息模板", notes = "获取消息模板")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.message.get", method = RequestMethod.POST, permission = true)
    public WechatMiniProgramMessage getMessage(MiniMessageGetParam param) {
        return wechatMiniProgramSubscribeMessageService.getMessage(param);
    }

    @ApiOperation(value = "获取消息模板id列表", notes = "获取消息模板id列表")
    @PreAuthorize("hasAuthority('mdc:wechat:subscribeMessage')")
    @ApiMapping(value = "backend.mdc.wechat.message.list", method = RequestMethod.POST, permission = true)
    public List<WechatMiniProgramMessage> listMessage(MiniMessageListParam param) {
        return wechatMiniProgramSubscribeMessageService.listMessage(param);
    }
}
