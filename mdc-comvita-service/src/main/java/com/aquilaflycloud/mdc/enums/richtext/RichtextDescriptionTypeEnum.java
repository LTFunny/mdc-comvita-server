package com.aquilaflycloud.mdc.enums.richtext;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * RichtextDescriptionTypeEnum
 *
 * @author Zengqingjie
 * @date 2020-03-07
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum RichtextDescriptionTypeEnum {
    //1:景区服务;2:景区介绍;3:自助积分指南
    SCENIC_SERVICE(1, "景区服务"),
    SCENIC_INTRODUCTION(2, "景区介绍"),
    SCORE_GUIDE(3, "自助积分指南"),
    ;

    RichtextDescriptionTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    @EnumValue
    private final int type;

    private final String name;

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
