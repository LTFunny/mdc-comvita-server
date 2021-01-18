package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * GoodsInfoGetParam
 *
 * @author star
 * @date 2021/1/18
 */
@Accessors(chain = true)
@Data
public class GoodsInfoGetParam {
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private String id;
}
