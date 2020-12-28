package com.aquilaflycloud.mdc.param.information;

import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.information.ImportanceEnum;
import com.aquilaflycloud.mdc.enums.information.InfoTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * InfoAddParam
 *
 * @author star
 * @date 2020-03-07
 */
@Data
@Accessors(chain = true)
public class InfoAddParam {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId = MdcConstant.UNIVERSAL_APP_ID;

    @ApiModelProperty(value = "资讯类型(information.InfoTypeEnum)", required = true)
    @NotNull(message = "资讯类型不能为空")
    private InfoTypeEnum infoType;

    @ApiModelProperty(value = "资讯标题", required = true)
    @NotBlank(message = "资讯标题不能为空")
    private String infoTitle;

    @ApiModelProperty(value = "资讯内容", required = true)
    @NotBlank(message = "资讯内容不能为空")
    private String infoContent;

    @ApiModelProperty(value = "重要等级(information.ImportanceEnum)", required = true)
    @NotNull(message = "重要等级不能为空")
    private ImportanceEnum importance;
}


