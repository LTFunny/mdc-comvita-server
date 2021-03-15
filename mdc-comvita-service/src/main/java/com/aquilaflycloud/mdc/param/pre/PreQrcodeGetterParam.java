package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreQrcodeGetterParam
 * @author linkq
 */
@Data
public class PreQrcodeGetterParam {
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long activityId;

    @ApiModelProperty(value = "门店名称")
    private String shopName;
}
