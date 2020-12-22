package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 订单状态
 * 状态（未付款 0;已付款 10;未取票超时未核销11;已取票 20;已取票超时未核销 21;已部分核销 22;全部核销 23;部分退款 30;全部退款31;退款审核中 32;已作废 40）
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum OrderInfoStatusEnum {
    // 购票订单状态类型
    UN_PAID(0, "未付款"),
    PAID(10, "已付款"),
//    NOT_COLLECTION_NOT_VERIFICATION(11, "未取票超时未核销"),
    FAIL_PAID(12, "付款失败"),
    COLLECTION(20, "已取票"),
//    COLLECTION_NOT_VERIFICATION(21, "已取票超时未核销"),
//    PART_VERIFICATION(22, "已部分核销"),
    PART_VERIFICATION(22, "已部分入园"),
//    ALL_VERIFICATION(23, "全部核销"),
    ALL_VERIFICATION(23, "已入园"),
//    PART_REFUND(30, "部分退款"),
    ALL_REFUND(31, "全部退款"),
//    REFUND_REVIEW(32, "退款审核中"),
    REFUNDING(33, "退款中"),
    FAIL_REFUNDING(34, "退款失败"),
    INVALID(40, "已取消"),
//    CANCELING(41, "订单取消中")
    ;

    OrderInfoStatusEnum(int type, String name) {
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
