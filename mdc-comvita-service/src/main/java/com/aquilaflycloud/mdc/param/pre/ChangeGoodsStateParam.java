package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * PreGoodsInfoListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class ChangeGoodsStateParam {
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Long id;

}
