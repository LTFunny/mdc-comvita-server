package com.aquilaflycloud.mdc.param.shop;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * ShopDownloadCodeParam
 *
 * @author zengqingjie
 * @date 2020-04-20
 */
@Data
@Accessors(chain = true)
public class ShopDownloadCodeParam implements Serializable {

    private static final long serialVersionUID = 7232974641789212868L;

    @ApiModelProperty(value = "商户二维码url", required = true)
    @NotBlank(message = "商户二维码url不能为空")
    private String qrCode;
}