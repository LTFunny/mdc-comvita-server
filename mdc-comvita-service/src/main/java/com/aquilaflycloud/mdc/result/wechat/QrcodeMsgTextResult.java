package com.aquilaflycloud.mdc.result.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrcodeMsgTextResult {
	@ApiModelProperty(value = "回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）")
	private String content;
}
