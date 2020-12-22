package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RelTypeEnum
 *
 * @author star
 * @date 2020-04-19
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RecommendTypeEnum {
    // 商品推荐类型
    HOME(1, "首页推荐"),
    GOODGIFT(2, "兑换好礼"),
    RECOMMEND(3, "好物推荐"),
    POPULAR(4, "热门商品"),
    ;

    RecommendTypeEnum(int type, String name) {
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
