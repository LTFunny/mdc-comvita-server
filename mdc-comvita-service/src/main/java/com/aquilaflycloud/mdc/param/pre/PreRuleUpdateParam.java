package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreRuleUpdateParam
 * @author linkq
 */
@Data
public class PreRuleUpdateParam extends PreRuleAddParam{

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

}
