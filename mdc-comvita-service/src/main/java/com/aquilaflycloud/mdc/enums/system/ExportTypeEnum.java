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
    EXCHANGE_ORDER(9, "兑换订单"),
    COUPON_REL(10, "优惠券记录"),
    PRE_PICKING_CARD(11, "提留卡信息"),
    SALES_VOUME(12,"销量报表"),
    ORDER_INFO(13,"订单报表"),
    READY_GOODS(14,"待发订单"),
    AFTER_SALES(15,"售后订单"),
    READY_INFO(16,"统计订单报表"),
    GUIDE_INFO(17,"导购员报表"),
    PRE_ACTIVITY_INFO(18,"预售活动报表"),
    FLASH_ACTIVITY_INFO(19,"快闪活动报表")
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
