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

    @ApiOperation(value = "获取预授权链接", notes = "获取预授权链接,可供第三方授权微信公众号或小程序")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.preAuthUrl.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getPreAuthUrl(PreAuthUrlGetParam param) {
        return wechatAuthorSiteService.getPreAuthUrl(param);
    }

    @ApiOperation(value = "更新第三方授权账号", notes = "更新第三方授权账号")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.author.update", method = RequestMethod.POST, permission = true)
    public void updateAuthor(WechatAuthorSiteGetParam param) {
        wechatAuthorSiteService.updateAuthor(param);
    }

    @ApiOperation(value = "加载公众号模板消息列表", notes = "加载公众号模板消息列表,保存并返回")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.templateList.load", method = RequestMethod.POST, permission = true)
    public List<WechatAuthorSiteTemplate> loadTemplateList(WechatAuthorSiteGetParam param) {
        return wechatAuthorSiteService.loadTemplateList(param);
    }

    @ApiOperation(value = "获取授权页面URL", notes = "获取授权页面URL,根据参数生成Oauth2链接,通过此链接可获取微信openId等参数")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.oauth2Url.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getOauth2Url(Oauth2UrlGetParam param) {
        return new BaseResult<String>().setResult(wechatAuthorSiteService.getOauth2Url(param));
    }

    @ApiOperation(value = "获取jssdk签名参数", notes = "获取jssdk签名参数")
    @PreAuthorize("hasAuthority('mdc:wechat:jsSignGet')")
    @ApiMapping(value = "backend.comvita.wechat.jsapiSign.get", method = RequestMethod.POST, permission = true)
    public WxJsapiSignature getJsapiSign(JsapiSignGetParam param) {
        return wechatAuthorSiteService.getJsapiSign(param);
    }

    @ApiOperation(value = "查询公众号扫码回复内容列表(分页)", notes = "查询公众号扫码回复内容列表(分页)")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.qrCodeMsg.page", method = RequestMethod.POST, permission = true)
    public IPage<WechatAuthorSiteQrcodeMsg> pageQrCodeMsg(QrCodeMsgPageParam param) {
        return wechatAuthorSiteService.pageQrCodeMsg(param);
    }

    @ApiOperation(value = "新增公众号扫码回复内容", notes = "新增公众号扫码回复内容")
    @PreAuthorize("hasAuthority('mdc:wechat:authorSiteSave')")
    @ApiMapping(value = "backend.comvita.wechat.qrCodeMsg.add", method = RequestMethod.POST, permission = true)
    public void addQrCodeMsg(QrCodeMsgAddParam param) {
        wechatAuthorSiteService.addQrCodeMsg(param);
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
