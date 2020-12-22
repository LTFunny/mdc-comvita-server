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
public enum CatalogHierarchyTypeEnum {
    //动态信息、游玩攻略、五力街区、本土手信、诚信商街
    DYNAMIC_INFO(1, "动态信息"),
    PLAY_STRATEGY(2, "游玩攻略"),
    BRAND_SELECT(3, "五力街区"),
    LOCAL_PRESENT(4, "本土手信"),
    SINCERITY_STREE(5, "诚信商街"),
    SMART_SERVICE(6, "智慧服务"),
    ;

    CatalogHierarchyTypeEnum(int type, String name) {
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
