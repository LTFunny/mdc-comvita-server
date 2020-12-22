package com.aquilaflycloud.mdc.extra.wechat.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenComponentServiceImpl;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;

@Slf4j
public class WechatOpenComponentService extends WxOpenComponentServiceImpl {

    WechatOpenComponentService(WxOpenService wxOpenService) {
        super(wxOpenService);
    }

    @Override
    public String route(final WxOpenXmlMessage wxMessage) throws WxErrorException {
        log.info("====第三方平台事件===" + JSONUtil.toJsonStr(wxMessage));
        if (wxMessage == null) {
            throw new NullPointerException("message is empty");
        }
        if (StrUtil.equalsIgnoreCase(wxMessage.getInfoType(), "component_verify_ticket")) {
            getWxOpenConfigStorage().setComponentVerifyTicket(wxMessage.getComponentVerifyTicket());
            ((WechatOpenPlatformService) getWxOpenService()).updateComponentVerifyTicket(wxMessage.getComponentVerifyTicket());
            return "success";
        }
        //新增、更新授权
        if (StrUtil.equalsAnyIgnoreCase(wxMessage.getInfoType(), "authorized", "updateauthorized")) {
            // 全网发布测试代码
            if (StrUtil.equalsAnyIgnoreCase(wxMessage.getAuthorizerAppid(), "wxd101a85aa106f53e", "wx570bc396a51b8ff8")) {
                ((WechatOpenPlatformService) getWxOpenService()).saveWeChatAuthorSite(wxMessage.getPreAuthCode(), wxMessage.getAuthorizationCode(), true);
            } else {
                WxOpenQueryAuthResult queryAuth = getWxOpenService().getWxOpenComponentService().getQueryAuth(wxMessage.getAuthorizationCode());
                if (queryAuth == null || queryAuth.getAuthorizationInfo() == null || queryAuth.getAuthorizationInfo().getAuthorizerAppid() == null) {
                    throw new NullPointerException("getQueryAuth");
                }
            }
            return "success";
        }
        //取消授权
        if (StrUtil.equalsAnyIgnoreCase(wxMessage.getInfoType(), "unauthorized")) {
            ((WechatOpenPlatformService) getWxOpenService()).deleteWeChatAuthorSite(wxMessage.getAuthorizerAppid());
            return "success";
        }
        //快速创建小程序
        if (StrUtil.equalsIgnoreCase(wxMessage.getInfoType(), "notify_third_fasteregister") && wxMessage.getStatus() == 0) {
            WxOpenQueryAuthResult queryAuth = getWxOpenService().getWxOpenComponentService().getQueryAuth(wxMessage.getAuthCode());
            if (queryAuth == null || queryAuth.getAuthorizationInfo() == null || queryAuth.getAuthorizationInfo().getAuthorizerAppid() == null) {
                throw new NullPointerException("getQueryAuth");
            }
            return "success";
        }
        return "";
    }
}
