package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * DisableTypeEnum
 *
 * @author star
 * @date 2020-06-28
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum DisableTypeEnum {
    // 下架类型
    MANUAL(1, "手动下架"),
    REGULAR(2, "定时下架"),
    ;

    DisableTypeEnum(int type, String name) {
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
