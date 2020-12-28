package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SiteSiteTypeEnum
 *
 * @author star
 * @date 2019-10-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SiteSiteTypeEnum {
    // 微信应用来源类型
    MALL(1, "商场"),
    SHOP(2, "商户"),
    ;

    SiteSiteTypeEnum(int type, String name) {
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
