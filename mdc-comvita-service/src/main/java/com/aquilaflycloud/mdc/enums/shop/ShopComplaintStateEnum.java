package com.aquilaflycloud.mdc.enums.shop;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShopComplaintStateEnum
 *
 * @author zengqingjie
 * @date 2020-04-22
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShopComplaintStateEnum {
    // 申述状态类型
    ACCEPT(1, "已受理"),
    PASS(2, "已通过"),
    NOPASS(3, "不通过"),
    APPEAL_SUCCESS(4, "申诉成功"),
    APPEAL_FAIL(5, "申诉失败"),
    APPEALIMG(6, "申诉中"),
    ;

    ShopComplaintStateEnum(int type, String name) {
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
