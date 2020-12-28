package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MiniMessageTypeEnum
 *
 * @author star
 * @date 2020-03-04
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum MiniMessageTypeEnum {
    // 小程序订阅消息业务类型
    APPLYRECORDAUDIT(1, "报名记录审核", "活动名称, 报名结果, 提示, 通知时间"),
    ;

    MiniMessageTypeEnum(int type, String name, String desc) {
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    @EnumValue
    private final int type;

    private final String name;

    private final String desc;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
