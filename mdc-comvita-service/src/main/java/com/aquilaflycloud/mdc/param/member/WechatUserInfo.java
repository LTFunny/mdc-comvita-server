package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
class WechatUserInfo implements Serializable {

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像图片的URL")
    private String avatarUrl;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "用户所在城市")
    private String city;

    @ApiModelProperty(value = "用户所在省份")
    private String province;

    @ApiModelProperty(value = "用户所在国家")
    private String country;

    @ApiModelProperty(value = "显示 country，province，city 所用的语言")
    private String language;
}