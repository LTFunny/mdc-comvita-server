package com.aquilaflycloud.mdc.param.exchange;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.exchange.DeliveryTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.RefundTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.ShelveTypeEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * GoodsAddParam
 *
 * @author star
 * @date 2020-03-15
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "goodsType", fieldValue = "COUPON", notNullFieldName = "coupon", message = "优惠券信息不能为空"),
        @AnotherFieldHasValue(fieldName = "goodsType", fieldValue = "PARKING", notNullFieldName = "parkingCoupon", message = "停车券信息不能为空"),
        @AnotherFieldHasValue(fieldName = "singleReward", notNullFieldName = "rewardType", message = "奖励类型不能为空"),
        @AnotherFieldHasValue(fieldName = "shelveType", fieldValue = "REGULAR", notNullFieldName = "shelveTime", message = "上架时间不能为空"),

        @AnotherFieldHasValue(fieldName = "goodsType", fieldValue = "PHYSICAL", notNullFieldName = "deliveryType", message = "配送类型不能为空"),
        @AnotherFieldHasValue(fieldName = "deliveryType", fieldValue = "GETSTORE", notNullFieldName = "deliveryAddress", message = "配送地址不能为空"),
        @AnotherFieldHasValue(fieldName = "goodsType", fieldValue = "PHYSICAL", notNullFieldName = "skuAddInfos", message = "商品sku信息不能为空"),
        @AnotherFieldHasValue(fieldName = "goodsType", fieldValue = "PHYSICAL", notNullFieldName = "goodsSpecValueAddInfos", message = "商品规格配置信息"),
        @AnotherFieldHasValue(fieldName = "goodsType", fieldValue = "COUPON", notNullFieldName = "inventory", message = "库存不能为空"),
        @AnotherFieldHasValue(fieldName = "goodsType", fieldValue = "PARKING", notNullFieldName = "inventory", message = "库存不能为空"),
})
@Data
public class GoodsAddParam implements Serializable {
    @ApiModelProperty(value = "商品类型(exchange.GoodsTypeEnum)", required = true)
    @NotNull(message = "商品类型不能为空")
    private GoodsTypeEnum goodsType;

    @ApiModelProperty(value = "商品名称", required = true)
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;

    @ApiModelProperty(value = "商品图片列表", required = true)
    @NotEmpty(message = "商品图片列表不能为空")
    private List<String> goodsImgList;

    @ApiModelProperty(value = "商品详细", required = true)
    @NotBlank(message = "商品名称不能为空")
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
    private BigDecimal singlePrice = BigDecimal.ZERO;

    @ApiModelProperty(value = "市场参考价")
    @DecimalMin(value = "0", message = "市场参考价不能小于0")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "库存")
    private Integer inventory;

    @ApiModelProperty(value = "会员兑换上限(0表示无上限)", required = true)
    @NotNull(message = "上限不能为空")
    @Min(value = 0, message = "上限不能小于0")
    private Integer exchangeLimit;

    @ApiModelProperty(value = "配送类型")
    private DeliveryTypeEnum deliveryType;

    @ApiModelProperty(value = "自提地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "快递金额")
    @DecimalMin(value = "0", message = "快递金额不能小于0")
    private BigDecimal expressPrice = BigDecimal.ZERO;

    @ApiModelProperty(value = "退款类型(exchange.RefundTypeEnum)")
    private RefundTypeEnum refundType;

    @ApiModelProperty(value = "是否过期自动退款(common.WhetherEnum)")
    private WhetherEnum refundExpired = WhetherEnum.NO;

    @ApiModelProperty(value = "上架类型(exchange.ShelveTypeEnum)")
    private ShelveTypeEnum shelveType;

    @ApiModelProperty(value = "上架时间")
    private Date shelveTime;

    @ApiModelProperty(value = "商户ids(多个以,分隔)")
    private String designateOrgIds;

    @ApiModelProperty(value = "分类id列表", required = true)
    @NotEmpty(message = "分类id列表不能为空")
    private List<Long> catalogIdList;

    @ApiModelProperty(value = "优惠券信息")
    @Valid
    private GoodsCouponAddParam coupon;

    @ApiModelProperty(value = "停车券信息")
    @Valid
    private GoodsParkingCouponAddParam parkingCoupon;

    @ApiModelProperty(value = "商品sku信息")
    @Valid
    private List<ExchangeGoodsSkuAddInfo> skuAddInfos;

    @ApiModelProperty(value = "商品规格配置信息")
    @Valid
    private List<ExchangeGoodsSpecValueAddInfo> goodsSpecValueAddInfos;
}
