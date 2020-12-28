package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 景区信息表
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@Data
@TableName(value = "ticket_scenic_spot_info")
public class TicketScenicSpotInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键")
    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "景区名称")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "景区简称")
    @TableField(value = "simple_name")
    private String simpleName;

    @ApiModelProperty(value = "景区图片url")
    @TableField(value = "pic_url")
    private String picUrl;

    @ApiModelProperty(value = "地址")
    @TableField(value = "address")
    private String address;

    @ApiModelProperty(value = "营业开始时间")
    @TableField(value = "business_start_time")
    private String businessStartTime;

    @ApiModelProperty(value = "营业结束时间")
    @TableField(value = "business_end_time")
    private String businessEndTime;

    @ApiModelProperty(value = "客服联系电话")
    @TableField(value = "contact_number")
    private String contactNumber;

    @ApiModelProperty(value = "景区简介")
    @TableField(value = "introduction")
    private String introduction;

    @ApiModelProperty(value = "特色景点")
    @TableField(value = "special_scenic_spot")
    private String specialScenicSpot;

    @ApiModelProperty(value = "景区类型(ticket.ScenicSpotTypeEnum)")
    @TableField(value = "type")
    private ScenicSpotTypeEnum type;

    @ApiModelProperty(value = "经度")
    @TableField(value = "longitude")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    @TableField(value = "latitude")
    private String latitude;

    @ApiModelProperty(value = "省编码")
    @TableField(value = "province")
    private String province;

    @ApiModelProperty(value = "市编码")
    @TableField(value = "city")
    private String city;

    @ApiModelProperty(value = "账号id")
    @TableField(value = "account_id", updateStrategy = FieldStrategy.IGNORED)
    private Long accountId;

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
}
