package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * PreGoodsInfoListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class GoodsSaleNumParam {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编码")
    @NotNull(message = "商品编码不能为空")
    private String goodsCode;

    @ApiModelProperty(value = "30日",hidden = true)
    private Date goodsThirtyTime;

    @ApiModelProperty(value = "15日",hidden = true)
    private Date goodsFifteenTime;

    @ApiModelProperty(value = "7日",hidden = true)
    private Date goodsSevenTime;

}
