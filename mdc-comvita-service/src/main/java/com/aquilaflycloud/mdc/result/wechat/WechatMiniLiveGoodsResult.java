package com.aquilaflycloud.mdc.result.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WechatMiniLiveGoodsResult {
	@ApiModelProperty(value = "商品名称")
	private String name;

	@ApiModelProperty(value = "商品价格")
	private Integer price;

	@ApiModelProperty(value = "商品图片")
	private String coverImg;

	@ApiModelProperty(value = "商品地址")
	private String url;
}
