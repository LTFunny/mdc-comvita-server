package com.aquilaflycloud.mdc.param.parking;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.parking.ConsumeStateEnum;
import com.aquilaflycloud.mdc.enums.parking.CouponTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * StatisticsGetParam
 *
 * @author star
 * @date 2020-05-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StatisticsGetParam extends AuthParam {
    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "停车券编码")
    private String couponCode;

    @ApiModelProperty(value = "停车券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠类型(parking.CouponTypeEnum)")
    private CouponTypeEnum couponType;

    @ApiModelProperty(value = "创建来源(common.CreateSourceEnum)")
    private CreateSourceEnum createSource;

    @ApiModelProperty(value = "领取开始时间")
    private Date receiveTimeStart;

    @ApiModelProperty(value = "领取结束时间")
    private Date receiveTimeEnd;

    @ApiModelProperty(value = "核销状态(parking.ConsumeStateEnum)")
    private ConsumeStateEnum consumeState;

    @ApiModelProperty(value = "核销开始时间")
    private Date consumeTimeStart;

    @ApiModelProperty(value = "核销结束时间")
    private Date consumeTimeEnd;

    @ApiModelProperty(value = "创建记录人名称")
    private String creatorName;

    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;
}
