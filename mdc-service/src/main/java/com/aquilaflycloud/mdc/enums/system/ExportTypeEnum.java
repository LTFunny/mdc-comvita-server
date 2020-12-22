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
    TICKET_REFUND_INFO(3, "退票记录"),
    TICKET_VERIFICATE_INFO(4, "门票核销记录"),
    TICKET_CHANNEL_SALE_INFO(5, "渠道销售概况"),
    TICKET_PRODUCT_SALE_INFO(6, "产品售卖概况"),
    TICKET_PRODUCT_INFO_RECORD(7, "产品售卖概况"),
    SHOP_INFO(8, "店铺信息"),
    EXCHANGE_ORDER(9, "兑换订单"),
    COUPON_REL(10, "优惠券记录"),
    PARKING_COUPON_REL(11, "停车券派发记录"),
    PARKING_DETAIL_ANALYSIS(12, "停车券派发明细分析"),
    PARKING_SUMMARY_ANALYSIS(13, "停车券派发汇总分析"),
    PARKING_RECORD_ANALYSIS(14, "停车券派发记录名称金额分析"),
    MEMBER_CONSUMPTION_TICKET_INFO(15, "拍照积分记录"),
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
