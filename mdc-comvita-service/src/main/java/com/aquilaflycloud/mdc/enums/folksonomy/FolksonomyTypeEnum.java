package com.aquilaflycloud.mdc.enums.folksonomy;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * FolksonomyTypeEnum
 *
 * @author star
 * @date 2019-11-28
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum FolksonomyTypeEnum {
    // 标签类型
    BUSINESS(1, "业务功能标签"),
    MEMBER(2, "会员兴趣标签"),
    ;

    FolksonomyTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
