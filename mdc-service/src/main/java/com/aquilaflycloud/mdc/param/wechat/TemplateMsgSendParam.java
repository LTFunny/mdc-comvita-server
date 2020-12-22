package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class TemplateMsgSendParam {

    @ApiModelProperty(value = "微信模板消息id", required = true)
    @NotBlank(message = "微信模板消息id不能为空")
    private String templateId;

    @ApiModelProperty(value = "微信openId", required = true)
    @NotBlank(message = "微信openId不能为空")
    private String openId;

    @ApiModelProperty(value = "微信公众号AppId", required = true)
    @NotBlank(message = "微信公众号AppId不能为空")
    private String appId;

    @ApiModelProperty(value = "消息跳转url", required = true)
    @NotBlank(message = "消息跳转url不能为空")
    private String url;

    @ApiModelProperty(value = "微信模板内容", required = true)
    @NotEmpty(message = "微信模板内容不能为空")
    private List<WxMpTemplateData> data;

    @ApiModelProperty(value = "消息跳转小程序appId")
    private String miniAppId;

    @ApiModelProperty(value = "消息跳转小程序页面路径")
    private String pagePath;
}
