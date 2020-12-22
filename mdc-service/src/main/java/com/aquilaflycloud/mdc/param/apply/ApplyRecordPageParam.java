package com.aquilaflycloud.mdc.param.apply;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.apply.ApplyActivity;
import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * ApplyRecordPageParam
 *
 * @author star
 * @date 2020-05-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ApplyRecordPageParam extends PageParam<ApplyMemberRecord> {
    @ApiModelProperty(value = "报名活动id", required = true)
    @NotNull(message = "报名活动id不能为空")
    private String applyId;
}


