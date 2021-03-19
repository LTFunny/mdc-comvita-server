package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * PreCommentInfoResult
 *
 * @author star
 * @date 2021/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PreCommentInfoResult extends PreCommentInfo {
    @ApiModelProperty(value = "点赞数")
    private Long likeNum;

    @ApiModelProperty(value = "活动照片")
    private String activityPicture;
}
