package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * PreCommentPageResult
 * @author linkq
 */
@Accessors(chain = true)
@Data
public class PreCommentPageResult extends PreCommentInfo {
    @ApiModelProperty(value = "标签名称")
    private String folksonomyNames;

    @ApiModelProperty(value = "点赞数量")
    private Long comLikeCount;

}
