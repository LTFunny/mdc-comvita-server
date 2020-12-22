package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * MaterialGetParam
 *
 * @author star
 * @date 2019-10-09
 */
@Data
@Accessors(chain = true)
public class MaterialGetParam {

    @ApiModelProperty(value = "公众号appId", required = true)
    @NotBlank(message = "公众号appId不能为空")
    private String appId;

    @ApiModelProperty(value = "素材id", required = true)
    @NotBlank(message = "素材id不能为空")
    private String mediaId;
}
