package com.aquilaflycloud.mdc.enums.ad;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * AdPlacementEnum
 *
 * @author star
 * @date 2019-11-16
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum AdPlacementEnum {
    // 广告位置类型
    BANNER(1, "首页推荐"),
    POPUPS(2, "首页弹窗"),
    PARKING(3, "停车缴费"),
    ;

    AdPlacementEnum(int type, String name) {
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
