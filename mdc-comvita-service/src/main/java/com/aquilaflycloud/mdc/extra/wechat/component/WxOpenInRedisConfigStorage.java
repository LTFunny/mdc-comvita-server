package com.aquilaflycloud.mdc.extra.wechat.component;

import cn.hutool.core.util.StrUtil;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author star
 */
public class WxOpenInRedisConfigStorage extends WxOpenInMemoryConfigStorage {
    private final static String COMPONENT_VERIFY_TICKET_KEY = "wechat_component_verify_ticket:";
    private final static String COMPONENT_ACCESS_TOKEN_KEY = "wechat_component_access_token:";

    private final static String AUTHORIZER_REFRESH_TOKEN_KEY = "wechat_authorizer_refresh_token:";
    private final static String AUTHORIZER_ACCESS_TOKEN_KEY = "wechat_authorizer_access_token:";
    private final static String JSAPI_TICKET_KEY = "wechat_jsapi_ticket:";
    private final static String CARD_API_TICKET_KEY = "wechat_card_api_ticket:";

    private RedisTemplate<String, String> redisTemplate;

    /**
     * redis 存储的 key 的前缀，可为空
     */
    private String keyPrefix;
    private String componentVerifyTicketKey;
    private String componentAccessTokenKey;
    private String authorizerRefreshTokenKey;
    private String authorizerAccessTokenKey;
    private String jsapiTicketKey;
    private String cardApiTicket;

    public WxOpenInRedisConfigStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public WxOpenInRedisConfigStorage(RedisTemplate<String, String> redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public void setComponentAppId(String componentAppId) {
        super.setComponentAppId(componentAppId);
        String prefix = StrUtil.isBlank(keyPrefix) ? "" :
                (StrUtil.endWith(keyPrefix, ":") ? keyPrefix : (keyPrefix + ":"));
        componentVerifyTicketKey = prefix + COMPONENT_VERIFY_TICKET_KEY.concat(componentAppId);
        componentAccessTokenKey = prefix + COMPONENT_ACCESS_TOKEN_KEY.concat(componentAppId);
        authorizerRefreshTokenKey = prefix + AUTHORIZER_REFRESH_TOKEN_KEY.concat(componentAppId);
        authorizerAccessTokenKey = prefix + AUTHORIZER_ACCESS_TOKEN_KEY.concat(componentAppId);
        this.jsapiTicketKey = JSAPI_TICKET_KEY.concat(componentAppId);
        this.cardApiTicket = CARD_API_TICKET_KEY.concat(componentAppId);
    }

    @Override
    public String getComponentVerifyTicket() {
        return redisTemplate.opsForValue().get(this.componentVerifyTicketKey);
    }

    @Override
    public void setComponentVerifyTicket(String componentVerifyTicket) {
        redisTemplate.opsForValue().set(this.componentVerifyTicketKey, componentVerifyTicket);
    }

    @Override
    public String getComponentAccessToken() {
        return redisTemplate.opsForValue().get(this.componentAccessTokenKey);
    }

    @Override
    public boolean isComponentAccessTokenExpired() {
        Long expired = redisTemplate.getExpire(this.componentAccessTokenKey);
        return expired != null && expired < 2;
    }

    @Override
    public void expireComponentAccessToken() {
        redisTemplate.expire(this.componentAccessTokenKey, 0, TimeUnit.SECONDS);
    }

    @Override
    public void updateComponentAccessToken(String componentAccessToken, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.componentAccessTokenKey, componentAccessToken, expiresInSeconds, TimeUnit.SECONDS);
    }

    private String getKey(String prefix, String appId) {
        return prefix.endsWith(":") ? prefix.concat(appId) : prefix.concat(":").concat(appId);
    }

    @Override
    public String getAuthorizerRefreshToken(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.authorizerRefreshTokenKey, appId));
    }

    @Override
    public void setAuthorizerRefreshToken(String appId, String authorizerRefreshToken) {
        redisTemplate.opsForValue().set(this.getKey(this.authorizerRefreshTokenKey, appId), authorizerRefreshToken);
    }

    @Override
    public String getAuthorizerAccessToken(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.authorizerAccessTokenKey, appId));
    }

    @Override
    public boolean isAuthorizerAccessTokenExpired(String appId) {
        Long expired = redisTemplate.getExpire(this.getKey(this.authorizerAccessTokenKey, appId));
        return expired != null && expired < 2;
    }

    @Override
    public void expireAuthorizerAccessToken(String appId) {
        redisTemplate.expire(this.getKey(this.authorizerAccessTokenKey, appId), 0, TimeUnit.SECONDS);
    }

    @Override
    public void updateAuthorizerAccessToken(String appId, String authorizerAccessToken, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.getKey(this.authorizerAccessTokenKey, appId), authorizerAccessToken, expiresInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String getJsapiTicket(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.jsapiTicketKey, appId));
    }

    @Override
    public boolean isJsapiTicketExpired(String appId) {
        Long expired = redisTemplate.getExpire(this.getKey(this.jsapiTicketKey, appId));
        return expired != null && expired < 2;
    }

    @Override
    public void expireJsapiTicket(String appId) {
        redisTemplate.expire(this.getKey(this.jsapiTicketKey, appId), 0, TimeUnit.SECONDS);
    }

    @Override
    public void updateJsapiTicket(String appId, String jsapiTicket, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.getKey(this.jsapiTicketKey, appId), jsapiTicket, expiresInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public String getCardApiTicket(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.cardApiTicket, appId));
    }

    @Override
    public boolean isCardApiTicketExpired(String appId) {
        Long expired = redisTemplate.getExpire(this.getKey(this.cardApiTicket, appId));
        return expired != null && expired < 2;
    }

    @Override
    public void expireCardApiTicket(String appId) {
        redisTemplate.expire(this.getKey(this.cardApiTicket, appId), 0, TimeUnit.SECONDS);
    }

    @Override
    public void updateCardApiTicket(String appId, String cardApiTicket, int expiresInSeconds) {
        redisTemplate.opsForValue().set(this.getKey(this.cardApiTicket, appId), cardApiTicket, expiresInSeconds, TimeUnit.SECONDS);
    }
}
