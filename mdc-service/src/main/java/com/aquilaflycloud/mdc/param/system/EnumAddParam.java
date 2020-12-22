package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * EnumAddParam
 *
 * @author star
 * @date 2019-10-21
 */
@Data
@Accessors(chain = true)
public class EnumAddParam {
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", required = true)
    @NotNull(message = "type不能为空")
    private Integer type;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    @NotBlank(message = "name不能为空")
    private String name;

    /**
     * 枚举名称(英文字母)
     */
    @ApiModelProperty(value = "枚举名称(英文字母)", required = true)
    @NotBlank(message = "enumName不能为空")
    private String enumName;

    /**
     * 枚举包名
     */
    @ApiModelProperty(value = "枚举包名", required = true)
    @NotBlank(message = "enumPackage不能为空")
    private String enumPackage;
}
