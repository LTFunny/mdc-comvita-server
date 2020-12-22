package com.aquilaflycloud.mdc.result.wechat;

import com.aquilaflycloud.mdc.model.wechat.WechatMiniProgramCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WechatMiniProgramCodeResult extends WechatMiniProgramCode {
    @ApiModelProperty(value="小程序名称")
    private String appName;
}