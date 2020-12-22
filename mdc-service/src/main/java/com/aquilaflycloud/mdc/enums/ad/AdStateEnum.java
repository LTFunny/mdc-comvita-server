package com.aquilaflycloud.mdc.enums.ad;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AdStateEnum
 *
 * @author star
 * @date 2019-11-18
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AdStateEnum {
    // 广告状态类型
    NORMAL(1, "启用"),
    DISABLE(2, "停用"),
    PENDING(3, "待生效"),
    EXPIRED(4, "已过期"),
    ;

    AdStateEnum(int type, String name) {
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
