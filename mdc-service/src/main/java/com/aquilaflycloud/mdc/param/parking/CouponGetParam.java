package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * CouponGetParam
 *
 * @author star
 * @date 2020-01-14
 */
@Data
@Accessors(chain = true)
public class CouponGetParam {
    @ApiModelProperty(value = "停车券id", required = true)
    @NotNull(message = "停车券id不能为空")
    private Long id;
}
