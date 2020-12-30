package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author pengyongliang
 * @Date 2020/12/30 11:10
 * @Version 1.0
 */
@Data
public class PreOrderInfoGetResult extends PreOrderInfo {

    @ApiModelProperty(value = "商品信息")
    private PreGoodsInfo preGoodsInfo;


}
