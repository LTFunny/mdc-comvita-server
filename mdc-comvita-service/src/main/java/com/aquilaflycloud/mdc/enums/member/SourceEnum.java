package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SourceEnum
 *
 * @author star
 * @date 2019-10-25
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SourceEnum {
    // 会员来源类型
    GZH(1, "公众号"),
    MINI(2, "微信小程序"),
    MANUAL(5, "手工录入"),
    IMPORT(6, "导入"),
    EXTRA(7, "外部系统"),
    ;

    SourceEnum(int type, String name) {
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
