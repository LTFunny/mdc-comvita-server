package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PreOrderGoodsResult
 *
 * @author star
 * @date 2021/3/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PreOrderGoodsResult extends PreOrderGoods {
    @ApiModelProperty(value = "收件详细地址")
    private String deliveryDetailAddress;

    @ApiModelProperty(value = "活动名称")
    private String activityName;
}
