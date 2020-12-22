package com.aquilaflycloud.mdc.model.ticket;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoPageEnum;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoPageTypeEnum;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoStateEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 渠道信息
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@Data
@TableName(value = "ticket_channel_info")
public class TicketChannelInfo {
    @ApiModelProperty(value = "主键")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "渠道名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "渠道负责人")
    @TableField("responsible_person")
    private String responsiblePerson;

    @ApiModelProperty(value = "联系电话")
    @TableField("contact_number")
    private String contactNumber;

    @ApiModelProperty(value = "备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value = "微信appId")
    @TableField("app_id")
    private String appId;

    @ApiModelProperty(value = "渠道二维码页面类型")
    @TableField("show_page_type")
    private ChannelInfoPageTypeEnum showPageType;

    @ApiModelProperty(value = "扫码渠道二维码成功后展示的页面")
    @TableField("show_page")
    private ChannelInfoPageEnum showPage;

    @ApiModelProperty(value = "关联数据id")
    @TableField("ref_id")
    private Long refId;

    @ApiModelProperty(value = "小程序二维码url")
    @TableField("small_qr_code_url")
    private String smallQrCodeUrl;

    @ApiModelProperty(value = "状态")
    @TableField(value = "state")
    private ChannelInfoStateEnum state;

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
