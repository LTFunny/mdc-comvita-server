package com.aquilaflycloud.mdc.extra.alipay.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.msg.AlipayMsgClient;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayOpenPlatformService;
import com.aquilaflycloud.mdc.model.alipay.AlipayOpenPlatform;
import com.aquilaflycloud.mdc.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class AlipayMsgHandler {
    @Resource
    private MemberService memberService;
    @Resource
    private AlipayOpenPlatformService alipayOpenPlatformService;

    public void initMsgHandler(AlipayOpenPlatform alipayOpenPlatform) throws Exception {
        if (StrUtil.isNotBlank(alipayOpenPlatform.getServerHost())) {
            final AlipayMsgClient alipayMsgClient = AlipayMsgClient.getInstance(alipayOpenPlatform.getComponentAppid());
            alipayMsgClient.setConnector(alipayOpenPlatform.getServerHost());
            alipayMsgClient.setSecurityConfig(alipayOpenPlatform.getSignType(), alipayOpenPlatform.getComponentPrivateKey(), alipayOpenPlatform.getComponentPublicKey());
            alipayMsgClient.setMessageHandler((msgApi, msgId, bizContent) -> {
                log.info("receive message. msgApi:" + msgApi + " msgId:" + msgId + " bizContent:" + bizContent);
                JSONObject result = JSONUtil.parseObj(bizContent);
                if ("alipay.open.auth.appauth.cancelled".equals(msgApi)) {
                    //第三方应用授权取消消息
                    alipayOpenPlatformService.deleteAlipayAuthorSite(result.getStr("auth_app_id"));
                } else if ("alipay.open.auth.userauth.cancelled".equals(msgApi)) {
                    //支付宝用户取消授权
                    memberService.editUnShareMember(result.getStr("user_id"), result.getStr("app_id"));
                }
            });
            alipayMsgClient.connect();
            log.info("支付宝消息通信WebSocket连接成功");
        }
    }
}
