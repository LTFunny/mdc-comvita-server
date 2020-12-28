package com.aquilaflycloud.mdc.param.ad;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.ad.EffectiveModeEnum;
import com.aquilaflycloud.mdc.enums.ad.EffectivePeriodEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * AdEditParam
 *
 * @author star
 * @date 2019-11-18
 */
@Data
@Accessors(chain = true)
public class AdEditParam {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "广告id不能为空")
    private Long id;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "广告名称")
    private String adName;

    @ApiModelProperty(value = "广告详情")
    private String adDesc;

    @ApiModelProperty(value = "有效时段起始时间")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效时段结束时间")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "上线时间")
    private Date onlineTime;

    @ApiModelProperty(value = "下线时间")
    private Date offlineTime;

    @ApiModelProperty(value = "有效时段类型(ad.EffectivePeriodEnum)")
    private EffectivePeriodEnum effectivePeriod;

    @ApiModelProperty(value = "广告生效方式(ad.EffectiveModeEnum)")
    private EffectiveModeEnum effectiveMode;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "优先级")
    @Min(value = 1, message = "优先级不能小于1")
    private Integer priority;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}