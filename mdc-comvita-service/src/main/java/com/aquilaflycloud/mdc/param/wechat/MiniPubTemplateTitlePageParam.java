package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.dataAuth.common.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MiniPubTemplateTitlePageParam extends PageParam {
    @ApiModelProperty(value = "微信appId", required = true)
    @NotBlank(message = "微信appId不能为空")
    private String appId;

    @ApiModelProperty(value = "类目id", required = true)
    @NotEmpty(message = "类目id不能为空")
    private String[] ids;
}
