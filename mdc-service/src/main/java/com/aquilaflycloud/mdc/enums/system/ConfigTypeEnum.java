package com.aquilaflycloud.mdc.enums.system;

/**
 * ConfigTypeEnum
 *
 * @author star
 * @date 2020-03-15
 */
public enum ConfigTypeEnum {
    // 后端配置类型
    IGNORE_JUDGMENT_NORMAL_APPID("IGNORE_JUDGMENT_NORMAL_APPID", "忽略检验合法性的appId"),
    OPEN_API_DOMAIN_NAME("OPEN_API_DOMAIN_NAME", "鹰云开放平台接口域名"),
    EASYPAY_API_DOMAIN_NAME("EASYPAY_API_DOMAIN_NAME", "惠云支付接口域名"),
    MDC_URL_DOMAIN_NAME("MDC_URL_DOMAIN_NAME", "MDC后端访问域名"),
    MDC_WECHAT_REDIRECT_URL("MDC_WECHAT_REDIRECT_URL", "MDC后端微信授权重定向链接"),
    MDC_ALIPAY_REDIRECT_URL("MDC_ALIPAY_REDIRECT_URL", "MDC后端支付宝授权重定向链接"),
    MDC_WECHAT_SUCCESS_RETURN_URL("MDC_WECHAT_SUCCESS_RETURN_URL", "MDC后端微信授权成功重定向链接"),
    MDC_ALIPAY_SUCCESS_RETURN_URL("MDC_ALIPAY_SUCCESS_RETURN_URL", "MDC后端支付宝授权成功重定向链接"),
    ALIPAY_CASH_VOUCHER_REDIRECT_ROUTE_NAME("ALIPAY_CASH_VOUCHER_REDIRECT_ROUTE_NAME", "支付宝现金抵扣券支付成功回调路由"),
    ;

    ConfigTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private String type;

    private String name;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
