package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SiteSourceEnum
 *
 * @author star
 * @date 2019-10-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum LiveStatusEnum {
    // 小程序直播状态类型
    LIVING(101, "直播中"),
    UNSTART(102, "未开始"),
    ENDED(103, "已结束"),
    DISABLE(104, "禁播"),
    PAUSE(105, "暂停中"),
    UNUSUAL(106, "异常"),
    EXPIRED(107, "已过期"),
    ;

    LiveStatusEnum(int type, String name) {
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
