package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class MiniQrcodeAddParam {
    @ApiModelProperty(value = "微信appId", required = true)
    @NotBlank(message = "微信appId不能为空")
    private String appId;

    @ApiModelProperty(value = "扫码进入的小程序页面路径", required = true)
    @NotBlank(message = "扫码进入的小程序页面路径不能为空")
    @Length(max = 128, message = "path长度不能大于128")
    private String path;

    @ApiModelProperty(value = "二维码的宽度，单位 px。")
    @Min(value = 280, message = "不能小于280")
    @Max(value = 1280, message = "不能大于1280")
    private Integer width = 430;
}
