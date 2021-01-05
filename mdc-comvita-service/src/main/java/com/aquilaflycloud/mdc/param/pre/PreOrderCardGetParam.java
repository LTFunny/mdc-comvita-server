package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author pengyongliang
 * @Date 2021/1/5 11:39
 * @Version 1.0
 */
@Data
public class PreOrderCardGetParam {

    @ApiModelProperty(value = "提货卡号" , required = true)
    @NotNull(message = "提货卡号不能为空")
    private String pickingCode;

}
