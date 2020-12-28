package com.aquilaflycloud.mdc.enums.catalog;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * CatalogTypeEnum
 *
 * @author star
 * @date 2020-03-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum CatalogTypeEnum {
    // 分类类型
    NORMAL(1, "普通"),
    OTHER(2, "其他"),
    ;

    CatalogTypeEnum(int type, String name) {
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