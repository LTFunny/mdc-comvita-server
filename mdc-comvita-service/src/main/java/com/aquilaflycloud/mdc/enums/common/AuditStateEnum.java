package com.aquilaflycloud.mdc.enums.common;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AuditStateEnum
 *
 * @author star
 * @date 2019-11-18
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AuditStateEnum {
    // 审核状态类型
    PENDING(1, "待审"),
    APPROVE(2, "通过"),
    REJECT(3, "驳回"),
    ;

    AuditStateEnum(int type, String name) {
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
