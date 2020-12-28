package com.aquilaflycloud.mdc.result.ticket;

import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketScenicSpotInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * TicketScenicSpotInfoResult
 *
 * @author star
 * @date 2019-12-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketScenicSpotInfoResult extends TicketScenicSpotInfo {
    @ApiModelProperty(value = "标签信息列表")
    List<FolksonomyInfo> folksonomyInfoList;
}
