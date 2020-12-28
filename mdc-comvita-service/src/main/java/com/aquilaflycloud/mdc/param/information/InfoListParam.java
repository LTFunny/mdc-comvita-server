package com.aquilaflycloud.mdc.param.information;

import com.aquilaflycloud.mdc.enums.information.InfoTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * InfoListParam
 *
 * @author star
 * @date 2020-03-07
 */
@Data
@Accessors(chain = true)
public class InfoListParam {
    @ApiModelProperty(value = "资讯类型(information.InfoTypeEnum)", required = true)
    @NotNull(message = "资讯类型不能为空")
    private InfoTypeEnum infoType;
}


