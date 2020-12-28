package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * IdTypeEnum
 *
 * @author star
 * @date 2020-04-19
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum IdTypeEnum {
    // 证件类型
    IDCARD(1, "身份证"),
    OFFICER(2, "士官证"),
    PASSPORT(3, "护照"),
    PASS(4, "港澳通行证"),
    OTHER(5, "其他"),
    ;

    IdTypeEnum(int type, String name) {
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
