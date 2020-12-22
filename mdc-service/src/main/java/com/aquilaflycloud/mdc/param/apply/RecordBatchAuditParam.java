package com.aquilaflycloud.mdc.param.apply;

import com.aquilaflycloud.common.AnotherFieldHasValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * RecordBatchAuditParam
 *
 * @author star
 * @date 2020-02-27
 */
@AnotherFieldHasValue(fieldName = "isSurplus", fieldValue = "false", notNullFieldName = "ids", message = "报名记录ids不能为空")
@Data
@Accessors(chain = true)
public class RecordBatchAuditParam {
    @ApiModelProperty(value = "报名活动id", required = true)
    @NotNull(message = "报名活动id不能为空")
    private Long applyId;

    @ApiModelProperty(value = "是否选择剩余未审核记录(默认false)")
    private Boolean isSurplus = false;

    @ApiModelProperty(value = "报名记录ids")
    private List<Long> ids;

    @ApiModelProperty(value = "是否审核通过", required = true)
    @NotNull(message = "isApprove不能为空")
    private Boolean isApprove;

    @ApiModelProperty(value = "审核详情")
    private String auditRemark;
}




