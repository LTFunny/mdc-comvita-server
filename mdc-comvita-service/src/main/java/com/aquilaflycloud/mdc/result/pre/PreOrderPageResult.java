package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zly
 */
@Data
public class PreOrderPageResult {

    @ApiModelProperty(value = "预约数量")
    private int reservationNum;

    @ApiModelProperty(value = "礼包数量")
    private int ingGiftNum;

}
