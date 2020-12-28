package com.aquilaflycloud.mdc.enums.system;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * TenantConfigTypeEnum
 *
 * @author star
 * @date 2020-04-09
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum TenantConfigTypeEnum {
    // 租户配置类型
    ALIPAYDIRECTPAY(1, "支付宝直联支付"),
    WECHATDIRECTPAY(2, "微信直联支付"),
    AUTHORUNIVERSAL(3, "通用应用显示"),
    UNIFIEDMEMBER(4, "统一会员"),
    ;

    TenantConfigTypeEnum(int type, String name) {
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
