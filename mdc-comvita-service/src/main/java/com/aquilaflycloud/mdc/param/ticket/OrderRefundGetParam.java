package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author zhi
 */
@Data
public class OrderRefundGetParam implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
