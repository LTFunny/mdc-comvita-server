package com.aquilaflycloud.mdc.param.member;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MemberFaceAddParam implements Serializable {
    @ApiModelProperty(value = "自拍人脸url", required = true)
    @NotBlank(message = "自拍人脸url不能为空")
    private String faceUrl;

}
