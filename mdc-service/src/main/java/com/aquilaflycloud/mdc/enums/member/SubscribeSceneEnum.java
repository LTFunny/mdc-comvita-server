package com.aquilaflycloud.mdc.enums.member;

import com.alibaba.fastjson.annotation.JSONType;
import com.aquilaflycloud.dataAuth.component.EnumDeserializer;
import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * SubscribeSceneEnum
 *
 * @author star
 * @date 2019-10-25
 */
@JSONType(serializeEnumAsJavaBean = true, deserializer = EnumDeserializer.class)
public enum SubscribeSceneEnum {
    // 微信公众号关注来源类型
    ADD_SCENE_SEARCH(1, "公众号搜索"),
    ADD_SCENE_ACCOUNT_MIGRATION(2, "公众号迁移"),
    ADD_SCENE_PROFILE_CARD(3, "名片分享"),
    ADD_SCENE_QR_CODE(4, "扫描二维码"),
    ADD_SCENE_PROFILE_LINK(5, "图文页内名称点击"),
    ADD_SCENE_PROFILE_ITEM(6, "图文页右上角菜单"),
    ADD_SCENE_PAID(7, "支付后关注"),
    ADD_SCENE_OTHERS(8, "其他"),
    ;

    SubscribeSceneEnum(int type, String name) {
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
