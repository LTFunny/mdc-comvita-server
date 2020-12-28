package com.aquilaflycloud.mdc.enums.ad;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * EffectiveModeEnum
 *
 * @author star
 * @date 2019-11-16
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum EffectiveModeEnum {
    // 生效类型
    FOREVER(1, "永久生效"),
    CUSTOM(2, "按时段生效"),
    ;

    EffectiveModeEnum(int type, String name) {
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
