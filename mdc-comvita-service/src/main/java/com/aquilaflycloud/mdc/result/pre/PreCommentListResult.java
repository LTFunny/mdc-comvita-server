package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.model.pre.PreCommentReplyInfo;
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
public class PreCommentListResult extends PreCommentInfo {
    @ApiModelProperty(value = "会员头像")
    private String avatarUrl;
    /**
     * 回复列表
     */
    @ApiModelProperty(value = "回复列表")
    private List<PreCommentReplyInfo> commentReplyList;
}
