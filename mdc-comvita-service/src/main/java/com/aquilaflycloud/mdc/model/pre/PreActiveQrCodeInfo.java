package com.aquilaflycloud.mdc.model.pre;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "pre_activity_qr_code_info")
public class PreActiveQrCodeInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 活动id
     */
    @TableField(value = "activity_id")
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 关联门店id
     */
    @TableField(value = "org_id")
    @ApiModelProperty(value = "关联门店id")
    private Long orgId;

    /**
     * 关联门店名称
     */
    @TableField(value = "org_name")
    @ApiModelProperty(value = "关联门店名称")
    private String orgName;

    /**
     * 关联门店地址
     */
    @TableField(value = "org_address")
    @ApiModelProperty(value = "关联门店地址")
    private String orgAddress;

    /**
     * 微信appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信appId")
    private String appId;

    /**
     * 小程序路径
     */
    @TableField(value = "page_path")
    @ApiModelProperty(value = "小程序路径")
    private String pagePath;

    /**
     * 二维码地址
     */
    @TableField(value = "qr_code_url")
    @ApiModelProperty(value = "二维码地址")
    private String qrCodeUrl;

    /**
     * 小程序码oss标识
     */
    @TableField(value = "qr_code_file_key")
    @ApiModelProperty(value = "二维码oss标识", hidden = true)
    @JSONField(serialize = false)
    private String qrCodeFileKey;

    /**
     * 状态
     */
    @TableField(value = "state", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "状态")
    private StateEnum state;

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
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}
