package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.enums.member.SexEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderOperateRecord;
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
public class AdministrationDetailsResult {

    @ApiModelProperty(value = "订单编码")
    private String orderCode;

    @ApiModelProperty(value = "门店名称")
    private String shopName;

    @ApiModelProperty(value = "导购员名称")
    private String guideName;

    @ApiModelProperty(value = "总金额")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "订单状态")
    private OrderInfoStateEnum orderState;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "确认时间")
    private Date confirmTime;

    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "销售小票url")
    private String ticketUrl;

    @ApiModelProperty(value = "买家姓名")
    private String buyerName;

    @ApiModelProperty(value = "买家手机")
    private String buyerPhone;

    @ApiModelProperty(value = "买家详细地址")
    private String buyerAddress;

    @ApiModelProperty(value = "买家地址邮编")
    private String buyerPostalCode;

    @ApiModelProperty(value = "买家地址-省")
    private String buyerProvince;

    @ApiModelProperty(value = "买家地址-市")
    private String buyerCity;

    @ApiModelProperty(value = "买家地址-区")
    private String buyerDistrict;

    @ApiModelProperty(value = "快递名称")
    private String expressName;

    @ApiModelProperty(value = "快递单号")
    private String expressOrder;

    @ApiModelProperty(value = "最后核销时间")
    private Date lastUpdateTime;

   @ApiModelProperty(value = "订单明细")
    private List<PreOrderGoods> detailsList;

    @ApiModelProperty(value = "操作记录")
    private List<PreOrderOperateRecord> operationList ;

    @ApiModelProperty(value = "买家性别")
    private SexEnum buyerSex;

    @ApiModelProperty(value = "买家生日")
    private Date buyerBirthday;

    @ApiModelProperty(value = "积分")
    private BigDecimal score;
}
