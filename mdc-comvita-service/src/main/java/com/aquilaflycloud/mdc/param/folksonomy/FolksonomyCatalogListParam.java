package com.aquilaflycloud.mdc.param.folksonomy;

import com.aquilaflycloud.mdc.enums.folksonomy.FolksonomyTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * FolksonomyCatalogListParam
 *
 * @author star
 * @date 2021/1/12
 */
@Data
@Accessors(chain = true)
public class FolksonomyCatalogListParam {
    @ApiModelProperty(value = "标签类型(folksonomy.FolksonomyTypeEnum)")
    private FolksonomyTypeEnum type;
}


