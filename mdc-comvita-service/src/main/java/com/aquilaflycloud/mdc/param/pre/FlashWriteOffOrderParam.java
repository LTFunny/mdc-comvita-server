package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zly
 */
@Data
public class FlashWriteOffOrderParam {

    @ApiModelProperty(value = "核销码")
    private String flashCode;



}
