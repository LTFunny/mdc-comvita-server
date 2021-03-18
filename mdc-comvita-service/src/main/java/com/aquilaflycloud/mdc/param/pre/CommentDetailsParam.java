package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @Author zly
 */
@Data
public class CommentDetailsParam {


    /**
     * 点评id
     */
    @ApiModelProperty(value = "点评id", required = true)
    @NotNull(message = "点评id不能为空")
    private Long id;



}
