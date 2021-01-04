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
public enum GoodsTypeStateEnum {
    // 商品类型1-预售商品、2-赠品、3-普通商品
    PRESALE(1, "预售商品"),
    GIFT(2, "赠品"),
    ORDINARY(3, "普通商品");

    GoodsTypeStateEnum(int type, String name) {
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
