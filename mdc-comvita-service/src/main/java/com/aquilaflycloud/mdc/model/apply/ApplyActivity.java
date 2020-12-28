package com.aquilaflycloud.mdc.model.apply;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.apply.ApplyStateEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "apply_activity")
public class ApplyActivity implements Serializable {
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
     * 报名活动名称
     */
    @TableField(value = "apply_name")
    @ApiModelProperty(value = "报名活动名称")
    private String applyName;

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
     * 活动省份
     */
    @TableField(value = "province")
    @ApiModelProperty(value = "活动省份")
    private String province;

    /**
     * 活动城市
     */
    @TableField(value = "city")
    @ApiModelProperty(value = "活动城市")
    private String city;

    /**
     * 活动区域
     */
    @TableField(value = "county")
    @ApiModelProperty(value = "活动区域")
    private String county;

    /**
     * 省-市-区名称
     */
    @TableField(value = "address")
    @ApiModelProperty(value = "省-市-区名称")
    private String address;

    /**
     * 活动地址名称
     */
    @TableField(value = "place_name")
    @ApiModelProperty(value = "活动地址名称")
    private String placeName;

    /**
     * 地址经度
     */
    @TableField(value = "longitude")
    @ApiModelProperty(value = "地址经度")
    private String longitude;

    /**
     * 地址维度
     */
    @TableField(value = "latitude")
    @ApiModelProperty(value = "地址维度")
    private String latitude;

    /**
     * 活动图片地址
     */
    @TableField(value = "image_url")
    @ApiModelProperty(value = "活动图片地址")
    private String imageUrl;

    /**
     * 报名人数上限,0表示无上限
     */
    @TableField(value = "apply_limit")
    @ApiModelProperty(value = "报名人数上限(0表示无上限)")
    private Integer applyLimit;

    /**
     * 是否置顶
     */
    @TableField(value = "is_top")
    @ApiModelProperty(value = "是否置顶")
    private WhetherEnum isTop;

    /**
     * 设置置顶时间
     */
    @TableField(value = "set_top_time")
    @ApiModelProperty(value = "设置置顶时间")
    private Date setTopTime;

    /**
     * 报名规则内容(json串)
     */
    @TableField(value = "apply_rule_content")
    @ApiModelProperty(value = "报名规则内容(json串)")
    private String applyRuleContent;

    /**
     * 活动介绍
     */
    @TableField(value = "introduction")
    @ApiModelProperty(value = "活动介绍")
    private String introduction;

    /**
     * 状态
     */
    @TableField(value = "state")
    @ApiModelProperty(value = "状态")
    private ApplyStateEnum state;

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