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
    WAITINGDELIVERY (3, "已预约"),
    STAYSENDGOODS(4, "待发货"),
    STAYSIGN(5,"待签收"),
    BEENCOMPLETED(6,"已完成"),
    //给会员订单显示标识用的
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
