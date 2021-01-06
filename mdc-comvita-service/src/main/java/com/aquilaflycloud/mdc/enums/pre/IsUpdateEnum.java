package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author pengyongliang
 * @Date 2021/1/6 10:45
 * @Version 1.0
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum IsUpdateEnum {

    // 是否可修改 0.是 1.否
    YES(0, "是"),
    NO(1, "否");

    IsUpdateEnum(int type, String name) {
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
