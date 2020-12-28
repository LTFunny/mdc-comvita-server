package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

/**
 * RelTimePageParam
 *
 * @author star
 * @date 2020-01-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RelTimePageParam extends PageAuthParam<Map<String, Object>> {
    @ApiModelProperty(value = "领取开始时间")
    private Date receiveTimeStart;

    @ApiModelProperty(value = "领取结束时间")
    private Date receiveTimeEnd;

    @ApiModelProperty(value = "核销开始时间")
    private Date consumeTimeStart;

    @ApiModelProperty(value = "核销结束时间")
    private Date consumeTimeEnd;
}
