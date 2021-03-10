package com.aquilaflycloud.mdc.enums.pre;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * ButtonStateEnum
 *
 * @author star
 * @date 2021/3/5
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum ButtonStateEnum {

    /**
     * 马上参与
     */
    JOIN(1, "马上参与"),
    /**
     * 已参与
     */
    JOINED(2, "已参与"),
    /**
     * 查看核销码
     */
    SHOW(3, "查看核销码"),
    /**
     * 点评
     */
    COMMENT(4, "点评"),
    /**
     * 参加人数已达到上限
     */
    FULL(5, "参加人数已达到上限"),
    /**
     * 活动已结束
     */
    FINISHED(6, "活动已结束"),
    ;

    ButtonStateEnum(int type, String name) {
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
