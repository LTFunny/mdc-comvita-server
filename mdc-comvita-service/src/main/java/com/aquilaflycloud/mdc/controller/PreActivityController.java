package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.model.pre.PreFlashOrderInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.service.FlashOrderService;
import com.aquilaflycloud.mdc.service.PreActivityService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * PreActivityController
 * @author linkq
 */
@RestController
@Api(tags = "销售活动")
public class PreActivityController {

    @Resource
    private PreActivityService preActivityService;
    @Resource
    private FlashOrderService flashOrderService;
    @ApiOperation(value = "销售活动分页信息", notes = "销售活动分页信息")
//    @PreAuthorize("hasAuthority('mdc:pre:activity:page')")
    @ApiMapping(value = "backend.comvita.pre.activity.page", method = RequestMethod.POST, permission = true)
    public IPage<PreActivityPageResult> page(PreActivityPageParam param) {
        return preActivityService.page(param);
    }

    @ApiOperation(value = "活动新增", notes = "活动新增")
//    @PreAuthorize("hasAuthority('mdc:pre:activity:add')")
    @ApiMapping(value = "backend.comvita.pre.activity.add", method = RequestMethod.POST, permission = true)
    public void add(PreActivityAddParam param) {
        preActivityService.add(param);
    }

    @ApiOperation(value = "活动详情", notes = "活动详情")
//    @PreAuthorize("hasAuthority('mdc:pre:activity:get')")
    @ApiMapping(value = "backend.comvita.pre.activity.get", method = RequestMethod.POST, permission = true)
    public PreActivityDetailResult get(PreActivityGetParam param) {
        return preActivityService.get(param);
    }

    @ApiOperation(value = "活动编辑", notes = "活动编辑")
//    @PreAuthorize("hasAuthority('mdc:pre:activity:update')")
    @ApiMapping(value = "backend.comvita.pre.activity.update", method = RequestMethod.POST, permission = true)
    public void update(PreActivityUpdateParam param) {
        preActivityService.update(param);
    }

    @ApiOperation(value = "活动上架(下架)", notes = "活动上架(下架)")
//    @PreAuthorize("hasAuthority('mdc:pre:activity:state:change')")
    @ApiMapping(value = "backend.comvita.pre.activity.state.change", method = RequestMethod.POST, permission = true)
    public void changeState(PreActivityCancelParam param) {
        preActivityService.changeState(param);
    }

    /**
     * 获取活动概况
     * @param param
     * @return
     */
    @ApiOperation(value = "预售活动概况", notes = "预售活动概况")
//    @PreAuthorize("hasAuthority('mdc:pre:activity:analyse')")
    @ApiMapping(value = "backend.comvita.pre.activity.analyse", method = RequestMethod.POST, permission = true)
    public PreActivityAnalysisResult analyse(PreActivityAnalysisParam param) {
        return preActivityService.analyse(param);
    }

    @ApiOperation(value = "快闪活动概况", notes = "快闪活动概况")
//    @PreAuthorize("hasAuthority('mdc:pre:activity:analyse')")
    @ApiMapping(value = "backend.comvita.flash.statistics.get", method = RequestMethod.POST, permission = true)
    public FlashStatisticsGetResult getFlashStatistics(FlashStatisticsGetParam param) {
        return preActivityService.getFlashStatistics(param);
    }

    @ApiOperation(value = "概况领取明细", notes = "概况领取明细")
    @ApiMapping(value = "backend.comvita.order.info.flash.detailed", method = RequestMethod.POST, permission = true)
    public IPage<PreFlashOrderInfo> getFlashOrderInfoDetailed(FlashPageParam param) {
        return flashOrderService.pageFlashOrderInfo(param);
    }
    @ApiOperation(value = "生成快闪活动二维码", notes = "生成快闪活动二维码")
    @ApiMapping(value = "backend.comvita.pre.qrcode.add", method = RequestMethod.POST, permission = true)
    public void addQrcode(PreQrcodeAddParam param) {
        preActivityService.addQrcode(param);
    }

    @ApiOperation(value = "删除快闪活动二维码", notes = "删除快闪活动二维码")
    @ApiMapping(value = "backend.comvita.pre.qrcode.delete", method = RequestMethod.POST, permission = true)
    public void deleteQrcode(PreQrcodeDeleteParam param) {
        preActivityService.deleteQrcode(param);
    }

    @ApiOperation(value = "(批量)下载快闪活动二维码", notes = "(批量)下载快闪活动二维码")
    @ApiMapping(value = "backend.comvita.pre.qrcode.download", method = RequestMethod.POST, permission = true)
    public void downloadQrcode(PreQrcodeDownloadParam param) {
        preActivityService.downloadQrcode(param);
    }

    @ApiOperation(value = "获取下载快闪活动二维码列表", notes = "获取下载快闪活动二维码列表")
    @ApiMapping(value = "backend.comvita.pre.qrcode.get", method = RequestMethod.POST, permission = true)
    public List<PreActivityQrCodeResult> getQrcode(PreQrcodeGetterParam param) {
        return preActivityService.getQrcode(param);
    }
}
