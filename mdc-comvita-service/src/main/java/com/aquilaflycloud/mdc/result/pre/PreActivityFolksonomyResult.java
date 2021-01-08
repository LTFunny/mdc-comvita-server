package com.aquilaflycloud.mdc.result.pre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PreActivityFolksonomyResult
 * @author linkq
 */
@Data
public class PreActivityFolksonomyResult {
    @ApiModelProperty(value = "标签id")
    private Long folksonomyId;

    @ApiModelProperty(value = "标签名")
    private String folksonomyName;
}
