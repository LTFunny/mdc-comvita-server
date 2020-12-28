package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberGradeEditParam {
    @ApiModelProperty(value = "会员等级id", required = true)
    @NotNull(message = "等级id不能为空")
    private Long id;

    @ApiModelProperty(value = "等级称号")
    private String gradeTitle;

    @ApiModelProperty(value = "奖励范围最小值")
    private Integer minValue;

    @ApiModelProperty(value = "奖励范围最大值")
    private Integer maxValue;

    @ApiModelProperty(value = "等级排序")
    private Integer gradeOrder;
}