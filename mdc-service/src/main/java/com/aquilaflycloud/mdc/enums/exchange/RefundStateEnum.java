package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RefundStateEnum
 *
 * @author star
 * @date 2020-03-21
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RefundStateEnum {
    // 退款状态类型
    REFUNDAUDIT(1, "退款审核中"),
    REFUNDREVOKE(2, "退款撤销"),
    REFUNDREJECT(3, "退款审核驳回"),
    REFUNDCONFIRM(4, "退款待确认"),
    REFUNDING(5, "退款中"),
    REFUNDSUCC(6, "退款成功"),
    REFUNDFAIL(7, "退款失败"),
    ;

    RefundStateEnum(int type, String name) {
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
