package com.aquilaflycloud.mdc.service;

import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.template.WxMaPubTemplateTitleListResult;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramMessage;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * WechatMiniProgramSubscribeMessageService
 *
 * @author star
 * @date 2020-03-03
 */
public interface WechatMiniProgramSubscribeMessageService {
    List<WxMaSubscribeService.CategoryData> listCategory(WechatAuthorSiteGetParam param);

    IPage<WxMaPubTemplateTitleListResult.TemplateItem> pagePubTemplateTitle(MiniPubTemplateTitlePageParam param);

    List<WxMaSubscribeService.PubTemplateKeyword> listPubTemplateKeyword(MiniPubTemplateKeywordGetParam param);

    BaseResult<String> addTemplate(MiniTemplateAddParam param);

    List<WxMaSubscribeService.TemplateInfo> listTemplate(WechatAuthorSiteGetParam param);

    void deleteTemplate(MiniTemplateGetParam param);

    void saveMessage(MiniMessageSaveParam param);

    void editMessage(MiniMessageEditParam param);

    void toggleMessage(MiniMessageGetParam param);

    WechatMiniProgramMessage getMessage(MiniMessageGetParam param);

    List<WechatMiniProgramMessage> listMessage(MiniMessageListParam param);

    List<String> listTmplId(MiniMessageTmplIdListParam param);

    void sendMiniMessage(List<MiniMemberInfo> miniMemberInfos, MiniMessageTypeEnum messageType, Long id, String... contents);
}

