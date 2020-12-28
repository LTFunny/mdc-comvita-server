package com.aquilaflycloud.mdc.enums.shop;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopComplaintAnonymousEnum
 *
 * @author zengqingjie
 * @date 2020-04-23
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopComplaintAnonymousEnum {
    // 评价状态类型
    ANONYMOUS(0, "匿名"),
    NOANONYMOUS(1, "不匿名"),
    ;

    ShopComplaintAnonymousEnum(int type, String name) {
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
