package com.aquilaflycloud.mdc.enums.ad;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * effectivePeriodEnum
 *
 * @author star
 * @date 2019-11-16
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum EffectivePeriodEnum {
    // 有效类型
    ALLDAY(1, "全天有效"),
    CUSTOM(2, "自定义时段"),
    ;

    EffectivePeriodEnum(int type, String name) {
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
