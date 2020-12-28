package com.aquilaflycloud.mdc.param.apply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * RecordAuditParam
 *
 * @author star
 * @date 2020-02-27
 */
@Data
@Accessors(chain = true)
public class RecordAuditParam {
    @ApiModelProperty(value = "报名记录id", required = true)
    @NotNull(message = "报名记录id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否审核通过", required = true)
    @NotNull(message = "isApprove不能为空")
    private Boolean isApprove;

    @ApiModelProperty(value = "审核详情")
    private String auditRemark;
}


