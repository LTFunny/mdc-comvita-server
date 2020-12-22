package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.coupon.VerificateStateEnum;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * CouponMemberPageParam
 *
 * @author star
 * @date 2020-03-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponInfoRelPageParam extends PageParam<CouponMemberRel> implements Serializable {
    @ApiModelProperty(value = "状态(coupon.VerificateStateEnum)")
    private VerificateStateEnum verificateState;
}
