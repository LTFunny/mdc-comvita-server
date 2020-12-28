package com.aquilaflycloud.mdc.enums.shop;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopShowIntroductionDescEnum
 *
 * @author zengqingjie
 * @date 2020-04-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopShowIntroductionDescEnum {
    // 是否使用备注
    NOUSE(0, "使用商户备注"),
    USE(1, "使用店铺简介"),
    ;

    ShopShowIntroductionDescEnum(int type, String name) {
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
