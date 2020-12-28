package com.aquilaflycloud.mdc.param.ticket;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoStateEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketChannelInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 渠道添加
 *
 * @author Zengqingjie
 * @date 2019-11-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ChannelInfoPageParam extends PageAuthParam<TicketChannelInfo> implements Serializable {

    private static final long serialVersionUID = 4282170858851634238L;
    @ApiModelProperty(value = "渠道名称")
    private String name;

    @ApiModelProperty(value = "渠道负责人")
    private String responsiblePerson;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "状态(ticket.ChannelInfoStateEnum)1:正常;2:停用")
    private ChannelInfoStateEnum state;

    @ApiModelProperty(value = "添加开始时间")
    private Date startCreateTime;

    @ApiModelProperty(value = "添加结束时间")
    private Date endCreateTime;

}
