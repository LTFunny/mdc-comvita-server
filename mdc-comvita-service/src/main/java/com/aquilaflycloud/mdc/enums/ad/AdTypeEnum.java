package com.aquilaflycloud.mdc.enums.ad;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AdTypeEnum
 *
 * @author star
 * @date 2019-11-16
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AdTypeEnum {
    // 广告跳转类型
    NORMAL(1, "普通广告"),
    COUPON(2, "优惠券"),
    LOTTERY(3, "欢乐抽奖"),
    /*OFFLINESIGN(4, "打卡有礼"),
    MISSION(5, "活动任务"),*/
    APPLY(6, "活动报名"),
    RECOMMEND(7, "最新推荐"),
    PUBLICARTICLE(8, "微信推文"),
    CUSTOMPAGE(9, "自定义路径"),
    ;

    AdTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
