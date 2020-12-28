package com.aquilaflycloud.mdc.model.lottery;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.lottery.PrizeTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "lottery_prize")
public class LotteryPrize implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 抽奖活动id
     */
    @TableField(value = "lottery_id")
    @ApiModelProperty(value = "抽奖活动id")
    private Long lotteryId;

    /**
     * 奖品等级
     */
    @TableField(value = "prize_level")
    @ApiModelProperty(value = "奖品等级")
    private String prizeLevel;

    /**
     * 奖品图片
     */
    @TableField(value = "prize_img")
    @ApiModelProperty(value = "奖品图片")
    private String prizeImg;

    /**
     * 奖品排序
     */
    @TableField(value = "prize_order")
    @ApiModelProperty(value = "奖品排序")
    private Integer prizeOrder;

    /**
     * 奖品类型
     */
    @TableField(value = "prize_type")
    @ApiModelProperty(value = "奖品类型")
    private PrizeTypeEnum prizeType;

    /**
     * 关联奖品id
     */
    @TableField(value = "rel_id")
    @ApiModelProperty(value = "关联奖品id")
    private Long relId;

    /**
     * 关联奖品内容
     */
    @TableField(value = "rel_content")
    @ApiModelProperty(value = "关联奖品内容")
    private String relContent;

    /**
     * 奖品库存
     */
    @TableField(value = "inventory")
    @ApiModelProperty(value = "奖品库存")
    private Integer inventory;

    /**
     * 中奖数
     */
    @TableField(value = "won_count")
    @ApiModelProperty(value = "中奖数")
    private Integer wonCount;

    /**
     * 抽奖算法内容
     */
    @TableField(value = "algorithm_content")
    @ApiModelProperty(value = "抽奖算法内容")
    private String algorithmContent;

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
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}