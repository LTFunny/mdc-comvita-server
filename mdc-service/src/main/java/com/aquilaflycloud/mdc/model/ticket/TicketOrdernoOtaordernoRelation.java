package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.ticket.OrderPayStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "ticket_orderno_otaorderno_relation")
public class TicketOrdernoOtaordernoRelation implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商户订单号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value = "商户订单号")
    private String orderNo;

    /**
     * OTA订单号
     */
    @TableField(value = "OTA_order_no")
    @ApiModelProperty(value = "OTA订单号")
    private String otaOrderNo;

    /**
     * 渠道ID
     */
    @TableField(value = "channel_id")
    @ApiModelProperty(value = "渠道ID")
    private Long channelId;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 支付状态
     */
    @TableField(value = "pay_status")
    @ApiModelProperty(value = "支付状态")
    private OrderPayStatusEnum payStatus;

    /**
     * 支付方式
     */
    @TableField(value = "payment_type")
    @ApiModelProperty(value = "支付方式")
    private PaymentTypeEnum paymentType;

    /**
     * 微信昵称
     */
    @TableField(value = "wx_nick_name")
    @ApiModelProperty(value = "微信昵称")
    private String wxNickName;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    /**
     * 退款时间
     */
    @TableField(value = "refund_time")
    @ApiModelProperty(value = "退款时间")
    private Date refundTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后更新时间")
    private Date lastUpdateTime;

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
     * 创建用户所属部门ids
     */
    @TableField(value = "creator_org_ids", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门ids", hidden = true)
    @JSONField(serialize = false)
    private String creatorOrgIds;

    /**
     * 创建用户所属部门名称
     */
    @TableField(value = "creator_org_names", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建用户所属部门名称")
    private String creatorOrgNames;

    /**
     * 指定用户部门ids
     */
    @TableField(value = "designate_org_ids")
    @ApiModelProperty(value = "指定用户部门ids")
    private String designateOrgIds;

    /**
     * 指定用户部门名称
     */
    @TableField(value = "designate_org_names")
    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;

    /**
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;

    public TicketOrdernoOtaordernoRelation(Long id, String orderNo, String otaOrderNo,
                                           Long channelId, String creatorOrgIds, String designateOrgIds,
                                           String creatorOrgNames, String designateOrgNames,
                                           Long memberId, String wxNickName, OrderPayStatusEnum payStatus) {
        this.id = id;
        this.orderNo = orderNo;
        this.otaOrderNo = otaOrderNo;
        this.channelId = channelId;
        this.creatorOrgIds = creatorOrgIds;
        this.creatorOrgNames = creatorOrgNames;
        this.designateOrgIds = designateOrgIds;
        this.designateOrgNames = designateOrgNames;
        this.memberId = memberId;
        this.wxNickName = wxNickName;
        this.payStatus = payStatus;
    }

    public TicketOrdernoOtaordernoRelation() {
    }
}