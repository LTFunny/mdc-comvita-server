package com.aquilaflycloud.mdc.mapper;

import com.aquilaflycloud.mdc.model.ticket.TicketOrdernoOtaordernoRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TicketOrdernoOtaordernoRelationMapper extends BaseMapper<TicketOrdernoOtaordernoRelation> {
    int updateRelationById(TicketOrdernoOtaordernoRelation relation);
}