package com.aquilaflycloud.mdc.param.author;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.author.SiteSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AuthorSiteListParam extends AuthParam {
    @ApiModelProperty(value = "应用名")
    private String appName;

    @ApiModelProperty(value = "是否返回通用AppId(默认false)")
    private Boolean returnUniversal = false;

    @ApiModelProperty(value="授权类型(author.SiteSourceEnum)")
    private SiteSourceEnum source;

    @ApiModelProperty(value = "应用类型(WECHAT)")
    private AuthorType type;

    public enum AuthorType {
        // 授权应用类型
        WECHAT
    }
}
