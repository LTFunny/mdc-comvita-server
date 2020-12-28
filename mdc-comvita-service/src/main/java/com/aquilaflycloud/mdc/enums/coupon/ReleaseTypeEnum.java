package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ReleaseTypeEnum
 *
 * @author star
 * @date 2020-03-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ReleaseTypeEnum {
    // 优惠券发放类型
    IMMEDIATELY(1, "立即发放"),
    REGULAR(2, "定时发放");

    ReleaseTypeEnum(int type, String name) {
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
