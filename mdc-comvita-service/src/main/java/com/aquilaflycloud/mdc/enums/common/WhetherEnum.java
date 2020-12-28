package com.aquilaflycloud.mdc.enums.common;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * WhetherEnum
 *
 * @author star
 * @date 2019-10-09
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum WhetherEnum {
    // 是否类型
    YES(1, "是"),
    NO(0, "否"),
    ;

    WhetherEnum(int type, String name) {
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
