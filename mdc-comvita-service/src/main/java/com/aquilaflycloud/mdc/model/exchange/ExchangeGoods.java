package com.aquilaflycloud.mdc.model.exchange;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.exchange.*;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "exchange_goods")
public class ExchangeGoods implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 商品编码
     */
    @TableField(value = "goods_code")
    @ApiModelProperty(value = "商品编码")
    private String goodsCode;

    /**
     * 商品类型
     */
    @TableField(value = "goods_type")
    @ApiModelProperty(value = "商品类型")
    private GoodsTypeEnum goodsType;

    /**
     * 商品名称
     */
    @TableField(value = "goods_name")
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品图片
     */
    @TableField(value = "goods_imgs")
    @ApiModelProperty(value = "商品图片")
    private String goodsImgs;

    /**
     * 商品详细
     */
    @TableField(value = "goods_detail")
    @ApiModelProperty(value = "商品详细")
    private String goodsDetail;

    /**
     * 商品备注
     */
    @TableField(value = "goods_remark")
    @ApiModelProperty(value = "商品备注")
    private String goodsRemark;

    /**
     * 售后说明
     */
    @TableField(value = "goods_service")
    @ApiModelProperty(value = "售后说明")
    private String goodsService;

    /**
     * 关联商品id
     */
    @TableField(value = "rel_id")
    @ApiModelProperty(value = "关联商品id")
    private Long relId;

    /**
     * 关联商品内容
     */
    @TableField(value = "rel_content")
    @ApiModelProperty(value = "关联商品内容")
    private String relContent;

    /**
     * 奖励类型
     */
    @TableField(value = "reward_type")
    @ApiModelProperty(value = "奖励类型")
    private RewardTypeEnum rewardType;

    /**
     * 单件奖励值
     */
    @TableField(value = "single_reward")
    @ApiModelProperty(value = "单件奖励值")
    private Integer singleReward;

    /**
     * 单件金额
     */
    @TableField(value = "single_price")
    @ApiModelProperty(value = "单件金额")
    private BigDecimal singlePrice;

    /**
     * 市场参考价
     */
    @TableField(value = "market_price")
    @ApiModelProperty(value = "市场参考价")
    private BigDecimal marketPrice;

    /**
     * 库存
     */
    @TableField(value = "inventory")
    @ApiModelProperty(value = "库存")
    private Integer inventory;

    /**
     * 会员兑换上限
     */
    @TableField(value = "exchange_limit")
    @ApiModelProperty(value = "会员兑换上限")
    private Integer exchangeLimit;

    /**
     * 已兑换数
     */
    @TableField(value = "exchange_count")
    @ApiModelProperty(value = "已兑换数")
    private Integer exchangeCount;

    /**
     * 退款类型
     */
    @TableField(value = "refund_type")
    @ApiModelProperty(value = "退款类型")
    private RefundTypeEnum refundType;

    /**
     * 是否过期自动退款
     */
    @TableField(value = "refund_expired")
    @ApiModelProperty(value = "是否过期自动退款")
    private WhetherEnum refundExpired;

    /**
     * 快递金额
     */
    @TableField(value = "express_price")
    @ApiModelProperty(value = "快递金额")
    private BigDecimal expressPrice;

    /**
     * 上架类型
     */
    @TableField(value = "shelve_type")
    @ApiModelProperty(value = "上架类型")
    private ShelveTypeEnum shelveType;

    /**
     * 上架时间
     */
    @TableField(value = "shelve_time")
    @ApiModelProperty(value = "上架时间")
    private Date shelveTime;

    /**
     * 审核状态
     */
    @TableField(value = "audit_state", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "审核状态")
    private AuditStateEnum auditState;

    /**
     * 审核说明
     */
    @TableField(value = "audit_remark")
    @ApiModelProperty(value = "审核说明")
    private String auditRemark;

    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态")
    private GoodsStateEnum state;

    /**
     * 配送类型
     */
    @TableField(value = "delivery_type")
    @ApiModelProperty(value = "配送类型")
    private DeliveryTypeEnum deliveryType;

    /**
     * 自提地址
     */
    @TableField(value = "delivery_address")
    @ApiModelProperty(value = "自提地址")
    private String deliveryAddress;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(value = "is_delete", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "逻辑删除字段", hidden = true)
    @JSONField(serialize = false)
    private Integer isDelete;

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
