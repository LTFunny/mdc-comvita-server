package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.GoodsStateStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PreGoodsInfoListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class PreGoodsInfoListParam extends PageParam {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    @ApiModelProperty(value = "GoodsStateStateEnum商品状态(0-在售 1-下架)")
    private String goodsState;

    @ApiModelProperty(value = "OrderGoodsTypeEnum商品类型(1-预售商品、2-赠品、3-普通商品)")
    private String goodsType;

    @ApiModelProperty(value = "标签id")
    private String  folksonomyId;
}
