package com.aquilaflycloud.mdc.model.sign;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.sign.LimitTypeEnum;
import com.aquilaflycloud.mdc.enums.sign.SignRewardTypeEnum;
import com.aquilaflycloud.mdc.enums.sign.SignStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "offline_sign_activity")
public class OfflineSignActivity implements Serializable {
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
     * 打卡活动名称
     */
    @TableField(value = "sign_name")
    @ApiModelProperty(value = "打卡活动名称")
    private String signName;

    /**
     * 打卡活动详情
     */
    @TableField(value = "sign_detail")
    @ApiModelProperty(value = "打卡活动详情")
    private String signDetail;

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
     * 活动图片
     */
    @TableField(value = "img_url")
    @ApiModelProperty(value = "活动图片")
    private String imgUrl;

    /**
     * 打卡限制类型
     */
    @TableField(value = "limit_type")
    @ApiModelProperty(value = "打卡限制类型")
    private LimitTypeEnum limitType;

    /**
     * 打卡奖励类型
     */
    @TableField(value = "sign_reward_type")
    @ApiModelProperty(value = "打卡奖励类型")
    private SignRewardTypeEnum signRewardType;

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
     * 小程序码地址
     */
    @TableField(value = "code_url")
    @ApiModelProperty(value = "小程序码地址")
    private String codeUrl;

    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态")
    private SignStateEnum state;

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
