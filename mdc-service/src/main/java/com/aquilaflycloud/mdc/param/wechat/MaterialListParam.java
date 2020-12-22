package com.aquilaflycloud.mdc.param.wechat;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.wechat.MaterialTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * MaterialListParam
 *
 * @author star
 * @date 2019-10-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MaterialListParam extends PageParam {

    @ApiModelProperty(value = "公众号appId", required = true)
    @NotBlank(message = "公众号appId不能为空")
    private String appId;

    @ApiModelProperty(value = "素材类型(wechat.MaterialTypeEnum)", required = true)
    @NotNull(message = "素材类型不能为空")
    private MaterialTypeEnum type;
}
