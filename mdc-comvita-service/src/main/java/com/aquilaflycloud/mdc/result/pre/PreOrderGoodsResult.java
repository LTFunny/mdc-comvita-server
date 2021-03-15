package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PreOrderInfoResult
 * @author zly
 */
@Accessors(chain = true)
@Data
public class PreOrderGoodsResult extends PreOrderGoods {

    @ApiModelProperty(value = "活动名称")
    private String activityName;
}
