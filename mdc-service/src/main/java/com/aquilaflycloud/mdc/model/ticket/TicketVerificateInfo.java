package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
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
@TableName(value = "ticket_verificate_info")
public class TicketVerificateInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 商户编码
     */
    @TableField(value = "merchant_code")
    @ApiModelProperty(value="商户编码")
    private String merchantCode;

    /**
     * 商户名称
     */
    @TableField(value = "merchant_name")
    @ApiModelProperty(value="商户名称")
    private String merchantName;

    /**
     * 商户订单号
     */
    @TableField(value = "order_no")
    @ApiModelProperty(value="商户订单号")
    private String orderNo;

    /**
     * OTA订单号
     */
    @TableField(value = "out_order_no")
    @ApiModelProperty(value="OTA订单号")
    private String outOrderNo;

    /**
     * 道控产品ID
     */
    @TableField(value = "product_id")
    @ApiModelProperty(value="道控产品ID")
    private Integer productId;

    /**
     * 结算价
     */
    @TableField(value = "settle_fee")
    @ApiModelProperty(value="结算价")
    private BigDecimal settleFee;

    /**
     * 售卖价
     */
    @TableField(value = "selling_fee")
    @ApiModelProperty(value="售卖价")
    private BigDecimal sellingFee;

    /**
     * 订单数量
     */
    @TableField(value = "order_qty")
    @ApiModelProperty(value="订单数量")
    private Integer orderQty;

    /**
     * 凭证码
     */
    @TableField(value = "ecode")
    @ApiModelProperty(value="凭证码")
    private String ecode;

    /**
     * 二维码链接
     */
    @TableField(value = "url_ecode")
    @ApiModelProperty(value="二维码链接")
    private String urlEcode;

    /**
     * 核销数量
     */
    @TableField(value = "verificate_num")
    @ApiModelProperty(value="核销数量")
    private Integer verificateNum;

    /**
     * 核销次数
     */
    @TableField(value = "verificate_times")
    @ApiModelProperty(value="核销次数")
    private Integer verificateTimes;

    /**
     * 游玩开始时间
     */
    @TableField(value = "start_date")
    @ApiModelProperty(value="游玩开始时间")
    private Date startDate;

    /**
     * 结束时间
     */
    @TableField(value = "end_date")
    @ApiModelProperty(value="结束时间")
    private Date endDate;

    /**
     * 手机号
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value="手机号")
    private String mobile;

    /**
     * 姓名
     */
    @TableField(value = "id_card_name")
    @ApiModelProperty(value="姓名")
    private String idCardName;

    /**
     * 身份证
     */
    @TableField(value = "id_card_no")
    @ApiModelProperty(value="身份证")
    private String idCardNo;

    /**
     * 商户订单号与OTA订单号关系ID
     */
    @TableField(value = "orderno_otaorderno_relation_id")
    @ApiModelProperty(value="商户订单号与OTA订单号关系ID")
    private Long ordernoOtaordernoRelationId;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value="会员id")
    private Long memberId;

    /**
     * 渠道id
     */
    @TableField(value = "channel_id")
    @ApiModelProperty(value="渠道id")
    private Long channelId;

    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    @ApiModelProperty(value="产品名称")
    private String productName;

    @ApiModelProperty(value = "可核销总数量")
    @TableField("code_qty")
    private Integer codeQty;

    @ApiModelProperty(value = "可核销总次数")
    @TableField("code_cnt")
    private Integer codeCnt;

    @ApiModelProperty(value = "已核销数量")
    @TableField("use_qty")
    private Integer useQty;

    @ApiModelProperty(value = "已核销次数")
    @TableField("use_cnt")
    private Integer useCnt;

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
}