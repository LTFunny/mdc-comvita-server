package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * GoodsStateEnum
 *
 * @author star
 * @date 2020-03-15
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum GoodsStateEnum {
    // 商品状态类型
    NORMAL(1, "正常"),
    NOTSHELVE(2, "待上架"),
    OFFSHELVE(3, "下架"),
    SOLDOUT(4, "售罄"),
    INVALID(5, "失效"),
    PENDING(6, "待审核"),
    REJECT(7, "不通过"),
    ;

    GoodsStateEnum(int type, String name) {
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
