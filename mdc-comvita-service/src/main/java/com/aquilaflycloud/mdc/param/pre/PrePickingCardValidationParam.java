package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 14:29
 * @Version 1.0
 */
@Data
public class PrePickingCardValidationParam {

    @ApiModelProperty(value = "卡号")
    private String pickingCode;

}
