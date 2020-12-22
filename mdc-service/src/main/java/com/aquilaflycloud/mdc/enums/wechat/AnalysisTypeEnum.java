package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AnalysisTypeEnum
 *
 * @author star
 * @date 2020/8/17
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AnalysisTypeEnum {
    // 微信小程序留存分析类型
    DAILY(1, "日留存"),
    WEEKLY(2, "周留存"),
    MONTHLY(3, "月留存"),
    ;

    AnalysisTypeEnum(int type, String name) {
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
