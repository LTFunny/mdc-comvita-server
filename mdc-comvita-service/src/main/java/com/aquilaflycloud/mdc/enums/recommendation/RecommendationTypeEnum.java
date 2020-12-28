package com.aquilaflycloud.mdc.enums.recommendation;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RecommendationTypeEnum
 *
 * @author star
 * @date 2020-03-28
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RecommendationTypeEnum {
    // 推荐类型
    TEXT(1, "文本图片"),
    COUPON(2, "优惠券"),
    APPLY(3, "活动报名"),
    LOTTERY(4,"抽奖活动"),
    /*H5(5,"H5任务"),
    GROWTH(6,"成长任务"),
    OFFLINESIGN(7,"打卡有礼"),*/
    PRODUCTINFO(8, "门票产品"),
    ;

    RecommendationTypeEnum(int type, String name) {
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
