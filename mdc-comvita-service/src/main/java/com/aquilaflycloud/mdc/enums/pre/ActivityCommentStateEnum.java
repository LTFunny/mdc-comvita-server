package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 *
 * 状态 1-待审核 2-审核通过 3-审核不通过
 * @author linkq
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ActivityCommentStateEnum {
    /**
     * 待审核
     */
    WAITING(1, "待审核"),
    /**
     * 审核通过
     */
    PASS(2,"审核通过"),
    /**
     * 审核不通过
     */
    NO_PASS(3,"审核不通过")
    ;

    ActivityCommentStateEnum(int type, String name) {
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
