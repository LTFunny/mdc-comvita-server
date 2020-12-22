package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * VerificateModeEnum
 *
 * @author star
 * @date 2020-03-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum VerificateModeEnum {
    // 优惠券核销方式类型
    POINTOFSALES(1, "POS机核销"),
    CONSUMECODE(2, "核销码核销"),
    ALIPAY(3, "支付宝核销"),
    ;

    VerificateModeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
