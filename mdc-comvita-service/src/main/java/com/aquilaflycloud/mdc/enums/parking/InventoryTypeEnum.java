package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * InventoryTypeEnum
 *
 * @author star
 * @date 2020-01-10
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum InventoryTypeEnum {
    // 库存类型
    QUOTA(1, "额度"),
    AMOUNT(2, "数量"),
    ;

    InventoryTypeEnum(int type, String name) {
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
