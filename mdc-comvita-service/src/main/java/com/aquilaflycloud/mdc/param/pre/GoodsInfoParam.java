package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.GoodsStateStateEnum;
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
public class GoodsInfoParam {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品主键")
    @NotNull(message = "商品主键不能为空")
    private String id;


}
