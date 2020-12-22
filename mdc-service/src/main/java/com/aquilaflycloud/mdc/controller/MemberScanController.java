package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.member.MemberScanRecord;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.service.MemberScanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberScanController
 *
 * @author zengqingjie
 * @date 2020-06-28
 */
@RestController
@Api(tags = "扫码积分和拍照积分")
public class MemberScanController {
    @Resource
    private MemberScanService memberScanService;

    @ApiOperation(value = "查询拍照积分信息(分页)", notes = "查询拍照积分信息(分页)")
    @PreAuthorize("hasAuthority('mdc:memberTicket:page')")
    @ApiMapping(value = "backend.mdc.member.ticket.page", method = RequestMethod.POST, permission = true)
    public IPage<MemberScanRecord> pageConsumptionTicket(MemberConsumptionTicketPageParam param) {
        return memberScanService.pageConsumptionTicket(param);
    }

    @ApiOperation(value = "查询拍照积分详情", notes = "查询拍照积分详情")
    @PreAuthorize("hasAuthority('mdc:memberTicket:get')")
    @ApiMapping(value = "backend.mdc.member.ticket.get", method = RequestMethod.POST, permission = true)
    public MemberScanRecord getConsumptionTicket(MemberConsumptionTicketGetParam param) {
        return memberScanService.getConsumptionTicket(param);
    }

    @ApiOperation(value = "审核拍照积分", notes = "审核拍照积分")
    @PreAuthorize("hasAuthority('mdc:memberTicket:audit')")
    @ApiMapping(value = "backend.mdc.member.ticket.audit", method = RequestMethod.POST, permission = true)
    public void auditConsumptionTicket(MemberConsumptionTicketAuditParam param) {
        memberScanService.auditConsumptionTicket(param);
    }

    @ApiOperation(value = "编辑金额或店铺信息", notes = "编辑金额或店铺信息")
    @PreAuthorize("hasAuthority('mdc:memberTicket:edit')")
    @ApiMapping(value = "backend.mdc.member.ticket.edit", method = RequestMethod.POST, permission = true)
    public void editConsumptionTicket(MemberConsumptionTicketEditParam param) {
        memberScanService.editConsumptionTicket(param);
    }

    @ApiOperation(value = "上下条获取", notes = "上下条获取")
    @PreAuthorize("hasAuthority('mdc:memberTicket:page')")
    @ApiMapping(value = "backend.mdc.member.ticket.preNextInfo", method = RequestMethod.POST, permission = true)
    public MemberScanRecord preNextConsumptionTicketInfo(MemberConsumptionTicketPreNextInfoParam param) {
        return memberScanService.preNextConsumptionTicketInfo(param);
    }
}
