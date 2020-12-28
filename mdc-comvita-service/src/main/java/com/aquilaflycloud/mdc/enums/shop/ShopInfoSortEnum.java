package com.aquilaflycloud.mdc.enums.shop;


import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopInfoSortEnum
 *
 * @author zengqingjie
 * @date 2020-06-05
 */

@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopInfoSortEnum {
    // 排序类型
    SMART_SORT(1, "智能排序"),
    ALL_SORT(2, "总体优先"),
    TASTE_SORT(3, "口味优先"),
    ENVIRONMENT_SORT(4, "环境优先"),
    SERVICE_SORT(5, "服务优先"),
    ;

    ShopInfoSortEnum(int type, String name) {
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
