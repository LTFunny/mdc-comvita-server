package com.aquilaflycloud.mdc.enums.common;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * DateUnitTypeEnum
 *
 * @author star
 * @date 2020-03-06
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum DateUnitTypeEnum {
    // 时间单位类型
    YEAR(1, "年"),
    MONTH(2, "月"),
    DAY(3, "日"),
    ;

    DateUnitTypeEnum(int type, String name) {
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
