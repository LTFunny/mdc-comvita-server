package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.ticket.OrderInfoStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "ticket_order_info")
public class TicketOrderInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商户编码
     */
    @TableField(value = "merchant_code")
    @ApiModelProperty(value = "商户编码")
    private String merchantCode;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_name")
    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    /**
     * 商户订单号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value = "商户订单号")
    private String orderNo;

    /**
     * OTA订单号
     */
    @TableField(value = "out_order_no")
    @ApiModelProperty(value = "OTA订单号")
    private String outOrderNo;

    /**
     * 道控产品ID
     */
    @TableField(value = "product_id")
    @ApiModelProperty(value = "道控产品ID")
    private Integer productId;

    /**
     * 结算价
     */
    @TableField(value = "settle_fee")
    @ApiModelProperty(value = "结算价")
    private BigDecimal settleFee;

    /**
     * 售卖价
     */
    @TableField(value = "selling_fee")
    @ApiModelProperty(value = "售卖价")
    private BigDecimal sellingFee;

    /**
     * 订单数量
     */
    @TableField(value = "order_qty")
    @ApiModelProperty(value = "订单数量")
    private Integer orderQty;

    /**
     * 凭证码
     */
    @TableField(value = "ecode")
    @ApiModelProperty(value = "凭证码")
    private String ecode;

    /**
     * 二维码链接
     */
    @TableField(value = "url_ecode")
    @ApiModelProperty(value = "二维码链接")
    private String urlEcode;

    /**
     * 可核销总数量
     */
    @TableField(value = "code_qty")
    @ApiModelProperty(value = "可核销总数量")
    private Integer codeQty;

    /**
     * 可核销总次数
     */
    @TableField(value = "code_cnt")
    @ApiModelProperty(value = "可核销总次数")
    private Integer codeCnt;

    /**
     * 已核销数量
     */
    @TableField(value = "use_qty")
    @ApiModelProperty(value = "已核销数量")
    private Integer useQty;

    /**
     * 已核销次数
     */
    @TableField(value = "use_cnt")
    @ApiModelProperty(value = "已核销次数")
    private Integer useCnt;

    /**
     * 已退票数量
     */
    @TableField(value = "refund_qty")
    @ApiModelProperty(value = "已退票数量")
    private Integer refundQty;

    /**
     * 已退次数
     */
    @TableField(value = "refund_cnt")
    @ApiModelProperty(value = "已退次数")
    private Integer refundCnt;

    /**
     * 游玩开始时间
     */
    @TableField(value = "start_date")
    @ApiModelProperty(value = "游玩开始时间")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField(value = "end_date")
    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    /**
     * 游玩时间
     */
    @ApiModelProperty(value = "游玩时间")
    @TableField("play_date")
    private Date playDate;

    /**
     * 状态
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    private OrderInfoStatusEnum status;

    /**
     * 手机号
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 姓名
     */
    @TableField(value = "id_card_name")
    @ApiModelProperty(value = "姓名")
    private String idCardName;

    /**
     * 身份证
     */
    @TableField(value = "id_card_no")
    @ApiModelProperty(value = "身份证")
    private String idCardNo;

    /**
     * 商户订单号与OTA订单号关系ID
     */
    @TableField(value = "orderno_otaorderno_relation_id")
    @ApiModelProperty(value = "商户订单号与OTA订单号关系ID")
    private Long ordernoOtaordernoRelationId;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 渠道id
     */
    @TableField(value = "channel_id")
    @ApiModelProperty(value = "渠道id")
    private Long channelId;

    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 总金额(创建时计算)
     */
    @TableField(value = "amount")
    @ApiModelProperty(value = "总金额(创建时计算)")
    private BigDecimal amount;

    /**
     * 微信昵称
     */
    @TableField(value = "wx_nick_name")
    @ApiModelProperty(value = "微信昵称")
    private String wxNickName;

    /**
     * 支付方式
     */
    @TableField(value = "payment_type")
    @ApiModelProperty(value = "支付方式")
    private PaymentTypeEnum paymentType;

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

    public TicketOrderInfo() {
    }

    public TicketOrderInfo(Long id, String merchantCode, String merchantName, String orderNo, String outOrderNo, Integer productId, BigDecimal settleFee, BigDecimal sellingFee, Integer orderQty, String ecode, String urlEcode, Integer codeQty, Integer codeCnt, Integer useQty, Integer useCnt, Integer refundQty, Integer refundCnt, Date startDate, Date endDate, OrderInfoStatusEnum status, String mobile, String idCardName, String idCardNo, Long memberId, String wxNickName) {
        this.id = id;
        this.merchantCode = merchantCode;
        this.merchantName = merchantName;
        this.orderNo = orderNo;
        this.outOrderNo = outOrderNo;
        this.productId = productId;
        this.settleFee = settleFee;
        this.sellingFee = sellingFee;
        this.orderQty = orderQty;
        this.ecode = ecode;
        this.urlEcode = urlEcode;
        this.codeQty = codeQty;
        this.codeCnt = codeCnt;
        this.useQty = useQty;
        this.useCnt = useCnt;
        this.refundQty = refundQty;
        this.refundCnt = refundCnt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mobile = mobile;
        this.idCardName = idCardName;
        this.idCardNo = idCardNo;
        this.memberId = memberId;
        this.wxNickName = wxNickName;
    }

    public TicketOrderInfo(String merchantCode, String merchantName, String orderNo, String outOrderNo, Integer productId, BigDecimal settleFee, BigDecimal sellingFee, Integer orderQty, String ecode, Integer codeQty, Integer useQty, Integer refundQty, Integer refundCnt, Date startDate, Date endDate, OrderInfoStatusEnum status, String mobile, String idCardName) {
        this.id = id;
        this.merchantCode = merchantCode;
        this.merchantName = merchantName;
        this.orderNo = orderNo;
        this.outOrderNo = outOrderNo;
        this.productId = productId;
        this.settleFee = settleFee;
        this.sellingFee = sellingFee;
        this.orderQty = orderQty;
        this.ecode = ecode;
        this.codeQty = codeQty;
        this.useQty = useQty;
        this.refundQty = refundQty;
        this.refundCnt = refundCnt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mobile = mobile;
        this.idCardName = idCardName;
    }

    public TicketOrderInfo(String merchantCode, String merchantName, String orderNo, String outOrderNo, Integer productId, BigDecimal settleFee, BigDecimal sellingFee, Integer orderQty, String ecode, String urlEcode, Integer codeQty, Integer useQty, Integer refundQty, Integer refundCnt, Date startDate, Date endDate, OrderInfoStatusEnum status, String mobile, String idCardName, String idCardNo) {
        this.merchantCode = merchantCode;
        this.merchantName = merchantName;
        this.orderNo = orderNo;
        this.outOrderNo = outOrderNo;
        this.productId = productId;
        this.settleFee = settleFee;
        this.sellingFee = sellingFee;
        this.orderQty = orderQty;
        this.ecode = ecode;
        this.urlEcode = urlEcode;
        this.codeQty = codeQty;
        this.useQty = useQty;
        this.refundQty = refundQty;
        this.refundCnt = refundCnt;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mobile = mobile;
        this.idCardName = idCardName;
        this.idCardNo = idCardNo;
    }
}