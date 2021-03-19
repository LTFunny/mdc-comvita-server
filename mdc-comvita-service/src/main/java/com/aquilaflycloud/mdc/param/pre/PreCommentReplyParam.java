package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * PreCommentReplyParam
 * @author linkq
 */
@Data
public class PreCommentReplyParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "message", required = true)
    @NotNull(message = "回复内容不能为空")
    private String message;


}
