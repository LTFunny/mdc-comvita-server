package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * PreCommentLikePageParam
 *
 * @author star
 * @date 2021/3/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PreCommentLikePageParam extends PageParam<MemberInteraction> {
    @ApiModelProperty(value = "评论id")
    private Long id;
}


