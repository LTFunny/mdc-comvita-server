package com.aquilaflycloud.mdc.enums.ticket;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ChannelInfoPageEnum
 *
 * @author star
 * @date 2019-12-27
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ChannelInfoPageEnum {
    // 渠道页面枚举
    ALL(0, "全部产品页面", "pages/tickets/tickets"),
    OCEAN(1, "海洋馆页面", "pages/themePark/themePark"),
    RAINFOREST(2, "雨林馆页面", "pages/themePark/themePark"),
    MUSEUM(3, "博物馆页面", "pages/themePark/themePark"),
    ;

    ChannelInfoPageEnum(int type, String name, String path) {
        this.type = type;
        this.name = name;
        this.path = path;
    }

    @EnumValue
    private final int type;

    private final String name;

    private final String path;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
