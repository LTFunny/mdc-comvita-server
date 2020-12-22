package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MiniMessageLangEnum
 *
 * @author star
 * @date 2020-03-04
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum MiniMessageLangEnum {
    // 小程序语言类型
    zh_CN(1, "简体中文"),
    en_US(2, "英文"),
    zh_HK(3, "繁体中文(HK)"),
    zh_TW(4, "繁体中文(TW)"),
    ;

    MiniMessageLangEnum(int type, String name) {
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
