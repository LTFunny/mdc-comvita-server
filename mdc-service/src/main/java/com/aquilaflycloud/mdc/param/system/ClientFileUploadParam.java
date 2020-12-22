package com.aquilaflycloud.mdc.param.system;

import com.aquilaflycloud.mdc.enums.member.UploadBusinessTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * ClientFileUploadParam
 *
 * @author star
 * @date 2020-04-07
 */
@Data
public class ClientFileUploadParam {
    @ApiModelProperty(value = "文件", required = true)
    @NotNull(message = "请上传文件")
    private MultipartFile file;

    @ApiModelProperty(value = "业务类型(member.UploadBusinessTypeEnum)", required = true)
    @NotNull(message = "业务类型不能为空")
    private UploadBusinessTypeEnum businessType;

    @ApiModelProperty(value = "是否图片(默认true)")
    private Boolean isImg = true;
}
