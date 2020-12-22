package com.aquilaflycloud.mdc.param.alipay;

import com.aquilaflycloud.mdc.enums.alipay.AlipayOauth2ScopeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * AlipayOauth2UrlGetParam
 *
 * @author star
 * @date 2019-12-25
 */
@Data
@Accessors(chain = true)
public class AlipayOauth2UrlGetParam {

    @ApiModelProperty(value = "生活号appId")
    private String appId;

    @ApiModelProperty(value = "回调url链接", required = true)
    @NotBlank(message = "回调url链接不能为空")
    private String url;

    @ApiModelProperty(value = "授权作用域(alipay.AlipayOauth2ScopeEnum)")
    private AlipayOauth2ScopeEnum scopeType = AlipayOauth2ScopeEnum.auth_user;

    @ApiModelProperty(value = "链接是否加密", example = "true")
    private Boolean encrypt = true;
}
