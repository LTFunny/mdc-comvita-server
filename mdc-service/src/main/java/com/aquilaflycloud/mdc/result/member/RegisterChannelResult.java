package com.aquilaflycloud.mdc.result.member;

import com.aquilaflycloud.mdc.model.member.MemberRegisterChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * RegisterChannelResult
 *
 * @author star
 * @date 2020-02-20
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterChannelResult extends MemberRegisterChannel implements Serializable {
    @ApiModelProperty(value = "会员数量")
    private Integer memberCount;
}
