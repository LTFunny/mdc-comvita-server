package com.aquilaflycloud.mdc.enums.coupon;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * CouponTypeEnum
 *
 * @author star
 * @date 2020-03-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum CouponTypeEnum {
    // 优惠券类型
    DISCOUNT(1,1, "折扣券"),
    REDUCTION(1,2, "满减券"),
    UNLIMITED(1,3, "代金券"),
    OTHER(1,4, "兑换券"),
    ;

    CouponTypeEnum(int parent, int type, String name) {
        this.parent = parent;
        this.type = type;
        this.name = name;
    }

    private final int parent;

    @EnumValue
    private final int type;

    private final String name;

    public int getParent() {
        return parent;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
