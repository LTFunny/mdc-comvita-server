package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.aquilaflycloud.mdc.enums.alipay.AlipayOauth2ScopeEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.Oauth2ScopeEnum;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayOpenPlatformService;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.service.MemberService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * AuthorSiteOauthController
 *
 * @author star
 * @date 2019-10-08
 */
@RestController
@Slf4j
public class AuthorSiteOauthController {
    @Resource
    private MemberService memberService;
    @Resource
    private WechatOpenPlatformService wechatOpenPlatformService;
    @Resource
    private AlipayOpenPlatformService alipayOpenPlatformService;

    public static void main(String[] args) {
        System.out.println();
    }

    @RequestMapping(value = "wechatAuthorSiteOauthUrl", method = RequestMethod.GET)
    public void wechatAuthorSiteOauthUrl(@RequestParam(required = false) String encryptParam, HttpServletRequest request, HttpServletResponse response) {
        StrBuilder url = StrBuilder.create();
        String scopeType;
        String appId;
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isNotBlank(encryptParam) && !StrUtil.isNullOrUndefined(encryptParam)) {
            String paramArray = MdcUtil.getCryption(Long.valueOf(encryptParam));
            for (String param : StrUtil.split(paramArray, ";")) {
                if (StrUtil.isNotBlank(param)) {
                    String[] p = StrUtil.split(param, "=");
                    params.put(p[0], p[1]);
                }
            }
            url.append(URLUtil.decode(params.get("url"), "utf-8"));
            scopeType = params.get("scopeType");
            appId = params.get("appId");
            params.remove("url");
            Map<String, String> otherParams = getParams(request);
            otherParams.remove("encryptParam");
            params.putAll(otherParams);
        } else {
            url.append(URLUtil.decode(request.getParameter("url"), "utf-8"));
            scopeType = request.getParameter("scopeType");
            appId = request.getParameter("appId");
            params = getParams(request);
        }
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(scopeType)) {
            return;
        }
        // 真实的回调URL放入缓存中不对外暴露，也避免微信端对回调地址不支持#号与多参数的场景
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (url.toString().contains("?")) {
                url.append("&").append(entry.getKey()).append("=").append(URLUtil.encode(entry.getValue(), "utf-8"));
            } else {
                url.append("?").append(entry.getKey()).append("=").append(URLUtil.encode(entry.getValue(), "utf-8"));
            }
        }
        String paramKey = MdcUtil.getSnowflakeIdStr();
        RedisUtil.valueRedis().set(paramKey, url.toString(), 5, TimeUnit.MINUTES); // 缓存五分钟
        url.clear().append(paramKey);
        String scope = scopeType.toLowerCase();
        String redirectUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/wechatAuthorSiteOauthReturnUrl").toString();
        redirectUrl += "?url=" + url;
        String resUrl = wechatOpenPlatformService.getWxOpenComponentService().oauth2buildAuthorizationUrl(appId, redirectUrl, scope, null);
        log.info("微信授权地址:" + resUrl);
        try {
            response.sendRedirect(resUrl);
        } catch (IOException e) {
            log.error("重定向失败:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "wechatAuthorSiteOauthReturnUrl", method = RequestMethod.GET)
    public void wechatAuthorSiteOauthReturnUrl(@RequestParam String code, @RequestParam String url, HttpServletResponse response) {
        log.info("微信授权回调参数:code=" + code + ";" + "url=" + url);
        // 解密回调URL
        url = RedisUtil.<String>valueRedis().get(url);
        log.info("回调URL:" + url);
        if (StrUtil.containsAny(url, "appId=")) {
            String appId = url.split("appId=")[1].split("&")[0];
            // 获取用户openId
            try {
                WxMpOAuth2AccessToken token = wechatOpenPlatformService.getWxOpenComponentService().oauth2getAccessToken(appId, code);
                // 获取信息并添加会员
                MdcUtil.getTtlExecutorService().submit(() -> {
                    if (token.getScope().contains(Oauth2ScopeEnum.SNSAPI_USERINFO.name())) {
                        try {
                            WxMpUser wxMpUser = wechatOpenPlatformService.getWxOpenComponentService().getWxMpServiceByAppid(appId)
                                    .getOAuth2Service().getUserInfo(token, null);
                            memberService.addSubscribeMember(appId, wxMpUser);
                        } catch (WxErrorException e) {
                            e.printStackTrace();
                        }
                    } else if (token.getScope().contains(Oauth2ScopeEnum.SNSAPI_BASE.name())) {
                        //保存无用户信息会员
                        memberService.addSubscribeMember(appId, token.getOpenId(), token.getUnionId());
                    }
                });
                String openId = token.getOpenId();
                // 判断微信授权是否成功
                if (StrUtil.isNotBlank(openId)) {
                    if (url.contains("?")) {
                        url += "&openId=" + openId;
                    } else {
                        url += "?openId=" + openId;
                    }
                }
                log.info("微信授权回调:url=" + url);
                response.sendRedirect(url);
            } catch (WxErrorException e) {
                log.error("获取AccessToken失败: " + e.getError().getErrorMsg());
                e.printStackTrace();
            } catch (IOException e) {
                log.error("重定向失败:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "alipayAuthorSiteOauthUrl", method = RequestMethod.GET)
    public void alipayAuthorSiteOauthUrl(@RequestParam(required = false) String encryptParam, HttpServletRequest request, HttpServletResponse response) {
        StrBuilder url = StrBuilder.create();
        String scopeType;
        String appId;
        Map<String, String> params = new HashMap<>();
        if (StrUtil.isNotBlank(encryptParam) && !StrUtil.isNullOrUndefined(encryptParam)) {
            String paramArray = MdcUtil.getCryption(Long.valueOf(encryptParam));
            for (String param : StrUtil.split(paramArray, ";")) {
                if (StrUtil.isNotBlank(param)) {
                    String[] p = StrUtil.split(param, "=");
                    params.put(p[0], p[1]);
                }
            }
            url.append(URLUtil.decode(params.get("url"), "utf-8"));
            scopeType = params.get("scopeType");
            appId = params.get("appId");
            params.remove("url");
            Map<String, String> otherParams = getParams(request);
            otherParams.remove("encryptParam");
            params.putAll(otherParams);
        } else {
            url.append(URLUtil.decode(request.getParameter("url"), "utf-8"));
            scopeType = request.getParameter("scopeType");
            appId = request.getParameter("appId");
            params = getParams(request);
        }
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(scopeType)) {
            return;
        }
        // 真实的回调URL放入缓存中不对外暴露，也避免支付宝端对回调地址不支持#号与多参数的场景
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (url.toString().contains("?")) {
                url.append("&").append(entry.getKey()).append("=").append(URLUtil.encode(entry.getValue(), "utf-8"));
            } else {
                url.append("?").append(entry.getKey()).append("=").append(URLUtil.encode(entry.getValue(), "utf-8"));
            }
        }
        String paramKey = MdcUtil.getSnowflakeIdStr();
        RedisUtil.valueRedis().set(paramKey, url.toString(), 5, TimeUnit.MINUTES); // 缓存五分钟
        url.clear().append(paramKey);
        String scope = scopeType.toLowerCase();
        String redirectUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/alipayAuthorSiteOauthReturnUrl").toString();
        String resUrl = alipayOpenPlatformService.oauth2buildAuthorizationUrl(appId, redirectUrl, scope, url.toString());
        log.info("支付宝授权地址:" + resUrl);
        try {
            response.sendRedirect(resUrl);
        } catch (IOException e) {
            log.error("重定向失败:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "alipayAuthorSiteOauthReturnUrl", method = RequestMethod.GET)
    public void alipayAuthorSiteOauthReturnUrl(@RequestParam("auth_code") String code, @RequestParam String state,
                                               @RequestParam(name = "app_id") String appId, @RequestParam(name = "scope") String scopeType,
                                               HttpServletResponse response) {
        log.info("支付宝授权回调参数:code=" + code + ";" + "url=" + state);
        // 解密回调URL
        String url = RedisUtil.<String>valueRedis().get(state);
        log.info("回调URL:" + url);
        if (StrUtil.isNotBlank(appId)) {
            // 获取用户openId
            try {
                AlipaySystemOauthTokenResponse tokenResponse = alipayOpenPlatformService.getSystemOauthToken(appId, code);
                // 获取信息并添加会员
                if (scopeType.contains(AlipayOauth2ScopeEnum.auth_user.name())) {
                    AlipayUserInfoShareResponse shareResponse = alipayOpenPlatformService.getUserInfoShare(appId, tokenResponse.getAccessToken());
                    memberService.addShareMember(appId, shareResponse);
                } else if (scopeType.contains(AlipayOauth2ScopeEnum.auth_base.name())) {
                    //暂不保存无用户信息会员
                    //memberService.addShareMember(appId, accessToken.getOpenId(), accessToken.getUnionId());
                }
                String userId = tokenResponse.getUserId();
                // 判断支付宝授权是否成功
                if (StrUtil.isNotBlank(userId)) {
                    if (url.contains("?")) {
                        url += "&userId=" + userId;
                    } else {
                        url += "?userId=" + userId;
                    }
                }
                log.info("支付宝授权回调:url=" + url);
                response.sendRedirect(url);
            } catch (IOException e) {
                log.error("重定向失败:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }
}
