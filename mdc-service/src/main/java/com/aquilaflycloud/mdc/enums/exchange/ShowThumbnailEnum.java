package com.aquilaflycloud.mdc.enums.exchange;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ShowThumbnailEnum
 *
 * @author zengqingjie
 * @date 2020-07-02
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ShowThumbnailEnum {
    // 是否展示缩略图
    HIDE(0, "不展示缩略图"),
    SHOW(1, "展示缩略图"),
    ;

    ShowThumbnailEnum(int type, String name) {
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
