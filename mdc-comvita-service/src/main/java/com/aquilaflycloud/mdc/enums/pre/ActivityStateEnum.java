package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ActivityStateEnum
 * 状态（1-未开始 2-进行中 3-已结束 4-已下架）
 * @author linkq
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ActivityStateEnum {

    NOT_STARTED(1, "未开始"),
    IN_PROGRESS(2, "进行中"),
    FINISHED(3, "已结束"),
    CANCELED(4, "已下架")
    ;

    ActivityStateEnum(int type, String name) {
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
