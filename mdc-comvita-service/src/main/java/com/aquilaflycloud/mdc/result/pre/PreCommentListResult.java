package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PreCommentPageResult
 * @author linkq
 */
@Accessors(chain = true)
@Data
public class PreCommentListResult extends PreCommentResult {

    /**
     * 回复列表
     */
    @ApiModelProperty(value = "回复列表")
    private List<PreCommentResult> commentReplyList;
}
