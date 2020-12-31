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
    RESERVATION_DELIVERY(1,"待预约+待提货"),
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
