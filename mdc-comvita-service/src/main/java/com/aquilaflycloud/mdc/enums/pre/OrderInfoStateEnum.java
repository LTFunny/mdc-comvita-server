package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author pengyongliang
 * @Date 2020/12/29 16:00
 * @Version 1.0
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum  OrderInfoStateEnum {

    //订单状态
    STAYCONFIRM(1, "待确认"),
    STAYRESERVATION(2, "待预约"),
    PARTRESERVATION(3, "部分预约"),
    RESERVATION(4, "已预约"),
    WAITINGDELIVERY (5, "待提货"),
    PARTWAITINGDELIVERY(6, "部分待提货"),
    STAYSENDGOODS(7, "待发货"),
    STAYSIGN(8,"待签收"),
    BEENCOMPLETED(9,"已经完成"),
    AFTERSALEREFUND(10,"售后/退款"),
    CONFIRMRECEIPT(11,"确认签收"),
    ;

    OrderInfoStateEnum(int type, String name) {
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
