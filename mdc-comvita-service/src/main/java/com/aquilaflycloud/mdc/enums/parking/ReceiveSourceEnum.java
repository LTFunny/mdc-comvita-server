package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ReceiveSourceEnum
 *
 * @author star
 * @date 2020-03-25
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ReceiveSourceEnum {
    // 停车券领取来源类型
    BACKEND(1, "后台派发"),
    EXCHANGE(2, "兑换商城");

    ReceiveSourceEnum(int type, String name) {
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