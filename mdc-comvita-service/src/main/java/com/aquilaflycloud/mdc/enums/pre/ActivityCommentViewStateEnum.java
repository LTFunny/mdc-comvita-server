package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 展示状态 1-隐藏 2公开
 * @author linkq
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ActivityCommentViewStateEnum {
    /**
     * 隐藏
     */
    HIDE(1, "隐藏"),
    /**
     * 公开
     */
    OPEN(2,"公开")
    ;

    ActivityCommentViewStateEnum(int type, String name) {
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
