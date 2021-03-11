package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PreActivityQrCodeResult
 * 快闪活动二维码返回结果集
 * @author linkq
 */
@Data
public class PreActivityQrCodeResult {

    /**
     * 二维码id
     */
    @ApiModelProperty(value = "二维码Id")
    private Long qrId;

    /**
     * 活动
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    /**
     * 关联门店id
     */
    @ApiModelProperty(value = "关联门店id")
    private Long orgId;

    /**
     * 关联门店名称
     */
    @ApiModelProperty(value = "关联门店名称")
    private String orgName;

}
