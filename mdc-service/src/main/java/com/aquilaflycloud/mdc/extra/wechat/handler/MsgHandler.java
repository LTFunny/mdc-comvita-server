package com.aquilaflycloud.mdc.extra.wechat.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfOnlineList;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author star
 */
@Component
@Slf4j
public class MsgHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        switch (wxMessage.getMsgType()) {
            case WxConsts.XmlMsgType.TEXT: {
                log.info("文本消息content: " + wxMessage.getContent());
                break;
            }
            case WxConsts.XmlMsgType.IMAGE: {
                String mediaId = wxMessage.getMediaId();
                String picUrl = wxMessage.getPicUrl();
                log.info("图片消息媒体mediaId: " + mediaId);
                log.info("图片路径picUrl: " + picUrl);
                break;
            }
            case WxConsts.XmlMsgType.VOICE: {
                String mediaId = wxMessage.getMediaId();
                String voiceFormat = wxMessage.getFormat();
                String recognition = wxMessage.getRecognition();
                log.info("语音格式format: " + voiceFormat);
                log.info("语音消息媒体mediaId: " + mediaId);
                log.info("语音识别消息recognition: " + recognition);
                break;
            }
            case WxConsts.XmlMsgType.VIDEO: {
                String mediaId = wxMessage.getMediaId();
                String thumbMediaId = wxMessage.getThumbMediaId();
                log.info("视频消息媒体mediaId: " + mediaId);
                log.info("视频缩略图媒体thumbMediaId: " + thumbMediaId);
                break;
            }
            case WxConsts.XmlMsgType.LOCATION: {
                String locationX = wxMessage.getLocationX().toString();
                String locationY = wxMessage.getLocationY().toString();
                String label = wxMessage.getLabel();
                String scale = wxMessage.getScale().toString();
                log.info("地理位置信息label: " + label);
                log.info("地图缩放大小scale: " + scale);
                log.info("地理位置维度locationX: " + locationX);
                log.info("地理位置经度locationY: " + locationY);
                break;
            }
            case WxConsts.XmlMsgType.LINK: {
                String title = wxMessage.getTitle();
                String linkUrl = wxMessage.getUrl();
                String description = wxMessage.getDescription();
                log.info("链接消息标题title: " + title);
                log.info("链接消息地址url: " + linkUrl);
                log.info("链接消息描述description: " + description);
                break;
            }
            default:
        }

        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
        /*if (StrUtil.startsWithAny(wxMessage.getContent(), "你好", "客服")
                && hasKefuOnline(wxMpService)) {
            return WxMpXmlOutMessage
                    .TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
        }

        String content = "回复信息内容";
        return WxMpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();*/
        return null;

    }

    private boolean hasKefuOnline(WxMpService wxMpService) {
        try {
            WxMpKfOnlineList kfOnlineList = wxMpService.getKefuService().kfOnlineList();
            return kfOnlineList != null && kfOnlineList.getKfOnlineList().size() > 0;
        } catch (Exception e) {
            log.error("获取客服在线状态异常: " + e.getMessage(), e);
        }

        return false;
    }

}
