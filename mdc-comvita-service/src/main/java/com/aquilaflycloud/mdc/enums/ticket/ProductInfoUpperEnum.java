package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ProductInfoUpperEnum {
    // 产品是否上架
    NOTUPPER(0, "下架"),
    UPPER(1, "上架");

    ProductInfoUpperEnum(int type, String name) {
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
