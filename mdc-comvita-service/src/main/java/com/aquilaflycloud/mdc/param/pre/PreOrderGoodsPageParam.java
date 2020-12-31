package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 11:40
 * @Version 1.0
 */
@Data
public class PreOrderGoodsPageParam extends PageParam {

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "状态(pre.PickingCardStateEnum)(1-未销售、2-已售卖、3-已预约、4-已核销、5-已作废)")
    private PickingCardStateEnum pickingState;

}
