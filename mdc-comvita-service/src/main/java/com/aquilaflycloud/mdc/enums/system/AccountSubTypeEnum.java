package com.aquilaflycloud.mdc.enums.system;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AccountSubTypeEnum
 *
 * @author star
 * @date 2020-03-24
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AccountSubTypeEnum {
    // 第三方账号子类型
    EASYPAYQUERY(2, 0, "支付查询"),
    EASYPAYTICKET(2, 1, "购票支付"),
    EASYPAYPARKING(2, 2, "停车缴费"),
    EASYPAYEXCHANGE(2, 3, "兑换商城"),
    ;

    AccountSubTypeEnum(int parent, int type, String name) {
        this.parent = parent;
        this.type = type;
        this.name = name;
    }

    private final int parent;

    @EnumValue
    private final int type;

    private final String name;

    public int getParent() {
        return parent;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
