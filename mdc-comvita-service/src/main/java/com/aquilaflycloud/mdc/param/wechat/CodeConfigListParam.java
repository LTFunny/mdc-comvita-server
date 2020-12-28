package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CodeConfigListParam {

	@ApiModelProperty(value = "模板配置名称")
	private String templateConfigName;

	@ApiModelProperty(value = "用户描述")
	private String userDesc;

	@ApiModelProperty(value = "模板类型")
	private String templateType;
}
