package com.aquilaflycloud.mdc.param.wechat;

import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class MiniProgramQrCodeUnLimitGetParam {

    @ApiModelProperty(value = "小程序appId", required = true)
    @NotBlank(message = "appId不能为空")
    private String appId;

    @ApiModelProperty(value = "小程序码参数", required = true)
    @NotBlank(message = "小程序码参数不能为空")
    private String scene;

    @ApiModelProperty(value = "小程序页面路径", required = true)
    @NotBlank(message = "小程序页面路径不能为空")
    private String pagePath;

    @ApiModelProperty(value = "二维码的宽度")
    @Min(value = 280, message = "appId不能为空")
    @Max(value = 1280, message = "appId不能为空")
    private int width = 430;

    @ApiModelProperty(value = "自动配置线条颜色")
    private Boolean autoColor = false;

    @ApiModelProperty(value = "小程序码颜色")
    private WxMaCodeLineColor lineColor;

    @ApiModelProperty(value = "是否需要透明底色")
    private Boolean isHyaline = false;
}
