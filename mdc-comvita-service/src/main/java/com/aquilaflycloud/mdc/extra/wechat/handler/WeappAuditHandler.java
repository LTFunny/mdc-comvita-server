package com.aquilaflycloud.mdc.extra.wechat.handler;

import com.aquilaflycloud.mdc.enums.wechat.CodeAuditStateEnum;
import com.aquilaflycloud.mdc.service.WechatMiniProgramCodeService;
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
public class WeappAuditHandler implements WxMpMessageHandler {

    @Resource
    private WechatMiniProgramCodeService wechatMiniProgramCodeService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        Long time = wxMessage.getSuccTime();
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        log.info("小程序代码审核通知: " + appId);
        CodeAuditStateEnum status;
        String reason = null, screenShot = null;
        if (time != null) {
            status = CodeAuditStateEnum.SUCCESS;
        } else {
            time = wxMessage.getFailTime();
            reason = wxMessage.getReason();
            screenShot = (String) wxMessage.getAllFieldsMap().get("ScreenShot");
            status = CodeAuditStateEnum.FAILED;
        }
        time = time.toString().length() <= 10 ? wxMessage.getCreateTime() * 1000 : wxMessage.getCreateTime();
        wechatMiniProgramCodeService.updateAudit(appId, time, reason, screenShot, status);
        return null;
    }
}
