package com.aquilaflycloud.mdc.enums.alipay;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SiteStatusEnum
 *
 * @author star
 * @date 2019-12-23
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SiteStatusEnum {
    // 支付宝应用授权状态类型
    VALID(1, "有效状态"),
    INVALID(0, "无效状态"),
    ;

    SiteStatusEnum(int type, String name) {
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
