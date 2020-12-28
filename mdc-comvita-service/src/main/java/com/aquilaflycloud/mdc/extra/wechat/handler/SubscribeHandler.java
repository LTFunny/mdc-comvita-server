package com.aquilaflycloud.mdc.extra.wechat.handler;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.wechat.QrcodeHandlerTypeEnum;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteQrcodeMsgMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSiteQrcodeMsg;
import com.aquilaflycloud.mdc.result.member.MemberScanResult;
import com.aquilaflycloud.mdc.result.member.MemberScanRewardResult;
import com.aquilaflycloud.mdc.result.wechat.*;
import com.aquilaflycloud.mdc.service.MemberScanService;
import com.aquilaflycloud.mdc.service.MemberService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author star
 */
@Component
@Slf4j
public class SubscribeHandler implements WxMpMessageHandler {
    @Resource
    private MemberService memberService;
    @Resource
    private MemberScanService memberScanService;
    @Resource
    private WechatAuthorSiteQrcodeMsgMapper wechatAuthorSiteQrcodeMsgMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        WxMpUser wxMpUser = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);

        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        if (wxMpUser != null) {
            try {
                memberService.addSubscribeMember(appId, wxMpUser);
            } catch (ServiceException e) {
                log.error("====新增会员 Error====" + e.getMessage(), e);
            }
        }

        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = handleSpecial(appId, wxMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return WxMpXmlOutMessage.TEXT().content("感谢关注")
                    .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(String appId, WxMpXmlMessage wxMessage) {
        String valueKey = "";
        if (StrUtil.isNotBlank(wxMessage.getEventKey())) {
            valueKey = wxMessage.getEventKey().split("_")[1];
        }
        // 如果eventKey不为空
        if (StrUtil.isNotBlank(valueKey)) {
            WechatAuthorSiteQrcodeMsg msg = wechatAuthorSiteQrcodeMsgMapper.normalSelectOne(Wrappers.<WechatAuthorSiteQrcodeMsg>lambdaQuery()
                    .eq(WechatAuthorSiteQrcodeMsg::getAppId, appId)
                    .eq(WechatAuthorSiteQrcodeMsg::getSceneString, valueKey));
            if (msg != null) {
                if (msg.getHandlerType() == QrcodeHandlerTypeEnum.SCANCONSUME) {
                    JSONObject jsonObject = JSONUtil.parseObj(msg.getHandlerContent());
                    String content;
                    try {
                        MemberScanResult result = memberScanService.addQrcodeScanConsume(valueKey, wxMessage.getFromUser(), jsonObject.getStr("miniAppId"));
                        StrBuilder reward = StrBuilder.create();
                        for (MemberScanRewardResult rewardResult : result.getRewardList()) {
                            reward.append(String.valueOf(rewardResult.getCanReward())).append(rewardResult.getRewardType().getName()).append("，");
                        }
                        String rewardStr = StrUtil.subBefore(reward.toString(), "，", true);
                        content = StrUtil.format(jsonObject.getStr("success"), rewardStr);
                    } catch (ServiceException e) {
                        content = StrUtil.format(jsonObject.getStr("failed"), e.getMessage());
                    }
                    return WxMpXmlOutMessage.TEXT().content(content)
                            .fromUser(wxMessage.getToUser())
                            .toUser(wxMessage.getFromUser())
                            .build();
                }
                switch (msg.getMsgType()) {
                    case TEXT: {
                        QrcodeMsgTextResult result = JSONUtil.toBean(msg.getMsgContent(), QrcodeMsgTextResult.class);
                        return WxMpXmlOutMessage.TEXT().content(result.getContent())
                                .fromUser(wxMessage.getToUser())
                                .toUser(wxMessage.getFromUser())
                                .build();
                    }
                    case IMAGE: {
                        QrcodeMsgImageResult result = JSONUtil.toBean(msg.getMsgContent(), QrcodeMsgImageResult.class);
                        return WxMpXmlOutMessage.IMAGE().mediaId(result.getMediaId())
                                .fromUser(wxMessage.getToUser())
                                .toUser(wxMessage.getFromUser())
                                .build();
                    }
                    case VOICE: {
                        QrcodeMsgVoiceResult result = JSONUtil.toBean(msg.getMsgContent(), QrcodeMsgVoiceResult.class);
                        return WxMpXmlOutMessage.VOICE().mediaId(result.getMediaId())
                                .fromUser(wxMessage.getToUser())
                                .toUser(wxMessage.getFromUser())
                                .build();
                    }
                    case VIDEO: {
                        QrcodeMsgVideoResult result = JSONUtil.toBean(msg.getMsgContent(), QrcodeMsgVideoResult.class);
                        return WxMpXmlOutMessage.VIDEO().mediaId(result.getMediaId())
                                .title(result.getTitle())
                                .description(result.getDescription())
                                .fromUser(wxMessage.getToUser())
                                .toUser(wxMessage.getFromUser())
                                .build();
                    }
                    case MUSIC: {
                        QrcodeMsgMusicResult result = JSONUtil.toBean(msg.getMsgContent(), QrcodeMsgMusicResult.class);
                        return WxMpXmlOutMessage.MUSIC().thumbMediaId(result.getThumbMediaId())
                                .title(result.getTitle())
                                .description(result.getDescription())
                                .musicUrl(result.getMusicUrl())
                                .hqMusicUrl(result.getHqMusicUrl())
                                .fromUser(wxMessage.getToUser())
                                .toUser(wxMessage.getFromUser())
                                .build();
                    }
                    case NEWS: {
                        List<WxMpXmlOutNewsMessage.Item> articles = new ArrayList<>();
                        List<QrcodeMsgNewsResult> news = JSONUtil.toList(JSONUtil.parseArray(msg.getMsgContent()), QrcodeMsgNewsResult.class);
                        for (QrcodeMsgNewsResult result : news) {
                            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                            item.setTitle(result.getTitle());
                            item.setDescription(result.getDescription());
                            item.setPicUrl(result.getPicUrl());
                            item.setUrl(result.getUrl());
                            articles.add(item);
                        }
                        return WxMpXmlOutMessage.NEWS().articles(articles)
                                .fromUser(wxMessage.getToUser())
                                .toUser(wxMessage.getFromUser())
                                .build();
                    }
                    default:
                }
            }
        }
        return null;
    }

}
