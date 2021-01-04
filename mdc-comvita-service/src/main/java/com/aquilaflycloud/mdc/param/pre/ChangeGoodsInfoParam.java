package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.GoodsStateStateEnum;
import com.aquilaflycloud.mdc.enums.pre.GoodsTypeStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * PreGoodsInfoListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class ChangeGoodsInfoParam {

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品主键")
    @NotNull(message = "商品主键不能为空")
    private String id;

    /**
     * 商品状态(0-在售 1-下架)
     */
    @ApiModelProperty(value = "商品状态(0-在售 1-下架)")
    @NotNull(message = "商品状态不能为空")
    private GoodsStateStateEnum goodsState;

}
