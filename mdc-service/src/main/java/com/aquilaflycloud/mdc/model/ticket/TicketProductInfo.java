package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.ticket.*;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 产品信息
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@Data
@TableName(value = "ticket_product_info")
public class TicketProductInfo {
    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "商户编码")
    @TableField("merchant_code")
    private String merchantCode;

    @ApiModelProperty(value = "产品名称")
    @TableField("product_name")
    private String productName;

    @ApiModelProperty(value = "产品类型")
    @TableField("product_type")
    private Integer productType;

    @ApiModelProperty(value = "道控产品ID")
    @TableField("product_id")
    private Integer productId;

    @ApiModelProperty(value = "产品编码")
    @TableField("product_series_code")
    private String productSeriesCode;

    @ApiModelProperty(value = "线下编码")
    @TableField("offline_code")
    private String offlineCode;

    @ApiModelProperty(value = "票面名称")
    @TableField("ticket_name")
    private String ticketName;

    @ApiModelProperty(value = "结算价")
    @TableField("product_price")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "售卖价")
    @TableField("product_sell_price")
    private BigDecimal productSellPrice;

    @ApiModelProperty(value = "票面价")
    @TableField("ticket_price")
    private BigDecimal ticketPrice;

    @ApiModelProperty(value = "可使用次数")
    @TableField("expire_num")
    private Integer expireNum;

    @ApiModelProperty(value = "有效天数")
    @TableField("expiry_day")
    private Integer expiryDay;

    @ApiModelProperty(value = "有效开始时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty(value = "有效结束时间")
    @TableField("end_time")
    private Date endTime;

    @ApiModelProperty(value = "是否可预售(ticket.ProductInfoPreSellEnum)")
    @TableField("is_pre_sell")
    private ProductInfoPreSellEnum isPreSell;

    @ApiModelProperty(value = "预售开始时间")
    @TableField("pre_sell_start_date")
    private Date preSellStartDate;

    @ApiModelProperty(value = "预售结束时间")
    @TableField("pre_sell_end_date")
    private Date preSellEndDate;

    @ApiModelProperty(value = "是否延迟入园")
    @TableField("is_date_line")
    private Integer isDateLine;

    @ApiModelProperty(value = "延迟小时")
    @TableField("date_line_hour")
    private Integer dateLineHour;

    @ApiModelProperty(value = "是否发送短信")
    @TableField("is_send_message")
    private Integer isSendMessage;

    @ApiModelProperty(value = "核销终端")
    @TableField("is_ternimal")
    private String isTernimal;

    @ApiModelProperty(value = "是否当天售卖(ticket.ProductInfoSellTodayEnum)")
    @TableField("is_sell_today")
    private ProductInfoSellTodayEnum isSellToday;

    @ApiModelProperty(value = "最大可售")
    @TableField("sell_max")
    private Integer sellMax;

    @ApiModelProperty(value = "最小可售")
    @TableField("sell_min")
    private Integer sellMin;

    @ApiModelProperty(value = "核销开始时间")
    @TableField("check_start_time")
    private Date checkStartTime;

    @ApiModelProperty(value = "核销结束时间")
    @TableField("check_end_time")
    private Date checkEndTime;

    @ApiModelProperty(value = "核销类型")
    @TableField("check_allow_type")
    private String checkAllowType;

    @ApiModelProperty(value = "是否允许退款")
    @TableField("is_can_refund")
    private Integer isCanRefund;

    @ApiModelProperty(value = "过期多少天内可退")
    @TableField("refund_check_after")
    private Integer refundCheckAfter;

    @ApiModelProperty(value = "当日是否可退")
    @TableField("refund_can_today")
    private Integer refundCanToday;

    @ApiModelProperty(value = "退款是否需要审核")
    @TableField("refund_show_audit")
    private Integer refundShowAudit;

    @ApiModelProperty(value = "是否大票模式:大票：每一条商品明细将会生成一个入园凭证码（不区分，每一个明细有多少游玩人信息），且可以只传1个联系人小票：每一条商品明细，会根据该明细的游玩人信息（数量），生成对应数量的入园凭证码，且商品明细数量必须和游玩人匹配(ticket.ProductInfoBigTicketEnum)")
    @TableField("is_big_ticket")
    private ProductInfoBigTicketEnum isBigTicket;

    @ApiModelProperty(value = "状态(ticket.ProductInfoStateEnum)")
    @TableField("state")
    private ProductInfoStateEnum state;

    @ApiModelProperty(value = "是否推荐(ticket.ProductInfoRecommendEnum)")
    @TableField("is_recommend")
    private ProductInfoRecommendEnum isRecommend;

    @ApiModelProperty(value = "是否推荐(ticket.ProductInfoTopEnum)")
    @TableField("is_top")
    private ProductInfoTopEnum isTop;

    @ApiModelProperty(value = "置顶时间")
    @TableField(value = "top_time")
    private Date topTime;

    @ApiModelProperty(value = "产品类型(ticket.ProductInfoTypeEnum)")
    @TableField("type")
    private ProductInfoTypeEnum type;

    @ApiModelProperty(value = "联票设置景区枚举集合")
    @TableField("scenic_spot_types")
    private String scenicSpotTypes;

    @ApiModelProperty(value = "景区id")
    @TableField("scenic_spot_id")
    private Long scenicSpotId;

    @ApiModelProperty(value = "景区类型(ticket.ScenicSpotTypeEnum)")
    @TableField("scenic_spot_type")
    private ScenicSpotTypeEnum scenicSpotType;

    @ApiModelProperty(value = "景区名称")
    @TableField("scenic_spot_name")
    private String scenicSpotName;

    @ApiModelProperty(value = "当日最晚可购时间")
    @TableField(value = "buy_min_time")
    private Date buyMinTime;

    @ApiModelProperty(value = "是否设置游玩时间(ticket.ProductInfoAppointPlayTimeEnum)")
    @TableField(value = "is_set_play_time")
    private ProductInfoSetPlayTimeEnum isSetPlayTime;

    @ApiModelProperty(value = "可选日期类型(ticket.ProductInfoPlayTimeType)")
    @TableField(value = "play_time_type")
    private ProductInfoPlayTimeType playTimeType;

    @ApiModelProperty(value = "自定义游玩开始时间")
    @TableField(value = "play_start_time")
    private Date playStartTime;

    @ApiModelProperty(value = "自定义游玩结束时间")
    @TableField(value = "play_end_time")
    private Date playEndTime;

    @ApiModelProperty(value = "不可选日期集合")
    @TableField(value = "not_play_time")
    private String notPlayTime;

    @ApiModelProperty(value = "购买须知")
    @TableField(value = "buy_introduce")
    private String buyIntroduce;

    @ApiModelProperty(value = "是否上架")
    @TableField(value = "is_upper")
    private ProductInfoUpperEnum isUpper;

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

    public TicketProductInfo(Long id, String merchantCode, String productName,
                             Integer productType, Integer productId, String productSeriesCode,
                             String offlineCode, String ticketName, BigDecimal productPrice,
                             BigDecimal productSellPrice, BigDecimal ticketPrice, Integer expireNum,
                             Integer expiryDay, Date startTime, Date endTime, ProductInfoPreSellEnum isPreSell,
                             Date preSellStartDate, Date preSellEndDate, Integer isDateLine, Integer dateLineHour,
                             Integer isSendMessage, String isTernimal, ProductInfoSellTodayEnum isSellToday,
                             Integer sellMax, Integer sellMin, Date checkStartTime, Date checkEndTime,
                             String checkAllowType, Integer isCanRefund, Integer refundCheckAfter,
                             Integer refundCanToday, Integer refundShowAudit, ProductInfoBigTicketEnum isBigTicket,
                             ProductInfoStateEnum state, ProductInfoRecommendEnum isRecommend, ProductInfoTopEnum isTop,
                             ProductInfoTypeEnum type, Date buyMinTime, String designateOrgIds, String designateOrgNames,
                             Long scenicSpotId, ScenicSpotTypeEnum scenicSpotType, String scenicSpotName) {
        this.id = id;
        this.merchantCode = merchantCode;
        this.productName = productName;
        this.productType = productType;
        this.productId = productId;
        this.productSeriesCode = productSeriesCode;
        this.offlineCode = offlineCode;
        this.ticketName = ticketName;
        this.productPrice = productPrice;
        this.productSellPrice = productSellPrice;
        this.ticketPrice = ticketPrice;
        this.expireNum = expireNum;
        this.expiryDay = expiryDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isPreSell = isPreSell;
        this.preSellStartDate = preSellStartDate;
        this.preSellEndDate = preSellEndDate;
        this.isDateLine = isDateLine;
        this.dateLineHour = dateLineHour;
        this.isSendMessage = isSendMessage;
        this.isTernimal = isTernimal;
        this.isSellToday = isSellToday;
        this.sellMax = sellMax;
        this.sellMin = sellMin;
        this.checkStartTime = checkStartTime;
        this.checkEndTime = checkEndTime;
        this.checkAllowType = checkAllowType;
        this.isCanRefund = isCanRefund;
        this.refundCheckAfter = refundCheckAfter;
        this.refundCanToday = refundCanToday;
        this.refundShowAudit = refundShowAudit;
        this.isBigTicket = isBigTicket;
        this.state = state;
        this.isRecommend = isRecommend;
        this.isTop = isTop;
        this.type = type;
        this.buyMinTime = buyMinTime;
        this.designateOrgIds = designateOrgIds;
        this.designateOrgNames = designateOrgNames;
        this.scenicSpotId = scenicSpotId;
        this.scenicSpotType = scenicSpotType;
        this.scenicSpotName = scenicSpotName;
    }

    public TicketProductInfo(Long id, String merchantCode, String productName,
                             Integer productType, Integer productId, String productSeriesCode,
                             String offlineCode, String ticketName, BigDecimal productPrice,
                             BigDecimal productSellPrice, BigDecimal ticketPrice, Integer expireNum,
                             Integer expiryDay, Date startTime, Date endTime, ProductInfoPreSellEnum isPreSell,
                             Date preSellStartDate, Date preSellEndDate, Integer isDateLine, Integer dateLineHour,
                             Integer isSendMessage, String isTernimal, ProductInfoSellTodayEnum isSellToday,
                             Integer sellMax, Integer sellMin, Date checkStartTime, Date checkEndTime,
                             String checkAllowType, Integer isCanRefund, Integer refundCheckAfter, Integer refundCanToday,
                             Integer refundShowAudit, ProductInfoBigTicketEnum isBigTicket, ProductInfoStateEnum state,
                             ProductInfoRecommendEnum isRecommend, ProductInfoTopEnum isTop, ProductInfoTypeEnum type,
                             Date buyMinTime, String designateOrgIds, String designateOrgNames, Long scenicSpotId,
                             ScenicSpotTypeEnum scenicSpotType, String scenicSpotName, Long tenantId, Long subTenantId) {
        this.id = id;
        this.merchantCode = merchantCode;
        this.productName = productName;
        this.productType = productType;
        this.productId = productId;
        this.productSeriesCode = productSeriesCode;
        this.offlineCode = offlineCode;
        this.ticketName = ticketName;
        this.productPrice = productPrice;
        this.productSellPrice = productSellPrice;
        this.ticketPrice = ticketPrice;
        this.expireNum = expireNum;
        this.expiryDay = expiryDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isPreSell = isPreSell;
        this.preSellStartDate = preSellStartDate;
        this.preSellEndDate = preSellEndDate;
        this.isDateLine = isDateLine;
        this.dateLineHour = dateLineHour;
        this.isSendMessage = isSendMessage;
        this.isTernimal = isTernimal;
        this.isSellToday = isSellToday;
        this.sellMax = sellMax;
        this.sellMin = sellMin;
        this.checkStartTime = checkStartTime;
        this.checkEndTime = checkEndTime;
        this.checkAllowType = checkAllowType;
        this.isCanRefund = isCanRefund;
        this.refundCheckAfter = refundCheckAfter;
        this.refundCanToday = refundCanToday;
        this.refundShowAudit = refundShowAudit;
        this.isBigTicket = isBigTicket;
        this.state = state;
        this.isRecommend = isRecommend;
        this.isTop = isTop;
        this.type = type;
        this.buyMinTime = buyMinTime;
        this.designateOrgIds = designateOrgIds;
        this.designateOrgNames = designateOrgNames;
        this.scenicSpotId = scenicSpotId;
        this.scenicSpotType = scenicSpotType;
        this.scenicSpotName = scenicSpotName;
        this.tenantId = tenantId;
        this.subTenantId = subTenantId;
    }

    public TicketProductInfo(Long id, String merchantCode, String productName, Integer productType, Integer productId, String productSeriesCode, String offlineCode, String ticketName, BigDecimal productPrice, BigDecimal productSellPrice, BigDecimal ticketPrice, Integer expireNum, Integer expiryDay, Date startTime, Date endTime, ProductInfoPreSellEnum isPreSell, Date preSellStartDate, Date preSellEndDate, Integer isDateLine, Integer dateLineHour, Integer isSendMessage, String isTernimal, ProductInfoSellTodayEnum isSellToday, Integer sellMax, Integer sellMin, Date checkStartTime, Date checkEndTime, String checkAllowType, Integer isCanRefund, Integer refundCheckAfter, Integer refundCanToday, Integer refundShowAudit, ProductInfoBigTicketEnum isBigTicket, ProductInfoStateEnum state, ProductInfoRecommendEnum isRecommend, ProductInfoTopEnum isTop, ProductInfoTypeEnum type, Date buyMinTime, String designateOrgIds, Long scenicSpotId, ScenicSpotTypeEnum scenicSpotType, String scenicSpotName, Long tenantId, Long subTenantId) {
        this.id = id;
        this.merchantCode = merchantCode;
        this.productName = productName;
        this.productType = productType;
        this.productId = productId;
        this.productSeriesCode = productSeriesCode;
        this.offlineCode = offlineCode;
        this.ticketName = ticketName;
        this.productPrice = productPrice;
        this.productSellPrice = productSellPrice;
        this.ticketPrice = ticketPrice;
        this.expireNum = expireNum;
        this.expiryDay = expiryDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isPreSell = isPreSell;
        this.preSellStartDate = preSellStartDate;
        this.preSellEndDate = preSellEndDate;
        this.isDateLine = isDateLine;
        this.dateLineHour = dateLineHour;
        this.isSendMessage = isSendMessage;
        this.isTernimal = isTernimal;
        this.isSellToday = isSellToday;
        this.sellMax = sellMax;
        this.sellMin = sellMin;
        this.checkStartTime = checkStartTime;
        this.checkEndTime = checkEndTime;
        this.checkAllowType = checkAllowType;
        this.isCanRefund = isCanRefund;
        this.refundCheckAfter = refundCheckAfter;
        this.refundCanToday = refundCanToday;
        this.refundShowAudit = refundShowAudit;
        this.isBigTicket = isBigTicket;
        this.state = state;
        this.isRecommend = isRecommend;
        this.isTop = isTop;
        this.type = type;
        this.buyMinTime = buyMinTime;
        this.designateOrgIds = designateOrgIds;
        this.scenicSpotId = scenicSpotId;
        this.scenicSpotType = scenicSpotType;
        this.scenicSpotName = scenicSpotName;
        this.tenantId = tenantId;
        this.subTenantId = subTenantId;
    }

    public TicketProductInfo() {
    }
}
