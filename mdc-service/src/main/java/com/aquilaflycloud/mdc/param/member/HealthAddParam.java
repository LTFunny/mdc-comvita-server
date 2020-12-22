package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.mdc.enums.member.IdTypeEnum;
import com.aquilaflycloud.mdc.enums.member.SexEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class HealthAddParam {
    @ApiModelProperty(value = "真实姓名", required = true)
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @ApiModelProperty(value = "报备手机", required = true)
    @NotBlank(message = "报备手机不能为空")
    private String reportPhone;

    @ApiModelProperty(value = "证件类型", required = true)
    @NotNull(message = "证件类型不能为空")
    private IdTypeEnum idType;

    @ApiModelProperty(value = "证件号码", required = true)
    @NotBlank(message = "证件号码不能为空")
    private String idCard;

    @ApiModelProperty(value = "性别", required = true)
    @NotNull(message = "性别不能为空")
    private SexEnum sex;

    @ApiModelProperty(value = "健康状态1")
    private String healthState1;

    @ApiModelProperty(value = "健康状态2")
    private String healthState2;

    @ApiModelProperty(value = "健康状态3")
    private String healthState3;

    @ApiModelProperty(value = "小程序码参数")
    private String sceneStr;
}
