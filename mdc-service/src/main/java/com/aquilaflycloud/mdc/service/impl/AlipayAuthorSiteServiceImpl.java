package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.alipay.SiteSourceEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteStateEnum;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayOpenPlatformService;
import com.aquilaflycloud.mdc.mapper.AlipayAuthorSiteMapper;
import com.aquilaflycloud.mdc.model.alipay.AlipayAuthorSite;
import com.aquilaflycloud.mdc.param.alipay.*;
import com.aquilaflycloud.mdc.result.alipay.PublicInfo;
import com.aquilaflycloud.mdc.service.AlipayAuthorSiteService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * WechatAuthorSiteServiceImpl
 *
 * @author star
 * @date 2019-10-08
 */
@Service
public class AlipayAuthorSiteServiceImpl implements AlipayAuthorSiteService {
    @Resource
    private AlipayAuthorSiteMapper alipayAuthorSiteMapper;
    @Resource
    private AlipayOpenPlatformService alipayOpenPlatformService;

    @Override
    public IPage<AlipayAuthorSite> pageAuthor(AlipayAuthorSitePageParam param) {
        return alipayAuthorSiteMapper.selectPage(param.page(), Wrappers.<AlipayAuthorSite>lambdaQuery()
                .eq(AlipayAuthorSite::getState, SiteStateEnum.AUTHORIZED)
                .like(StrUtil.isNotBlank(param.getAppName()), AlipayAuthorSite::getAppName, param.getAppName())
                .eq(param.getSource() != null, AlipayAuthorSite::getSource, param.getSource())
                .eq(param.getIsAgent() != null, AlipayAuthorSite::getIsAgent, param.getIsAgent())
                .orderByDesc(AlipayAuthorSite::getCreateTime));
    }

    @Override
    public BaseResult<String> getPreAuthUrl(AlipayPreAuthUrlGetParam param) {
        String redirectUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                .append("/rest/").append(MdcUtil.getServerName()).append("/alipayAuthorSiteCodeHandler").toString();
        //生成state把租户信息放进缓存
        String state = MdcUtil.getSnowflakeIdStr();
        RedisUtil.valueRedis().set(state, new JSONObject().set("tenantId", MdcUtil.getCurrentTenantId())
                .set("subTenantId", MdcUtil.getCurrentSubTenantId())
                .set("siteSource", param.getApplicationType()), 1, TimeUnit.HOURS);
        String appId = alipayOpenPlatformService.getAlipayOpenPlatform().getComponentAppid();
        String applicationType = param.getApplicationType().name();
        String url = String.format("https://openauth.alipay.com/oauth2/appToAppBatchAuth.htm?app_id=%s&application_type=%s&redirect_uri=%s&state=%s",
                appId, applicationType, redirectUrl, state);
        return new BaseResult<String>().setResult(url);
    }

    @Override
    public void addAuthor(AlipayAuthorSiteAddParam param) {
        AlipayAuthorSite site = alipayAuthorSiteMapper.selectOne(Wrappers.<AlipayAuthorSite>lambdaQuery()
                .eq(AlipayAuthorSite::getAppId, param.getAlipayAppId())
                .eq(AlipayAuthorSite::getIsAgent, WhetherEnum.NO)
        );
        int count;
        if (site != null) {
            if (site.getState() == SiteStateEnum.AUTHORIZED) {
                throw new ServiceException("该支付宝应用已存在");
            } else {
                AlipayAuthorSite update = new AlipayAuthorSite();
                BeanUtil.copyProperties(param, update);
                update.setId(site.getId());
                update.setState(SiteStateEnum.AUTHORIZED);
                count = alipayAuthorSiteMapper.updateById(update);
                BeanUtil.copyProperties(update, site, CopyOptions.create().ignoreNullValue());
            }
        } else {
            site = new AlipayAuthorSite();
            BeanUtil.copyProperties(param, site);
            site.setAppId(param.getAlipayAppId());
            site.setIsAgent(WhetherEnum.NO);
            site.setState(SiteStateEnum.AUTHORIZED);
            count = alipayAuthorSiteMapper.insert(site);
        }
        if (count <= 0) {
            throw new ServiceException("新增应用失败");
        }
        alipayOpenPlatformService.refreshAlipayAuthorSite(site);
    }

