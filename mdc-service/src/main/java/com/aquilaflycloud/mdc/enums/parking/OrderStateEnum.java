package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * OrderStateEnum
 *
 * @author star
 * @date 2020-02-01
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum OrderStateEnum {
    // 停车订单状态类型
    NOTPAY(1, "未缴费"),
    PAID(2, "已缴费"),
    REFUNDCONFIRM(3, "退款待确认"),
    REFUNDING(4, "退款中"),
    REFUNDSUCC(5, "退款成功"),
    REFUNDFAIL(6, "退款失败"),
    ;

    OrderStateEnum(int type, String name) {
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
