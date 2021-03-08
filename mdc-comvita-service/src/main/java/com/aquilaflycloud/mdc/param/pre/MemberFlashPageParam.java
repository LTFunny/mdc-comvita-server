package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.FlashOrderInfoStateEnum;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * MemberFlashPageParam
 *
 * @author star
 * @date 2021/3/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberFlashPageParam extends PageParam<PreActivityInfo> {
    @ApiModelProperty(value = "状态(pre.FlashOrderInfoStateEnum)")
    private FlashOrderInfoStateEnum flashOrderState;
}
