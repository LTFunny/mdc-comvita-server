package com.aquilaflycloud.mdc.enums.information;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * InfoTypeEnum
 *
 * @author star
 * @date 2020-03-07
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum InfoTypeEnum {
    // 消息类型
    BULLETIN(1, "快报"),
    CONVENTION(2, "公约"),
    ;

    InfoTypeEnum(int type, String name) {
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
