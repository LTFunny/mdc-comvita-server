package com.aquilaflycloud.mdc.enums.system;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AccountTypeEnum
 *
 * @author star
 * @date 2019-12-04
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AccountTypeEnum {
    // 第三方账号类型
    TENCENTPOSITION(1, "腾讯位置服务"),
    EASYPAY(2, "惠云支付系统"),
    ;

    AccountTypeEnum(int type, String name) {
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
