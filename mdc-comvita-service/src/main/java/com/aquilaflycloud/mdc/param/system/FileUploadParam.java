package com.aquilaflycloud.mdc.param.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * FileUploadParam
 *
 * @author star
 * @date 2019-11-26
 */
@Data
public class FileUploadParam {

    @ApiModelProperty(value = "文件", required = true)
    @NotNull(message = "请上传文件")
    private MultipartFile file;
}
