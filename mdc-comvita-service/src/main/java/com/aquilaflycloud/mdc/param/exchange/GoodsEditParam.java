package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.exchange.DeliveryTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.RefundTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.ShelveTypeEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * GoodsEditParam
 *
 * @author star
 * @date 2020-03-15
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "singlePrice", notNullFieldName = "rewardType", message = "奖励类型不能为空"),
        @AnotherFieldHasValue(fieldName = "shelveType", fieldValue = "REGULAR", notNullFieldName = "shelveTime", message = "上架时间不能为空"),
})
@Data
public class GoodsEditParam implements Serializable {
    @ApiModelProperty(value = "商品id", required = true)
    @NotNull(message = "商品id不能为空")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片列表")
    private List<String> goodsImgList;

    @ApiModelProperty(value = "商品详细")
    private String goodsDetail;

    @ApiModelProperty(value = "商品备注")
    private String goodsRemark;

    @ApiModelProperty(value = "售后说明")
    private String goodsService;

    @ApiModelProperty(value = "奖励类型(member.RewardTypeEnum)")
    private RewardTypeEnum rewardType;

    @ApiModelProperty(value = "单件奖励值")
    @Min(value = 0, message = "单件奖励值不能小于0")
    private Integer singleReward;

    @ApiModelProperty(value = "单件金额")
    @DecimalMin(value = "0", message = "单件金额不能小于0")
    private BigDecimal singlePrice;

    @ApiModelProperty(value = "市场参考价")
    @DecimalMin(value = "0", message = "市场参考价不能小于0")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "库存")
    @Min(value = 0, message = "库存不能小于0")
    private Integer inventoryIncrease;

    @ApiModelProperty(value = "会员兑换上限(0表示无上限)")
    @Min(value = 0, message = "上限不能小于0")
    private Integer exchangeLimit;

    @ApiModelProperty(value = "配送类型")
    private DeliveryTypeEnum deliveryType;

    @ApiModelProperty(value = "自提地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "快递金额")
    @DecimalMin(value = "0", message = "快递金额不能小于0")
    private BigDecimal expressPrice;

    @ApiModelProperty(value = "上架类型(exchange.ShelveTypeEnum)")
    private ShelveTypeEnum shelveType;

    @ApiModelProperty(value = "退款类型(exchange.RefundTypeEnum)")
    private RefundTypeEnum refundType;

    @ApiModelProperty(value = "是否过期自动退款(common.WhetherEnum)")
    private WhetherEnum refundExpired;

    @ApiModelProperty(value = "上架时间")
    private Date shelveTime;

    @ApiModelProperty(value = "商户ids(多个以,分隔)")
    private String designateOrgIds;

    @ApiModelProperty(value = "分类id列表")
    private List<Long> catalogIdList;

    @ApiModelProperty(value = "优惠券信息")
    @Valid
    private GoodsCouponEditParam coupon;

    @ApiModelProperty(value = "sku编辑信息")
    @Valid
    private List<ExchangeGoodsSkuEditInfo> skuEditInfos;
}
