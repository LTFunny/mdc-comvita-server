package com.aquilaflycloud.mdc.result.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TencentPositionAccountResult implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "开发者密钥")
    private String key;

    @ApiModelProperty(value = "开发者私钥")
    private String secret;
}
