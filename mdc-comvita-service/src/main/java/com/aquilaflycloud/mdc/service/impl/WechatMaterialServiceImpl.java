package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.param.wechat.MaterialGetParam;
import com.aquilaflycloud.mdc.param.wechat.MaterialListParam;
import com.aquilaflycloud.mdc.service.WechatMaterialService;
import com.aquilaflycloud.util.AliOssUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.sop.servercommon.exception.ServiceException;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.material.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * WechatAuthorSiteServiceImpl
 *
 * @author star
 * @date 2019-10-08
 */
@Service
public class WechatMaterialServiceImpl implements WechatMaterialService {
    @Resource
    private WechatOpenPlatformService wechatOpenPlatformService;

    @Override
    public IPage<WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem> pageMaterialNews(MaterialListParam param) {
        try {
            int start = Convert.toInt(param.page().offset());
            int limit = Convert.toInt(param.getPageSize());
            WxMpMaterialNewsBatchGetResult newsResult = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMpServiceByAppid(param.getAppId()).getMaterialService()
                    .materialNewsBatchGet(start, limit);
            for (WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem item : newsResult.getItems()) {
                for (WxMpNewsArticle article : item.getContent().getArticles()) {
                    if (StrUtil.isNotBlank(article.getThumbMediaId()) && StrUtil.isNotBlank(article.getThumbUrl())) {
                        String imgUrl = getWechatMediaUrl(param.getAppId(), article.getThumbMediaId(), article.getThumbUrl());
                        article.setThumbUrl(imgUrl);
                    }
                    article.setContent(null);
                }
            }
            IPage page = new Page(param.getPageNum(), param.getPageSize(), newsResult.getTotalCount());
            page.setRecords(newsResult.getItems());
            return page;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public IPage<WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem> pageMaterial(MaterialListParam param) {
        try {
            int start = Convert.toInt(param.page().offset());
            int limit = Convert.toInt(param.getPageSize());
            WxMpMaterialFileBatchGetResult fileResult = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMpServiceByAppid(param.getAppId()).getMaterialService()
                    .materialFileBatchGet(param.getType().name().toLowerCase(), start, limit);
            for (WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem item : fileResult.getItems()) {
                if (StrUtil.isNotBlank(item.getMediaId()) && StrUtil.isNotBlank(item.getUrl())) {
                    String imgUrl = getWechatMediaUrl(param.getAppId(), item.getMediaId(), item.getUrl());
                    item.setUrl(imgUrl);
                }
            }
            IPage page = new Page(param.getPageNum(), param.getPageSize(), fileResult.getTotalCount());
            page.setRecords(fileResult.getItems());
            return page;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public WxMpMaterialNews getMaterialNews(MaterialGetParam param) {
        try {
            WxMpMaterialNews materialNews = wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMpServiceByAppid(param.getAppId()).getMaterialService().materialNewsInfo(param.getMediaId());
            for (WxMpNewsArticle article : materialNews.getArticles()) {
                if (StrUtil.isNotBlank(article.getThumbMediaId()) && StrUtil.isNotBlank(article.getThumbUrl())) {
                    String imgUrl = getWechatMediaUrl(param.getAppId(), article.getThumbMediaId(), article.getThumbUrl());
                    article.setThumbUrl(imgUrl);
                }
                article.setContent(null);
            }
            return materialNews;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public WxMpMaterialVideoInfoResult getMaterialVideo(MaterialGetParam param) {
        try {
            return wechatOpenPlatformService.getWxOpenComponentService()
                    .getWxMpServiceByAppid(param.getAppId()).getMaterialService().materialVideoInfo(param.getMediaId());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    @Override
    public BaseResult<String> getMaterial(MaterialGetParam param) {
        String url = getWechatMediaUrl(param.getAppId(), param.getMediaId(), null);
        return new BaseResult<String>().setResult(url);
    }

    //微信图片链接转oss链接(解决微信防外链导致显示不了图片)
    private String getWechatMediaUrl(String appId, String mediaId, String mediaUrl) {
        String ext = "jpeg";
        if (mediaUrl != null && mediaUrl.contains("?")) {
            String[] s = mediaUrl.split("[?]");
            ext = s[1].split("=")[1];
        }
        String key = "getUploadUrl" + mediaId;
        String finalExt = ext;
        return RedisUtil.valueGet(key, 7, () -> {
            try {
                InputStream is = wechatOpenPlatformService.getWxOpenComponentService().getWxMpServiceByAppid(appId).getMaterialService().materialImageOrVoiceDownload(mediaId);
                return AliOssUtil.uploadExistFile(mediaId + "." + finalExt, is);
            } catch (WxErrorException e) {
                e.printStackTrace();
                throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
            }
        });
    }
}
