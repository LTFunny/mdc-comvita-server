package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * BusinessTypeEnum
 *
 * @author star
 * @date 2019-11-19
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum BusinessTypeEnum {
    // 业务功能类型
    /**
     * 广告
     */
    AD(1, "广告"),
    /**
     * 欢乐抽奖
     */
    LOTTERY(2, "欢乐抽奖"),
//    OFFLINESIGN(3, "打卡有礼"),
    /**
     * 活动报名
     */
    APPLY(4, "活动报名"),
    /**
     * 最新推荐
     */
    RECOMMEND(5, "最新推荐"),
    /**
     * 兑换商品
     */
    EXCHANGEGOODS(8, "兑换商品"),
    /**
     * 营销商品
     */
    PREGOODS(9, "营销商品"),
    /**
     * 营销活动
     */
    PREACTIVITY(10, "营销活动"),
    /**
     * 活动点评
     */
    ACTIVITYCOMMENT(11, "活动点评")
    ;

    BusinessTypeEnum(int type, String name) {
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
