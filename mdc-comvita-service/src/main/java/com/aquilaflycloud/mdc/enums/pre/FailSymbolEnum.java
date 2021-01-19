package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @Author pengyongliang
 * @Date 2021/1/8 15:52
 * @Version 1.0
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum FailSymbolEnum {

    // 是否为不通过 0.是 1.否
    YES(0, "是"),
    NO(1, "否"),
    YES_NO(2,"都不是");

    FailSymbolEnum(int type, String name) {
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
