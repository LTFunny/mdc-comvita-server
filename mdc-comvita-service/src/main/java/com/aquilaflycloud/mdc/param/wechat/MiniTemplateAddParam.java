package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class MiniTemplateAddParam {
    @ApiModelProperty(value = "微信appId", required = true)
    @NotBlank(message = "微信appId不能为空")
    private String appId;

    @ApiModelProperty(value = "模板标题id", required = true)
    @NotBlank(message = "模板标题id不能为空")
    private String tid;

    @ApiModelProperty(value = "开发者自行组合好的模板关键词列表，关键词顺序可以自由搭配（例如 [3,5,4] 或 [4,5,3]），最多支持5个，最少2个关键词组合", required = true)
    @NotEmpty(message = "关键词id不能为空")
    private List<Integer> kidList;

    @ApiModelProperty(value = "服务场景描述，15个字以内", required = true)
    @NotBlank(message = "服务场景描述不能为空")
    private String sceneDesc;
}
