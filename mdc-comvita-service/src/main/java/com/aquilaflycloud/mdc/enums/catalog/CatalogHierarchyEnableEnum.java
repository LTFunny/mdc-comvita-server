package com.aquilaflycloud.mdc.enums.catalog;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * CatalogHierarchyTypeEnum
 *
 * @author zengqingjie
 * @date 2020-08-13
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum CatalogHierarchyEnableEnum {
    // 状态类型
    NO(0, "禁用"),
    YES(1, "启用"),
    ;

    CatalogHierarchyEnableEnum(int type, String name) {
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
