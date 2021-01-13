package com.aquilaflycloud.mdc.enums.folksonomy;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * FolksonomyNodeTypeEnum
 *
 * @author star
 * @date 2021/1/13
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum FolksonomyNodeTypeEnum {
    // 标签类型
    CATALOG(1, "目录"),
    FOLKSONOMY(2, "标签"),
    ;

    FolksonomyNodeTypeEnum(int type, String name) {
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
