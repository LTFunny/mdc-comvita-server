package com.aquilaflycloud.mdc.result.catalog;

import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CatalogResult
 *
 * @author star
 * @date 2020-03-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CatalogResult extends CatalogInfo {
    @ApiModelProperty(value = "业务对象数量")
    private Integer businessCount;
}
