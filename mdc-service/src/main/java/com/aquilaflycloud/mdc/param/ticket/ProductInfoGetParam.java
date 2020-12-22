package com.aquilaflycloud.mdc.param.ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取产品信息详情
 */
@Data
@Accessors(chain = true)
public class ProductInfoGetParam implements Serializable {
    private static final long serialVersionUID = -8220361184239256254L;

    @ApiModelProperty(value = "产品id", required = true)
    @NotNull(message = "产品id不能为空")
    private Long id;
}
