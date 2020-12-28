package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.parking.CouponTypeEnum;
import com.aquilaflycloud.mdc.model.parking.ParkingCouponOrderRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * CouponRecordPageParam
 *
 * @author star
 * @date 2020-01-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CouponRecordPageParam extends PageAuthParam<ParkingCouponOrderRecord> {
    @ApiModelProperty(value = "停车券编码")
    private String couponCode;

    @ApiModelProperty(value = "停车券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠类型(parking.CouponTypeEnum)")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "创建来源(common.CreateSourceEnum)")
    private CreateSourceEnum createSource;

    @ApiModelProperty(value = "停车券所属部门名称")
    private String designateOrgNames;
}
