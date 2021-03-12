package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * PreQrcodeDownloadParam
 * @author linkq
 */
@Data
public class PreQrcodeDownloadParam {
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long activityId;

    @ApiModelProperty(value = "活动名称", required = true)
    @NotNull(message = "活动名称不能为空")
    private String activityName;

    @ApiModelProperty(value = "二维码Id列表", required = true)
    @NotEmpty(message = "二维码Id列表不能为空")
    private List<Long> qrIdList;

}
