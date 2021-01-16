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
public class InputOrderNumberParam {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "主键")
    @NotNull(message = "主键不能为空")
    private String id;


    @ApiModelProperty(value = "快递名称")
    private String expressName;

    @ApiModelProperty(value = "快递单号")
    @NotNull(message = "快递名称不能为空")
    private String expressOrder;

    @NotNull(message = "快递编码不能为空")
    @ApiModelProperty(value = "快递编码")
    private String expressCode;
}
