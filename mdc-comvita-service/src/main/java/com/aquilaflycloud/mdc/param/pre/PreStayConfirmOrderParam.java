package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pengyongliang
 * @Date 2020/12/29 16:40
 * @Version 1.0
 */
@Data
public class PreStayConfirmOrderParam {

    @ApiModelProperty(value = "导购员id")
    private Long guideId;


}
