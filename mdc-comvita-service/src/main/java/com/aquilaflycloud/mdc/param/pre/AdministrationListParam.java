package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.GoodsStateStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * AdministrationListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class AdministrationListParam extends PageParam {

    @ApiModelProperty(value = "核销门店ids")
    private String verificaterOrgIds;

    @ApiModelProperty(value = "门店id")
    private Long shopId;

    @ApiModelProperty(value = "商品状态(0-在售 1-下架)")
    private GoodsStateStateEnum goodsState;

    @ApiModelProperty(value = "商品类型(1-预售商品、2-赠品、3-普通商品)")
    private OrderGoodsTypeEnum goodsType;

    @ApiModelProperty(value = "标签id")
    private Long  folksonomyId;
}
