package com.aquilaflycloud.mdc.enums.shop;


import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopInfoScoreEnum
 *
 * @author zengqingjie
 * @date 2020-06-05
 */

@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopCommentScoreEnum {
    // 评分类型
    ALL(1, "总体评分"),
    TASTE(2, "口味评分"),
    ENVIRONMENT(3, "环境评分"),
    SERVICE(4, "服务评分"),
    ;

    ShopCommentScoreEnum(int type, String name) {
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
