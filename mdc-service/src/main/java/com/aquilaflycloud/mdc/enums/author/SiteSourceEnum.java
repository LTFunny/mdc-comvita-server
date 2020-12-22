package com.aquilaflycloud.mdc.enums.author;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SiteSourceEnum
 *
 * @author star
 * @date 2020-04-04
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SiteSourceEnum {
    // 应用类型
    UNIVERSAL(0, "通用应用"),
    WECAHATPUBLIC(1, "微信公众号"),
    WECAHATMINI(2, "微信小程序"),
    ALIPAYPUBLIC(3, "支付宝生活号"),
    ALIPAYTINY(4, "支付宝小程序"),
    ALIPAYAPP(5, "支付宝网页&移动应用"),
    ;

    SiteSourceEnum(int type, String name) {
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
