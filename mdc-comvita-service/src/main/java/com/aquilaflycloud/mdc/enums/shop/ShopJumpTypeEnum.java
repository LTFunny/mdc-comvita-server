package com.aquilaflycloud.mdc.enums.shop;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopJumpTypeEnum
 *
 * @author zengqingjie
 * @date 2020-08-21
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopJumpTypeEnum {
    // 跳转类型
    MINI(1,"小程序"),
    OFFICIAL_WEBSITE(2,"官网"),
//    OFFICIAL_ACCOUNT(3,"公众号"),
    ;

    ShopJumpTypeEnum(int type, String name) {
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
