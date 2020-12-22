package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MemberFeedbackGetParam implements Serializable {
    @ApiModelProperty(value = "反馈id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
