package com.aquilaflycloud.mdc.enums.information;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ImportanceEnum
 *
 * @author star
 * @date 2020-03-07
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ImportanceEnum {
    // 消息等级类型
    GENERAL(1, "一般"),
    IMPORTANT(2, "重要"),
    VERYIMPORTANT(3, "十分重要"),
    ;

    ImportanceEnum(int type, String name) {
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
