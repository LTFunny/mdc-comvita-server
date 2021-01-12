package com.aquilaflycloud.mdc.param.folksonomy;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * FolksonomyPageParam
 *
 * @author star
 * @date 2019-12-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class FolksonomyPageParam extends PageParam<FolksonomyInfo> {
    @ApiModelProperty(value = "名称")
    private String name;

}


