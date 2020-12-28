package com.aquilaflycloud.mdc.enums.sign;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SignStateEnum
 *
 * @author star
 * @date 2020-05-07
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SignStateEnum {
    // 打卡活动状态类型
    NORMAL(1, "进行中"),
    DISABLE(2, "停用"),
    PENDING(3, "未开始"),
    EXPIRED(4, "已结束"),
    ;

    SignStateEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
