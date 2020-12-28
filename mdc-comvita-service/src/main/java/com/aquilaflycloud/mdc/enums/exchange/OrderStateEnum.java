package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * OrderStateEnum
 *
 * @author star
 * @date 2020-03-15
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum OrderStateEnum {
    // 兑换订单状态类型
    NOTPAY(1, "待支付"),
    NOTCONSUME(2, "待核销"),
    PENDING(3, "待处理"),
    NOTDELIVERY(4, "待发货"),
    DELIVERYING(5, "待收货"),
    SUCCESS(6, "已完成"),
    FAILED(7, "交易失败"),
    CLOSED(8, "交易关闭"),
    REFUNDAUDIT(9, "退款审核中"),
    REFUNDING(10, "退款中"),
    REFUNDSUCC(11, "退款成功"),
    REFUNDFAIL(12, "退款失败"),
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
