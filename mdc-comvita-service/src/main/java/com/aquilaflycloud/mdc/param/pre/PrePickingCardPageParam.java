package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PrePickingCardPageParam
 *
 * @author zengqingjie
 * @date 2020-12-28
 */
@Data
public class PrePickingCardPageParam extends PageParam<PrePickingCard> {
    @ApiModelProperty(value = "配送卡号")
    private String pickingCode;

    @ApiModelProperty(value = "状态(pre.PickingCardStateEnum)(1-未销售、2-已售卖、3-已预约、4-已核销、5-已作废)")
    private PickingCardStateEnum pickingState;
}
