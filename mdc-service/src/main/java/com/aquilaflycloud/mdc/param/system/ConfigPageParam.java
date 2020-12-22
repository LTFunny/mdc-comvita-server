package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.model.system.SystemTenantConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ConfigPageParam
 *
 * @author star
 * @date 2020-04-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigPageParam extends PageParam<SystemTenantConfig> {
    @ApiModelProperty(value="配置类型(system.TenantConfigTypeEnum)")
    private TenantConfigTypeEnum configType;
}
