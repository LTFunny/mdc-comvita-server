package com.aquilaflycloud.mdc.param.folksonomy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * FolksonomyCatalogDeleteParam
 *
 * @author star
 * @date 2021/1/12
 */
@Data
@Accessors(chain = true)
public class FolksonomyCatalogDeleteParam {
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否强制删除")
    private Boolean enforceDelete;
}


