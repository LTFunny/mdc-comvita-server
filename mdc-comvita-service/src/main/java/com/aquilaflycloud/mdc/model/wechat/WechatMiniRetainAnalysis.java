package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.wechat.AnalysisTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 微信小程序留存分析表
 */
@Data
@TableName(value = "wechat_mini_retain_analysis")
public class WechatMiniRetainAnalysis implements Serializable {
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
     * 数据的日期
     */
    @TableField(value = "ref_date")
    @ApiModelProperty(value = "数据的日期")
    private String refDate;

    /**
     * 分析类型
     */
    @TableField(value = "analysis_type")
    @ApiModelProperty(value = "分析类型")
    private AnalysisTypeEnum analysisType;

    /**
     * 新增用户留存(当天/当周/当月)
     */
    @TableField(value = "visit_uv_new")
    @ApiModelProperty(value = "新增用户留存(当天/当周/当月)")
    private Integer visitUvNew;

    /**
     * 活跃用户留存(当天/当周/当月)
     */
    @TableField(value = "visit_uv")
    @ApiModelProperty(value = "活跃用户留存(当天/当周/当月)")
    private Integer visitUv;

    /**
     * 新增用户留存(多天/多周/多月)
     */
    @TableField(value = "visit_uv_new_content")
    @ApiModelProperty(value = "新增用户留存(多天/多周/多月)")
    private String visitUvNewContent;

    /**
     * 活跃用户留存(多天/多周/多月)
     */
    @TableField(value = "visit_uv_content")
    @ApiModelProperty(value = "活跃用户留存(多天/多周/多月)")
    private String visitUvContent;

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

    private static final long serialVersionUID = 1L;
}