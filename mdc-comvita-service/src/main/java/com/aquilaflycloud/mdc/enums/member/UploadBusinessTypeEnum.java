package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * UploadBusinessTypeEnum
 *
 * @author star
 * @date 2020-03-02
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum UploadBusinessTypeEnum {
    // 上传业务类型
    APPLY(1, "活动报名"),
    FACE(2, "会员自拍"),
    SHOP_COMMENT(3, "商铺评论"),
    SHOP_COMPLAINT(4, "商铺投诉"),
    ;

    UploadBusinessTypeEnum(int type, String name) {
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
