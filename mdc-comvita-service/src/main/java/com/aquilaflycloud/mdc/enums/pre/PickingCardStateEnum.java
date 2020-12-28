package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * PickingCardStateEnum
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum PickingCardStateEnum {
    NO_SALE(1, "未销售"),
    SALE(2, "已销售"),
    RESERVE(3, "已预约"),
    VERIFICATE(4, "已核销"),
    CANCEL(5, "已作废"),
    ;

    PickingCardStateEnum(int type, String name) {
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
