package com.aquilaflycloud.mdc.model.pre;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动门店会员关联表
 * @author linkq
 */
@Data
@TableName(value = "pre_activity_organization_member_ref")
public class PreActiveOrganizationMemberRef implements Serializable {

    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 活动id
     */
    @TableId(value = "activity_id")
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 会员id
     */
    @TableId(value = "member_id")
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    /**
     * 门店id
     */
    @TableId(value = "ums_organization_id")
    @ApiModelProperty(value = "关联门店id")
    private Long umsOrganizationId;

    /**
     * 门店名称
     */
    @TableField(value = "ums_organization_name")
    @ApiModelProperty(value = "门店名称")
    private String umsOrganizationName;

    /**
     * 门店地址
     */
    @TableField(value = "ums_organization_address")
    @ApiModelProperty(value = "门店地址")
    private String umsOrganizationAddress;

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

}