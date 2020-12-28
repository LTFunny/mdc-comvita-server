package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * JsapiSignGetParam
 *
 * @author star
 * @date 2019-10-09
 */
@Data
@Accessors(chain = true)
public class JsapiSignGetParam {

    @ApiModelProperty(value = "公众号appId", required = true)
    @NotBlank(message = "公众号appId不能为空")
    private String appId;

    @ApiModelProperty(value = "需调用微信js的链接地址", required = true)
    @NotBlank(message = "url不能为空")
    private String url;

}
