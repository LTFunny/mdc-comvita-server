package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.model.member.MemberFeedbackInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MemberFeedbackPageParam extends PageAuthParam<MemberFeedbackInfo> {
    @ApiModelProperty(value = "用户名称")
    private String nickName;

    @ApiModelProperty(value = "提交开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "提交结束时间")
    private Date createEndTime;

    @ApiModelProperty(value = "服务评分")
    private Integer score;
}
