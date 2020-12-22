package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WechatAuthorSiteListParam {
    @ApiModelProperty(value = "授权类型(wechat.SiteSourceEnum)")
    private SiteSourceEnum source;

    @ApiModelProperty(value = "授权方昵称")
    private String nickName;
}
