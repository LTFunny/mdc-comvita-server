package com.aquilaflycloud.mdc.enums.wechat;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * UserSourceEnum
 *
 * @author star
 * @date 2019-11-13
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum UserSourceEnum {
    // 微信公众号粉丝来源类型
    USERSOURCE0(0, "其他合计"),
    USERSOURCE1(1, "公众号搜索"),
    USERSOURCE17(17, "名片分享"),
    USERSOURCE30(30, "扫描二维码"),
    USERSOURCE43(43, "图文页右上角菜单"),
    USERSOURCE51(51, "支付后关注（在支付完成页）"),
    USERSOURCE57(57, "图文页内公众号名称"),
    USERSOURCE75(75, "公众号文章广告"),
    USERSOURCE78(78, "朋友圈广告"),
    USERSOURCE100(100, "微信广告"),
    USERSOURCE161(161, "他人转载"),
    USERSOURCE176(176, "专辑页内账号名称"),
    USERSOURCE999(999, "累计用户数"),
    ;

    UserSourceEnum(int type, String name) {
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
