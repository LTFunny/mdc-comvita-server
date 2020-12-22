package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * TencentPositionAccountSaveParam
 *
 * @author star
 * @date 2019-12-04
 */
@Data
@Accessors(chain = true)
public class TencentPositionAccountSaveParam {
    @ApiModelProperty(value = "开发者密钥", required = true)
    @NotBlank(message = "密钥不能为空")
    private String key;

    @ApiModelProperty(value = "开发者私钥", required = true)
    @NotBlank(message = "私钥不能为空")
    private String secret;
}
