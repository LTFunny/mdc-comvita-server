package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * ChargeStateEnum
 *
 * @author star
 * @date 2020-02-14
 */
@JSONType(serializeEnumAsJavaBean = true)
public enum ChargeStateEnum {
    // 安居宝接口返回状态类型
    NOTPAY("0000", "未缴费"),
    NOTNEEDPAY("0006", "暂不需缴费"),
    NOTINPARK("0179", "车辆不在场"),
    FREEPAY("0190", "免费卡"),
    EXPIRE("0191", "月保过期"),
    CANNOTPAY("0500", "已缴费未离场"),
    OVERTIME("0501", "超时未离场"),
    FREE("0503", "入场未需缴费"),
    COUPON("0506", "使用停车券"),
    ;

    ChargeStateEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private final String type;

    private final String name;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
