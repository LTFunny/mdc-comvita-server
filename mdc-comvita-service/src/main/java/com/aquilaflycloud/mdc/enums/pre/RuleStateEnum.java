package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RuleStateEnum
 * 状态(1-已启用、0-已停用)
 * @author linkq
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RuleStateEnum {

    /**
     * 已启用
     */
    ENABLE(1, "已启用"),
    /**
     * 已停用
     */
    DISABLE(0, "已停用")
    ;

    RuleStateEnum(int type, String name) {
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
