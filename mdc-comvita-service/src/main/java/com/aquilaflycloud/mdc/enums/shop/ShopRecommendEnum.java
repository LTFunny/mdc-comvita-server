package com.aquilaflycloud.mdc.enums.shop;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopRecommendEnum
 *
 * @author zengqingjie
 * @date 2020-04-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopRecommendEnum {
    // 是否推荐
    NORECOMMEND(0, "否"),
    RECOMMEND(1, "是"),
    ;

    ShopRecommendEnum(int type, String name) {
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
