package com.aquilaflycloud.mdc.enums.easypay;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * OrderTypeEnum
 *
 * @author star
 * @date 2019-12-07
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum OrderTypeEnum {
    // 订单类型
    EXCHANGE(3, "兑换商城订单"),
    ;

    OrderTypeEnum(int type, String name) {
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
