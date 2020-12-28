package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class InterfaceAccountInfoAddParam extends AuthParam implements Serializable {
    @ApiModelProperty(value = "商户编码", required = true)
    @NotBlank(message = "商户编码不能为空")
    private String merchantCode;

    @ApiModelProperty(value = "OTA账户", required = true)
    @NotBlank(message = "OTA账户不能为空")
    private String interfaceAccount;

    @ApiModelProperty(value = "秘钥", required = true)
    @NotBlank(message = "秘钥不能为空")
    private String secret;

    @ApiModelProperty(value = "指定部门ids(多个以,分隔)", required = true)
    @NotBlank(message = "指定部门不能为空")
    private String designateOrgIds;

    @ApiModelProperty(value = "指定部门json串", required = true)
    @NotBlank(message = "指定部门json串不能为空")
    private String content;

    @ApiModelProperty(value = "接口基础url", required = true)
    @NotBlank(message = "接口地址不能为空")
    private String baseUrl;

    @ApiModelProperty(value = "景区类型", required = true)
    @NotNull(message = "景区类型不能为空")
    private ScenicSpotTypeEnum scenicSpotType;
}
