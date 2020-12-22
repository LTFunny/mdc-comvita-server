package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MaterialTypeEnum
 *
 * @author star
 * @date 2019-10-09
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum MaterialTypeEnum {
    // 公众号资料类型
    IMAGE(1, "图片"),
    VIDEO(2, "视频"),
    VOICE(3, "语音"),
    NEWS(4, "图文"),
    ;

    MaterialTypeEnum(int type, String name) {
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
