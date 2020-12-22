package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RewardSourceEnum
 *
 * @author star
 * @date 2019-12-27
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RewardSourceEnum {
    // 奖励来源类型
    CLEAN(0, "自动清零"),
    SYSTEM(1, "系统操作"),
    SIGN(2, "每日签到"),
    SCAN(3, "消费扫码"),
    EXCHANGE(4, "兑换商品"),
    LOTTERY(5, "欢乐抽奖"),
    OFFLINESIGN(6, "线下打卡"),
    MISSION(7, "会员任务"),
    ;

    RewardSourceEnum(int type, String name) {
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
