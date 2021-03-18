package com.aquilaflycloud.mdc.param.pre;

import com.aquilaflycloud.mdc.enums.pre.ActivityCommentStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * PreCommentAuditParam
 * 审核参数
 * @author linkq
 */
@Data
public class PreCommentAuditParam {

    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "活动标签id列表")
    private List<Long> folksonomyIds;

    @ApiModelProperty(value = "审核反馈")
    private String auditFeedback;

    /**
     * 审核操作 通过/不通过
     */
    @ApiModelProperty(value = "审核操作(pre.ActivityCommentStateEnum)")
    private ActivityCommentStateEnum auditOperateType;


}
