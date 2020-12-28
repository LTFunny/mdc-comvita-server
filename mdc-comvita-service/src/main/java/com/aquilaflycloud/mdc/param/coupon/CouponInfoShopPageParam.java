package com.aquilaflycloud.mdc.param.coupon;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * CouponInfoShopPageParam
 *
 * @author zengqingjie
 * @date 2020-04-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponInfoShopPageParam extends PageParam<CouponInfo> implements Serializable {
    @ApiModelProperty(value = "relationId", required = true)
    @NotNull(message = "relationId不能为空")
    private Long relationId;

}
