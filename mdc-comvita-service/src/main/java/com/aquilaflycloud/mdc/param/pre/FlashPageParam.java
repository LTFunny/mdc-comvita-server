package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.FlashOrderInfoStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * FlashPageParam
 *
 * @author zly
 */
@Data
public class FlashPageParam  extends PageParam {
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long id;

    @ApiModelProperty(value = "参加渠道")
    private String shopId;


    @ApiModelProperty(value = "领取时间")
    private Date createStartTime;

    @ApiModelProperty(value = "领取时间")
    private Date createEndTime;

    @ApiModelProperty(value = "状态 FlashOrderInfoStateEnum")
    private FlashOrderInfoStateEnum flashOrderState;
}
