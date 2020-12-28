package com.aquilaflycloud.mdc.result.parking;

import com.aquilaflycloud.mdc.enums.parking.ChargeStateEnum;
import com.aquilaflycloud.mdc.model.parking.ParkingCouponMemberRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class ParkingOrderChargeResult implements Serializable {
    @ApiModelProperty(value = "短车卡号")
    private String shortCardId;

    @ApiModelProperty(value = "总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "实收价")
    private BigDecimal actualPrice;

    @ApiModelProperty(value = "优惠金额(元)")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "优惠时间(分)")
    private Integer discountTime;

    @ApiModelProperty(value = "计费状态")
    private ChargeStateEnum chargeState;

    @ApiModelProperty(value = "计费代码")
    private String chargeCode;

    @ApiModelProperty(value = "计费开始")
    private String startTime;

    @ApiModelProperty(value = "计费结束")
    private String endTime;

    @ApiModelProperty(value = "停车时长(格式:*时*分)")
    private String parkTime;

    @ApiModelProperty(value = "计费时长(格式:*时*分)")
    private String chargeTime;

    @ApiModelProperty(value = "计费标准")
    private String chargingCar;

    @ApiModelProperty(value = "应收金额(单位:元)")
    private String totalCharge;

    @ApiModelProperty(value = "已付金额(单位:元)")
    private String hasCharge;

    @ApiModelProperty(value = "已含优惠金额(单位:元)")
    private String favMoney;

    @ApiModelProperty(value = "已含优惠时间(单位:分)")
    private String favTime;

    @ApiModelProperty(value = "当前优惠金额(单位:元)")
    private String currFavMoney;

    @ApiModelProperty(value = "当前优惠时间(单位:分)")
    private String currFavTime;

    @ApiModelProperty(value = "实收金额(单位:元)")
    private String payCharge;

    @ApiModelProperty(value = "入场时间")
    private String inTime;

    @ApiModelProperty(value = "卡片类型")
    private String cardType;

    @ApiModelProperty(value = "卡片编号")
    private String cardSnId;

    @ApiModelProperty(value = "物理卡号")
    private String cardId;

    @ApiModelProperty(value = "车牌号码")
    private String carNo;

    @ApiModelProperty(value = "可用停车券数量")
    private Integer relCount;

    @ApiModelProperty(value = "已用停车券列表")
    private List<ParkingCouponMemberRel> relList;
}