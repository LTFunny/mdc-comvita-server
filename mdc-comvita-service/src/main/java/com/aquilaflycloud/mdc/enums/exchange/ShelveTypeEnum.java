package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShelveTypeEnum
 *
 * @author star
 * @date 2020-03-15
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShelveTypeEnum {
    // 上架类型
    IMMEDIATELY(1, "立即上架"),
    REGULAR(2, "定时上架"),
    ;

    ShelveTypeEnum(int type, String name) {
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
