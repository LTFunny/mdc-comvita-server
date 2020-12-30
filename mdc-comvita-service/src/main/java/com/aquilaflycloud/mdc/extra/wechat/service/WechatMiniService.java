package com.aquilaflycloud.mdc.extra.wechat.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum;
import com.aquilaflycloud.mdc.extra.wechat.component.RedisWxRedisOps;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.param.wechat.MiniProgramQrCodeUnLimitGetParam;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.AliOssUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.redis.WxRedisOps;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WechatPayService
 *
 * @author star
 * @date 2020/8/6
 */
@Service
@Slf4j
public class WechatMiniService {
    @Resource
    private WechatAuthorSiteMapper wechatAuthorSiteMapper;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private RedissonClient redissonClient;

    private static final Map<String, WxMaService> WX_MA_SERVICE_MAP = new ConcurrentHashMap<>();

    private static final Map<String, WechatAuthorSite> WX_AUTHOR_MAP = new ConcurrentHashMap<>();

    public WxMaService getWxMaServiceByAppId(String appId) {
        WxMaService wxMaService = WX_MA_SERVICE_MAP.get(appId);
        if (wxMaService == null) {
            synchronized (WX_MA_SERVICE_MAP) {
                wxMaService = WX_MA_SERVICE_MAP.get(appId);
                if (wxMaService == null) {
                    WechatAuthorSite site = wechatAuthorSiteMapper.normalSelectOne(Wrappers.<WechatAuthorSite>lambdaQuery()
                            .eq(WechatAuthorSite::getAppId, appId)
                            .eq(WechatAuthorSite::getIsAgent, WhetherEnum.NO)
                    );
                    if (site != null) {
                        WxRedisOps ops = new RedisWxRedisOps(redisTemplate, redissonClient);
                        WxMaRedisBetterConfigImpl config = new WxMaRedisBetterConfigImpl(ops, "");
                        config.setAppid(site.getAppId());
                        config.setSecret(site.getSecret());
                        config.setToken(site.getToken());
                        config.setAesKey(site.getAesKey());
                        config.setMsgDataFormat(site.getMsgDataFormat());
                        wxMaService = new WxMaServiceImpl();
                        wxMaService.setWxMaConfig(config);
                        WX_MA_SERVICE_MAP.put(site.getAppId(), wxMaService);
                    }
                }
            }
        }
        if (wxMaService == null) {
            throw new ServiceException("小程序不存在");
        }
        return wxMaService;
    }

    public WechatAuthorSite getWxAuthorByAppId(String appId) {
        WechatAuthorSite authorSite = WX_AUTHOR_MAP.get(appId);
        if (authorSite == null) {
            synchronized (WX_AUTHOR_MAP) {
                authorSite = WX_AUTHOR_MAP.get(appId);
                if (authorSite == null) {
                    authorSite = wechatAuthorSiteMapper.normalSelectOne(Wrappers.<WechatAuthorSite>lambdaQuery()
                            .eq(WechatAuthorSite::getAppId, appId)
                            .eq(WechatAuthorSite::getSource, SiteSourceEnum.MINIPRO)
                            .eq(WechatAuthorSite::getIsAgent, WhetherEnum.NO)
                    );
                    if (authorSite != null) {
                        WX_AUTHOR_MAP.put(authorSite.getAppId(), authorSite);
                    }
                }
            }
        }
        if (authorSite == null) {
            throw new ServiceException("小程序不存在");
        }
        return authorSite;
    }

    @PostConstruct
    public void init() {
        log.info("======初始化WechatMiniService配置 start======");
        List<WechatAuthorSite> wechatAuthorSiteList = wechatAuthorSiteMapper.normalSelectList(Wrappers.<WechatAuthorSite>lambdaQuery()
                .eq(WechatAuthorSite::getIsAgent, WhetherEnum.NO)
        );
        if (wechatAuthorSiteList.size() > 0) {
            WxRedisOps ops = new RedisWxRedisOps(redisTemplate, redissonClient);
            for (WechatAuthorSite site : wechatAuthorSiteList) {
                WxMaRedisBetterConfigImpl config = new WxMaRedisBetterConfigImpl(ops, "");
                config.setAppid(site.getAppId());
                config.setSecret(site.getSecret());
                config.setToken(site.getToken());
                config.setAesKey(site.getAesKey());
                config.setMsgDataFormat(site.getMsgDataFormat());
                WxMaService wxMaService = new WxMaServiceImpl();
                wxMaService.setWxMaConfig(config);
                WX_MA_SERVICE_MAP.put(site.getAppId(), wxMaService);
                WX_AUTHOR_MAP.put(site.getAppId(), site);
            }
        }
        log.info(".======初始化WechatMiniService配置 end======");
    }

    public String miniCodeUnLimitGet(MiniProgramQrCodeUnLimitGetParam param) {
        try {
            File file = getWxMaServiceByAppId(param.getAppId()).getQrcodeService()
                    .createWxaCodeUnlimit(param.getScene(), param.getPagePath(), param.getWidth(), param.getAutoColor(), param.getLineColor(), param.getIsHyaline());
            //上传文件至服务器并返回url
            try {
                String path = param.getAppId() + "/" + param.getPagePath().replace("/", ".");
                String name = MdcUtil.getSnowflakeIdStr();
                return AliOssUtil.uploadFile(path, StrUtil.appendIfMissing(name, ".png"), new FileInputStream(file), AliOssUtil.MEMBER_STYLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new ServiceException("上传小程序码失败");
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }
}
