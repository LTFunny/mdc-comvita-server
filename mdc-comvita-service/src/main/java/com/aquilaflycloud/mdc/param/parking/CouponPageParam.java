package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.parking.CouponStateEnum;
import com.aquilaflycloud.mdc.enums.parking.CouponTypeEnum;
import com.aquilaflycloud.mdc.model.parking.ParkingCoupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * CouponPageParam
 *
 * @author star
 * @date 2020-01-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CouponPageParam extends PageAuthParam<ParkingCoupon> {
    @ApiModelProperty(value = "停车券编码")
    private String couponCode;

    @ApiModelProperty(value = "停车券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠类型(parking.CouponTypeEnum)")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "状态(parking.CouponStateEnum)")
    private CouponStateEnum state;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "停车券所属部门名称")
    private String designateOrgNames;
}
