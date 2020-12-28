package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AliPayUserStatusEnum
 *
 * @author star
 * @date 2019-10-25
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AliPayUserStatusEnum {
    // 支付宝用户状态类型
    Q(1, "快速注册用户"),
    T(2, "已认证用户"),
    B(3, "被冻结账户"),
    W(4, "已注册，未激活的账户"),
    ;

    AliPayUserStatusEnum(int type, String name) {
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
