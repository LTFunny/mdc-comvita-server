package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * CarNoPageParam
 *
 * @author star
 * @date 2020-02-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CarNoPageParam extends PageParam {
    @ApiModelProperty(value = "车牌号", required = true)
    @NotNull(message = "车牌号不能为空")
    private String carNo;
}
