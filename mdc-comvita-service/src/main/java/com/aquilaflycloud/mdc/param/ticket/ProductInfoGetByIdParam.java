package com.aquilaflycloud.mdc.param.ticket;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据产品id获取产品信息
 */
@Data
@Accessors(chain = true)
public class ProductInfoGetByIdParam implements Serializable {
    private static final long serialVersionUID = -3034891827657871133L;
    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long id;
}
