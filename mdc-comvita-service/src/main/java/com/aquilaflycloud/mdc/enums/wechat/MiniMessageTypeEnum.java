package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MiniMessageTypeEnum
 *
 * @author star
 * @date 2020-03-04
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum MiniMessageTypeEnum {
    // 小程序订阅消息业务类型
//    APPLYRECORDAUDIT(1, "报名记录审核", "活动名称, 报名结果, 提示, 通知时间"),
    PREORDERCHANGE(2, "订单状态变化通知", "订单编号, 订单状态, 更新时间, 备注"),
    PREORDERDELIVERY(3, "订单待发货提醒", "订单编号, 收件人, 收货地址, 物流单号, 备注"),
    PREORDERREFUND(4, "退货审核结果通知", "订单编号, 退货金额, 审核结果, 温馨提示"),
    PREORDERALLCONSUME(5, "全部核销成功通知", "订单号, 商品名称, 订单金额, 温馨提示, 核销总额"),
    PREGOODSCONSUME(6, "核销成功通知", "门店, 核销时间, 温馨提示"),
    PREGOODSTAKE(7, "自提提醒", "提货码, 提货门店, 自提日期, 温馨提示"),
    PREORDERAUDIT(8, "审核结果提醒", "订单号, 温馨提示, 审核结果, 审核内容"),
    ;

    MiniMessageTypeEnum(int type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    @EnumValue
    private final int type;

    private final String name;

    private final String desc;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
