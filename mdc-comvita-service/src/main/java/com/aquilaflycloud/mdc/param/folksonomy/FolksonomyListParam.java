package com.aquilaflycloud.mdc.param.folksonomy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * FolksonomyListParam
 *
 * @author star
 * @date 2019-11-28
 */
@Data
@Accessors(chain = true)
public class FolksonomyListParam {
    @ApiModelProperty(value = "目录id")
    private Long catalogId;

    @ApiModelProperty(value = "名称")
    private String name;
}


