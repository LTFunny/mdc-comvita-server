package com.aquilaflycloud.mdc.enums.sign;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * LimitTypeEnum
 *
 * @author star
 * @date 2020-05-07
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum LimitTypeEnum {
    // 报名限制类型
    DAILY(1, "每天一次"),
    TOTAL(2, "每个活动一次"),
    ;

    LimitTypeEnum(int type, String name) {
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
