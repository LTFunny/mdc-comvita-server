package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * EffectiveTypeEnum
 *
 * @author star
 * @date 2020-01-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum EffectiveTypeEnum {
    // 有效时间类型
    TWENTYFOUR(1, "24小时内"),
    TODAY(2, "当天内"),
    EVERLASTING(3, "永久"),
    CUSTOM(4, "自定义"),
    ;

    EffectiveTypeEnum(int type, String name) {
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
