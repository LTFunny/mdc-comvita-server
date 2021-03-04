package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ActivityGettingWayEnum
 * 活动领取方式（1-线下领取 2-物流配送）
 * @author linkq
 *
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ActivityGettingWayEnum {

    /**
     * 线下领取
     */
    OFF_LINE(1, "线下领取"),
    /**
     * 物流配送
     */
    BY_EXPRESS(2, "物流配送")
    ;

    ActivityGettingWayEnum(int type, String name) {
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
