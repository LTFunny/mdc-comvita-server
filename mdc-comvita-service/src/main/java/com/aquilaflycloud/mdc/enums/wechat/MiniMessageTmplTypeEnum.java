package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MiniMessageTmplTypeEnum
 *
 * @author star
 * @date 2020-03-04
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum MiniMessageTmplTypeEnum {
    // 小程序订阅消息类型
    ONCESUBSCRIBE(2, "一次性订阅"),
    FOREVERSUBSCRIBE(3, "长期订阅"),
    ;

    MiniMessageTmplTypeEnum(int type, String name) {
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
