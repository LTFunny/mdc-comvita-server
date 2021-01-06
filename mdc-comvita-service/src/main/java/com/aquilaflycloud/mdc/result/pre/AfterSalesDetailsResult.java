package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.pre.AfterSaleTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderOperateRecord;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * AdministrationListParam
 * <p>
 * zly
 */
@Accessors(chain = true)
@Data
public class AfterSalesDetailsResult  {

    /**
     * 售后单编码
     */
    @ApiModelProperty(value = "售后单编码")
    private String refundCode;

    /**
     * 退款金额
     */
    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    /**
     * 售后商品状态
     */
    @ApiModelProperty(value = "售后商品状态")
    private AfterSaleTypeEnum refundGoodsState;

    /**
     * 售后时间
     */
    @ApiModelProperty(value = "售后时间")
    private Date refundTime;

    /**
     * 售后原因
     */
    @ApiModelProperty(value = "售后原因")
    private String refundReason;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    private String orderCode;


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    /**
     * 门店id
     */
    @ApiModelProperty(value = "门店id")
    private Long shopId;

    /**
     * 门店名称
     */
    @ApiModelProperty(value = "门店名称")
    private String shopName;

    /**
     * 导购员id
     */
    @ApiModelProperty(value = "导购员id")
    private Long guideId;

    /**
     * 导购员名称
     */
    @ApiModelProperty(value = "导购员名称")
    private String guideName;
    /**
     * 导购员id
     */
    @ApiModelProperty(value = "售后导购员id")
    private Long afterGuideId;

    /**
     * 导购员名称
     */
    @ApiModelProperty(value = "售后导购员名称")
    private String afterGuideName;
    /**
     * 总金额
     */
    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    /**
     * 销售小票url
     */
    @ApiModelProperty(value = "销售小票url")
    private String ticketUrl;

    /**
     * 快递编码
     */
    @ApiModelProperty(value = "快递编码")
    private String expressCode;

    /**
     * 快递名称
     */
    @ApiModelProperty(value = "快递名称")
    private String expressName;

    /**
     * 快递单号
     */
    @ApiModelProperty(value = "快递单号")
    private String expressOrder;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    /**
     * 收货时间
     */
    @ApiModelProperty(value = "收货时间")
    private Date receiveTime;

    /**
     * 确认时间
     */
    @ApiModelProperty(value = "确认时间")
    private Date confirmTime;

    /**
     * 买家姓名
     */
    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    /**
     * 买家手机
     */
    @ApiModelProperty(value = "买家手机")
    private String buyerPhone;

    /**
     * 买家地址邮编
     */
    @ApiModelProperty(value = "买家地址邮编")
    private String buyerPostalCode;

    /**
     * 买家详细地址
     */
    @ApiModelProperty(value = "买家详细地址")
    private String buyerAddress;

    /**
     * 订单状态
     */
    @ApiModelProperty(value = "订单状态")
    private Integer orderState;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

   @ApiModelProperty(value = "订单明细")
    private List<PreOrderGoods> detailsList;

    @ApiModelProperty(value = "操作记录")
    private List<PreOrderOperateRecord> operationList ;



}
