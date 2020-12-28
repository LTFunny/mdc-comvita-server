package com.aquilaflycloud.mdc.result.exchange;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * GoodsValueInfoResult
 *
 * @author zengqingjie
 * @date 2020-07-04
 */
@Data
public class GoodsValueInfoResult implements Serializable {
    @ApiModelProperty(value = "规格值id")
    private Long specValueId;

    @ApiModelProperty(value = "规格值名称")
    private String name;

    public GoodsValueInfoResult() {
    }

    public GoodsValueInfoResult(Long specValueId, String name) {
        this.specValueId = specValueId;
        this.name = name;
    }
}
