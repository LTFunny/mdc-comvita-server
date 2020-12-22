package com.aquilaflycloud.mdc.enums.easypay;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * PaymentTypeEnum
 *
 * @author star
 * @date 2019-12-09
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum PaymentTypeEnum {
    // 支付类型
    OTHER(0, "其他"),
    CARD(5, "刷卡"),
    CASH(6, "现金"),
    RECHARGE(9, "充值"),
    WECHAT(10, "微信"),
    ALIPAY(11, "支付宝"),
    JDWALLET(13, "京东钱包"),
    QQWALLET(15, "QQ钱包"),
    UNIONPAY(16, "银联钱包"),
    ;

    PaymentTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
