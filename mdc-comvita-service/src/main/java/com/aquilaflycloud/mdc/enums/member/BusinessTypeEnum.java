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
    AD(1, "广告"),
    LOTTERY(2, "欢乐抽奖"),
//    OFFLINESIGN(3, "打卡有礼"),
    APPLY(4, "活动报名"),
    RECOMMEND(5, "最新推荐"),
    SCENICSPOTS(6, "景区信息"),
    SHOP(7, "商户标签"),
    EXCHANGEGOODS(8, "兑换商品"),
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
