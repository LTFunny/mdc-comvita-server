package com.aquilaflycloud.mdc.param.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * OrderChargeBatchGetParam
 *
 * @author star
 * @date 2020-03-24
 */
@Data
@Accessors(chain = true)
public class OrderChargeBatchGetParam {
    @ApiModelProperty(value = "是否刷新最新计费数据(默认false)")
    private Boolean refresh = false;
}
