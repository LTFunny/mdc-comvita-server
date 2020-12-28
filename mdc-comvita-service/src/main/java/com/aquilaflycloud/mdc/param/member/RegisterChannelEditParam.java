package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RegisterChannelEditParam {
    @ApiModelProperty(value = "渠道id", required = true)
    @NotNull(message = "渠道id不能为空")
    private Long id;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "渠道位置")
    private String channelPosition;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "指定用户部门ids(多个以,分隔)")
    private String designateOrgIds;
}