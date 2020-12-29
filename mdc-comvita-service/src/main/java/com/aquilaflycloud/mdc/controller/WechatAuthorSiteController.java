package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSiteQrcodeMsg;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSiteTemplate;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.WechatUserAnalysisSumResult;
import com.aquilaflycloud.mdc.service.WechatAuthorSiteService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * WechatAuthorSiteController
 *
 * @author star
 * @date 2019-10-08
 */
@Api(tags = "微信授权接口")
@RestController
public class WechatAuthorSiteController {

    @Resource
    private WechatAuthorSiteService wechatAuthorSiteService;

    @ApiOperation(value = "查询授权号列表(分页)", notes = "查询授权号列表(分页)")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteList')")
    @ApiMapping(value = "backend.comvita.wechat.author.page", method = RequestMethod.POST, permission = true)
    public IPage<WechatAuthorSite> pageAuthor(WechatAuthorSitePageParam param) {
        return wechatAuthorSiteService.pageAuthor(param);
    }

    @ApiOperation(value = "获取授权页面URL", notes = "获取授权页面URL,根据参数生成Oauth2链接,通过此链接可获取微信openId等参数")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.oauth2Url.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getOauth2Url(Oauth2UrlGetParam param) {
        return new BaseResult<String>().setResult(wechatAuthorSiteService.getOauth2Url(param));
    }

    @ApiOperation(value = "查询公众号扫码回复内容列表(分页)", notes = "查询公众号扫码回复内容列表(分页)")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.qrCodeMsg.page", method = RequestMethod.POST, permission = true)
    public IPage<WechatAuthorSiteQrcodeMsg> pageQrCodeMsg(QrCodeMsgPageParam param) {
        return wechatAuthorSiteService.pageQrCodeMsg(param);
    }

    @ApiOperation(value = "编辑公众号扫码回复内容", notes = "编辑公众号扫码回复内容")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.qrCodeMsg.edit", method = RequestMethod.POST, permission = true)
    public void editQrCodeMsg(QrCodeMsgEditParam param) {
        wechatAuthorSiteService.editQrCodeMsg(param);
    }

    @ApiOperation(value = "获取公众号粉丝分析统计", notes = "获取公众号粉丝分析统计")
    @PreAuthorize("hasAuthority('mdc:wechat:userAnalysisGet')")
    @ApiMapping(value = "backend.comvita.wechat.userAnalysis.get", method = RequestMethod.POST, permission = true)
    public List<WechatUserAnalysisSumResult> getUserAnalysis(UserAnalysisSumGetParam param) {
        return wechatAuthorSiteService.getWechatUserAnalysisSum(param);
    }
}
