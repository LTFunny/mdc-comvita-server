package com.aquilaflycloud.mdc.result.wechat;

import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WechatMiniAccountResult extends WechatAuthorSite {

	@ApiModelProperty(value = "是否已提交版本")
	private WhetherEnum isSubmit = WhetherEnum.NO;

	@ApiModelProperty(value = "是否已发布版本")
	private WhetherEnum isRelease = WhetherEnum.NO;
}
