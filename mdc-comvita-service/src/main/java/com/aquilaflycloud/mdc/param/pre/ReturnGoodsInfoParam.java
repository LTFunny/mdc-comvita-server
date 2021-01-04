package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.GoodsStateStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsTyoeEnum;
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
public class ReturnGoodsInfoParam  {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    @NotNull(message = "商品编号不能为空")
    private String goodsCode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    @NotNull(message = "商品名称不能为空")
    private String goodsName;

    /**
     * 商品类型(1-预售商品、2-赠品、3-普通商品)
     */
    @ApiModelProperty(value = "商品类型(1-预售商品、2-赠品、3-普通商品)")
    @NotNull(message = "商品类型不能为空")
    private OrderGoodsTyoeEnum goodsType;

    /**
     * 零售价
     */
    @ApiModelProperty(value = "零售价")
    @NotNull(message = "零售价不能为空")
    private BigDecimal goodsPrice;

    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述")
    @NotNull(message = "商品描述不能为空")
    private String goodsDescription;

    /**
     * 商品照片
     */
    @ApiModelProperty(value = "商品照片")
    @NotNull(message = "商品照片不能为空")
    private String goodsPicture;

    /**
     * 商品状态(0-在售 1-下架)
     */
    @ApiModelProperty(value = "商品状态(0-在售 1-下架)")
    private GoodsStateStateEnum goodsState;


    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}
