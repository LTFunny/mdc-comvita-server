package com.aquilaflycloud.mdc.enums.easypay;

public enum FrpCodeEnum {
    // 支付码类型
    ALIPAY_NATIVE("ALIPAY_NATIVE", "支付宝扫码(主扫)"),
    ALIPAY_CARD("ALIPAY_CARD", "支付宝刷卡(被扫)"),
    ALIPAY_APP("ALIPAY_APP", "支付宝 APP 支付"),
    ALIPAY_H5("ALIPAY_H5", "支付宝 H5"),
    ALIPAY_FWC("ALIPAY_FWC", "支付宝服务窗"),
    WEIXIN_NATIVE("WEIXIN_NATIVE", "微信扫码(主扫)"),
    APPLET_PAY("APPLET_PAY", "微信小程序"),
    WEIXIN_APP("WEIXIN_APP", "微信 APP 支付"),
    WEIXIN_H5("WEIXIN_H5", "微信 H5"),
    WEIXIN_GZH("WEIXIN_GZH", "微信公众号"),
    JD_NATIVE("JD_NATIVE", "京东扫码(主扫)"),
    JD_CARD("JD_CARD", "京东刷卡(被扫)"),
    JD_APP("JD_APP", "京东 APP 支付"),
    JD_H5("JD_H5", "京东 H5"),
    QQ_NATIVE("QQ_NATIVE", "QQ 扫码(主扫)"),
    QQ_CARD("QQ_CARD", "QQ 刷卡(被扫)"),
    QQ_APP("QQ_APP", "QQ APP 支付"),
    QQ_H5("QQ_H5", "QQH5"),
    UNIONPAY_NATIVE("UNIONPAY_NATIVE", "银联扫码(主扫)"),
    UNIONPAY_CARD("UNIONPAY_CARD", "银联刷卡(被扫)"),
    UNIONPAY_APP("UNIONPAY_APP", "银联 APP 支付"),
    UNIONPAY_H5("UNIONPAY_H5", "银联 H5"),
    BAIDU_NATIVE("BAIDU_NATIVE", "百度扫码(主扫)"),
    ;

    FrpCodeEnum(String type, String name) {
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
