package com.aquilaflycloud.mdc.param.alipay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * AlipayPreAuthUrlGetParam
 *
 * @author star
 * @date 2019-12-24
 */
@Data
@Accessors(chain = true)
public class AlipayPreAuthUrlGetParam {
    @ApiModelProperty(value = "应用类型(PUBLICAPP, TINYAPP)", required = true)
    @NotNull(message = "应用类型不能为空")
    private ApplicationTypeEnum applicationType;

    /*enum ApplicationTypeEnum {
        MOBILEAPP, WEBAPP, PUBLICAPP, TINYAPP, ARAPP
    }*/

    public enum ApplicationTypeEnum {
        // 支付宝授权应用类型
        PUBLICAPP, TINYAPP
    }
}
