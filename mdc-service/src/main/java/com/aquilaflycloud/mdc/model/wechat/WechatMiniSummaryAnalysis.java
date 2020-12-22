package com.aquilaflycloud.mdc.model.wechat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信小程序访问数据概况
 */
@Data
@TableName(value = "wechat_mini_summary_analysis")
public class WechatMiniSummaryAnalysis implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 微信公众号appId
     */
    @TableField(value = "app_id")
    @ApiModelProperty(value = "微信公众号appId")
    private String appId;

    /**
     * 数据的日期
     */
    @TableField(value = "ref_date")
    @ApiModelProperty(value = "数据的日期")
    private Date refDate;

    /**
     * 累计用户数
     */
    @TableField(value = "visit_total")
    @ApiModelProperty(value = "累计用户数")
    private Long visitTotal;

    /**
     * 转发次数
     */
    @TableField(value = "share_pv")
    @ApiModelProperty(value = "转发次数")
    private Long sharePv;

    /**
     * 转发人数
     */
    @TableField(value = "share_uv")
    @ApiModelProperty(value = "转发人数")
    private Long shareUv;

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