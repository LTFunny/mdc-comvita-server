package com.aquilaflycloud.mdc.result.wechat;

import com.aquilaflycloud.mdc.model.wechat.WechatMiniLiveInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WechatMiniLiveInfoResult extends WechatMiniLiveInfo {
	@ApiModelProperty(value = "商品信息")
	private List<WechatMiniLiveGoodsResult> goods;
}
