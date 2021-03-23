package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PreCommentResult
 *
 * @author linkq
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PreCommentResult extends PreCommentInfo {
    @ApiModelProperty(value = "是否点赞")
    private boolean isLiked;

    @ApiModelProperty(value = "是否点赞")
    private Long likeNum;

    @ApiModelProperty(value = "回复列表")
    private List<PreCommentResult> commentReplyList;
}
