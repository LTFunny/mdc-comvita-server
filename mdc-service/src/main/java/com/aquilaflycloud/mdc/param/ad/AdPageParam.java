package com.aquilaflycloud.mdc.param.ad;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ad.AdPlacementEnum;
import com.aquilaflycloud.mdc.enums.ad.AdStateEnum;
import com.aquilaflycloud.mdc.enums.ad.AdTypeEnum;
import com.aquilaflycloud.mdc.enums.ad.EffectiveModeEnum;
import com.aquilaflycloud.mdc.model.ad.AdInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * AdListParam
 *
 * @author star
 * @date 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdPageParam extends PageAuthParam<AdInfo> {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "广告名称")
    private String adName;

    @ApiModelProperty(value = "广告类型(ad.AdTypeEnum)")
    private AdTypeEnum adType;

    @ApiModelProperty(value = "投放位置(ad.AdPlacementEnum)")
    private AdPlacementEnum adPlacement;

    @ApiModelProperty(value = "上线时间")
    private Date onlineTime;

    @ApiModelProperty(value = "下线时间")
    private Date offlineTime;

    @ApiModelProperty(value = "广告生效方式(ad.EffectiveModeEnum)")
    private EffectiveModeEnum effectiveMode;

    @ApiModelProperty(value = "状态(ad.AdStateEnum)")
    private AdStateEnum state;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;
}


