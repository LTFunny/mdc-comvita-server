package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * MemberRewardResult
 *
 * @author star
 * @date 2020-01-03
 */
@Data
@Accessors(chain = true)
public class MemberOtherResult implements Serializable {
    @ApiModelProperty(value = "等级名称")
    private String gradeName;

    @ApiModelProperty(value = "等级占比")
    private BigDecimal gradeRate;

    @ApiModelProperty(value = "奖励值")
    private Integer total;

    @ApiModelProperty(value = "排名")
    private Long rank;

    @ApiModelProperty(value = "优惠券数")
    private Integer couponCount;

    @ApiModelProperty(value = "停车券数")
    private Integer parkingCouponCount;
}
