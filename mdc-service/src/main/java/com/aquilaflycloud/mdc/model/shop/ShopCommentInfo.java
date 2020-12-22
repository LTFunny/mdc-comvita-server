package com.aquilaflycloud.mdc.model.shop;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentAnonymousEnum;
import com.aquilaflycloud.mdc.enums.shop.ShopCommentStateEnum;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "shop_comment_info")
public class ShopCommentInfo implements Serializable {

    private static final long serialVersionUID = 4077092116119783997L;
    @ApiModelProperty(value = "主键id")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "评论内容")
    @TableField("content")
    private String content;

    @ApiModelProperty(value = "总体评分")
    @TableField("all_score")
    private Integer allScore;

    @ApiModelProperty(value = "服务评分")
    @TableField("service_score")
    private Integer serviceScore;

    @ApiModelProperty(value = "环境评分")
    @TableField("environment_score")
    private Integer environmentScore;

    @ApiModelProperty(value = "口味评分")
    @TableField("taste_score")
    private Integer tasteScore;

    @ApiModelProperty(value = "人均消费")
    @TableField("average_consumption")
    private BigDecimal averageConsumption;

    @ApiModelProperty(value = "评论图片json")
    @TableField("pic_url")
    private String picUrl;

    @ApiModelProperty(value = "昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "会员id")
    @TableField("member_id")
    private Long memberId;

    @ApiModelProperty(value = "头像url")
    @TableField("avatar_url")
    private String avatarUrl;

    @ApiModelProperty(value = "是否匿名(0:不匿名;1:匿名)")
    @TableField("is_anonymous")
    private ShopCommentAnonymousEnum isAnonymous;

    @ApiModelProperty(value = "状态(shop.ShopCommentStateEnum)")
    @TableField("state")
    private ShopCommentStateEnum state;

    @ApiModelProperty(value = "审核备注")
    @TableField("remark")
    private String remark;

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
}
