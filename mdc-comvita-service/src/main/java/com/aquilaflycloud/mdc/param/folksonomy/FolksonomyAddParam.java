package com.aquilaflycloud.mdc.param.folksonomy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * FolksonomyAddParam
 *
 * @author star
 * @date 2019-11-28
 */
@Data
@Accessors(chain = true)
public class FolksonomyAddParam {
    @ApiModelProperty(value = "目录id", required = true)
    @NotBlank(message = "目录id不能为空")
    private String catalogId;

    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;
}


