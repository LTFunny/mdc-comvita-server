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
 * GoodsInfoAddParam
 *
 * @author star
 * @date 2021/1/18
 */
@Accessors(chain = true)
@Data
public class GoodsInfoAddParam {
    @ApiModelProperty(value = "商品编号", required = true)
    @NotEmpty(message = "商品编号不能为空")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotEmpty(message = "商品名称不能为空")
    private String goodsName;

    @ApiModelProperty(value = "商品类型(pre.GoodsTypeEnum)", required = true)
    @NotNull(message = "商品类型不能为空")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "零售价", required = true)
    @NotNull(message = "零售价不能为空")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "商品描述", required = true)
    @NotNull(message = "商品描述不能为空")
    private String goodsDescription;

    @ApiModelProperty(value = "商品照片", required = true)
    @NotNull(message = "商品照片不能为空")
    private String goodsPicture;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}
