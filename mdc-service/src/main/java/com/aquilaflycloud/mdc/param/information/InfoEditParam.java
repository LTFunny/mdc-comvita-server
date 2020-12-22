package com.aquilaflycloud.mdc.param.information;

import com.aquilaflycloud.mdc.enums.information.ImportanceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * InfoEditParam
 *
 * @author star
 * @date 2020-03-07
 */
@Data
@Accessors(chain = true)
public class InfoEditParam {
    @ApiModelProperty(value = "资讯id", required = true)
    @NotNull(message = "资讯id不能为空")
    private Long id;

    @ApiModelProperty(value = "微信或支付宝appId")
    private String appId;

    @ApiModelProperty(value = "资讯标题")
    private String infoTitle;

    @ApiModelProperty(value = "资讯内容")
    private String infoContent;

    @ApiModelProperty(value = "重要等级(information.ImportanceEnum)")
    private ImportanceEnum importance;
}


