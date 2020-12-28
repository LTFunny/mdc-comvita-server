package com.aquilaflycloud.mdc.model.member;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "member_event_log")
public class MemberEventLog implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 会员id
     */
    @TableField(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 业务类型
     */
    @TableField(value = "business_type")
    @ApiModelProperty(value = "业务类型")
    private BusinessTypeEnum businessType;

    /**
     * 业务id
     */
    @TableField(value = "business_id")
    @ApiModelProperty(value = "业务id")
    private Long businessId;

    /**
     * 事件类型
     */
    @TableField(value = "event_type")
    @ApiModelProperty(value = "事件类型")
    private EventTypeEnum eventType;

    /**
     * 事件发生时间
     */
    @TableField(value = "event_time")
    @ApiModelProperty(value = "事件发生时间")
    private Date eventTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

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
     * isv的appId
     */
    @TableField(value = "app_key", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "isv的appId", hidden = true)
    @JSONField(serialize = false)
    private String appKey;

    private static final long serialVersionUID = 1L;
}