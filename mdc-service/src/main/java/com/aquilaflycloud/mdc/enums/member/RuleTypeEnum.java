package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RuleTypeEnum
 *
 * @author star
 * @date 2019-11-14
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RuleTypeEnum {
    // 奖励规则类型
    CLEAN(0, "奖励清零规则"),
    SIGN(1, "每日签到规则"),
    SCAN(2, "消费奖励规则"),
    ;

    RuleTypeEnum(int type, String name) {
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
