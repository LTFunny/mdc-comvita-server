package com.aquilaflycloud.mdc.model.parking;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "parking_ajb_coupon_record")
public class ParkingAjbCouponRecord implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 数据标识
     */
    @TableField(value = "ajb_id")
    @ApiModelProperty(value = "数据标识")
    private String ajbId;

    /**
     * 企业编号
     */
    @TableField(value = "ltd_code")
    @ApiModelProperty(value = "企业编号")
    private String ltdCode;

    /**
     * 车场编号
     */
    @TableField(value = "park_code")
    @ApiModelProperty(value = "车场编号")
    private String parkCode;

    /**
     * 车场名称
     */
    @TableField(value = "park_name")
    @ApiModelProperty(value = "车场名称")
    private String parkName;

    /**
     * 物理卡号
     */
    @TableField(value = "card_id")
    @ApiModelProperty(value = "物理卡号")
    private String cardId;

    /**
     * 车牌号码
     */
    @TableField(value = "car_no")
    @ApiModelProperty(value = "车牌号码")
    private String carNo;

    /**
     * 进场时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "in_time")
    @ApiModelProperty(value = "进场时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String inTime;

    /**
     * 优惠券码
     */
    @TableField(value = "bar_code")
    @ApiModelProperty(value = "优惠券码")
    private String barCode;

    /**
     * 商户标识
     */
    @TableField(value = "merchant_no")
    @ApiModelProperty(value = "商户标识")
    private String merchantNo;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_account")
    @ApiModelProperty(value = "商户名称")
    private String merchantAccount;

    /**
     * 优惠类型(取值:1=金额、2=时间、3=折扣、6=全免、7=时间段)
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "优惠类型(取值:1=金额、2=时间、3=折扣、6=全免、7=时间段)")
    private String type;

    /**
     * 优惠券类(取值:1=纸质、2=微信、3=优惠点、11=APP、15=APP 临时、22=APP专用)
     */
    @TableField(value = "kind")
    @ApiModelProperty(value = "优惠券类(取值:1=纸质、2=微信、3=优惠点、11=APP、15=APP 临时、22=APP专用)")
    private String kind;

    /**
     * 优惠金额(单位:元)
     */
    @TableField(value = "value")
    @ApiModelProperty(value = "优惠金额(单位:元)")
    private String value;

    /**
     * 优惠时间(单位:分)
     */
    @TableField(value = "time")
    @ApiModelProperty(value = "优惠时间(单位:分)")
    private String time;

    /**
     * 优惠折扣(最小:0.00，最大:1.00)
     */
    @TableField(value = "discount")
    @ApiModelProperty(value = "优惠折扣(最小:0.00，最大:1.00)")
    private String discount;

    /**
     * 实销金额(单位:元)
     */
    @TableField(value = "actual_amount")
    @ApiModelProperty(value = "实销金额(单位:元)")
    private String actualAmount;

    /**
     * 有效开始(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "有效开始(格式:yyyy-MM-dd HH:mm:ss)")
    private String startTime;

    /**
     * 有效结束(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "有效结束(格式:yyyy-MM-dd HH:mm:ss)")
    private String endTime;

    /**
     * 派发时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "distribute_time")
    @ApiModelProperty(value = "派发时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String distributeTime;

    /**
     * 购物票号
     */
    @TableField(value = "shopping_ticket")
    @ApiModelProperty(value = "购物票号")
    private String shoppingTicket;

    /**
     * 消费金额(单位:元)
     */
    @TableField(value = "shopping_money")
    @ApiModelProperty(value = "消费金额(单位:元)")
    private String shoppingMoney;

    /**
     * 创建时间(格式:yyyy-MM-dd HH:mm:ss)
     */
    @TableField(value = "create_date")
    @ApiModelProperty(value = "创建时间(格式:yyyy-MM-dd HH:mm:ss)")
    private String createDate;

    /**
     * 推送内容
     */
    @TableField(value = "data_content")
    @ApiModelProperty(value = "推送内容")
    private String dataContent;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    @ApiModelProperty(value = "租户id", hidden = true)
    @JSONField(serialize = false)
    private Long tenantId;

    /**
     * 子租户id
     */
    @TableField(value = "sub_tenant_id")
    @ApiModelProperty(value = "子租户id", hidden = true)
    @JSONField(serialize = false)
    private Long subTenantId;

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}