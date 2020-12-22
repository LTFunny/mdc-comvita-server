package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.system.SystemSqlInjector;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * SqlPageParam
 *
 * @author star
 * @date 2020/8/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SqlPageParam extends PageParam<SystemSqlInjector> {
    @ApiModelProperty(value = "名称")
    private String name;
}


