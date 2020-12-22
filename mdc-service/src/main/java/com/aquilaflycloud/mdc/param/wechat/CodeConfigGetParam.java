package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CodeConfigGetParam {

	@ApiModelProperty(value="模板配置id", required = true)
	@NotNull(message = "模板配置id不能为空")
	private Long id;
}
