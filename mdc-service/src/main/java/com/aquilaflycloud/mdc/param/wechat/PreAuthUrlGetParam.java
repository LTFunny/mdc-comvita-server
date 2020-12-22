package com.aquilaflycloud.mdc.param.wechat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * PreAuthUrlGetParam
 *
 * @author star
 * @date 2019-10-09
 */
@Data
@Accessors(chain = true)
public class PreAuthUrlGetParam {

    @ApiModelProperty(value = "指定授权唯一的小程序或公众号AppId")
    private String appId;

    @ApiModelProperty(value = "授权帐号类型(PUBLIC, MINI)")
    private AuthType authType;

    @ApiModelProperty(value = "是否生成移动端授权链接")
    private Boolean isMobile = false;

    public enum AuthType {
        // 授权帐号类型
        PUBLIC, MINI
    }
}
