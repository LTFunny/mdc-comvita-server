package com.aquilaflycloud.mdc.enums.alipay;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SiteSourceEnum
 *
 * @author star
 * @date 2020-03-29
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SiteSourceEnum {
    // 支付宝应用类型
    PUBLIC(1, "生活号"),
    TINY(2, "小程序"),
    APP(3, "网页&移动应用"),
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
