package com.aquilaflycloud.mdc.extra.alipay.util;

import com.alipay.easysdk.kernel.Client;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.Context;
import com.aquilaflycloud.mdc.extra.alipay.component.AlipayConfig;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端工厂，用于快速配置和访问各种场景下的API Client
 * <p>
 * 注：该Factory获取的Client不可储存重复使用，请每次均通过Factory完成调用
 *
 * @author zhongyu
 * @version $Id: Factory.java, v 0.1 2020年01月18日 11:26 AM zhongyu Exp $
 */
public class MultiFactory {

    public static final String SDK_VERSION = "alipay-easysdk-java-2.0.0";

    private static final Map<String, Context> CONTEXTS = new HashMap<>();

    private static HashOperations<String, String, AlipayConfig> ALIPAY_CONFIG_CACHE;

    private static final String ALIPAY_CONFIG_KEY = "alipay_config";

    /**
     * 初始化缓存,使用缓存保存相关配置
     *
     * @param redisTemplate 缓存实例
     */
    public static void initCache(RedisTemplate<String, AlipayConfig> redisTemplate) {
        ALIPAY_CONFIG_CACHE = redisTemplate.opsForHash();
    }

    private static AlipayConfig createAlipayConfig(Config config) {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setProtocol(config.protocol);
        alipayConfig.setGatewayHost(config.gatewayHost);
        alipayConfig.setAppId(config.appId);
        alipayConfig.setSignType(config.signType);
        alipayConfig.setAlipayPublicKey(config.alipayPublicKey);
        alipayConfig.setMerchantPrivateKey(config.merchantPrivateKey);
        alipayConfig.setMerchantCertPath(config.merchantCertPath);
        alipayConfig.setAlipayCertPath(config.alipayCertPath);
        alipayConfig.setAlipayRootCertPath(config.alipayRootCertPath);
        alipayConfig.setNotifyUrl(config.notifyUrl);
        alipayConfig.setEncryptKey(config.encryptKey);
        return alipayConfig;
    }

    private static Config createConfig(AlipayConfig alipayConfig) {
        Config config = new Config();
        config.protocol = alipayConfig.getProtocol();
        config.gatewayHost = alipayConfig.getGatewayHost();
        config.appId = alipayConfig.getAppId();
        config.signType = alipayConfig.getSignType();
        config.alipayPublicKey = alipayConfig.getAlipayPublicKey();
        config.merchantPrivateKey = alipayConfig.getMerchantPrivateKey();
        config.merchantCertPath = alipayConfig.getMerchantCertPath();
        config.alipayCertPath = alipayConfig.getAlipayCertPath();
        config.alipayRootCertPath = alipayConfig.getAlipayRootCertPath();
        config.notifyUrl = alipayConfig.getNotifyUrl();
        config.encryptKey = alipayConfig.getEncryptKey();
        return config;
    }

