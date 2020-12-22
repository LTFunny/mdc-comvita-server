package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ProductInfoAllTypeEnum {
    // 产品类型
    OCEAN(0, "海洋馆门票"),
    MUSEUM(1, "博物馆门票"),
    RAINFOREST(2, "雨林馆门票"),
    UNIT(3, "联票");

    ProductInfoAllTypeEnum(int type, String name) {
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
