package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum  ProductInfoStateEnum {
    // 产品状态
    NORMAL(1, "正常"),
    OUT(2, "过期");

    ProductInfoStateEnum(int type, String name) {
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
