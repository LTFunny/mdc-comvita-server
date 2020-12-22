package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.dataAuth.component.AfcBaseMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketOrderInfo;
import com.aquilaflycloud.mdc.param.ticket.OrderRefundPageParam;
import com.aquilaflycloud.mdc.param.ticket.TicketVerificatePageParam;
import com.aquilaflycloud.mdc.result.ticket.TicketOrderResult;
import com.aquilaflycloud.mdc.result.ticket.TicketVerificateStatisticResult;

import java.util.List;

public interface TicketOrderInfoMapper extends AfcBaseMapper<TicketOrderInfo> {
    Long selectCodeUnt(TicketVerificatePageParam param);

    List<TicketOrderResult> selectByParams(Long offset, OrderRefundPageParam param);

    Long countByParams(OrderRefundPageParam param);

    int updateOrderInfo(TicketOrderInfo item);

    TicketVerificateStatisticResult verificateStatistic(TicketVerificatePageParam param);
}