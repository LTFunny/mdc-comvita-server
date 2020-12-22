package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class WechatAuthorSitePageParam extends PageParam<WechatAuthorSite> {
    @ApiModelProperty(value = "授权类型(wechat.SiteSourceEnum)")
    private SiteSourceEnum source;

    @ApiModelProperty(value = "授权方昵称")
    private String nickName;
}
