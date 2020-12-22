package com.aquilaflycloud.mdc.extra.wechat.service;

import com.aquilaflycloud.mdc.extra.wechat.component.WxPayInRedisConfigStorage;
import com.aquilaflycloud.mdc.mapper.WechatPayConfigMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatPayConfig;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.XmlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WechatPayService
 *
 * @author star
 * @date 2020/8/6
 */
@Service
@Slf4j
public class WechatPayService {
    private WxPayInRedisConfigStorage wxPayInRedisConfigStorage;
    @Resource
    private WechatPayConfigMapper wechatPayConfigMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final Map<String, WxPayService> WX_PAY_SERVICE_MAP = new ConcurrentHashMap<>();

    public WxPayService getWxPayServiceByAppId(String appId) {
        WxPayService wxPayService = WX_PAY_SERVICE_MAP.get(appId);
        if (wxPayService == null) {
            synchronized (WX_PAY_SERVICE_MAP) {
                wxPayService = WX_PAY_SERVICE_MAP.get(appId);
                if (wxPayService == null) {
                    wxPayService = new WxPayServiceImpl();
                    WxPayConfig config = new WxPayConfig();
                    config.setAppId(appId);
                    config.setMchId(wxPayInRedisConfigStorage.getMchId(appId));
                    config.setMchKey(wxPayInRedisConfigStorage.getMchKey(appId));
                    config.setSubAppId(wxPayInRedisConfigStorage.getSubAppId(appId));
                    config.setSubMchId(wxPayInRedisConfigStorage.getSubMchId(appId));
                    config.setKeyPath(wxPayInRedisConfigStorage.getKeyPath(appId));
                    config.setPrivateKeyPath(wxPayInRedisConfigStorage.getPrivateKeyPath(appId));
                    config.setPrivateCertPath(wxPayInRedisConfigStorage.getPrivateCertPath(appId));
                    config.setCertSerialNo(wxPayInRedisConfigStorage.getCertSerialNo(appId));
                    config.setApiV3Key(wxPayInRedisConfigStorage.getApiV3Key(appId));
                    try {
                        config.initApiV3HttpClient();
                    } catch (WxPayException ignored) {
                    }
                    wxPayService.setConfig(config);
                    WX_PAY_SERVICE_MAP.put(appId, wxPayService);
                }
            }
        }
        return wxPayService;
    }

    @PostConstruct
    public void init() {
        log.info("======初始化WechatPayService配置 start======");
        XmlConfig.fastMode = true;
        List<WechatPayConfig> payConfigList = wechatPayConfigMapper.normalSelectList(Wrappers.emptyWrapper());
        WxPayInRedisConfigStorage configStorage = new WxPayInRedisConfigStorage(redisTemplate);
        for (WechatPayConfig wechatPayConfig : payConfigList) {
            configStorage.setMchId(wechatPayConfig.getAppId(), wechatPayConfig.getMchId());
            configStorage.setMchKey(wechatPayConfig.getAppId(), wechatPayConfig.getMchKey());
            configStorage.setSubAppId(wechatPayConfig.getAppId(), wechatPayConfig.getSubAppId());
            configStorage.setSubMchId(wechatPayConfig.getAppId(), wechatPayConfig.getSubMchId());
            configStorage.setKeyPath(wechatPayConfig.getAppId(), wechatPayConfig.getKeyPath());
            configStorage.setPrivateCertPath(wechatPayConfig.getAppId(), wechatPayConfig.getPrivateCertPath());
            configStorage.setPrivateKeyPath(wechatPayConfig.getAppId(), wechatPayConfig.getPrivateKeyPath());
            configStorage.setCertSerialNo(wechatPayConfig.getAppId(), wechatPayConfig.getCertSerialNo());
            configStorage.setApiV3Key(wechatPayConfig.getAppId(), wechatPayConfig.getApiV3Key());
        }
        wxPayInRedisConfigStorage = configStorage;
        log.info(".======初始化WechatPayService配置 end======");
    }

    public WxPayConfig getPayConfig(String appId) {
        return getWxPayServiceByAppId(appId).getConfig();
    }

    public final boolean validate(String appId, String body, String serialNo, String sign, String timestamp, String nonce) {
        if (timestamp == null || nonce == null || serialNo == null || sign == null) {
            return false;
        }
        String message = timestamp + "\n" + nonce + "\n" + body + "\n";
        return getWxPayServiceByAppId(appId).getConfig().getVerifier().verify(serialNo, message.getBytes(StandardCharsets.UTF_8), sign);
    }

    public WxPayConfig getPayConfigByMallPath(String mallPath) {
        String lockName = "getPayConfigLock" + mallPath;
        String key = "getPayConfig" + mallPath;
        WechatPayConfig payConfig = RedisUtil.syncValueGet(lockName, key,
                () -> wechatPayConfigMapper.normalSelectOne(Wrappers.<WechatPayConfig>lambdaQuery()
                        .eq(WechatPayConfig::getMallPath, mallPath)
                ));
        return getWxPayServiceByAppId(payConfig.getAppId()).getConfig();
    }
}
