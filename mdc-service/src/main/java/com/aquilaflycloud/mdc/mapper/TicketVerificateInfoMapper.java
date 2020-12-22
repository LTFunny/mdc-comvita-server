package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketVerificateInfo;
import com.aquilaflycloud.mdc.param.ticket.TicketVerificatePageParam;
import com.aquilaflycloud.mdc.result.ticket.TicketVerificateChartResult;

import java.util.List;

public interface TicketVerificateInfoMapper extends AfcBaseMapper<TicketVerificateInfo> {

    List<TicketVerificateChartResult> getUseCntByDate(TicketVerificatePageParam param);

}