package com.aquilaflycloud.mdc.result.author;

import com.aquilaflycloud.mdc.enums.author.SiteSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AuthorSite implements Serializable {
    @ApiModelProperty(value="授权方appid")
    private String appId;

    @ApiModelProperty(value="应用名")
    private String appName;

    @ApiModelProperty(value="授权类型")
    private SiteSourceEnum source;

    private static final long serialVersionUID = 1L;
}