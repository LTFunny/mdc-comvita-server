package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * CouponStateEnum
 *
 * @author star
 * @date 2020-03-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum CouponStateEnum {
    // 优惠券状态类型
    UNACTIVATION(0, "待激活"),
    NORMAL(1, "进行中"),
    DISABLE(2, "已下线"),
    EXPIRING(3, "即将结束"),
    EXPIRED(4, "已结束"),
    NOTRECEIVE(5, "即将开抢"),
    NOTISSUED(6, "未上线"),
    PENDING(7, "待审核"),
    REJECT(8, "审核不通过"),
    SOLDOUT(9, "已领完"),
    ;


    CouponStateEnum(int type, String name) {
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
