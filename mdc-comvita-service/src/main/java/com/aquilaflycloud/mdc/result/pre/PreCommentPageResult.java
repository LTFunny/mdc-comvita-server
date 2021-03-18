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

    @ApiModelProperty(value = "标签名称列表")
    private List<String> folksonomyIds;

    @ApiModelProperty(value = "点赞记录列表")
    private List<LikeRecord> likeRecordList;

    @Data
    public static class LikeRecord {
        @ApiModelProperty(value = "昵称")
        private Long nickName;
        /**
         * 点赞/取消点赞
         */
        @ApiModelProperty(value = "操作")
        private String operateType;

        @ApiModelProperty(value = "操作时间")
        private Date shopAddress;
    }

}
