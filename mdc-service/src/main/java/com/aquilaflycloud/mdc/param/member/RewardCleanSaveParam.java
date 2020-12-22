package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import com.aquilaflycloud.mdc.enums.common.DateUnitTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@AnotherFieldHasValue.List({
        @AnotherFieldHasValue(fieldName = "dateUnitType", notNullFieldName = "cleanDelay", message = "延时清零时间不能为空"),
        @AnotherFieldHasValue(fieldName = "cleanDelay", notNullFieldName = "dateUnitType", message = "延时清零时间单位不能为空")
})
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RewardCleanSaveParam extends RewardSaveParam {
    @ApiModelProperty(value = "清零日期(MM-dd)", required = true)
    @NotBlank(message = "清零日期不能为空")
    private String cleanDate;

    @ApiModelProperty(value = "延时清零时间单位(common.DateUnitTypeEnum)")
    private DateUnitTypeEnum dateUnitType;

    @ApiModelProperty(value = "延时清零时间")
    private Integer cleanDelay;
}
