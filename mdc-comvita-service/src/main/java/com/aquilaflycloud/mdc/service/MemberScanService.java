package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.member.MemberScanRecord;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.MemberScanResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

/**
 * MemberScanService
 *
 * @author star
 * @date 2020-03-27
 */
public interface MemberScanService {
    MemberScanResult addScanInfo(ScanInfoAddParam param);

    void addConsume(ConsumeAddParam param);

    MemberScanResult addQrcodeScanConsume(String orderNo, String openId, String miniAppId);

    IPage<MemberScanRecord> pageInfo(MemberConsumptionTicketPageInfoParam param);

    void addConsumptionTicketnfo(MemberConsumptionTicketAddInfoParam param);

    IPage<MemberScanRecord> pageConsumptionTicket(MemberConsumptionTicketPageParam param);

    MemberScanRecord getConsumptionTicket(MemberConsumptionTicketGetParam param);

    @Transactional
    void auditConsumptionTicket(MemberConsumptionTicketAuditParam param);

    void editConsumptionTicket(MemberConsumptionTicketEditParam param);

    MemberScanRecord preNextConsumptionTicketInfo(MemberConsumptionTicketPreNextInfoParam param);
}

