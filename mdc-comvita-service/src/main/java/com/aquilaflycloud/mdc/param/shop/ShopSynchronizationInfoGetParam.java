package com.aquilaflycloud.mdc.param.shop;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * ShopSynchronizationInfoGetParam
 *
 * @author zengqingjie
 * @date 2020-04-07
 */
@Data
@Accessors(chain = true)
public class ShopSynchronizationInfoGetParam extends AuthParam implements Serializable {
    private static final long serialVersionUID = -2597059778994313243L;

    @ApiModelProperty(value = "appId", required = true)
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty(value = "进去商铺url", required = true)
    @NotBlank(message = "进去商铺url不能为空")
    private String urlParam;

    @ApiModelProperty(value = "简介", required = true)
    @NotBlank(message = "简介不能为空")
    private String describe;

}