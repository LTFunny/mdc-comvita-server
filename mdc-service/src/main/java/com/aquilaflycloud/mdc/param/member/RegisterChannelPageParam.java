package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.model.member.MemberRegisterChannel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class RegisterChannelPageParam extends PageAuthParam<MemberRegisterChannel> {
    @ApiModelProperty(value = "微信appId")
    private String appId;

    @ApiModelProperty(value = "渠道编码")
    private String channelCode;

    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    @ApiModelProperty(value = "状态(common.StateEnum)")
    private StateEnum state;

    @ApiModelProperty(value = "创建开始时间")
    private Date createTimeStart;

    @ApiModelProperty(value = "创建结束时间")
    private Date createTimeEnd;

    @ApiModelProperty(value = "指定用户部门名称")
    private String designateOrgNames;

    @ApiModelProperty(value = "关联记录创建开始时间")
    private Date relCreateTimeStart;

    @ApiModelProperty(value = "关联记录创建结束时间")
    private Date relCreateTimeEnd;
}