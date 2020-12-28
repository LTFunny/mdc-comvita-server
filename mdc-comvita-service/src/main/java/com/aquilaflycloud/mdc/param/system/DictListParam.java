package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * DictListParam
 *
 * @author star
 * @date 2019-10-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DictListParam extends AuthParam {
    @ApiModelProperty(value = "父字典值")
    private Integer parent;

    @ApiModelProperty(value = "字典名称", required = true)
    @NotBlank(message = "字典名称不能为空")
    private String name;
}
