package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * InputOrderNumberParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class OrderDetailsParam {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品主键")
    @NotNull(message = "商品主键不能为空")
    private String id;


}
