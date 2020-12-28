package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.exchange.GoodsStateEnum;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * GoodsPageParam
 *
 * @author star
 * @date 2020-03-15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsPageParam extends PageAuthParam<ExchangeGoods> implements Serializable {
    @ApiModelProperty(value = "商品类型(exchange.GoodsTypeEnum)")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "上架开始时间")
    private Date shelveTimeStart;

    @ApiModelProperty(value = "上架结束时间")
    private Date shelveTimeEnd;

    @ApiModelProperty(value = "状态(exchange.GoodsStateEnum)")
    private GoodsStateEnum state;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "分类id")
    private Long catalogId;

    @ApiModelProperty(value = "是否查询有效状态(common.WhetherEnum)(默认否, 为是时state无效)")
    private WhetherEnum valid = WhetherEnum.NO;

    @ApiModelProperty(value = "商铺关联id")
    private Long relationId;
}
