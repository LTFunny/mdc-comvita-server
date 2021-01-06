package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RuleDefaultEnum
 * 是否默认(0-否 1-是)
 * @author linkq
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RuleDefaultEnum {

    /**
     * 不是默认
     */
    NOT_DEFAULT(0, "非默认"),
    /**
     * 是默认
     */
    DEFAULT(1, "默认")
    ;

    RuleDefaultEnum(int type, String name) {
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
