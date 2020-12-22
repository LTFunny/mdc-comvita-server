package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.member.MemberScanRecord;
import com.aquilaflycloud.mdc.param.member.ConsumeAddParam;
import com.aquilaflycloud.mdc.param.member.MemberConsumptionTicketAddInfoParam;
import com.aquilaflycloud.mdc.param.member.MemberConsumptionTicketPageInfoParam;
import com.aquilaflycloud.mdc.param.member.ScanInfoAddParam;
import com.aquilaflycloud.mdc.result.member.MemberScanResult;
import com.aquilaflycloud.mdc.service.MemberScanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MemberScanApi
 *
 * @author star
 * @date 2020-03-27
 */
@RestController
@Api(tags = "会员消费奖励接口")
public class MemberScanApi {

    @Resource
    private MemberScanService memberScanService;

    @ApiOperation(value = "消费扫码获取奖励", notes = "消费扫码获取奖励")
    @ApiMapping(value = "mdc.scan.info.add", method = RequestMethod.POST)
    public MemberScanResult addScanInfo(ScanInfoAddParam param) {
        return memberScanService.addScanInfo(param);
    }

    @ApiOperation(value = "消费回调获取奖励", notes = "消费扫码获取奖励")
    @ApiMapping(value = "mdc.scan.consume.add", method = RequestMethod.POST, permission = true)
    public void addConsume(ConsumeAddParam param) {
        memberScanService.addConsume(param);
    }

    @ApiOperation(value = "获取拍照积分记录(分页)", notes = "获取拍照积分记录(分页)")
    @ApiMapping(value = "mdc.consumption.ticket.page", method = RequestMethod.POST)
    public IPage<MemberScanRecord> pageInfo(MemberConsumptionTicketPageInfoParam param) {
        return memberScanService.pageInfo(param);
    }

    @ApiOperation(value = "添加拍照积分记录", notes = "添加拍照积分记录")
    @ApiMapping(value = "mdc.consumption.ticket.add", method = RequestMethod.POST)
    public void addConsumptionTicketnfo(MemberConsumptionTicketAddInfoParam param) {
        memberScanService.addConsumptionTicketnfo(param);
    }
}