    /**
     * 设置客户端参数，只需设置一次，即可反复使用各种场景下的API Client
     *
     * @param optionsMap 客户端参数对象
     */
    public static void setOptions(Map<String, Config> optionsMap) {
        try {
            for (Map.Entry<String, Config> entry : optionsMap.entrySet()) {
                CONTEXTS.put(entry.getKey(), new Context(entry.getValue(), SDK_VERSION));
            }
            if (ALIPAY_CONFIG_CACHE != null) {
                for (Map.Entry<String, Config> entry : optionsMap.entrySet()) {
                    AlipayConfig alipayConfig = createAlipayConfig(entry.getValue());
                    ALIPAY_CONFIG_CACHE.put(ALIPAY_CONFIG_KEY, entry.getKey(), alipayConfig);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void addOptions(String appId, Config options) {
        try {
            CONTEXTS.put(appId, new Context(options, SDK_VERSION));
            if (ALIPAY_CONFIG_CACHE != null) {
                AlipayConfig alipayConfig = createAlipayConfig(options);
                ALIPAY_CONFIG_CACHE.put(ALIPAY_CONFIG_KEY, appId, alipayConfig);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void delOptions(String appId) {
        try {
            CONTEXTS.remove(appId);
            if (ALIPAY_CONFIG_CACHE != null) {
                ALIPAY_CONFIG_CACHE.delete(ALIPAY_CONFIG_KEY, appId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Context getOptions(String appId) {
        Context context = CONTEXTS.get(appId);
        if (context == null && ALIPAY_CONFIG_CACHE != null) {
            try {
                AlipayConfig alipayConfig = ALIPAY_CONFIG_CACHE.get(ALIPAY_CONFIG_KEY, appId);
                Config options = createConfig(alipayConfig);
                CONTEXTS.put(appId, new Context(options, SDK_VERSION));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return context;
    }

    /**
     * 支付能力相关
     */
    public static class Payment {
        /**
         * 获取支付通用API Client
         *
         * @return 支付通用API Client
         */
        public static com.alipay.easysdk.payment.common.Client Common(String appId) throws Exception {
            return new com.alipay.easysdk.payment.common.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取花呗相关API Client
         *
         * @return 花呗相关API Client
         */
        public static com.alipay.easysdk.payment.huabei.Client Huabei(String appId) throws Exception {
            return new com.alipay.easysdk.payment.huabei.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取当面付相关API Client
         *
         * @return 当面付相关API Client
         */
        public static com.alipay.easysdk.payment.facetoface.Client FaceToFace(String appId) throws Exception {
            return new com.alipay.easysdk.payment.facetoface.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取电脑网站支付相关API Client
         *
         * @return 电脑网站支付相关API Client
         */
        public static com.alipay.easysdk.payment.page.Client Page(String appId) throws Exception {
            return new com.alipay.easysdk.payment.page.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取手机网站支付相关API Client
         *
         * @return 手机网站支付相关API Client
         */
        public static com.alipay.easysdk.payment.wap.Client Wap(String appId) throws Exception {
            return new com.alipay.easysdk.payment.wap.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取手机APP支付相关API Client
         *
         * @return 手机APP支付相关API Client
         */
        public static com.alipay.easysdk.payment.app.Client App(String appId) throws Exception {
            return new com.alipay.easysdk.payment.app.Client(new Client(getOptions(appId)));
        }
    }

    /**
     * 基础能力相关
     */
    public static class Base {
        /**
         * 获取图片相关API Client
         *
         * @return 图片相关API Client
         */
        public static com.alipay.easysdk.base.image.Client Image(String appId) throws Exception {
            return new com.alipay.easysdk.base.image.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取视频相关API Client
         *
         * @return 视频相关API Client
         */
        public static com.alipay.easysdk.base.video.Client Video(String appId) throws Exception {
            return new com.alipay.easysdk.base.video.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取OAuth认证相关API Client
         *
         * @return OAuth认证相关API Client
         */
        public static com.alipay.easysdk.base.oauth.Client OAuth(String appId) throws Exception {
            return new com.alipay.easysdk.base.oauth.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取小程序二维码相关API Client
         *
         * @return 小程序二维码相关API Client
         */
        public static com.alipay.easysdk.base.qrcode.Client Qrcode(String appId) throws Exception {
            return new com.alipay.easysdk.base.qrcode.Client(new Client(getOptions(appId)));
        }
    }

    /**
     * 营销能力相关
     */
    public static class Marketing {
        /**
         * 获取生活号相关API Client
         *
         * @return 生活号相关API Client
         */
        public static com.alipay.easysdk.marketing.openlife.Client OpenLife(String appId) throws Exception {
            return new com.alipay.easysdk.marketing.openlife.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取支付宝卡包相关API Client
         *
         * @return 支付宝卡包相关API Client
         */
        public static com.alipay.easysdk.marketing.pass.Client Pass(String appId) throws Exception {
            return new com.alipay.easysdk.marketing.pass.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取小程序模板消息相关API Client
         *
         * @return 小程序模板消息相关API Client
         */
        public static com.alipay.easysdk.marketing.templatemessage.Client TemplateMessage(String appId) throws Exception {
            return new com.alipay.easysdk.marketing.templatemessage.Client(new Client(getOptions(appId)));
        }
    }

    /**
     * 会员能力相关
     */
    public static class Member {
        /**
         * 获取支付宝身份认证相关API Client
         *
         * @return 支付宝身份认证相关API Client
         */
        public static com.alipay.easysdk.member.identification.Client Identification(String appId) throws Exception {
            return new com.alipay.easysdk.member.identification.Client(new Client(getOptions(appId)));
        }
    }

    /**
     * 安全能力相关
     */
    public static class Security {
        /**
         * 获取文本风险识别相关API Client
         *
         * @return 文本风险识别相关API Client
         */
        public static com.alipay.easysdk.security.textrisk.Client TextRisk(String appId) throws Exception {
            return new com.alipay.easysdk.security.textrisk.Client(new Client(getOptions(appId)));
        }
    }

    /**
     * 辅助工具
     */
    public static class Util {
        /**
         * 获取OpenAPI通用接口，可通过自行拼装参数，调用几乎所有OpenAPI
         *
         * @return OpenAPI通用接口
         */
        public static com.alipay.easysdk.util.generic.Client Generic(String appId) throws Exception {
            return new com.alipay.easysdk.util.generic.Client(new Client(getOptions(appId)));
        }

        /**
         * 获取AES128加解密相关API Client，常用于会员手机号的解密
         *
         * @return AES128加解密相关API Client
         */
        public static com.alipay.easysdk.util.aes.Client AES(String appId) throws Exception {
            return new com.alipay.easysdk.util.aes.Client(new Client(getOptions(appId)));
        }
    }
}