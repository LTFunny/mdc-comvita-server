package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.GoodsStateEnum;
import com.aquilaflycloud.mdc.enums.pre.GoodsTypeEnum;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * PreGoodsInfoListParam
 * <p>
 * zly
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PreGoodsInfoListParam extends PageParam<PreGoodsInfo> {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品编号")
    private String goodsCode;

    @ApiModelProperty(value = "商品状态(pre.GoodsStateEnum)")
    private GoodsStateEnum goodsState;

    @ApiModelProperty(value = "商品类型(pre.GoodsTypeEnum)")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "标签id")
    private String folksonomyId;

    @ApiModelProperty(value = "小程序标识（1.B端不显示赠品）")
    private int miniSymbol;
}
