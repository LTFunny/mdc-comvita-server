package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.mdc.enums.wechat.AnalysisTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 微信小程序访问趋势分析表
 */
@Data
@TableName(value = "wechat_mini_visit_trend_analysis")
public class WechatMiniVisitTrendAnalysis implements Serializable {
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
     * 打开次数
     */
    @TableField(value = "session_cnt")
    @ApiModelProperty(value = "打开次数")
    private Long sessionCnt;

    /**
     * 访问次数
     */
    @TableField(value = "visit_pv")
    @ApiModelProperty(value = "访问次数")
    private Long visitPv;

    /**
     * 访问人数
     */
    @TableField(value = "visit_uv")
    @ApiModelProperty(value = "访问人数")
    private Long visitUv;

    /**
     * 新用户数
     */
    @TableField(value = "visit_uv_new")
    @ApiModelProperty(value = "新用户数")
    private Long visitUvNew;

    /**
     * 人均停留时长
     */
    @TableField(value = "stay_time_uv")
    @ApiModelProperty(value = "人均停留时长")
    private BigDecimal stayTimeUv;

    /**
     * 次均停留时长
     */
    @TableField(value = "stay_time_session")
    @ApiModelProperty(value = "次均停留时长")
    private BigDecimal stayTimeSession;

    /**
     * 平均访问深度
     */
    @TableField(value = "visit_depth")
    @ApiModelProperty(value = "平均访问深度")
    private BigDecimal visitDepth;

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