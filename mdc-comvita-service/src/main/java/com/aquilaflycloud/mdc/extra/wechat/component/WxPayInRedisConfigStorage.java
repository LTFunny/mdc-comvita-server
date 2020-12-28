package com.aquilaflycloud.mdc.extra.wechat.component;

import cn.hutool.core.util.StrUtil;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author star
 */
public class WxPayInRedisConfigStorage {
    private final static String WECHAT_PAY_MCH_ID_KEY = "wechat_pay_mch_id_key:";
    private final static String WECHAT_PAY_MCH_KEY_KEY = "wechat_pay_mch_key_key:";
    private final static String WECHAT_PAY_SUB_APP_ID_KEY = "wechat_pay_sub_app_id_key:";
    private final static String WECHAT_PAY_SUB_MCH_ID_KEY = "wechat_pay_sub_mch_id_key:";
    private final static String WECHAT_PAY_KEY_PATH_KEY = "wechat_pay_key_path_key:";
    private final static String WECHAT_PAY_PRIVATE_KEY_PATH_KEY = "wechat_pay_private_key_path_key:";
    private final static String WECHAT_PAY_PRIVATE_CERT_PATH_KEY = "wechat_pay_private_cert_path_key";
    private final static String WECHAT_PAY_CERT_SERIAL_NO_KEY = "wechat_pay_cert_serial_no_key";
    private final static String WECHAT_PAY_API_V3_KEY_KEY = "wechat_pay_api_v3_key_key";

    private final RedisTemplate<String, String> redisTemplate;
    private String keyPrefix;
    private String mchIdKey;
    private String mchKeyKey;
    private String subAppIdKey;
    private String subMchIdKey;
    private String keyPathKey;
    private String privateKeyPathKey;
    private String privateCertPathKey;
    private String certSerialNoKey;
    private String apiV3KeyKey;

    public WxPayInRedisConfigStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        initKey();
    }

    public WxPayInRedisConfigStorage(RedisTemplate<String, String> redisTemplate, String keyPrefix) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
        initKey();
    }

    public void initKey() {
        String prefix = StrUtil.isBlank(keyPrefix) ? "" :
                (StrUtil.endWith(keyPrefix, ":") ? keyPrefix : (keyPrefix + ":"));
        mchIdKey = prefix + WECHAT_PAY_MCH_ID_KEY;
        mchKeyKey = prefix + WECHAT_PAY_MCH_KEY_KEY;
        subAppIdKey = prefix + WECHAT_PAY_SUB_APP_ID_KEY;
        subMchIdKey = prefix + WECHAT_PAY_SUB_MCH_ID_KEY;
        keyPathKey = prefix + WECHAT_PAY_KEY_PATH_KEY;
        privateKeyPathKey = prefix + WECHAT_PAY_PRIVATE_KEY_PATH_KEY;
        privateCertPathKey = prefix + WECHAT_PAY_PRIVATE_CERT_PATH_KEY;
        certSerialNoKey = prefix + WECHAT_PAY_CERT_SERIAL_NO_KEY;
        apiV3KeyKey = prefix + WECHAT_PAY_API_V3_KEY_KEY;
    }

    private String getKey(String prefix, String appId) {
        return prefix.endsWith(":") ? prefix.concat(appId) : prefix.concat(":").concat(appId);
    }

    public String getMchId(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.mchIdKey, appId));
    }

    public void setMchId(String appId, String mchId) {
        redisTemplate.opsForValue().set(this.getKey(this.mchIdKey, appId), mchId);
    }

    public String getMchKey(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.mchKeyKey, appId));
    }

    public void setMchKey(String appId, String mchKey) {
        redisTemplate.opsForValue().set(this.getKey(this.mchKeyKey, appId), mchKey);
    }

    public String getSubAppId(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.subAppIdKey, appId));
    }

    public void setSubAppId(String appId, String subAppId) {
        redisTemplate.opsForValue().set(this.getKey(this.subAppIdKey, appId), subAppId);
    }

    public String getSubMchId(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.subMchIdKey, appId));
    }

    public void setSubMchId(String appId, String subMchId) {
        redisTemplate.opsForValue().set(this.getKey(this.subMchIdKey, appId), subMchId);
    }

    public String getKeyPath(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.keyPathKey, appId));
    }

    public void setKeyPath(String appId, String keyPath) {
        redisTemplate.opsForValue().set(this.getKey(this.keyPathKey, appId), keyPath);
    }

    public void setPrivateKeyPath(String appId, String privateKeyPath) {
        redisTemplate.opsForValue().set(this.getKey(this.privateKeyPathKey, appId), privateKeyPath);
    }

    public String getPrivateKeyPath(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.privateKeyPathKey, appId));
    }

    public void setPrivateCertPath(String appId, String privateCertPath) {
        redisTemplate.opsForValue().set(this.getKey(this.privateCertPathKey, appId), privateCertPath);
    }

    public String getPrivateCertPath(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.privateCertPathKey, appId));
    }

    public void setCertSerialNo(String appId, String certSerialNo) {
        redisTemplate.opsForValue().set(this.getKey(this.certSerialNoKey, appId), certSerialNo);
    }

    public String getCertSerialNo(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.certSerialNoKey, appId));
    }

    public void setApiV3Key(String appId, String apiV3Key) {
        redisTemplate.opsForValue().set(this.getKey(this.apiV3KeyKey, appId), apiV3Key);
    }

    public String getApiV3Key(String appId) {
        return redisTemplate.opsForValue().get(this.getKey(this.apiV3KeyKey, appId));
    }
}
