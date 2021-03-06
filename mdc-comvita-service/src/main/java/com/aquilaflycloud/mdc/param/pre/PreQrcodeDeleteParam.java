package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreQrcodeDeleteParam
 * @author linkq
 */
@Data
public class PreQrcodeDeleteParam {
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "二维码id", required = true)
    @NotNull(message = "二维码id不能为空")
    private Long qrId;

}
