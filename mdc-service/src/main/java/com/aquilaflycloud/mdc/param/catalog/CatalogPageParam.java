package com.aquilaflycloud.mdc.param.catalog;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CatalogPageParam
 *
 * @author star
 * @date 2020-03-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CatalogPageParam extends PageParam<CatalogInfo> {
    @ApiModelProperty(value = "分类名称")
    private String catalogName;

    @ApiModelProperty(value = "状态(common.StateEnum)")
    private StateEnum state;
}
