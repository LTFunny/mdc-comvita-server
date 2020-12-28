package com.aquilaflycloud.mdc.enums.parking;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * InvoiceTypeEnum
 *
 * @author star
 * @date 2020-06-24
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum InvoiceTypeEnum {
    // 发票单位类型
    PERSONAL(1, "个人"),
    COMPANY(2, "公司"),
    ;

    InvoiceTypeEnum(int type, String name) {
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
