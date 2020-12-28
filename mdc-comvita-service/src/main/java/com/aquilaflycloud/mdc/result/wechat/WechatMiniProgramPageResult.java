package com.aquilaflycloud.mdc.result.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WechatMiniProgramPageResult {
    @ApiModelProperty(value="小程序可选页面路径")
    private List<String> pageList;

    @ApiModelProperty(value="小程序不可选页面路径")
    private List<String> notPageList;
}