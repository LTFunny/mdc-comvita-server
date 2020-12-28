package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class RegisterChannelAddParam {
    @ApiModelProperty(value = "渠道名称", required = true)
    @NotBlank(message = "渠道名称不能为空")
    private String channelName;

    @ApiModelProperty(value = "渠道位置", required = true)
    @NotBlank(message = "渠道位置不能为空")
    private String channelPosition;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "微信appId", required = true)
    @NotBlank(message = "微信appId不能为空")
    private String appId;

    @ApiModelProperty(value = "小程序路径(默认pages/home/home)")
    private String pagePath = "pages/home/home";

    @ApiModelProperty(value = "指定用户部门ids(多个以,分隔)")
    private String designateOrgIds;
}
