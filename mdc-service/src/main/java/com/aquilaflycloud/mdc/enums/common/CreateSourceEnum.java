package com.aquilaflycloud.mdc.enums.common;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * CreateSourceEnum
 *
 * @author star
 * @date 2020-03-18
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum CreateSourceEnum {
    // 优惠券创建来源类型
    NORMAL(1, "原生创建"),
    EXCHANGE(2, "兑换商品");

    CreateSourceEnum(int type, String name) {
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
