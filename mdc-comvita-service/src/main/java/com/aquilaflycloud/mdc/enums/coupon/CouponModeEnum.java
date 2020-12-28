package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * CouponModeEnum
 *
 * @author star
 * @date 2020-06-29
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum CouponModeEnum {
    // 优惠券模式类型
    NORMAL(1, "原生券"),
    ALIPAY(2, "支付宝券"),
    ;

    CouponModeEnum(int type, String name) {
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