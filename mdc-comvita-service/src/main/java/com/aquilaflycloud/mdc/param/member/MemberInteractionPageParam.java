package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.member.InteractionBusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.InteractionTypeEnum;
import com.aquilaflycloud.mdc.model.member.MemberInteraction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MemberInteractionPageParam extends PageParam<MemberInteraction> {
    @ApiModelProperty(value = "业务数据id", required = true)
    @NotNull(message = "业务数据id不能为空")
    private Long businessId;

    @ApiModelProperty(value = "业务数据类型(member.InteractionBusinessTypeEnum)", required = true)
    @NotNull(message = "业务数据类型不能为空")
    private InteractionBusinessTypeEnum businessType;

    @ApiModelProperty(value = "互动类型(member.InteractionTypeEnum)", required = true)
    @NotNull(message = "互动类型不能为空")
    private InteractionTypeEnum interactionType;
}
