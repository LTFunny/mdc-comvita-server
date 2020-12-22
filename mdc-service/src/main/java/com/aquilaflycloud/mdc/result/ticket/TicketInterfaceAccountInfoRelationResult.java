package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.enums.ticket.ScenicSpotTypeEnum;
import com.aquilaflycloud.mdc.model.ticket.TicketInterfaceAccountInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 第三方接口(道控)账号信息表
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketInterfaceAccountInfoRelationResult extends TicketInterfaceAccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "景区类型")
    private ScenicSpotTypeEnum scenicSpotTypeEnum;

    public TicketInterfaceAccountInfoRelationResult() {
    }
}
