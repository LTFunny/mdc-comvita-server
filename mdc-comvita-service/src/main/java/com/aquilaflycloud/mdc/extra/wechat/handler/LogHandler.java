package com.aquilaflycloud.mdc.extra.wechat.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteEventLogMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSiteEventLog;
import com.aquilaflycloud.mdc.model.wechat.WechatPublicEventLog;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author star
 */
@Component
@Slf4j
public class LogHandler implements WxMpMessageHandler {

    /*@Resource
    private IMembershipRpcServices membershipRpcServices;*/

    @Resource
    private WechatOpenPlatformService wechatOpenPlatformService;

    @Resource
    private WechatAuthorSiteEventLogMapper wechatAuthorSiteEventLogMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String json = JSONUtil.toJsonStr(wxMessage);
        WechatAuthorSite authorSite = wechatOpenPlatformService.getWechatAuthorSiteByAppId(wxMpService.getWxMpConfigStorage().getAppId());
        if (authorSite != null) {
            String event = wxMessage.getEvent();
            WechatAuthorSiteEventLog eventLog = new WechatAuthorSiteEventLog();
            eventLog.setAppId(authorSite.getAppId());
            eventLog.setTenantId(authorSite.getTenantId());
            eventLog.setSubTenantId(authorSite.getSubTenantId());
            eventLog.setEvent(wxMessage.getEvent());
            eventLog.setMsgType(wxMessage.getMsgType());
            eventLog.setOpenId(wxMessage.getFromUser());
            long eventTime = wxMessage.getCreateTime().toString().length() <= 10 ? wxMessage.getCreateTime() * 1000 : wxMessage.getCreateTime();
            eventLog.setEventTime(new DateTime(eventTime));
            eventLog.setEventContent(json);
            wechatAuthorSiteEventLogMapper.normalInsert(eventLog);
            if ("subscribe".equals(event) || "unsubscribe".equals(event) || "SCAN".equals(event)
                    || "LOCATION".equals(event) || "CLICK".equals(event) || "VIEW".equals(event)
                    || "view_miniprogram".equals(event) || StrUtil.isBlank(event)) {
                log.info("===============记录会员公众号互动===================" + authorSite.getAppId());
                log.info("===============AppId===================" + authorSite.getAppId());
                log.info("===============TenantId==============" + authorSite.getTenantId());
                if (StrUtil.isNotBlank(authorSite.getAppId())) {
                    WechatPublicEventLog publicEventLog = new WechatPublicEventLog();
                    publicEventLog.setAppId(authorSite.getAppId());
                    publicEventLog.setTenantId(authorSite.getTenantId());
                    publicEventLog.setSubTenantId(authorSite.getSubTenantId());
                    publicEventLog.setEvent(event);
                    publicEventLog.setEventKey(wxMessage.getEventKey());
                    publicEventLog.setMsgType(wxMessage.getMsgType());
                    publicEventLog.setMsgContent(wxMessage.getContent());
                    publicEventLog.setOpenId(wxMessage.getFromUser());
                    publicEventLog.setEventContent(json);
                    publicEventLog.setEventTime(new DateTime(eventTime));
                    //membershipRpcServices.addEvent(eventModel);
                }
            }
        }
        return null;
    }

}
