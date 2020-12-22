package com.aquilaflycloud.mdc.enums.lottery;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * PrizeTypeEnum
 *
 * @author star
 * @date 2020-04-06
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum PrizeTypeEnum {
    // 抽奖奖品类型
    REWARD(1, "奖励"),
    COUPON(2, "优惠券"),
    ;

    PrizeTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
