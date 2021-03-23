package com.aquilaflycloud.mdc.result.pre;

import com.aquilaflycloud.mdc.model.pre.PreCommentInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * PreCommentResult
 * 加上会员头像信息
 * @author linkq
 */
@Accessors(chain = true)
@Data
public class PreCommentResult extends PreCommentInfo {

    @ApiModelProperty(value = "会员头像")
    private String avatarUrl;
}
