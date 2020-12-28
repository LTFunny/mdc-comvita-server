package com.aquilaflycloud.mdc.extra.wechat.util;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.util.SpringUtil;
import com.gitee.sop.servercommon.exception.ServiceException;

public class WechatFactoryUtil {
    public static <T> T getService(String appId, String method, String funcNum, Object... args) {
        WechatOpenPlatformService wechatOpenPlatformService = SpringUtil.getBean(WechatOpenPlatformService.class);
        WechatMiniService wechatMiniService = SpringUtil.getBean(WechatMiniService.class);
        WechatAuthorSite site = wechatOpenPlatformService.getWechatAuthorSiteByAppId(appId);
        if (site.getIsAgent() == WhetherEnum.YES && (StrUtil.isBlank(funcNum)
                || (JSONUtil.isJsonArray(site.getFuncInfo()) && JSONUtil.parseArray(site.getFuncInfo()).contains(funcNum)))) {
            return ReflectUtil.invoke(wechatOpenPlatformService.getWxOpenComponentService().getWxMaServiceByAppid(appId), method, args);
        } else if (site.getIsAgent() == WhetherEnum.NO || wechatOpenPlatformService.getWechatAuthorSiteByAppId(appId, false) != null) {
            return ReflectUtil.invoke(wechatMiniService.getWxMaServiceByAppId(appId), method, args);
        } else {
            throw new ServiceException("小程序接口权限不足");
        }
    }
}
