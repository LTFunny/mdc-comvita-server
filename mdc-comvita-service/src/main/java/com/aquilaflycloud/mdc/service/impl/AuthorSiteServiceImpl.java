package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.author.SiteSourceEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteStateEnum;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.param.author.AuthorSiteListParam;
import com.aquilaflycloud.mdc.result.author.AuthorSite;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.aquilaflycloud.mdc.service.AuthorSiteService;
import com.aquilaflycloud.mdc.service.SystemTenantConfigService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AuthorSiteServiceImpl
 *
 * @author star
 * @date 2020-04-04
 */
@Service
public class AuthorSiteServiceImpl implements AuthorSiteService {
    @Resource
    private WechatAuthorSiteMapper wechatAuthorSiteMapper;
    @Resource
    private SystemTenantConfigService systemTenantConfigService;

    private List<AuthorSite> getWechatAuthor(AuthorSiteListParam param, com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum source) {
        return wechatAuthorSiteMapper.selectList(Wrappers.<WechatAuthorSite>lambdaQuery()
                .select(WechatAuthorSite::getAppId, WechatAuthorSite::getNickName, WechatAuthorSite::getSource)
                .eq(WechatAuthorSite::getIsShow, WhetherEnum.YES)
                .eq(WechatAuthorSite::getState, SiteStateEnum.AUTHORIZED)
                .eq(source != null, WechatAuthorSite::getSource, source)
                .like(StrUtil.isNotBlank(param.getAppName()), WechatAuthorSite::getNickName, param.getAppName())
                .orderByDesc(WechatAuthorSite::getSource, WechatAuthorSite::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).stream().map(site -> {
            AuthorSite authorSite = new AuthorSite();
            authorSite.setAppId(site.getAppId());
            authorSite.setAppName(site.getNickName());
            switch (site.getSource()) {
                case PUBLIC: {
                    authorSite.setSource(SiteSourceEnum.WECAHATPUBLIC);
                    break;
                }
                case MINIPRO: {
                    authorSite.setSource(SiteSourceEnum.WECAHATMINI);
                    break;
                }
                default:
            }
            return authorSite;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AuthorSite> listAuthor(AuthorSiteListParam param) {
        List<AuthorSite> siteList = new ArrayList<>();
        if (param.getReturnUniversal()) {
            boolean visible = false;
            SystemTenantConfigResult result = systemTenantConfigService.getConfig(TenantConfigTypeEnum.UNIFIEDMEMBER);
            if (result != null && result.getUnifiedMemberConfig() != null) {
                visible = result.getUnifiedMemberConfig().getUnified();
            } else {
                result = systemTenantConfigService.getConfig(TenantConfigTypeEnum.AUTHORUNIVERSAL);
                if (result != null && result.getAuthorUniversalConfig() != null) {
                    visible = result.getAuthorUniversalConfig().getVisible();
                }
            }
            if (visible) {
                AuthorSite authorSite = new AuthorSite();
                authorSite.setAppId(MdcConstant.UNIVERSAL_APP_ID);
                authorSite.setAppName("通用应用");
                authorSite.setSource(SiteSourceEnum.UNIVERSAL);
                siteList.add(authorSite);
            }
        }
        if (param.getType() == AuthorSiteListParam.AuthorType.WECHAT) {
            siteList.addAll(getWechatAuthor(param, null));
        } else {
            if (param.getSource() == null) {
                List<AuthorSite> wechatList = getWechatAuthor(param, null);
                siteList.addAll(wechatList);
            } else {
                List<AuthorSite> list = new ArrayList<>();
                com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum wechatSource;
                switch (param.getSource()) {
                    case WECAHATPUBLIC: {
                        wechatSource = com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum.PUBLIC;
                        list = getWechatAuthor(param, wechatSource);
                        break;
                    }
                    case WECAHATMINI: {
                        wechatSource = com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum.MINIPRO;
                        list = getWechatAuthor(param, wechatSource);
                        break;
                    }
                    default:
                }
                siteList.addAll(list);
            }
        }
        return siteList;
    }
}
