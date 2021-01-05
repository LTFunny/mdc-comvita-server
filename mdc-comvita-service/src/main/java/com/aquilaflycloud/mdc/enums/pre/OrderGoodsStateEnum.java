package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * OrderGoodsStateEnum
 *
 * @author star
 * @date 2021/1/5
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum OrderGoodsStateEnum {

    /**
     * 订单商品状态
     */
    PREPARE(1,"待预约"),
    PRETAKE(2,"待提货"),
    TAKEN(3,"已提货"),
    REFUND(4,"已退货"),
    ;

    OrderGoodsStateEnum(int type, String name) {
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
