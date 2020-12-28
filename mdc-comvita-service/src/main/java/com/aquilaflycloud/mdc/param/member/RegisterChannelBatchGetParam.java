package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class RegisterChannelBatchGetParam {
    @ApiModelProperty(value = "渠道id列表", required = true)
    @NotEmpty(message = "渠道id列表不能为空")
    private List<Long> idList;
}
