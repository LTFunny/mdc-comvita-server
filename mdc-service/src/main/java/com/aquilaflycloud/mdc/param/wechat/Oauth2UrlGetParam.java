package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.mdc.enums.wechat.Oauth2ScopeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * Oauth2UrlGetParam
 *
 * @author star
 * @date 2019-10-09
 */
@Data
@Accessors(chain = true)
public class Oauth2UrlGetParam {

    @ApiModelProperty(value = "公众号appId", required = true)
    @NotBlank(message = "公众号appId不能为空")
    private String appId;

    @ApiModelProperty(value = "回调url链接", required = true)
    @NotBlank(message = "回调url链接不能为空")
    private String url;

    @ApiModelProperty(value = "授权作用域(wechat.Oauth2ScopeEnum)")
    private Oauth2ScopeEnum scopeType = Oauth2ScopeEnum.SNSAPI_BASE;

    @ApiModelProperty(value = "链接是否加密", example = "true")
    private Boolean encrypt = true;
}
