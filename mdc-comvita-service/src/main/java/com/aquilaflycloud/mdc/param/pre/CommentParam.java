package com.aquilaflycloud.mdc.param.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @Author zly
 */
@Data
public class CommentParam {
    /**
     * 点评内容
     */
    @ApiModelProperty(value = "点评内容", required = true)
    @NotNull(message = "点评内容不能为空")
    private String comContent;

    /**
     * 点评图片
     */
    @ApiModelProperty(value = "点评图片")
    private String comPicture;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id", required = true)
    @NotNull(message = "活动id不能为空")
    private Long activityId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称", required = true)
    @NotNull(message = "活动名称不能为空")
    private String activityName;


}
