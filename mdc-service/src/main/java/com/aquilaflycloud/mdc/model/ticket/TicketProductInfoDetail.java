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
@TableName(value = "ticket_product_info_detail")
public class TicketProductInfoDetail implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 价格日期
     */
    @TableField(value = "price_date")
    @ApiModelProperty(value = "价格日期")
    private Date priceDate;

    /**
     * 结算价
     */
    @TableField(value = "product_price")
    @ApiModelProperty(value = "结算价")
    private BigDecimal productPrice;

    /**
     * 售卖价格
     */
    @TableField(value = "product_sell_price")
    @ApiModelProperty(value = "售卖价格")
    private BigDecimal productSellPrice;

    /**
     * 票面价
     */
    @TableField(value = "ticket_price")
    @ApiModelProperty(value = "票面价")
    private BigDecimal ticketPrice;

    /**
     * 总库存
     */
    @TableField(value = "total_inventory")
    @ApiModelProperty(value = "总库存")
    private String totalInventory;

    /**
     * 已使用库存
     */
    @TableField(value = "use_inventory")
    @ApiModelProperty(value = "已使用库存")
    private String useInventory;

    /**
     * 平台产品信息ID
     */
    @TableField(value = "product_info_id")
    @ApiModelProperty(value = "平台产品信息ID")
    private Long productInfoId;

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
     * 指定部门ids
     */
    @TableField(value = "designate_org_ids")
    @ApiModelProperty(value = "指定部门ids")
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

    public TicketProductInfoDetail(Long id, Date priceDate, BigDecimal productPrice, BigDecimal productSellPrice, BigDecimal ticketPrice, String totalInventory, String useInventory, Long productInfoId) {
        this.id = id;
        this.priceDate = priceDate;
        this.productPrice = productPrice;
        this.productSellPrice = productSellPrice;
        this.ticketPrice = ticketPrice;
        this.totalInventory = totalInventory;
        this.useInventory = useInventory;
        this.productInfoId = productInfoId;
    }

    public TicketProductInfoDetail(Long id, Date priceDate, BigDecimal productPrice, BigDecimal productSellPrice, BigDecimal ticketPrice, String totalInventory, String useInventory, Long productInfoId, Long tenantId, Long subTenantId) {
        this.id = id;
        this.priceDate = priceDate;
        this.productPrice = productPrice;
        this.productSellPrice = productSellPrice;
        this.ticketPrice = ticketPrice;
        this.totalInventory = totalInventory;
        this.useInventory = useInventory;
        this.productInfoId = productInfoId;
        this.tenantId = tenantId;
        this.subTenantId = subTenantId;
    }

    public TicketProductInfoDetail() {
    }
}