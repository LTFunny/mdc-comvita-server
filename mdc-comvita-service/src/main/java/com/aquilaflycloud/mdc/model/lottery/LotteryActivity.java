package com.aquilaflycloud.mdc.model.lottery;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.lottery.LotteryModeEnum;
import com.aquilaflycloud.mdc.enums.lottery.LotteryStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "lottery_activity")
public class LotteryActivity implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信或支付宝appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    /**
     * 抽奖活动名称
     */
    @TableField(value = "lottery_name")
    @ApiModelProperty(value = "抽奖活动名称")
    private String lotteryName;

    /**
     * 活动开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    /**
     * 抽奖模板
     */
    @TableField(value = "lottery_mode")
    @ApiModelProperty(value = "抽奖模板")
    private LotteryModeEnum lotteryMode;

    /**
     * 抽奖详情
     */
    @TableField(value = "lottery_remark")
    @ApiModelProperty(value = "抽奖详情")
    private String lotteryRemark;

    /**
     * 抽奖规则内容
     */
    @TableField(value = "lottery_rule_content")
    @ApiModelProperty(value = "抽奖规则内容")
    private String lotteryRuleContent;

    /**
     * 抽奖算法内容
     */
    @TableField(value = "algorithm_content")
    @ApiModelProperty(value = "抽奖算法内容")
    private String algorithmContent;

    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态")
    private LotteryStateEnum state;

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