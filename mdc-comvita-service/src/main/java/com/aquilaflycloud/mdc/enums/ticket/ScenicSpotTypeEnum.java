package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ScenicSpotTypeEnum
 *
 * @author star
 * @date 2019-09-27
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ScenicSpotTypeEnum {
    // 三馆类型
    OCEAN(0, "海洋馆"),
    MUSEUM(1, "博物馆"),
    RAINFOREST(2, "雨林馆");

    ScenicSpotTypeEnum(int type, String name) {
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
