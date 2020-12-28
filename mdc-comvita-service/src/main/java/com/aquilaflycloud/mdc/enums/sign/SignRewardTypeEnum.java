package com.aquilaflycloud.mdc.enums.sign;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SignRewardTypeEnum
 *
 * @author star
 * @date 2020-04-06
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SignRewardTypeEnum {
    // 打卡奖励类型
    REWARD(1, "奖励"),
    //COUPON(2, "优惠券"),
    ;

    SignRewardTypeEnum(int type, String name) {
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
