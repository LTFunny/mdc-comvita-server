package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * CouponStateEnum
 *
 * @author star
 * @date 2020-01-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum CouponStateEnum {
    // 停车券状态类型
    NORMAL(1, "正常"),
    DISABLE(2, "停用"),
    SOLDOUT(3, "售罄"),
    EXPIRED(4, "过期"),
    ;

    CouponStateEnum(int type, String name) {
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
