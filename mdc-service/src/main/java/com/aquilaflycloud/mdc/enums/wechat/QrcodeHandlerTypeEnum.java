package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * QrcodeHandlerTypeEnum
 *
 * @author star
 * @date 2020-04-02
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum QrcodeHandlerTypeEnum {
    // 微信公众号二维码处理类型
    NORMAL(1, "普通"),
    SCANCONSUME(2, "消费奖励"),
    ;

    QrcodeHandlerTypeEnum(int type, String name) {
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
