package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MemberScanStateEnum
 *
 * @author zengqingjie
 * @date 202-06-28
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ConsumptionTicketStateEnum {
    //1:待审核;2:审核通过;3:审核不通过
    NO_AUDIT(1, "待审核"),
    PASS(2, "审核通过"),
    NO_PASS(3, "审核不通过"),
    ;

    ConsumptionTicketStateEnum(int type, String name) {
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
