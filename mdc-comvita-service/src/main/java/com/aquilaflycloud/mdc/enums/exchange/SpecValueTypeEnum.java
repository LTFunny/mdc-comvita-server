package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SpecValueTypeEnum
 *
 * @author zengqingjie
 * @date 2020-07-01
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SpecValueTypeEnum {
    // 商品规格类型
    SPEC(1, "规格"),
    VALUE(2, "规格值"),
    ;

    SpecValueTypeEnum(int type, String name) {
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
