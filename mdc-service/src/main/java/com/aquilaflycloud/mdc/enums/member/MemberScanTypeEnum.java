package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MemberScanTypeEnum
 *
 * @author zengqingjie
 * @date 202-06-28
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum MemberScanTypeEnum {
    // 消费奖励类型
    SCAN(1, "扫码积分"),
    PHOTO(2, "拍照积分"),
    AUTO(3, "自动积分"),
    ;

    MemberScanTypeEnum(int type, String name) {
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
