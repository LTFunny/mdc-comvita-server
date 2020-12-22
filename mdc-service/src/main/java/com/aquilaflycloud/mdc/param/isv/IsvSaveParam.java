package com.aquilaflycloud.mdc.param.isv;

import com.aquilaflycloud.auth.enums.UserTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * IsvSaveParam
 *
 * @author star
 * @date 2019-12-17
 */
@Data
@Accessors(chain = true)
public class IsvSaveParam {
    @ApiModelProperty(value = "isv应用id", required = true)
    @NotBlank(message = "isv应用id不能为空")
    private String appKey;

    @ApiModelProperty(value = "第三方appId")
    private String otherAppId;

    @ApiModelProperty(value = "类型", required = true)
    @NotNull(message = "类型不能为空")
    private UserTypeEnum type;

    @ApiModelProperty(value = "租户id", required = true)
    @NotNull(message = "租户id不能为空")
    private Long tenantId;

    @ApiModelProperty(value = "子租户id")
    private Long subTenantId;
}
