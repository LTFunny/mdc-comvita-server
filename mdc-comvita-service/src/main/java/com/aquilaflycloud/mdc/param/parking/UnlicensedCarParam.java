package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * UnlicensedCarParam
 *
 * @author star
 * @date 2020-02-11
 */
@Data
@Accessors(chain = true)
public class UnlicensedCarParam {
    @ApiModelProperty(value = "扫码参数", required = true)
    @NotBlank(message = "扫码参数不能为空")
    private String scanParam;

    @ApiModelProperty(value = "车牌号码")
    private String carNo;
}
