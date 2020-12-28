package com.aquilaflycloud.mdc.enums.mission;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * MissionTypeEnum
 *
 * @author star
 * @date 2020-05-08
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum MissionTypeEnum {
    // 任务类型
    COMPLETE(1, "完善资料"),
    FOLKSONOMY(2, "添加标签"),
    BINDINGPHONE(3, "绑定手机"),
    TOTALSIGN(4, "累计签到"),
    RECEIVECOUPON(5, "领取优惠券"),
    USECOUPON(6, "使用优惠券"),
    ;

    MissionTypeEnum(int type, String name) {
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
