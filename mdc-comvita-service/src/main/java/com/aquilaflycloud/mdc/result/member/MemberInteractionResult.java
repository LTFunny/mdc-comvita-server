package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * MemberInteractionResult
 *
 * @author star
 * @date 2021/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberInteractionResult extends MemberInteraction implements Serializable {
    @ApiModelProperty(value = "昵称")
    private String nickName;
}
