package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 订单支付状态
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum OrderPayStatusEnum {
    // 购票订单支付状态类型
    UN_PAID(0, "未付款"),
    PAID(10, "已付款"),
    FAIL_PAID(11, "付款失败"),
    REFUNDING(20, "退款中"),
    REFUNDED(21, "已退款"),
    FAIL_REFUNDING(22, "退款失败"),
    CANCELLING(30, "取消中"),
    CANCELLED(31, "已取消"),
    ;

    OrderPayStatusEnum(int type, String name) {
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
