package com.aquilaflycloud.mdc.extra.alipay.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AppTokenExchangeSubElement;
import com.alipay.api.response.*;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.util.generic.models.AlipayOpenApiGenericResponse;
import com.aliyun.tea.TeaConverter;
import com.aquilaflycloud.mdc.enums.alipay.SiteSourceEnum;
import com.aquilaflycloud.mdc.enums.alipay.SiteStatusEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteStateEnum;
import com.aquilaflycloud.mdc.extra.alipay.component.AlipayConfig;
import com.aquilaflycloud.mdc.extra.alipay.handler.AlipayMsgHandler;
import com.aquilaflycloud.mdc.extra.alipay.request.*;
import com.aquilaflycloud.mdc.extra.alipay.response.TinyPhoneNumberResponse;
import com.aquilaflycloud.mdc.extra.alipay.response.TinyRunDataResponse;
import com.aquilaflycloud.mdc.extra.alipay.util.MultiFactory;
import com.aquilaflycloud.mdc.mapper.AlipayAuthorSiteMapper;
import com.aquilaflycloud.mdc.mapper.AlipayOpenPlatformMapper;
import com.aquilaflycloud.mdc.model.alipay.AlipayAuthorSite;
import com.aquilaflycloud.mdc.model.alipay.AlipayOpenPlatform;
import com.aquilaflycloud.mdc.param.alipay.AlipayPreAuthUrlGetParam;
import com.aquilaflycloud.mdc.result.alipay.PublicInfo;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AlipayOpenPlatformService {
    @Getter
    private AlipayOpenPlatform alipayOpenPlatform;
    @Resource
    private AlipayOpenPlatformMapper alipayOpenPlatformMapper;
    @Resource
    private AlipayAuthorSiteMapper alipayAuthorSiteMapper;
    @Resource
    private AlipayMsgHandler alipayMsgHandler;
    @Resource
    private RedisTemplate<String, AlipayConfig> redisTemplate;

    @PostConstruct
    public void init() {
        log.info("======初始化AlipayOpenPlatformService配置 start======");
        MultiFactory.initCache(redisTemplate);
        initAlipaySite();
        alipayOpenPlatform = alipayOpenPlatformMapper.selectOne(null);
        if (alipayOpenPlatform != null) {
            try {
                initAlipayClient();
                alipayMsgHandler.initMsgHandler(alipayOpenPlatform);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("======初始化AlipayOpenPlatformService配置 end======");
    }

    private void initAlipayClient() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = alipayOpenPlatform.getSignType();
        config.appId = alipayOpenPlatform.getComponentAppid();
        config.merchantPrivateKey = alipayOpenPlatform.getComponentPrivateKey();
        config.alipayPublicKey = alipayOpenPlatform.getComponentPublicKey();
        config.encryptKey = alipayOpenPlatform.getEncodingAesKey();
        MultiFactory.addOptions(alipayOpenPlatform.getComponentAppid(), config);
    }

    private void addAlipaySite(AlipayAuthorSite site) {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipay.com";
        config.signType = "RSA2";
        config.appId = site.getAppId();
        config.merchantPrivateKey = site.getMerchantPrivateKey();
        config.alipayPublicKey = site.getAlipayPublicKey();
        config.encryptKey = site.getEncodingAesKey();
        MultiFactory.addOptions(site.getAppId(), config);
    }

    private void initAlipaySite(List<AlipayAuthorSite> siteList) {
        Map<String, Config> configs = new HashMap<>();
        for (AlipayAuthorSite site : siteList) {
            Config config = new Config();
            config.protocol = "https";
            config.gatewayHost = "openapi.alipay.com";
            config.signType = "RSA2";
            config.appId = site.getAppId();
            config.merchantPrivateKey = site.getMerchantPrivateKey();
            config.alipayPublicKey = site.getAlipayPublicKey();
            config.encryptKey = site.getEncodingAesKey();
            configs.put(site.getAppId(), config);
        }
        if (configs.size() > 0) {
            MultiFactory.setOptions(configs);
        }
    }

    private void initAlipaySite() {
        List<AlipayAuthorSite> siteList = alipayAuthorSiteMapper.normalSelectList(Wrappers.<AlipayAuthorSite>lambdaQuery()
                .eq(AlipayAuthorSite::getIsAgent, WhetherEnum.NO)
                .eq(AlipayAuthorSite::getState, SiteStateEnum.AUTHORIZED)
        );
        initAlipaySite(siteList);
    }

    public void saveAlipayAuthorSite(String state, String authCode) {
        //获取租户信息,租户信息不存在保存失败
        JSONObject tenantInfo = RedisUtil.<JSONObject>valueRedis().get(state);
        if (tenantInfo == null) {
            return;
        }
        AlipayOpenAuthTokenAppResponse response = getAlipayOpenAuthToken("authorization_code", authCode, null);
        log.info("getAlipayOpenAuthToken response: {}", JSONUtil.toJsonStr(response));
        if (response.isSuccess()) {
            for (AppTokenExchangeSubElement token : response.getTokens()) {
                AlipayAuthorSite site = getAgentAuthorSiteByAppId(token.getAuthAppId());
                if (site == null) {
                    site = new AlipayAuthorSite();
                    site.setComponentAppid(alipayOpenPlatform.getComponentAppid());
                    site.setUserId(token.getUserId());
                    site.setAuthToken(token.getAppAuthToken());
                    site.setRefreshToken(token.getAppRefreshToken());
                    site.setAppId(token.getAuthAppId());
                    site.setTenantId(tenantInfo.getLong("tenantId"));
                    site.setSubTenantId(tenantInfo.getLong("subTenantId"));
                    String source = tenantInfo.getStr("siteSource");
                    if (StrUtil.equals(source, AlipayPreAuthUrlGetParam.ApplicationTypeEnum.PUBLICAPP.toString())) {
                        site.setSource(SiteSourceEnum.PUBLIC);
                    } else if (StrUtil.equals(source, AlipayPreAuthUrlGetParam.ApplicationTypeEnum.TINYAPP.toString())) {
                        site.setSource(SiteSourceEnum.TINY);
                    }
                    AlipayOpenAuthTokenAppQueryResponse queryResponse = getAlipayOpenAuthTokenInfo(token.getAppAuthToken());
                    log.info("getAlipayOpenAuthTokenInfo response: " + JSONUtil.toJsonStr(response));
                    if (queryResponse.isSuccess()) {
                        site.setStatus(EnumUtil.likeValueOf(SiteStatusEnum.class, queryResponse.getStatus().toUpperCase()));
                        site.setAuthStartTime(queryResponse.getAuthStart());
                        site.setAuthEndTime(queryResponse.getAuthEnd());
                        site.setAuthMethods(JSONUtil.toJsonStr(queryResponse.getAuthMethods()));
                        if (queryResponse.getAuthMethods().contains("alipay.open.public.info.query") && site.getSource() == SiteSourceEnum.PUBLIC) {
                            PublicInfo publicInfo = getPublicInfo(token.getAuthAppId(), token.getAppAuthToken());
                            site.setAppName(publicInfo.getAppName());
                            site.setAppContent(JSONUtil.toJsonStr(publicInfo));
                            site.setSource(SiteSourceEnum.PUBLIC);
                        }
                    }
                    site.setIsAgent(WhetherEnum.YES);
                    site.setState(SiteStateEnum.AUTHORIZED);
                    alipayAuthorSiteMapper.normalInsert(site);
                } else {
                    AlipayAuthorSite update = new AlipayAuthorSite();
                    AlipayOpenAuthTokenAppQueryResponse queryResponse = getAlipayOpenAuthTokenInfo(token.getAppAuthToken());
                    update.setStatus(EnumUtil.likeValueOf(SiteStatusEnum.class, queryResponse.getStatus().toUpperCase()));
                    update.setAuthStartTime(queryResponse.getAuthStart());
                    update.setAuthEndTime(queryResponse.getAuthEnd());
                    update.setAuthMethods(JSONUtil.toJsonStr(queryResponse.getAuthMethods()));
                    if (queryResponse.getAuthMethods().contains("alipay.open.public.info.query")) {
                        PublicInfo publicInfo = getPublicInfo(token.getAuthAppId(), token.getAppAuthToken());
                        update.setAppName(publicInfo.getAppName());
                        update.setAppContent(JSONUtil.toJsonStr(publicInfo));
                        update.setSource(SiteSourceEnum.PUBLIC);
                    }
                    update.setId(site.getId());
                    update.setUserId(token.getUserId());
                    update.setAuthToken(token.getAppAuthToken());
                    update.setRefreshToken(token.getAppRefreshToken());
                    update.setState(SiteStateEnum.AUTHORIZED);
                    alipayAuthorSiteMapper.normalUpdateById(update);
                    RedisUtil.redis().delete("getAuthorSite" + site.getAppId());
                }
            }
        }
    }

    public void refreshAlipayAuthorSite(AlipayAuthorSite site) {
        if (site.getIsAgent() == WhetherEnum.NO) {
            addAlipaySite(site);
        }
        RedisUtil.redis().delete("getAuthorSite" + site.getAppId());
    }

    public void cancelAlipayAuthorSite(String appId) {
        MultiFactory.delOptions(appId);
        RedisUtil.redis().delete("getAuthorSite" + appId);
    }

    public PublicInfo getPublicInfo(String appId, String appAuthToken) {
        AlipayOpenPublicInfoQueryResponse response = getAlipayOpenPublicInfo(appId, appAuthToken);
        PublicInfo publicInfo = new PublicInfo();
        BeanUtil.copyProperties(response, publicInfo);
        publicInfo.setAuditStatusList(JSONUtil.toJsonStr(response.getAuditStatusList()));
        return publicInfo;
    }

    public void deleteAlipayAuthorSite(String appId) {
        AlipayAuthorSite authorSite = getAgentAuthorSiteByAppId(appId);
        AlipayAuthorSite site = new AlipayAuthorSite();
        site.setId(authorSite.getId());
        site.setState(SiteStateEnum.CANCELAUTHORIZED);
        alipayAuthorSiteMapper.normalUpdateById(site);
        RedisUtil.redis().delete("getAuthorSite" + appId);
    }

    private AlipayAuthorSite getAgentAuthorSiteByAppId(String appId) {
        return alipayAuthorSiteMapper.normalSelectOne(Wrappers.<AlipayAuthorSite>lambdaQuery()
                .eq(AlipayAuthorSite::getAppId, appId)
                .eq(AlipayAuthorSite::getIsAgent, WhetherEnum.YES)
                .eq(AlipayAuthorSite::getState, SiteStateEnum.AUTHORIZED)
        );
    }

    public AlipayAuthorSite getAlipayAuthorSiteByAppId(String appId) {
        AlipayAuthorSite site = RedisUtil.valueGet("getAuthorSite" + appId, 7, () ->
                alipayAuthorSiteMapper.normalSelectOne(Wrappers.<AlipayAuthorSite>lambdaQuery()
                        .eq(AlipayAuthorSite::getAppId, appId)
                        .eq(AlipayAuthorSite::getState, SiteStateEnum.AUTHORIZED)
                        .orderByAsc(AlipayAuthorSite::getIsAgent)
                        .last("limit 1")
                ));
        if (site == null) {
            throw new ServiceException("授权号不存在");
        }
        return site;
    }

    public String oauth2buildAuthorizationUrl(String appId, String redirectUri, String scope, String state) {
        if (StrUtil.isBlank(appId)) {
            appId = alipayOpenPlatform.getComponentAppid();
        }
        return String.format("https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=%s&scope=%s&redirect_uri=%s&state=%s",
                appId, scope, URLUtil.encode(redirectUri), StrUtil.trimToEmpty(state));
    }

    private <T extends AlipayResponse> T buildResponse(Map map, T response) {
        BeanUtil.fillBeanWithMap(map, response, true, true);
        return response;
    }

    private <T extends AlipayResponse> T buildResponse(String body, String method, T response) {
        Map<String, Map<String, String>> map = JSONUtil.toBean(body, Map.class);
        BeanUtil.fillBeanWithMap(map.get(StrUtil.replace(method, ".", "_") + "_response"),
                response, true, true);
        return response;
    }

    private AlipayOpenAuthTokenAppResponse getAlipayOpenAuthToken(String grantType, String code, String refreshToken) {
        String method = "alipay.open.auth.token.app";
        try {
            AlipayOpenApiGenericResponse genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                    .optional("grant_type", grantType)
                    .optional("code", code)
                    .optional("refresh_token", refreshToken)
                    .execute(method, TeaConverter.buildMap(), TeaConverter.buildMap());
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayOpenAuthTokenAppResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private AlipayOpenAuthTokenAppQueryResponse getAlipayOpenAuthTokenInfo(String authToken) {
        String method = "alipay.open.auth.token.app.query";
        try {
            AlipayOpenApiGenericResponse genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                    .optional("app_auth_token", authToken)
                    .execute(method, TeaConverter.buildMap(), TeaConverter.buildMap());
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayOpenAuthTokenAppQueryResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean rsaCheck(String appId, Map<String, String> params) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(appId);
        try {
            Boolean verify;
            if (site.getIsAgent() == WhetherEnum.YES) {
                verify = MultiFactory.Payment.Common(alipayOpenPlatform.getComponentAppid()).verifyNotify(params);
            } else {
                verify = MultiFactory.Payment.Common(appId).verifyNotify(params);
            }
            return verify;
        } catch (Exception e) {
            log.error("验签异常", e);
            throw new ServiceException("验签异常");
        }
    }

    public TinyPhoneNumberResponse getTinyPhoneNumber(String appId, String content) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(appId);
        try {
            String plainData;
            if (site.getIsAgent() == WhetherEnum.YES) {
                plainData = MultiFactory.Util.AES(alipayOpenPlatform.getComponentAppid()).decrypt(content);
            } else {
                plainData = MultiFactory.Util.AES(appId).decrypt(content);
            }
            TinyPhoneNumberResponse response = JSONUtil.toBean(plainData, TinyPhoneNumberResponse.class);
            if (StrUtil.isNotBlank(response.getSubCode())) {
                throw new ServiceException(response.getSubMsg());
            }
            return response;
        } catch (Exception e) {
            log.error("解密异常", e);
            throw new ServiceException("解密异常");
        }
    }

    public TinyRunDataResponse getTinyRunData(String appId, String content) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(appId);
        try {
            String plainData;
            if (site.getIsAgent() == WhetherEnum.YES) {
                plainData = MultiFactory.Util.AES(alipayOpenPlatform.getComponentAppid()).decrypt(content);
            } else {
                plainData = MultiFactory.Util.AES(appId).decrypt(content);
            }
            TinyRunDataResponse response = JSONUtil.toBean(plainData, TinyRunDataResponse.class);
            if (StrUtil.isNotBlank(response.getSubCode())) {
                throw new ServiceException(response.getSubMsg());
            }
            return response;
        } catch (Exception e) {
            log.error("解密异常", e);
            throw new ServiceException("解密异常");
        }
    }

    public AlipaySystemOauthTokenResponse getSystemOauthToken(String appId, String code) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(appId);
        try {
            com.alipay.easysdk.base.oauth.models.AlipaySystemOauthTokenResponse tokenResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                tokenResponse = MultiFactory.Base.OAuth(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .getToken(code);
            } else {
                tokenResponse = MultiFactory.Base.OAuth(appId).getToken(code);
            }
            if (!ResponseChecker.success(tokenResponse)) {
                throw new ServiceException(tokenResponse.subMsg);
            }
            return buildResponse(tokenResponse.toMap(), new AlipaySystemOauthTokenResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayUserInfoShareResponse getUserInfoShare(String appId, String accessToken) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(appId);
        String method = "alipay.user.info.share";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .auth(accessToken)
                        .execute(method, TeaConverter.buildMap(), TeaConverter.buildMap());
            } else {
                genericResponse = MultiFactory.Util.Generic(appId)
                        .auth(accessToken)
                        .execute(method, TeaConverter.buildMap(), TeaConverter.buildMap());
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayUserInfoShareResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayOpenPublicInfoQueryResponse getAlipayOpenPublicInfo(String appId, String authToken) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(appId);
        String method = "alipay.open.public.info.query";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(authToken)
                        .execute(method, TeaConverter.buildMap(), TeaConverter.buildMap());
            } else {
                genericResponse = MultiFactory.Util.Generic(appId)
                        .execute(method, TeaConverter.buildMap(), TeaConverter.buildMap());
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayOpenPublicInfoQueryResponse());
        } catch (Exception e) {
            log.error("获取生活号信息失败", e);
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayOpenAppQrcodeCreateResponse createAlipayOpenAppQrcode(String appId, String urlParam, String queryParam, String describe) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(appId);
        try {
            com.alipay.easysdk.base.qrcode.models.AlipayOpenAppQrcodeCreateResponse createResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                createResponse = MultiFactory.Base.Qrcode(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .create(urlParam, queryParam, describe);
            } else {
                createResponse = MultiFactory.Base.Qrcode(appId).create(urlParam, queryParam, describe);
            }
            if (!ResponseChecker.success(createResponse)) {
                throw new ServiceException(createResponse.subMsg);
            }
            return buildResponse(createResponse.toMap(), new AlipayOpenAppQrcodeCreateResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayTradeCreateResponse createAlipayTrade(TradeCreateRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String notifyUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/alipayNotify").toString();
        try {
            com.alipay.easysdk.payment.common.models.AlipayTradeCreateResponse createResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                createResponse = MultiFactory.Payment.Common(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .optional("body", request.getBody())
                        .optional("timeout_express", request.getTimeoutExpress())
                        .optional("extend_params", new JSONObject().set("sys_service_provider_id", request.getSysServiceProviderId()))
                        .asyncNotify(notifyUrl)
                        .create(request.getSubject(), request.getOutTradeNo(), request.getTotalAmount(), request.getBuyerId());
            } else {
                createResponse = MultiFactory.Payment.Common(request.getAppId())
                        .optional("body", request.getBody())
                        .optional("timeout_express", request.getTimeoutExpress())
                        .optional("extend_params", new JSONObject().set("sys_service_provider_id", request.getSysServiceProviderId()))
                        .asyncNotify(notifyUrl)
                        .create(request.getSubject(), request.getOutTradeNo(), request.getTotalAmount(), request.getBuyerId());
            }
            if (!ResponseChecker.success(createResponse)) {
                throw new ServiceException(createResponse.subMsg);
            }
            return buildResponse(createResponse.toMap(), new AlipayTradeCreateResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayTradeRefundResponse refundAlipayTrade(TradeRefundRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        try {
            com.alipay.easysdk.payment.common.models.AlipayTradeRefundResponse refundResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                refundResponse = MultiFactory.Payment.Common(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .optional("refund_reason", request.getRefundReason())
                        .optional("out_request_no", request.getOutRequestNo())
                        .refund(request.getOutTradeNo(), request.getRefundAmount());
            } else {
                refundResponse = MultiFactory.Payment.Common(request.getAppId())
                        .optional("refund_reason", request.getRefundReason())
                        .optional("out_request_no", request.getOutRequestNo())
                        .refund(request.getOutTradeNo(), request.getRefundAmount());
            }
            if (!ResponseChecker.success(refundResponse)) {
                throw new ServiceException(refundResponse.subMsg);
            }
            return buildResponse(refundResponse.toMap(), new AlipayTradeRefundResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayTradeQueryResponse queryAlipayTrade(TradeQueryRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        try {
            com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse queryResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                queryResponse = MultiFactory.Payment.Common(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .query(request.getOutTradeNo());
            } else {
                queryResponse = MultiFactory.Payment.Common(request.getAppId()).query(request.getOutTradeNo());
            }
            if (!ResponseChecker.success(queryResponse)) {
                throw new ServiceException(queryResponse.subMsg);
            }
            return buildResponse(queryResponse.toMap(), new AlipayTradeQueryResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingCashvoucherTemplateCreateResponse createVoucherTemplate(VoucherTemplateCreateRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.cashvoucher.template.create";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingCashvoucherTemplateCreateResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingCashvoucherTemplateModifyResponse modifyVoucherTemplate(VoucherTemplateModifyRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.cashvoucher.template.modify";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingCashvoucherTemplateModifyResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingVoucherTemplateDeleteResponse deleteVoucherTemplate(VoucherTemplateDeleteRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.voucher.template.delete";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingVoucherTemplateDeleteResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingVoucherTemplatelistQueryResponse queryVoucherTemplateList(VoucherTemplateListQueryRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.voucher.templatelist.query";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingVoucherTemplatelistQueryResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingVoucherTemplatedetailQueryResponse queryVoucherTemplate(VoucherTemplateDetailQueryRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.voucher.templatedetail.query";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingVoucherTemplatedetailQueryResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingVoucherSendResponse sendVoucher(VoucherSendRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.voucher.send";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingVoucherSendResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingVoucherListQueryResponse queryVoucherList(VoucherListQueryRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.voucher.list.query";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingVoucherListQueryResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public AlipayMarketingVoucherQueryResponse queryVoucherList(VoucherQueryRequest request) {
        AlipayAuthorSite site = getAlipayAuthorSiteByAppId(request.getAppId());
        String method = "alipay.marketing.voucher.list.query";
        try {
            AlipayOpenApiGenericResponse genericResponse;
            if (site.getIsAgent() == WhetherEnum.YES) {
                genericResponse = MultiFactory.Util.Generic(alipayOpenPlatform.getComponentAppid())
                        .agent(site.getAuthToken())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            } else {
                genericResponse = MultiFactory.Util.Generic(request.getAppId())
                        .execute(method, TeaConverter.buildMap(), BeanUtil.beanToMap(request, true, true));
            }
            if (!ResponseChecker.success(genericResponse)) {
                throw new ServiceException(genericResponse.subMsg);
            }
            return buildResponse(genericResponse.httpBody, method, new AlipayMarketingVoucherQueryResponse());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}