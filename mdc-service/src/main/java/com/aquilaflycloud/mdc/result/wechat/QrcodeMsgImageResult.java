package com.aquilaflycloud.mdc.result.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QrcodeMsgImageResult {
	@ApiModelProperty(value = "通过素材管理中的接口上传多媒体文件，得到的id。")
	private String mediaId;
}
