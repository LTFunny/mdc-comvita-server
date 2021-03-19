package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import com.aquilaflycloud.mdc.result.member.MemberInteractionResult;
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
public class PreCommentPageResult extends PreCommentInfo {
    @ApiModelProperty(value = "标签名称")
    private String folksonomyNames;
}
