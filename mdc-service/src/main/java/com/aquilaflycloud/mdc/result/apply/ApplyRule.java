package com.aquilaflycloud.mdc.result.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyRule {
    @ApiModelProperty(value = "是否需要审核(默认true)")
    private Boolean needAudit = true;

    @ApiModelProperty(value = "能否重复(默认false)")
    private Boolean canRepeat = false;

    @ApiModelProperty(value = "是否需要姓名(默认true)")
    private Boolean needName = true;

    @ApiModelProperty(value = "是否需要手机(默认true)")
    private Boolean needPhone = true;

    @ApiModelProperty(value = "手机能否重复(默认false)")
    private Boolean canRepeatPhone = false;

    @ApiModelProperty(value = "是否需要照片(默认false)")
    private Boolean needPhoto = false;

    @ApiModelProperty(value = "需要照片张数(默认1)")
    private Integer needPhotoCount = 1;

    @ApiModelProperty(value = "活动开始后是否能报名(默认false)")
    private Boolean canApplyAfterStart = false;
}