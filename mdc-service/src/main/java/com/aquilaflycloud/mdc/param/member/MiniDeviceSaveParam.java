package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MiniDeviceSaveParam implements Serializable {

    @ApiModelProperty(value = "设备信息json(wx.getSystemInfoSync返回结果)", required = true)
    @NotBlank(message = "deviceInfo不能为空")
    private String deviceInfo;

}
