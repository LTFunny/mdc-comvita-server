package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * VerificateStateEnum
 *
 * @author star
 * @date 2020-03-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum VerificateStateEnum {
    // 优惠券核销状态类型
    NOTCONSUME(1, "未核销"),
    CONSUMED(2, "已核销"),
    EXPIRED(3, "已过期"),
    REVOKED(4, "已撤回"),
    ;

    VerificateStateEnum(int type, String name) {
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
