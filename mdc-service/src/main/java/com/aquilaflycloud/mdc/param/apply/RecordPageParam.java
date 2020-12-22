package com.aquilaflycloud.mdc.param.apply;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.model.apply.ApplyMemberRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * RecordPageParam
 *
 * @author star
 * @date 2020-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RecordPageParam extends PageParam<ApplyMemberRecord> {
    @ApiModelProperty(value = "报名活动id", required = true)
    @NotNull(message = "报名活动id不能为空")
    private Long applyId;

    @ApiModelProperty(value = "微信或支付宝昵称")
    private String nickName;

    @ApiModelProperty(value = "报名人姓名")
    private String applyName;

    @ApiModelProperty(value = "报名电话")
    private String applyPhone;

    @ApiModelProperty(value = "审核状态(common.AuditStateEnum)")
    private AuditStateEnum auditState;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;
}


