package com.aquilaflycloud.mdc.param.folksonomy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * FolksonomyMemberRelParam
 *
 * @author star
 * @date 2021/1/12
 */
@Data
@Accessors(chain = true)
public class FolksonomyMemberRelParam {
    @ApiModelProperty(value = "标签id", required = true)
    @NotNull(message = "标签id不能为空")
    private Long id;

    @ApiModelProperty(value = "会员id", required = true)
    @NotNull(message = "会员id不能为空")
    private Long memberId;
}


