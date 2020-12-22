package com.aquilaflycloud.mdc.enums.system;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SqlParamTypeEnum
 *
 * @author star
 * @date 2020/9/1
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SqlParamTypeEnum {
    // sql入参字段类型
    STRING(1, "字符串"),
    TIME(2, "时间"),
    ARRAY(3, "数组"),
    ;

    SqlParamTypeEnum(int type, String name) {
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
