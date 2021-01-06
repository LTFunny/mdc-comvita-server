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
    PRETAKE(2,"已预约"),
    ALSENDGOODS(3, "已发货"),
    TAKEN(4,"已提货"),
    REFUND(5,"已退货"),
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
