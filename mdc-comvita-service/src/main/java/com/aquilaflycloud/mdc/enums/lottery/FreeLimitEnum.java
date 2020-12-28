package com.aquilaflycloud.mdc.enums.lottery;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * FreeLimitEnum
 *
 * @author star
 * @date 2020/12/11
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum FreeLimitEnum {
    // 抽奖活动类型
    NONE(0, "无免费条件"),
    AUTHTIME(1, "授权时间"),
    ;

    FreeLimitEnum(int type, String name) {
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
