package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.AuthParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class MemberBatchAddParam extends AuthParam {
    @ApiModelProperty(value = "上传文件字段名称对应的name属性", required = true)
    @NotNull(message = "文件流不能为空")
    private MultipartFile file;

    /*@ApiModelProperty(value = "文件url")
    private String fileUrl;*/
}
