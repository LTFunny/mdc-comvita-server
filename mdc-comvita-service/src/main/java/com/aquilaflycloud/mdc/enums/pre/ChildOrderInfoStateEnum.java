package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 18:15
 * @Version 1.0
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ChildOrderInfoStateEnum {

    /**
     * 订单子状态
     */
    STAYRESERVATION(1,"待预约"),
    PARTRESERVATION(2,"部分预约"),
    WAITINGDELIVERY(3,"待提货"),
    PARTWAITINGDELIVERY(4,"部分待提货"),
    STAYSENDGOODS(5, "待发货"),
    BEENCOMPLETED(9,"已完成"),
    ;


    ChildOrderInfoStateEnum(int type, String name) {
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
