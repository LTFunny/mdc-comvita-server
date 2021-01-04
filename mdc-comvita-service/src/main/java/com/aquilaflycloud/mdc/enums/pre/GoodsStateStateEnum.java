package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * GoodsTypeStateEnum
 *
 * @author zly
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum GoodsStateStateEnum {
    // 商品状态0-在售 1-下架
    ONSALE(0, "在售"),
    OFFTHESHELF(1, "下架");

    GoodsStateStateEnum(int type, String name) {
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
