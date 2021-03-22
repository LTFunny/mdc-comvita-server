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

    /**
     * 活动id
     */
    @ApiModelProperty(value = "activityId", required = true)
    @NotNull(message = "活动id不能为空")
    private Long activityId;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "activityName", required = true)
    @NotNull(message = "活动名称不能为空")
    private String activityName;

    /**
     * 活动点评id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "活动点评id不能为空")
    private Long commentId;

    /**
     * 回复内容
     */
    @ApiModelProperty(value = "content", required = true)
    @NotNull(message = "回复内容不能为空")
    private String content;

    /**
     * 回复图片
     */
    @ApiModelProperty(value = "回复图片")
    private String picture;

}
