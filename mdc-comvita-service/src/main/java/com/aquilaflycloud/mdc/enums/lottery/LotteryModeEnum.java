package com.aquilaflycloud.mdc.enums.lottery;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * LotteryModeEnum
 *
 * @author star
 * @date 2020-04-06
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum LotteryModeEnum {
    // 抽奖活动类型
    GOLDENEGG(1, "砸金蛋"),
    TURNTABLE(2, "大转盘"),
    ;

    LotteryModeEnum(int type, String name) {
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
