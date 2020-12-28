package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * QrcodeMspExpireEnum
 *
 * @author star
 * @date 2019-10-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum QrcodeMsgTypeEnum {
    // 微信公众号二维码消息类型
    TEXT(1, "文本"),
    IMAGE(2, "图片"),
    VOICE(3, "语音"),
    VIDEO(4, "视频"),
    MUSIC(5, "音乐"),
    NEWS(6, "图文"),
    ;

    QrcodeMsgTypeEnum(int type, String name) {
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
