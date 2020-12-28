package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ReceiveSourceEnum
 *
 * @author star
 * @date 2020-03-25
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ReceiveSourceEnum {
    // 优惠券领取来源类型
    COUPON(1, "领券中心"),
    EXCHANGE(2, "兑换商城"),
    LOTTERY(3, "欢乐抽奖"),
    ;

    ReceiveSourceEnum(int type, String name) {
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