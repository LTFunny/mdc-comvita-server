package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * EventTypeEnum
 *
 * @author star
 * @date 2019-11-19
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum EventTypeEnum {
    // 事件类型
    READ(1, "浏览"),
    SHARE(2, "分享"),
    SHOW(3, "展示"),
    CLICK(4, "点击"),
    LIKE(5, "点赞"),
    ;

    EventTypeEnum(int type, String name) {
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
