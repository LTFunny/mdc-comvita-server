package com.aquilaflycloud.mdc.service;

import cn.hutool.core.date.DateTime;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSiteQrcodeMsg;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSiteTemplate;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.WechatUserAnalysisSumResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import me.chanjar.weixin.common.bean.WxJsapiSignature;

import java.util.List;

/**
 * WechatAuthorSiteService
 *
 * @author star
 * @date 2019-10-08
 */
public interface WechatAuthorSiteService {

    IPage<WechatAuthorSite> pageAuthor(WechatAuthorSitePageParam param);

    BaseResult<String> getPreAuthUrl(PreAuthUrlGetParam param);

    void updateAuthor(WechatAuthorSiteGetParam param);

    List<WechatAuthorSiteTemplate> loadTemplateList(WechatAuthorSiteGetParam param);

    WechatAuthorSiteTemplate getWechatTemplateByType(String appId, String type);

    String getOauth2Url(Oauth2UrlGetParam param);

    WxJsapiSignature getJsapiSign(JsapiSignGetParam param);

    IPage<WechatAuthorSiteQrcodeMsg> pageQrCodeMsg(QrCodeMsgPageParam param);

    void addQrCodeMsg(QrCodeMsgAddParam param);

    void editQrCodeMsg(QrCodeMsgEditParam param);

    List<WechatUserAnalysisSumResult> getWechatUserAnalysisSum(UserAnalysisSumGetParam param);

    BaseResult<String> addQrCodeMsgForScan(QrCodeMsgForScanAddParam param);

    void addWechatUserAnalysis(String appId, DateTime begin, DateTime end);

    void addWechatMiniAnalysis(String appId, DateTime begin, DateTime end);
}

