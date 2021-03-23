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
    /**
     * 数据导出
     */
    DATA_EXPORT(0, "数据导出"),
    /**
     * 会员信息
     */
    MEMBER_INFO(1, "会员信息"),
    /**
     * 会员奖励记录
     */
    MEMBER_REWARD(2, "会员奖励记录"),
    /**
     * 兑换订单
     */
    EXCHANGE_ORDER(9, "兑换订单"),
    /**
     * 优惠券记录
     */
    COUPON_REL(10, "优惠券记录"),
    /**
     * 提留卡信息
     */
    PRE_PICKING_CARD(11, "提留卡信息"),
    /**
     * 销量报表
     */
    SALES_VOUME(12,"销量报表"),
    /**
     * 订单报表
     */
    ORDER_INFO(13,"订单报表"),
    /**
     * 待发订单
     */
    READY_GOODS(14,"待发订单"),
    /**
     * 售后订单
     */
    AFTER_SALES(15,"售后订单"),
    /**
     * 统计订单报表
     */
    READY_INFO(16,"统计订单报表"),
    /**
     * 导购员报表
     */
    GUIDE_INFO(17,"导购员报表"),
    /**
     * 预售活动报表
     */
    PRE_ACTIVITY_INFO(18,"预售活动报表"),
    /**
     * 快闪活动报表
     */
    FLASH_ACTIVITY_INFO(19,"快闪活动报表"),
    /**
     * 普通订单报表
     */
    FLASH_ORDER_INFO(20,"普通订单报表")
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
