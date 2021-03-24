package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * PreCommentLikeResult
 *
 * @author star
 * @date 2021/3/24
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class PreCommentLikeResult extends PreCommentInfo {
    @ApiModelProperty(value = "互动发生时间")
    private Date interactionTime;
}
