package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.wechat.LiveScreenTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.LiveStatusEnum;
import com.aquilaflycloud.mdc.enums.wechat.LiveTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "wechat_mini_live_info")
public class WechatMiniLiveInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信小程序appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信小程序appId")
    private String appId;

    /**
     * 直播房间id
     */
    @TableField(value = "room_id")
    @ApiModelProperty(value = "直播房间id")
    private Integer roomId;

    /**
     * 直播名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value = "直播名称")
    private String name;

    /**
     * 直播背景
     */
    @TableField(value = "cover_img")
    @ApiModelProperty(value = "直播背景")
    private String coverImg;

    /**
     * 计划开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value = "计划开始时间")
    private Date startTime;

    /**
     * 计划结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value = "计划结束时间")
    private Date endTime;

    /**
     * 主播名称
     */
    @TableField(value = "anchor_name")
    @ApiModelProperty(value = "主播名称")
    private String anchorName;

    /**
     * 主播微信
     */
    @TableField(value = "anchor_wechat")
    @ApiModelProperty(value = "主播微信")
    private String anchorWechat;

    /**
     * 主播头像
     */
    @TableField(value = "anchor_img")
    @ApiModelProperty(value = "主播头像")
    private String anchorImg;

    /**
     * 商品信息
     */
    @TableField(value = "goods_content")
    @ApiModelProperty(value = "商品信息")
    private String goodsContent;

    /**
     * 直播状态
     */
    @TableField(value = "live_status")
    @ApiModelProperty(value = "直播状态")
    private LiveStatusEnum liveStatus;

    /**
     * 分享图片
     */
    @TableField(value = "share_img")
    @ApiModelProperty(value = "分享图片")
    private String shareImg;

    /**
     * 直播间类型
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "直播间类型")
    private LiveTypeEnum type;

    /**
     * 屏幕类型
     */
    @TableField(value = "screen_type")
    @ApiModelProperty(value = "屏幕类型")
    private LiveScreenTypeEnum screenType;

    /**
     * 是否关闭点赞
     */
    @TableField(value = "close_like")
    @ApiModelProperty(value = "是否关闭点赞")
    private WhetherEnum closeLike;

    /**
     * 是否关闭商品货架
     */
    @TableField(value = "close_goods")
    @ApiModelProperty(value = "是否关闭商品货架")
    private WhetherEnum closeGoods;

    /**
     * 是否关闭评论
     */
    @TableField(value = "close_comment")
    @ApiModelProperty(value = "是否关闭评论")
    private WhetherEnum closeComment;

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