package com.aquilaflycloud.mdc.enums.recommendation;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RecommendationStateEnum
 *
 * @author star
 * @date 2020-03-27
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RecommendationStateEnum {
    // 推荐状态类型
    NORMAL(1, "正常"),
    DISABLE(2, "停用"),
    NOTPUBLISH(3, "未发布");

    RecommendationStateEnum(int type, String name) {
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
