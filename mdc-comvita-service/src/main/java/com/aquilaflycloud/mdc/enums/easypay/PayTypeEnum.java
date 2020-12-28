package com.aquilaflycloud.mdc.enums.easypay;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * PayTypeEnum
 *
 * @author star
 * @date 2020-04-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum PayTypeEnum {
    // 发起支付方式类型
    EASYPAY(1, "惠云支付"),
    WECHATPAY(3, "微信直联支付"),
    ;

    PayTypeEnum(int type, String name) {
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
