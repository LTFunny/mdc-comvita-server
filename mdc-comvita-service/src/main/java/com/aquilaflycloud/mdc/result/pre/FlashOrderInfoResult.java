package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * FlashOrderInfoResult
 *
 * @author zly
 */
@Data
public class FlashOrderInfoResult {
    @ApiModelProperty(value = "转发量")
    private Long forwardingVolume= 0L;

    @ApiModelProperty(value = "点击量（含扫码进入）")
    private Long clickPv = 0L;

    @ApiModelProperty(value = "点击人数（含扫码进入）")
    private Long clickUv = 0L;

    @ApiModelProperty(value = "参加人数")
    private Long joinPv = 0L;

    @ApiModelProperty(value = "参加转化率")
    private Long joinRatio = 0L;


}
