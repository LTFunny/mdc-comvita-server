package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * CouponInfoPageParam
 *
 * @author star
 * @date 2020-03-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponInfoPageParam extends PageParam<CouponInfo> implements Serializable {
    @ApiModelProperty(value = "分类id")
    private Long catalogId;

}
