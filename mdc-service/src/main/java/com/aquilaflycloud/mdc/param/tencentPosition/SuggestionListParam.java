package com.aquilaflycloud.mdc.param.tencentPosition;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class SuggestionListParam extends PageParam {
	@ApiModelProperty(value = "地址关键词", required = true)
	@NotBlank(message = "关键词不能为空")
	private String keyword;

	@ApiModelProperty(value = "城市范围", required = true)
	@NotBlank(message = "城市范围不能为空")
	private String region;

	@ApiModelProperty(value = "是否固定当前城市(common.WhetherEnum)")
	private WhetherEnum regionFix = WhetherEnum.NO;
}
