package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * PrePickingCardPageParam
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
@Data
public class PrePickingCardBatchAddParam {
    @ApiModelProperty(value = "新增张数", required = true)
    @NotNull(message = "新增张数不能为空")
    @Min(value = 1, message = "新增张数不能小于1")
    @Max(value = 500, message = "新增张数不能大于500")
    private Integer count;
}
