package com.aquilaflycloud.mdc.param.information;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.information.InfoTypeEnum;
import com.aquilaflycloud.mdc.model.information.Information;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * InfoOneGetParam
 *
 * @author star
 * @date 2020-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InfoOneGetParam extends PageParam<Information> {
    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "资讯类型(information.InfoTypeEnum)", required = true)
    @NotNull(message = "资讯类型不能为空")
    private InfoTypeEnum infoType;
}


