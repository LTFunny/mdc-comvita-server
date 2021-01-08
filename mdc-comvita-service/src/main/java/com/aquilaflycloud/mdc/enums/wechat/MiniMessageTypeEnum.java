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
    //订单签收(赠品签收)
    PREORDERSIGN(2, "订单签收通知(赠品签收)", "订单编号, 商品名称, 物流公司, 快递单号, 温馨提示"),
    //订单发货(赠品发货)
    PREORDERDELIVERY(3, "订单发货通知(赠品发货)", "订单编号, 收货地址, 快递公司, 快递单号, 温馨提示"),
    //某个商品(不包括赠品)签收
    PREORDERGOODSSIGN(4, "订单签收通知(预售签收)", "商品名称, 物流公司, 快递单号, 温馨提示"),
    //某个商品(不包括赠品)发货
    PREORDERGOODSELIVERY(5, "订单发货通知(预售发货)", "商品信息, 收货地址, 快递公司, 快递单号, 温馨提示"),
    //预约结果通知
    PREORDERCHANGE(6, "订单状态变化通知", "订单编号, 订单状态, 更新时间, 备注"),
    //售后订单
    PREORDERREFUNDAUDIT(7, "退货审核结果通知", "订单编号, 退货金额, 审核结果, 温馨提示"),
    //提交订单
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
