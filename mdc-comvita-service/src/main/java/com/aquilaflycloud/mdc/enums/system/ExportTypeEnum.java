package com.aquilaflycloud.mdc.enums.system;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ExportTypeEnum
 *
 * @author star
 * @date 2019-12-09
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ExportTypeEnum {
    // 导出类型
    DATA_EXPORT(0, "数据导出"),
    MEMBER_INFO(1, "会员信息"),
    MEMBER_REWARD(2, "会员奖励记录"),
    SHOP_INFO(8, "店铺信息"),
    EXCHANGE_ORDER(9, "兑换订单"),
    COUPON_REL(10, "优惠券记录"),
    ;

    ExportTypeEnum(int type, String name) {
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
