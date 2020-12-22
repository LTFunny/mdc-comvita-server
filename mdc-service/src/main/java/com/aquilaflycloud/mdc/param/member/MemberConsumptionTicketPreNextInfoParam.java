package com.aquilaflycloud.mdc.param.member;

import com.aquilaflycloud.dataAuth.common.PageAuthParam;
import com.aquilaflycloud.mdc.enums.member.ConsumptionTicketStateEnum;
import com.aquilaflycloud.mdc.enums.member.MemberScanTypeEnum;
import com.aquilaflycloud.mdc.model.member.MemberScanRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MemberConsumptionTicketPreNextInfoParam extends PageAuthParam<MemberScanRecord> {
    @ApiModelProperty(value = "上传开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "上传结束时间")
    private Date createEndTime;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "消费开始金额(元)")
    private BigDecimal startAmount;

    @ApiModelProperty(value = "消费结束金额(元)")
    private BigDecimal endAmount;

    @ApiModelProperty(value = "状态(member.ConsumptionTicketState)")
    private ConsumptionTicketStateEnum state;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "审核人名称")
    private String auditName;

    @ApiModelProperty(value = "类型(member.MemberScanTypeEnum)")
    private MemberScanTypeEnum type;
}
