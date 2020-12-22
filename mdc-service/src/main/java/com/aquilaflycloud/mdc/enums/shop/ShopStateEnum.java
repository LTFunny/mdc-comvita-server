package com.aquilaflycloud.mdc.enums.shop;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopStateEnum
 *
 * @author zengqingjie
 * @date 2020-04-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopStateEnum {
    // 商户状态类型
    INTENTION(0,"意向"),
    SIGN_CONTRACT(1,"签约"),
    SUSPEND_COOPERATION(2,"暂停合作"),
    IN_OPERATION(3,"营业中"),
    ENTER_ARENA(4,"进场"),
    DECORATION(5,"装修中"),
    NOT_IN_OPERATION(6,"非营业中"),
    EMPTY(7,"空置")//店铺显示状态专用
    ;

    ShopStateEnum(int type, String name) {
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
