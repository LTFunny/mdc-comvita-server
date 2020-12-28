package com.aquilaflycloud.mdc.enums.lottery;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * LotteryStateEnum
 *
 * @author star
 * @date 2020-04-06
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum LotteryStateEnum {
    // 抽奖活动状态类型
    NORMAL(1, "进行中"),
    DISABLE(2, "停用"),
    PENDING(3, "未开始"),
    EXPIRED(4, "已结束"),
    ;

    LotteryStateEnum(int type, String name) {
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
