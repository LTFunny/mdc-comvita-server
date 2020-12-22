package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ProductInfoStateEnum;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ProductInfoListParam 获取分页产品信息数据
 *
 * @author Zengqingjie
 * @date 2019-11-18
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProductInfoListParam extends PageAuthParam<TicketProductInfo> implements Serializable {
    private static final long serialVersionUID = 4960494098100569559L;
    @ApiModelProperty(value = "景区类型(ticket.ScenicSpotTypeEnum)(0海洋馆;1博物馆;2雨林馆),不传默认全部")
    private ScenicSpotTypeEnum scenicSpotType;

    @ApiModelProperty(value = "道控产品id")
    private Long productId;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "创建人")
    private String creatorName;

    @ApiModelProperty(value = "更新人")
    private String lastOperatorName;

    @ApiModelProperty(value = "开始票面价")
    private BigDecimal startTicketPrice;

    @ApiModelProperty(value = "结束票面价")
    private BigDecimal endTicketPrice;

    @ApiModelProperty(value = "开始售卖价")
    private BigDecimal startProductSellPrice;

    @ApiModelProperty(value = "结束售卖价")
    private BigDecimal endProductSellPrice;

    @ApiModelProperty(value = "生效开始时间")
    private Date startEffectiveTime;

    @ApiModelProperty(value = "生效结束时间")
    private Date endEffectiveTime;

    @ApiModelProperty(value = "失效开始时间")
    private Date startFailureTime;

    @ApiModelProperty(value = "失效结束时间")
    private Date endFailureTime;

    @ApiModelProperty(value = "状态(ticket.ProductInfoStateEnum)", hidden = true)
    private ProductInfoStateEnum state;
}
