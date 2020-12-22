package com.aquilaflycloud.mdc.enums.alipay;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AlipayOauth2ScopeEnum
 *
 * @author star
 * @date 2019-12-25
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AlipayOauth2ScopeEnum {
    // 支付宝oauth2授权类型
    auth_base(1, "静默授权"),
    auth_user(2, "用户授权"),
    ;

    AlipayOauth2ScopeEnum(int type, String name) {
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
