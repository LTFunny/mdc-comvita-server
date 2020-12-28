package com.aquilaflycloud.mdc.result.parking;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CouponLimit {
    @ApiModelProperty(value = "限制类型(PERSON, CAR)")
    @NotNull(message = "限制类型不能为空")
    private LimitType limitType;

    @ApiModelProperty(value = "限制范围(DAY, WEEK, MONTH, YEAR, LASTING)")
    @NotNull(message = "限制范围不能为空")
    private LimitRange limitRange;

    @ApiModelProperty(value = "限制值类型(QUOTA, AMOUNT)")
    @NotNull(message = "限制值类型不能为空")
    private LimitCountType limitCountType;

    @ApiModelProperty(value = "限制值")
    @NotNull(message = "限制值不能为空")
    private BigDecimal limitCount;

    public enum LimitType {
        // 限制类型
        PERSON, CAR
    }

    public enum LimitRange {
        // 限制范围
        DAY, WEEK, MONTH, YEAR, LASTING
    }

    public enum LimitCountType {
        // 限制值
        QUOTA, AMOUNT
    }
}