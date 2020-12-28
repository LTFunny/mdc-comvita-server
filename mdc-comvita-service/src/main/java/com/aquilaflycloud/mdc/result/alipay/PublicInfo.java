package com.aquilaflycloud.mdc.result.alipay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PublicInfo {
    @ApiModelProperty(value="应用名")
    private String appName;

    @ApiModelProperty(value="头像")
    private String logoUrl;

    @ApiModelProperty(value="欢迎语")
    private String publicGreeting;

    @ApiModelProperty(value="最新审核状态")
    private String auditStatus;

    @ApiModelProperty(value="最新审核状态描述")
    private String auditDesc;

    @ApiModelProperty(value="生活号是否上线")
    private String isOnline;

    @ApiModelProperty(value="生活号是否上架")
    private String isRelease;

    @ApiModelProperty(value="生活号简介")
    private String introduction;

    @ApiModelProperty(value="背景图片地址")
    private String backgroundUrl;

    @ApiModelProperty(value="商家经营类目")
    private String mccCodeDesc;

    @ApiModelProperty(value="审核状态表，目前支持名称、头像、名称与头像、简介审核状态")
    private String auditStatusList;
}