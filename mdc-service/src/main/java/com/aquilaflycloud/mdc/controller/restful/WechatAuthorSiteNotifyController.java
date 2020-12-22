package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.wechat.SiteStateEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.open.bean.message.WxOpenXmlMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * WechatAuthorSiteNotifyController
 *
 * @author star
 * @date 2019-10-09
 */
@RestController
@Slf4j
public class WechatAuthorSiteNotifyController {

    @Resource
    private WechatOpenPlatformService wechatOpenPlatformService;

    @RequestMapping("receiveTicket")
    public Object receiveTicket(@RequestBody(required = false) String requestBody, @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce, @RequestParam("signature") String signature,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                signature, encType, msgSignature, timestamp, nonce, requestBody);
        String out = "";
        if (!StrUtil.equalsIgnoreCase("aes", encType)
                || !wechatOpenPlatformService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        // aes加密的消息
        WxOpenXmlMessage inMessage;
        try {
            inMessage = WxOpenXmlMessage.fromEncryptedXml(requestBody,
                    wechatOpenPlatformService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return out;
        }
        log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        try {
            out = wechatOpenPlatformService.getWxOpenComponentService().route(inMessage);
            log.debug("\n组装回复信息：{}", out);
        } catch (WxErrorException e) {
            log.error("receive_ticket", e);
        }
        return out;
    }

    @RequestMapping("authorSiteEvent/{appId}")
    public Object authorSiteEvent(@RequestBody(required = false) String requestBody, @PathVariable("appId") String appId,
                                  @RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                                  @RequestParam("nonce") String nonce, @RequestParam("encrypt_type") String encType, @RequestParam("msg_signature") String msgSignature) {
        log.info("\n接收微信请求：[appId=[{}], signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                appId, signature, encType, msgSignature, timestamp, nonce, requestBody);
        if (!StrUtil.equalsIgnoreCase("aes", encType)
                || !wechatOpenPlatformService.getWxOpenComponentService().checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        String out = "";
        // aes加密的消息
        WxMpXmlMessage inMessage;
        try {
            inMessage = WxOpenXmlMessage.fromEncryptedMpXml(requestBody,
                    wechatOpenPlatformService.getWxOpenConfigStorage(), timestamp, nonce, msgSignature);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return out;
        }
        log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
        // 全网发布测试用例
        if (StrUtil.equalsAnyIgnoreCase(appId, "wxd101a85aa106f53e", "wx570bc396a51b8ff8")) {
            try {
                if (StrUtil.equals(inMessage.getMsgType(), "text")) {
                    if (StrUtil.equals(inMessage.getContent(), "TESTCOMPONENT_MSG_TYPE_TEXT")) {
                        out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(
                                WxMpXmlOutMessage.TEXT().content("TESTCOMPONENT_MSG_TYPE_TEXT_callback")
                                        .fromUser(inMessage.getToUser())
                                        .toUser(inMessage.getFromUser())
                                        .build(),
                                wechatOpenPlatformService.getWxOpenConfigStorage()
                        );
                    } else if (StrUtil.startWith(inMessage.getContent(), "QUERY_AUTH_CODE:")) {
                        String msg = inMessage.getContent().replace("QUERY_AUTH_CODE:", "") + "_from_api";
                        WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT().content(msg).toUser(inMessage.getFromUser()).build();
                        wechatOpenPlatformService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService().sendKefuMessage(kefuMessage);
                    }
                } else if (StrUtil.equals(inMessage.getMsgType(), "event")) {
                    WxMpKefuMessage kefuMessage = WxMpKefuMessage.TEXT().content(inMessage.getEvent() + "from_callback").toUser(inMessage.getFromUser()).build();
                    wechatOpenPlatformService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getKefuService().sendKefuMessage(kefuMessage);
                }
            } catch (WxErrorException e) {
                log.error("callback", e);
            }
        } else {
            try {
                WechatAuthorSite authorSite = wechatOpenPlatformService.getWechatAuthorSiteByAppId(appId);
                if (authorSite == null || authorSite.getState() == SiteStateEnum.CANCELAUTHORIZED) {
                    log.info("{}授权号不存在或已取消授权", appId);
                    return out;
                }
            } catch (Exception e) {
                log.error("授权号异常: {}", e.getMessage());
                return out;
            }
            WxMpXmlOutMessage outMessage = wechatOpenPlatformService.route(inMessage, new HashMap<>(), appId);
            if (outMessage != null) {
                out = WxOpenXmlMessage.wxMpOutXmlMessageToEncryptedXml(outMessage, wechatOpenPlatformService.getWxOpenConfigStorage());
            }
        }
        return out;
    }

    @RequestMapping("refreshToken")
    public Object refreshToken(@RequestBody String requestBody) {
        log.info("\n接收刷新微信第三方平台号和授权号请求：[requestBody=[\n{}\n] ", requestBody);
        int count;
        if (!JSONUtil.isJson(requestBody)) {
            return "";
        }
        if (!JSONUtil.parseObj(requestBody).containsKey("authorSites")) {
            JSONObject object = JSONUtil.parseObj(requestBody);
            wechatOpenPlatformService.getWxOpenConfigStorage().updateComponentAccessToken(object.getStr("componentAccessToken"), 7500);
            count = wechatOpenPlatformService.saveComponentAccessToken(false);
        } else {
            JSONObject jsonObject = JSONUtil.parseObj(requestBody);
            JSONArray array = jsonObject.getJSONArray("authorSites");
            for (JSONObject object : array.jsonIter()) {
                wechatOpenPlatformService.getWxOpenConfigStorage().updateAuthorizerAccessToken(object.getStr("appId"), object.getStr("accessToken"), 7500);
                wechatOpenPlatformService.getWxOpenConfigStorage().updateJsapiTicket(object.getStr("appId"), object.getStr("jsapiTicket"), 7500);
            }
            count = wechatOpenPlatformService.saveAuthorSite(false);
        }
        if (count > 0) {
            return "success";
        }
        return "";
    }
}
