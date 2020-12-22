package com.aquilaflycloud.mdc.result.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WechatUserAnalysisSumResult {

	@ApiModelProperty(value = "微信公众号appId")
	private String appId;

	@ApiModelProperty(value = "统计时间")
	private Date refDate;

	@ApiModelProperty(value = "新增用户数")
	private Integer newUser;

	@ApiModelProperty(value = "取消关注用户数")
	private Integer cancelUser;

	@ApiModelProperty(value = "净增用户数")
	private Integer increaseUser;

	@ApiModelProperty(value = "累计用户数")
	private Integer cumulateUser;
}
