package com.aquilaflycloud.mdc.model.pre;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 规则信息表
 */
@Data
@TableName(value = "pre_rule_info")
public class PreRuleInfo implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 规则名称
     */
    @TableField(value = "rule_name")
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 规则类型(3-下单满减、2-下单折扣、1-下单即送)
     */
    @TableField(value = "rule_type")
    @ApiModelProperty(value = "规则类型(3-下单满减、2-下单折扣、1-下单即送)")
    private Integer ruleType;

    /**
     * 类型详情  保存为json串 样例如: type为下单满减 满100减10  {"full_price":"100","reduce_price":"10"}
     */
    @TableField(value = "type_detail")
    @ApiModelProperty(value = "类型详情  保存为json串 样例如: type为下单满减 满100减10  {'full_price':'100','reduce_price':'10'}")
    private String typeDetail;

    /**
     * 状态(1-已启用、0-已停用)
     */
    @TableField(value = "rule_state")
    @ApiModelProperty(value = "状态(1-已启用、0-已停用)")
    private Integer ruleState;

    /**
     * 是否默认(0-否 1-是)
     */
    @TableField(value = "is_default")
    @ApiModelProperty(value = "是否默认(0-否 1-是)")
    private Integer isDefault;

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
