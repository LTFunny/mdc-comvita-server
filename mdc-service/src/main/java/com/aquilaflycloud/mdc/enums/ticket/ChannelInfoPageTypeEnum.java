package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ChannelInfoPageTypeEnum
 *
 * @author star
 * @date 2019-12-27
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ChannelInfoPageTypeEnum {
    // 渠道页面类型
    SCENICSPOT(1, "景区门票页面"),
    RECOMMENDATION(2, "最新推荐页面"),
    ;

    ChannelInfoPageTypeEnum(int type, String name) {
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
