package com.aquilaflycloud.mdc.result.coupon;

import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * CouponInfoResult
 *
 * @author star
 * @date 2020-03-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponInfoResult extends CouponInfo implements Serializable {
    @ApiModelProperty(value = "当前会员领取数量")
    private Integer memberReceiveCount;

    @ApiModelProperty(value = "当前会员领取未核销数量")
    private Integer memberUnVerificateCount;

    @ApiModelProperty(value = "当前会员领取已核销数量")
    private Integer memberVerificateCount;

    @ApiModelProperty(value = "当前会员领取未核销记录id列表")
    private List<String> memberUnVerificateRelIdList;

    @ApiModelProperty(value = "当前会员领取已核销记录id列表")
    private List<String> memberVerificateRelIdList;
}
