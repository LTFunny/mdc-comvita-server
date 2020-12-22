package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DictNameParam
 *
 * @author star
 * @date 2020-04-19
 */
@Data
@Accessors(chain = true)
public class DictNameParam {
    @ApiModelProperty(value = "字典名称列表", required = true)
    @NotEmpty(message = "字典名称列表不能为空")
    private List<String> nameList;
}
