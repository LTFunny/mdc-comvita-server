package com.aquilaflycloud.mdc.result.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * MemberEventSexAndAgeResult
 *
 * @author star
 * @date 2019-11-27
 */
@Data
public class MemberEventSexAndAgeResult implements Serializable {
    @ApiModelProperty(value = "未知性别数量")
    private Long sexUnknownSum;

    @ApiModelProperty(value = "男性性别数量")
    private Long sexMaleSum;

    @ApiModelProperty(value = "女性性别数量")
    private Long sexFemaleSum;

    @ApiModelProperty(value = "未知年龄数量")
    private Long ageUnknownSum;

    @ApiModelProperty(value = "17岁以下年龄数量")
    private Long ageBelow17Sum;

    @ApiModelProperty(value = "18-24岁年龄数量")
    private Long age18To24Sum;

    @ApiModelProperty(value = "25-29岁年龄数量")
    private Long age25To29Sum;

    @ApiModelProperty(value = "30-39岁年龄数量")
    private Long age30To39Sum;

    @ApiModelProperty(value = "40-49岁年龄数量")
    private Long age40To49Sum;

    @ApiModelProperty(value = "50岁以上年龄数量")
    private Long ageAbove50Sum;
}
