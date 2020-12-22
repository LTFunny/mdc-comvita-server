package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberFeedbackAddParam extends AuthParam {
    @ApiModelProperty(value = "问题描述", required = true)
    @NotNull(message = "问题描述不能为空")
    private String description;

    @ApiModelProperty(value = "图片链接")
    private String picUrl;

    @ApiModelProperty(value = "评分")
    private Integer score;
}
