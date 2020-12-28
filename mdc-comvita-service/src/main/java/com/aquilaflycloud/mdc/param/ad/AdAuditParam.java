package com.aquilaflycloud.mdc.param.ad;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * AdAuditParam
 *
 * @author star
 * @date 2019-11-19
 */
@AnotherFieldHasValue(fieldName = "isApprove", fieldValue = "false", notNullFieldName = "auditRemark", message = "审核详情不能为空")
@Data
@Accessors(chain = true)
public class AdAuditParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "广告id不能为空")
    private Long id;

    @ApiModelProperty(value = "是否审核通过", required = true)
    @NotNull(message = "isApprove不能为空")
    private Boolean isApprove;

    @ApiModelProperty(value = "审核详情")
    private String auditRemark;
}


