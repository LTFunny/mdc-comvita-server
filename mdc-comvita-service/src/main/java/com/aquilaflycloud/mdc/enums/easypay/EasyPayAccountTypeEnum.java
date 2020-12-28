package com.aquilaflycloud.mdc.enums.easypay;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * EasyPayAccountTypeEnum
 *
 * @author star
 * @date 2019-12-07
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum EasyPayAccountTypeEnum {
    // 支付账号类型
    QUERY(0, "查询支付账号"),
    EXCHANGE(3, "兑换商城支付账号"),
    ;

    EasyPayAccountTypeEnum(int type, String name) {
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
