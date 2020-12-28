package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ProductInfoTopEnum {
    // 产品是否置顶
    NOTTOP(0, "不置顶"),
    TOP(1, "置顶");

    ProductInfoTopEnum(int type, String name) {
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
