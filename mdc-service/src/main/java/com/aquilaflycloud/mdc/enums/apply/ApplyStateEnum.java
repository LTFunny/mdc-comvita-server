package com.aquilaflycloud.mdc.enums.apply;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ApplyStateEnum
 *
 * @author star
 * @date 2020-02-27
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ApplyStateEnum {
    // 报名活动状态类型
    NORMAL(1, "进行中"),
    DISABLE(2, "停用"),
    NOTSTART(3, "未开始"),
    EXPIRED(4, "已结束"),
    ;

    ApplyStateEnum(int type, String name) {
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
