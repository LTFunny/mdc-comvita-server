package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;


/**
 * PreCommentPageResult
 * @author linkq
 */
@Accessors(chain = true)
@Data
public class PreCommentPageResult extends PreCommentInfo {
    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
    private String folksonomyNames;

    /**
     * 点赞数量
     */
    @ApiModelProperty(value = "点赞数量")
    private Long comLikeCount;

    /**
     * 回复列表
     */
    @ApiModelProperty(value = "回复列表")
    private List<CommentReply> commentReplyList;
    @Data
    public static class CommentReply {
        @ApiModelProperty(value = "回复id")
        private Long replyId;

        @ApiModelProperty(value = "回复人")
        private String replier;

        @ApiModelProperty(value = "回复内容")
        private String content;

        @ApiModelProperty(value = "回复时间")
        private Date replyTime;

        @ApiModelProperty(value = "回复图片")
        private String picture;
    }

}
