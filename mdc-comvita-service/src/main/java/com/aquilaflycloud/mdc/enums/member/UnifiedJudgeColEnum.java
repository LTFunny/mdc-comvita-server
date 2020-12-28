package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * UnifiedJudgeColEnum
 *
 * @author star
 * @date 2020/12/2
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum UnifiedJudgeColEnum {
    // 统一会员判断字段
    PHONE(1, "手机号码", "phoneNumber"),
    IDCARD(2, "身份证", "idCard"),
    ;

    UnifiedJudgeColEnum(int type, String name, String colName) {
        this.type = type;
        this.name = name;
        this.colName = colName;
    }

    @EnumValue
    private final int type;

    private final String name;

    private final String colName;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getColName() {
        return colName;
    }
}
