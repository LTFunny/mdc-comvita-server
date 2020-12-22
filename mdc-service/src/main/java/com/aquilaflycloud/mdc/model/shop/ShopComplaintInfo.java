package com.aquilaflycloud.mdc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.shop.ShopComplaintAnonymousEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopComplaintStateEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName(value = "shop_complaint_info")
public class ShopComplaintInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "投诉内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "投诉图片")
    @TableField("pic_url")
    private String picUrl;

    @ApiModelProperty(value = "联系方式")
    @TableField("contact_number")
    private String contactNumber;

    @ApiModelProperty(value = "是否匿名(0:不匿名;1:匿名)")
    @TableField("is_anonymous")
    private ShopComplaintAnonymousEnum isAnonymous;

    @ApiModelProperty(value = "状态(1:已受理;2:已通过;3:未通过;4:申诉成功)")
    @TableField("state")
    private ShopComplaintStateEnum state;

    @ApiModelProperty(value = "通过备注")
    @TableField("pass_remark")
    private String passRemark;

    @ApiModelProperty(value = "通过图片")
    @TableField("pass_pic_url")
    private String passPicUrl;

    @ApiModelProperty(value = "申诉备注")
    @TableField("appeal_remark")
    private String appealRemark;

    @ApiModelProperty(value = "申诉图片")
    @TableField("appeal_pic_url")
    private String appealPicUrl;

    @ApiModelProperty(value = "申诉时间")
    @TableField("appeal_time")
    private Date appealTime;

    @TableField(value = "appeal_id")
    @ApiModelProperty(value = "创建记录人id", hidden = true)
    private Long appealId;

    @TableField(value = "appeal_name")
    @ApiModelProperty(value = "申诉人名称")
    private String appealName;

    @ApiModelProperty(value = "昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "会员id")
    @TableField("member_id")
    private Long memberId;

    @ApiModelProperty(value = "头像url")
    @TableField("avatar_url")
    private String avatarUrl;

    @ApiModelProperty(value = "商户id")
    @TableField("shop_id")
    private Long shopId;

    @ApiModelProperty(value = "商户名")
    @TableField("shop_full_name")
    private String shopFullName;

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
    @TableField(value = "last_update_time")
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


}