    @Override
    public void editAuthor(AlipayAuthorSiteEditParam param) {
        AlipayAuthorSite site = alipayAuthorSiteMapper.selectById(param.getId());
        if (site == null) {
            throw new ServiceException("该授权应用不存在");
        }
        if (site.getState() == SiteStateEnum.CANCELAUTHORIZED) {
            throw new ServiceException("该授权应用已" + site.getState().getName());
        }
        AlipayAuthorSite update = new AlipayAuthorSite();
        BeanUtil.copyProperties(param, update);
        if (site.getIsAgent() == WhetherEnum.YES) {
            update.setMerchantPrivateKey(null);
            update.setAlipayPublicKey(null);
            update.setSource(null);
        }
        int count = alipayAuthorSiteMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑应用失败");
        }
        BeanUtil.copyProperties(update, site, CopyOptions.create().ignoreNullValue());
        alipayOpenPlatformService.refreshAlipayAuthorSite(site);
    }

    @Override
    public void deleteAuthor(AlipayAuthorSiteGetParam param) {
        AlipayAuthorSite site = alipayAuthorSiteMapper.selectById(param.getId());
        if (site == null) {
            throw new ServiceException("该授权应用不存在");
        }
        if (site.getState() == SiteStateEnum.CANCELAUTHORIZED) {
            throw new ServiceException("该授权应用已" + site.getState().getName());
        }
        AlipayAuthorSite update = new AlipayAuthorSite();
        update.setState(SiteStateEnum.CANCELAUTHORIZED);
        int count = alipayAuthorSiteMapper.update(update, Wrappers.<AlipayAuthorSite>lambdaQuery()
                .eq(AlipayAuthorSite::getId, param.getId())
                .eq(AlipayAuthorSite::getIsAgent, WhetherEnum.NO)
                .eq(AlipayAuthorSite::getState, SiteStateEnum.AUTHORIZED)
        );
        if (count <= 0) {
            throw new ServiceException("删除授权应用失败");
        }
        alipayOpenPlatformService.cancelAlipayAuthorSite(site.getAppId());
    }

    @Override
    @Transactional
    public void updatePublicInfo(AlipayAuthorSiteGetParam param) {
        AlipayAuthorSite site = alipayAuthorSiteMapper.selectById(param.getId());
        if (site == null) {
            throw new ServiceException("该授权应用不存在");
        }
        if (site.getSource() != SiteSourceEnum.PUBLIC) {
            throw new ServiceException("不是生活号不能获取信息");
        }
        if (site.getState() == SiteStateEnum.CANCELAUTHORIZED) {
            throw new ServiceException("该授权应用已" + site.getState().getName());
        }
        AlipayAuthorSite update = new AlipayAuthorSite();
        PublicInfo publicInfo = alipayOpenPlatformService.getPublicInfo(site.getAppId(), site.getAuthToken());
        update.setId(param.getId());
        update.setAppName(publicInfo.getAppName());
        update.setAppContent(JSONUtil.toJsonStr(publicInfo));
        int count = alipayAuthorSiteMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("更新失败");
        }
    }

    @Override
    public String getOauth2Url(AlipayOauth2UrlGetParam param) {
        String url = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME)).append("/rest/").append(MdcUtil.getServerName()).toString();
        String appId = StrUtil.emptyIfNull(param.getAppId());
        if (param.getEncrypt()) {
            Long encryptParam = MdcUtil.setCryption("url=" + URLUtil.encode(param.getUrl())
                    + ";scopeType=" + param.getScopeType().name() + ";appId=" + appId);
            return String.format(url + "/alipayAuthorSiteOauthUrl?encryptParam=%s",
                    encryptParam);
        } else {
            return String.format(url + "/alipayAuthorSiteOauthUrl?url=%s&scopeType=%s&appId=%s",
                    URLUtil.encode(param.getUrl()), param.getScopeType().name(), appId);
        }
    }

    @Override
    public List<AlipayAuthorSite> listAuthor() {
        return alipayAuthorSiteMapper.normalSelectList(Wrappers.<AlipayAuthorSite>lambdaQuery()
                .eq(AlipayAuthorSite::getState, SiteStateEnum.AUTHORIZED)
                .isNotNull(AlipayAuthorSite::getUmengAppKey)
        );
    }
}
