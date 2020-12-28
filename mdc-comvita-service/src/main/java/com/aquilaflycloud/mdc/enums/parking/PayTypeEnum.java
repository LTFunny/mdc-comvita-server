package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * PayTypeEnum
 *
 * @author star
 * @date 2020-06-24
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum PayTypeEnum {
    // 停车支付类型
    TEMPPARKING(1, "临停"),
    ;

    PayTypeEnum(int type, String name) {
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
