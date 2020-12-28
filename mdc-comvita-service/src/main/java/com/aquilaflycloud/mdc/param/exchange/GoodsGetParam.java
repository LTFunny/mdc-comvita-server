package com.aquilaflycloud.mdc.param.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * GoodsGetParam
 *
 * @author star
 * @date 2020-03-15
 */
@Data
public class GoodsGetParam implements Serializable {
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Long id;
}
