package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RuleTypeEnum
 * 规则类型
 * 规则类型(3-下单满减、2-下单折扣、1-下单即送)
 * @author linkq
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RuleTypeEnum {

    ORDER_GIFTS(1, "下单即送"),
    ORDER_DISCOUNT(2, "下单折扣"),
    ORDER_FULL_REDUCE(3, "下单满减")
    ;

    RuleTypeEnum(int type, String name) {
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
