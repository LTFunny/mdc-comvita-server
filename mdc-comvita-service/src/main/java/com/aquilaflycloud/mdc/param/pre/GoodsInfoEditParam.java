package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.GoodsTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * GoodsInfoEditParam
 *
 * @author star
 * @date 2021/1/18
 */
@Accessors(chain = true)
@Data
public class GoodsInfoEditParam {
    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Long id;

    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品类型")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "零售价")
    @NotNull(message = "零售价不能为空")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "商品描述")
    @NotEmpty(message = "商品描述不能为空")
    private String goodsDescription;

    @ApiModelProperty(value = "商品照片")
    @NotEmpty(message = "商品照片不能为空")
    private String goodsPicture;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}
