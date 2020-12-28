package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * InvoiceGetParam
 *
 * @author star
 * @date 2020-06-24
 */
@Data
@Accessors(chain = true)
public class InvoiceGetParam {
    @ApiModelProperty(value = "发票记录id", required = true)
    @NotNull(message = "发票记录id不能为空")
    private Long id;
}
