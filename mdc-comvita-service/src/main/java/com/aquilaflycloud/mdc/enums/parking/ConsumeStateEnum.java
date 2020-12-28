package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ConsumeStateEnum
 *
 * @author star
 * @date 2020-01-15
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ConsumeStateEnum {
    // 核销状态类型
    NOTCONSUME(1, "未核销"),
    CONSUMED(2, "已核销"),
    EXPIRED(3, "已过期"),
    NOTACTIVE(4, "未生效"),
    REVOKED(5, "已撤回"),
    ;

    ConsumeStateEnum(int type, String name) {
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
