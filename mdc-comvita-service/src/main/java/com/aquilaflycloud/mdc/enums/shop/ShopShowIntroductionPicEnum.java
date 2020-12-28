package com.aquilaflycloud.mdc.enums.shop;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopShowIntroductionPicEnum
 *
 * @author zengqingjie
 * @date 2020-04-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopShowIntroductionPicEnum {
    // 是否使用介绍图
    NOUSE(0, "使用商户LOGO图"),
    USE(1, "使用店铺介绍图"),
    ;

    ShopShowIntroductionPicEnum(int type, String name) {
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
