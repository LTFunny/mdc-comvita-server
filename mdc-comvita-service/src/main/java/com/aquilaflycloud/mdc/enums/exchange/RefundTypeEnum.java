package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RefundTypeEnum
 *
 * @author star
 * @date 2020-03-20
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RefundTypeEnum {
    // 退款类型
    NONREFUNDABLE(1, "不可退款"),
    REFUNDANYTIME(2, "随时退款"),
    ;

    RefundTypeEnum(int type, String name) {
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
