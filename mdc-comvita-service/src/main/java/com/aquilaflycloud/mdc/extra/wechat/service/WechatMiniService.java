package com.aquilaflycloud.mdc.extra.wechat.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.extra.wechat.component.RedisWxRedisOps;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.WxRedisOps;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
            }
        }
        log.info(".======初始化WechatMiniService配置 end======");
    }
}
