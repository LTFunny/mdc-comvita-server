package com.aquilaflycloud.mdc.param.ad;

import com.alibaba.fastjson.annotation.JSONField;
import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.ad.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * AdAddParam
 *
 * @author star
 * @date 2019-11-18
 */
@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "effectiveMode", fieldValue = "CUSTOM", notNullFieldName = "onlineTime", message = "上线时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveMode", fieldValue = "CUSTOM", notNullFieldName = "offlineTime", message = "下线时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectiveMode", fieldValue = "CUSTOM", notNullFieldName = "effectivePeriod", message = "有效时段类型不能为空"),
        @AnotherFieldHasValue(fieldName = "effectivePeriod", fieldValue = "CUSTOM", notNullFieldName = "effectiveStartTime", message = "有效时段起始时间不能为空"),
        @AnotherFieldHasValue(fieldName = "effectivePeriod", fieldValue = "CUSTOM", notNullFieldName = "effectiveEndTime", message = "有效时段结束时间不能为空"),
})
@Data
@Accessors(chain = true)
public class AdAddParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId = MdcConstant.UNIVERSAL_APP_ID;

    @ApiModelProperty(value = "广告名称", required = true)
    @NotBlank(message = "广告名称不能为空")
    private String adName;

    @ApiModelProperty(value = "广告类型(ad.AdTypeEnum)", required = true)
    @NotNull(message = "广告类型不能为空")
    private AdTypeEnum adType;

    @ApiModelProperty(value = "广告详情", required = true)
    @NotBlank(message = "广告详情不能为空")
    private String adDesc;

    @ApiModelProperty(value = "投放位置(ad.AdPlacementEnum)", required = true)
    @NotNull(message = "投放位置不能为空")
    private AdPlacementEnum adPlacement;

    @ApiModelProperty(value = "有效时段起始时间")
    @JSONField(format = "HH:mm:ss")
    private Date effectiveStartTime;

    @ApiModelProperty(value = "有效时段结束时间")
    @JSONField(format = "HH:mm:ss")
    private Date effectiveEndTime;

    @ApiModelProperty(value = "上线时间")
    private Date onlineTime;

    @ApiModelProperty(value = "下线时间")
    private Date offlineTime;

    @ApiModelProperty(value = "有效时段类型(ad.EffectivePeriodEnum)")
    private EffectivePeriodEnum effectivePeriod;

    @ApiModelProperty(value = "广告生效方式(ad.EffectiveModeEnum)", required = true)
    @NotNull(message = "广告生效方式不能为空")
    private EffectiveModeEnum effectiveMode;

    @ApiModelProperty(value = "图片地址", required = true)
    @NotBlank(message = "图片地址不能为空")
    private String imageUrl;

    @ApiModelProperty(value = "优先级", required = true)
    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级不能小于1")
    private Integer priority;

    @ApiModelProperty(value = "标签ids")
    private List<Long> folksonomyIds;
}


