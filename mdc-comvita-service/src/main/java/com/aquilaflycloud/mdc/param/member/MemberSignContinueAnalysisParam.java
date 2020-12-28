package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class MemberSignContinueAnalysisParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "次数范围", required = true)
    @NotEmpty(message = "次数范围不能为空")
    @Valid
    private List<Rang> rangList;

    @Data
    public class Rang {
        @ApiModelProperty(value = "首值", required = true)
        @NotNull(message = "首值不能为空")
        private Integer start;

        @ApiModelProperty(value = "尾值", required = true)
        @NotNull(message = "尾值不能为空")
        private Integer end;
    }
}
