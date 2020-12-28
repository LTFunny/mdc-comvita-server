package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.ticket.*;
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
@TableName(value = "ticket_order_product_info")
public class TicketOrderProductInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 产品id
     */
    @TableField(value = "product_info_id")
    @ApiModelProperty(value = "产品id")
    private Long productInfoId;

    /**
     * 商户编码
     */
    @TableField(value = "merchant_code")
    @ApiModelProperty(value = "商户编码")
    private String merchantCode;

    /**
     * 产品名称
     */
    @TableField(value = "product_name")
    @ApiModelProperty(value = "产品名称")
    private String productName;

    /**
     * 产品类型
     */
    @TableField(value = "product_type")
    @ApiModelProperty(value = "产品类型")
    private Integer productType;

    /**
     * 道控产品ID
     */
    @TableField(value = "product_id")
    @ApiModelProperty(value = "道控产品ID")
    private Integer productId;

    /**
     * 产品编码
     */
    @TableField(value = "product_series_code")
    @ApiModelProperty(value = "产品编码")
    private String productSeriesCode;

    /**
     * 线下编码
     */
    @TableField(value = "offline_code")
    @ApiModelProperty(value = "线下编码")
    private String offlineCode;

    /**
     * 票面名称
     */
    @TableField(value = "ticket_name")
    @ApiModelProperty(value = "票面名称")
    private String ticketName;

    /**
     * 结算价
     */
    @TableField(value = "product_price")
    @ApiModelProperty(value = "结算价")
    private BigDecimal productPrice;

    /**
     * 售卖价
     */
    @TableField(value = "product_sell_price")
    @ApiModelProperty(value = "售卖价")
    private BigDecimal productSellPrice;

    /**
     * 票面价
     */
    @TableField(value = "ticket_price")
    @ApiModelProperty(value = "票面价")
    private BigDecimal ticketPrice;

    /**
     * 可使用次数
     */
    @TableField(value = "expire_num")
    @ApiModelProperty(value = "可使用次数")
    private Integer expireNum;

    /**
     * 有效天数
     */
    @TableField(value = "expiry_day")
    @ApiModelProperty(value = "有效天数")
    private Integer expiryDay;

    /**
     * 有效开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "有效开始时间")
    private Date startTime;

    /**
     * 有效结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "有效结束时间")
    private Date endTime;

    /**
     * 是否可预售
     */
    @TableField(value = "is_pre_sell")
    @ApiModelProperty(value = "是否可预售")
    private ProductInfoPreSellEnum isPreSell;

    /**
     * 预售开始时间
     */
    @TableField(value = "pre_sell_start_date")
    @ApiModelProperty(value = "预售开始时间")
    private Date preSellStartDate;

    /**
     * 预售结束时间
     */
    @TableField(value = "pre_sell_end_date")
    @ApiModelProperty(value = "预售结束时间")
    private Date preSellEndDate;

    /**
     * 是否延迟入园
     */
    @TableField(value = "is_date_line")
    @ApiModelProperty(value = "是否延迟入园")
    private Integer isDateLine;

    /**
     * 延迟小时
     */
    @TableField(value = "date_line_hour")
    @ApiModelProperty(value = "延迟小时")
    private Integer dateLineHour;

    /**
     * 是否发送短信
     */
    @TableField(value = "is_send_message")
    @ApiModelProperty(value = "是否发送短信")
    private Integer isSendMessage;

    /**
     * 核销终端
     */
    @TableField(value = "is_ternimal")
    @ApiModelProperty(value = "核销终端")
    private String isTernimal;

    /**
     * 是否当天售卖
     */
    @TableField(value = "is_sell_today")
    @ApiModelProperty(value = "是否当天售卖")
    private ProductInfoSellTodayEnum isSellToday;

    /**
     * 最大可售
     */
    @TableField(value = "sell_max")
    @ApiModelProperty(value = "最大可售")
    private Integer sellMax;

    /**
     * 最小可售
     */
    @TableField(value = "sell_min")
    @ApiModelProperty(value = "最小可售")
    private Integer sellMin;

    /**
     * 核销开始时间
     */
    @TableField(value = "check_start_time")
    @ApiModelProperty(value = "核销开始时间")
    private Date checkStartTime;

    /**
     * 核销结束时间
     */
    @TableField(value = "check_end_time")
    @ApiModelProperty(value = "核销结束时间")
    private Date checkEndTime;

    /**
     * 核销类型
     */
    @TableField(value = "check_allow_type")
    @ApiModelProperty(value = "核销类型")
    private String checkAllowType;

    /**
     * 是否允许退款
     */
    @TableField(value = "is_can_refund")
    @ApiModelProperty(value = "是否允许退款")
    private Integer isCanRefund;

    /**
     * 过期多少天内可退
     */
    @TableField(value = "refund_check_after")
    @ApiModelProperty(value = "过期多少天内可退")
    private Integer refundCheckAfter;

    /**
     * 当日是否可退
     */
    @TableField(value = "refund_can_today")
    @ApiModelProperty(value = "当日是否可退")
    private Integer refundCanToday;

    /**
     * 退款是否需要审核
     */
    @TableField(value = "refund_show_audit")
    @ApiModelProperty(value = "退款是否需要审核")
    private Integer refundShowAudit;

    /**
     * 是否大票模式:大票：每一条商品明细将会生成一个入园凭证码（不区分，每一个明细有多少游玩人信息），且可以只传1个联系人小票：每一条商品明细，会根据该明细的游玩人信息（数量），生成对应数量的入园凭证码，且商品明细数量必须和游玩人匹配
     */
    @TableField(value = "is_big_ticket")
    @ApiModelProperty(value = "是否大票模式:大票：每一条商品明细将会生成一个入园凭证码（不区分，每一个明细有多少游玩人信息），且可以只传1个联系人小票：每一条商品明细，会根据该明细的游玩人信息（数量），生成对应数量的入园凭证码，且商品明细数量必须和游玩人匹配")
    private ProductInfoBigTicketEnum isBigTicket;

    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态")
    private ProductInfoStateEnum state;

    /**
     * 是否推荐
     */
    @TableField(value = "is_recommend")
    @ApiModelProperty(value = "是否推荐")
    private ProductInfoRecommendEnum isRecommend;

    /**
     * 是否推荐
     */
    @TableField(value = "is_top")
    @ApiModelProperty(value = "是否推荐(0不置顶;1置顶)")
    private ProductInfoTopEnum isTop;

    /**
     * 产品类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "产品类型")
    private ProductInfoTypeEnum type;

    /**
     * 景区id
     */
    @TableField(value = "scenic_spot_id")
    @ApiModelProperty(value = "景区id")
    private Long scenicSpotId;

    /**
     * 景区类型
     */
    @TableField(value = "scenic_spot_type")
    @ApiModelProperty(value = "景区类型")
    private ScenicSpotTypeEnum scenicSpotType;

    /**
     * 景区名称
     */
    @TableField(value = "scenic_spot_name")
    @ApiModelProperty(value = "景区名称")
    private String scenicSpotName;

    /**
     * 置顶时间
     */
    @TableField(value = "top_time")
    @ApiModelProperty(value = "置顶时间")
    private Date topTime;

    /**
     * 当日最晚可购时间
     */
    @TableField(value = "buy_min_time")
    @ApiModelProperty(value = "当日最晚可购时间")
    private Date buyMinTime;

    /**
     * 是否指定游玩时间
     */
    @TableField(value = "is_set_play_time")
    @ApiModelProperty(value = "是否指定游玩时间")
    private ProductInfoSetPlayTimeEnum isSetPlayTime;

    /**
     * 可选类型
     */
    @TableField(value = "play_time_type")
    @ApiModelProperty(value = "可选类型")
    private ProductInfoPlayTimeType playTimeType;

    /**
     * 不可选日期集合
     */
    @TableField(value = "not_play_time")
    @ApiModelProperty(value = "不可选日期集合")
    private String notPlayTime;

    /**
     * 指定游玩开始时间
     */
    @TableField(value = "play_start_time")
    @ApiModelProperty(value = "指定游玩开始时间")
    private Date playStartTime;

    /**
     * 指定游玩结束时间
     */
    @TableField(value = "play_end_time")
    @ApiModelProperty(value = "指定游玩结束时间")
    private Date playEndTime;

    /**
     * 联票景区集合
     */
    @TableField(value = "scenic_spot_types")
    @ApiModelProperty(value = "联票景区集合")
    private String scenicSpotTypes;

    /**
     * 购买须知
     */
    @TableField(value = "buy_introduce")
    @ApiModelProperty(value = "购买须知")
    private String buyIntroduce;

    /**
     * 是否上架
     */
    @TableField(value = "is_upper")
    @ApiModelProperty(value = "是否上架")
    private ProductInfoUpperEnum isUpper;

    /**
     * 订单id
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value = "订单id")
    private Long orderId;

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
     * 创建记录人id
     */
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人id", hidden = true)
    @JSONField(serialize = false)
    private Long creatorId;

    /**
     * 创建记录人名称
     */
    @TableField(value = "creator_name", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建记录人名称")
    private String creatorName;

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
     * 最后操作人id
     */
    @TableField(value = "last_operator_id", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人id", hidden = true)
    @JSONField(serialize = false)
    private Long lastOperatorId;

    /**
     * 最后操作人名称
     */
    @TableField(value = "last_operator_name", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "最后操作人名称")
    private String lastOperatorName;

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