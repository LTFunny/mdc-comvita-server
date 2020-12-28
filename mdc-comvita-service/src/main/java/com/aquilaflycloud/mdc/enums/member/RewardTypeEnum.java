package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RewardTypeEnum
 *
 * @author star
 * @date 2019-12-17
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RewardTypeEnum {
    // 奖励类型
    GROWTH(1, "成长值"),
    SCORE(2, "积分"),
    ACTIVE(3, "活跃值"),
    ;

    RewardTypeEnum(int type, String name) {
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
