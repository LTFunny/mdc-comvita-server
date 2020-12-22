package com.aquilaflycloud.mdc.result.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrcodeMsgVideoResult {
	@ApiModelProperty(value = "通过素材管理中的接口上传多媒体文件，得到的id。")
	private String mediaId;

	@ApiModelProperty(value = "视频消息的标题")
	private String title;

	@ApiModelProperty(value = "视频消息的描述")
	private String description;
}
