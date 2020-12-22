package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * MemberCarParam
 *
 * @author star
 * @date 2020-02-10
 */
@Data
@Accessors(chain = true)
public class MemberCarParam {
    @ApiModelProperty(value = "车牌号", required = true)
    @NotBlank(message = "车牌号不能为空")
    private String carNo;
}
