package com.aquilaflycloud.mdc.extra.wechat.service;

import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.wechat.*;
import com.aquilaflycloud.mdc.extra.wechat.component.WxMessageInRedisDuplicateChecker;
import com.aquilaflycloud.mdc.extra.wechat.component.WxOpenInRedisConfigStorage;
import com.aquilaflycloud.mdc.extra.wechat.handler.*;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteMapper;
import com.aquilaflycloud.mdc.mapper.WechatOpenPlatformMapper;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.model.wechat.WechatOpenPlatform;
import com.aquilaflycloud.mdc.param.wechat.MiniProgramQrCodeUnLimitGetParam;
import com.aquilaflycloud.mdc.param.wechat.MiniProgramUniformMsgSentParam;
import com.aquilaflycloud.mdc.param.wechat.TemplateMsgSendParam;
import com.aquilaflycloud.mdc.service.MemberService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.AliOssUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizationInfo;
import me.chanjar.weixin.open.bean.auth.WxOpenAuthorizerInfo;
import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerInfoResult;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.EventType.*;
import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType.CLICK;
import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType.VIEW;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.CustomerService.*;
import static me.chanjar.weixin.mp.constant.WxMpEventConstants.POI_CHECK_NOTIFY;

/**
 * @author star
 */
@Service
@Slf4j
public class WechatOpenPlatformService extends WxOpenServiceImpl {
    @Resource
    protected LogHandler logHandler;

    @Resource
    protected NullHandler nullHandler;

    @Resource
    protected KfSessionHandler kfSessionHandler;

    @Resource
    protected StoreCheckNotifyHandler storeCheckNotifyHandler;

    @Resource
    private LocationHandler locationHandler;

    @Resource
    private MenuHandler menuHandler;

    @Resource
    private MsgHandler msgHandler;

    @Resource
    private UnsubscribeHandler unsubscribeHandler;

    @Resource
    private SubscribeHandler subscribeHandler;

    @Resource
    private ScanHandler scanHandler;

    @Resource
    private MassSendJobHandler massSendJobHandler;

    @Resource
    private WeappAuditHandler weappAuditHandler;

    @Resource
    private WechatOpenPlatformMapper wechatOpenPlatformMapper;

    @Resource
    private WechatAuthorSiteMapper wechatAuthorSiteMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private MemberService memberService;

    private WxOpenMessageRouter router;

    private WechatOpenComponentService wechatOpenComponentService = new WechatOpenComponentService(this);

    @Override
    public WxOpenComponentService getWxOpenComponentService() {
        return wechatOpenComponentService;
    }

    @PostConstruct
    public void init() {
        log.info("======初始化WechatOpenPlatformService配置 start======");
        WechatOpenPlatform wechatOpenPlatform = wechatOpenPlatformMapper.selectOne(null);
        if (wechatOpenPlatform != null) {
            WxOpenInRedisConfigStorage configStorage = new WxOpenInRedisConfigStorage(redisTemplate);
            configStorage.setComponentAppId(wechatOpenPlatform.getComponentAppid());
            configStorage.setComponentToken(wechatOpenPlatform.getVerifyToken());
            configStorage.setComponentAesKey(wechatOpenPlatform.getEncodingAesKey());
            configStorage.setComponentAppSecret(wechatOpenPlatform.getComponentAppsecret());
            configStorage.setComponentVerifyTicket(wechatOpenPlatform.getComponentVerifyTicket());
            configStorage.updateComponentAccessToken(wechatOpenPlatform.getComponentAccessToken(), 7500);
            List<WechatAuthorSite> list = wechatAuthorSiteMapper.normalSelectList(Wrappers.<WechatAuthorSite>lambdaQuery()
                    .eq(WechatAuthorSite::getComponentAppid, wechatOpenPlatform.getComponentAppid())
                    .eq(WechatAuthorSite::getState, SiteStateEnum.AUTHORIZED));
            for (WechatAuthorSite site : list) {
                if (configStorage.isAuthorizerAccessTokenExpired(site.getAppId())) {
                    configStorage.updateAuthorizerAccessToken(site.getAppId(), site.getAccessToken(), 7500);
                }
                if (configStorage.isJsapiTicketExpired(site.getAppId())) {
                    configStorage.updateJsapiTicket(site.getAppId(), site.getJsapiTicket(), 7500);
                }
                configStorage.setAuthorizerRefreshToken(site.getAppId(), site.getRefreshToken());
            }
            super.setWxOpenConfigStorage(configStorage);
            refreshRouter();
            log.info(".======初始化WechatOpenPlatformService配置 end======");
        } else {
            log.info(".======初始化WechatOpenPlatformService配置 error======");
        }
    }

    private void refreshRouter() {
        final WxOpenMessageRouter newRouter = new WxOpenMessageRouter(this);

        // 记录所有事件的日志
        newRouter.rule().handler(logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
                .handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
                .handler(kfSessionHandler).end();
        newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
                .handler(kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY)
                .handler(storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(CLICK).handler(menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(EVENT).event(VIEW).handler(nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(subscribeHandler).end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(EVENT).event(LOCATION).handler(locationHandler).end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(SCAN).handler(scanHandler).end();

        // 群发推送完成后事件
        newRouter.rule().async(false).msgType(EVENT).event(MASS_SEND_JOB_FINISH).handler(massSendJobHandler).end();

        // 小程序代码审核成功事件
        newRouter.rule().async(false).msgType(EVENT).event(WEAPP_AUDIT_SUCCESS).handler(weappAuditHandler).end();

        // 小程序代码审核失败事件
        newRouter.rule().async(false).msgType(EVENT).event(WEAPP_AUDIT_FAIL).handler(weappAuditHandler).end();

        // 默认
        newRouter.rule().async(false).handler(msgHandler).end();

        newRouter.setMessageDuplicateChecker(new WxMessageInRedisDuplicateChecker());

        router = newRouter;
    }

    public WxMpXmlOutMessage route(WxMpXmlMessage message, Map<String, Object> context, String appId) {
        try {
            return router.route(message, context, appId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /*==================== 相关持久化操作(数据库) start ====================*/

    public void saveWeChatAuthorSite(String state, String authCode, Boolean canNoTenant) {
        //获取租户信息,租户信息不存在保存失败
        JSONObject tenantInfo = RedisUtil.<JSONObject>valueRedis().get(state);
        if (!canNoTenant && tenantInfo == null) {
            return;
        }
        try {
            WxOpenQueryAuthResult queryAuthResult = getWxOpenComponentService().getQueryAuth(authCode);
            WxOpenAuthorizationInfo authorizationInfo = queryAuthResult.getAuthorizationInfo();
            WechatAuthorSite wechatAuthorSite = getAuthorizerInfo(authorizationInfo.getAuthorizerAppid());
            WechatAuthorSite site = wechatAuthorSiteMapper.normalSelectOne(Wrappers.<WechatAuthorSite>lambdaQuery()
                    .eq(WechatAuthorSite::getAppId, authorizationInfo.getAuthorizerAppid())
                    .eq(WechatAuthorSite::getIsAgent, WhetherEnum.YES)
            );
            if (site == null) {
                site = new WechatAuthorSite();
                site.setIsAgent(WhetherEnum.YES);
                site.setIsShow(WhetherEnum.YES);
                site.setComponentAppid(getWxOpenConfigStorage().getComponentAppId());
                site.setAppId(authorizationInfo.getAuthorizerAppid());
                site.setAuthorizationCode(authCode);
                site.setAccessToken(authorizationInfo.getAuthorizerAccessToken());
                site.setRefreshToken(authorizationInfo.getAuthorizerRefreshToken());
                site.setFuncInfo(JSONUtil.toJsonStr(authorizationInfo.getFuncInfo()));
                site.setTenantId(tenantInfo.getLong("tenantId"));
                site.setSubTenantId(tenantInfo.getLong("subTenantId"));
                BeanUtil.copyProperties(wechatAuthorSite, site, CopyOptions.create().ignoreNullValue());
                if (site.getSubTenantId() == null) {
                    site.setSiteType(SiteSiteTypeEnum.MALL);
                } else {
                    site.setSiteType(SiteSiteTypeEnum.SHOP);
                }
                site.setState(SiteStateEnum.AUTHORIZED);
                wechatAuthorSiteMapper.normalInsert(site);
                if (site.getSource() == SiteSourceEnum.PUBLIC) {
                    memberService.batchAddSubscribeMember(site.getAppId());
                }
            } else {
                WechatAuthorSite update = new WechatAuthorSite();
                BeanUtil.copyProperties(wechatAuthorSite, update, CopyOptions.create().ignoreNullValue());
                update.setId(site.getId());
                update.setAccessToken(authorizationInfo.getAuthorizerAccessToken());
                update.setRefreshToken(authorizationInfo.getAuthorizerRefreshToken());
                update.setFuncInfo(JSONUtil.toJsonStr(authorizationInfo.getFuncInfo()));
                update.setState(SiteStateEnum.AUTHORIZED);
                wechatAuthorSiteMapper.normalUpdateById(update);
                RedisUtil.redis().delete("getAuthorSite" + site.getAppId());
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    void deleteWeChatAuthorSite(String appId) {
        WechatAuthorSite authorSite = getWechatAuthorSiteByAppId(appId);
        WechatAuthorSite site = new WechatAuthorSite();
        site.setId(authorSite.getId());
        site.setState(SiteStateEnum.CANCELAUTHORIZED);
        wechatAuthorSiteMapper.normalUpdateById(site);
        RedisUtil.redis().delete("getAuthorSite" + appId);
    }

    public WechatAuthorSite getAuthorizerInfo(String appId) {
        try {
            WxOpenAuthorizerInfoResult authorizerInfoResult = getWxOpenComponentService().getAuthorizerInfo(appId);
            WxOpenAuthorizationInfo authorizationInfo = authorizerInfoResult.getAuthorizationInfo();
            WxOpenAuthorizerInfo authorizerInfo = authorizerInfoResult.getAuthorizerInfo();
            WechatAuthorSite authorSite = new WechatAuthorSite();
            authorSite.setAppId(appId);
            authorSite.setNickName(authorizerInfo.getNickName());
            authorSite.setHeadImg(authorizerInfo.getHeadImg());
            authorSite.setUserName(authorizerInfo.getUserName());
            authorSite.setServiceTypeInfo(EnumUtil.likeValueOf(SiteServiceTypeEnum.class, authorizerInfo.getServiceTypeInfo()));
            authorSite.setVerifyTypeInfo(EnumUtil.likeValueOf(SiteVerifyTypeEnum.class, authorizerInfo.getVerifyTypeInfo()));
            authorSite.setPrincipalName(authorizerInfo.getPrincipalName());
            authorSite.setBusinessInfo(JSONUtil.toJsonStr(authorizerInfo.getBusinessInfo()));
            authorSite.setAlias(authorizerInfo.getAlias());
            authorSite.setQrcodeUrl(authorizerInfo.getQrcodeUrl());
            if (authorizerInfo.getMiniProgramInfo() != null) {
                authorSite.setMiniprograminfo(JSONUtil.toJsonStr(authorizerInfo.getMiniProgramInfo()));
                authorSite.setSource(SiteSourceEnum.MINIPRO);
            } else {
                authorSite.setSource(SiteSourceEnum.PUBLIC);
            }
            authorSite.setFuncInfo(JSONUtil.toJsonStr(authorizationInfo.getFuncInfo()));
            return authorSite;
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    public WechatAuthorSite getWechatAuthorSiteByAppId(String appId, Boolean... isAgent) {
        WhetherEnum whether = null;
        if (isAgent.length > 0) {
            if (isAgent[0]) {
                whether = WhetherEnum.YES;
            } else {
                whether = WhetherEnum.NO;
            }
        }
        WhetherEnum finalWhether = whether;
        WechatAuthorSite site = RedisUtil.valueGet("getAuthorSite" + appId + finalWhether, 7,
                () -> wechatAuthorSiteMapper.normalSelectOne(Wrappers.<WechatAuthorSite>lambdaQuery()
                        .eq(WechatAuthorSite::getAppId, appId)
                        .eq(finalWhether != null, WechatAuthorSite::getIsAgent, finalWhether)
                        .last("limit 1")
                ));
        if (site == null) {
            throw new ServiceException("授权号不存在");
        }
        return site;
    }

    void updateComponentVerifyTicket(String componentVerifyTicket) {
        WechatOpenPlatform update = new WechatOpenPlatform();
        update.setComponentVerifyTicket(componentVerifyTicket);
        wechatOpenPlatformMapper.normalUpdate(update, Wrappers.<WechatOpenPlatform>lambdaUpdate()
                .eq(WechatOpenPlatform::getComponentAppid, getWxOpenConfigStorage().getComponentAppId()));
    }

    /*==================== 相关持久化操作(数据库) end ====================*/

    /*==================== 相关微信操作 start ====================*/

    /*============== 第三方平台操作 start ==============*/

    public int saveComponentAccessToken(boolean refresh) {
        try {
            String componentAccessToken = getWxOpenComponentService().getComponentAccessToken(refresh);
            WechatOpenPlatform update = new WechatOpenPlatform();
            update.setComponentAccessToken(componentAccessToken);
            return wechatOpenPlatformMapper.normalUpdate(update, Wrappers.<WechatOpenPlatform>lambdaUpdate()
                    .eq(WechatOpenPlatform::getComponentAppid, getWxOpenConfigStorage().getComponentAppId()));
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    /*============== 第三方平台操作 end ==============*/

    /*============== 公众号操作 start ==============*/

    public int saveAuthorSite(boolean refresh) {
        List<WechatAuthorSite> list = wechatAuthorSiteMapper.normalSelectList(Wrappers.<WechatAuthorSite>lambdaQuery()
                .eq(WechatAuthorSite::getComponentAppid, getWxOpenConfigStorage().getComponentAppId())
                .eq(WechatAuthorSite::getState, SiteStateEnum.AUTHORIZED));
        int count = 0;
        for (WechatAuthorSite site : list) {
            //更新授权公众号代调用接口凭证(AccessToken)
            String authorizerAccessToken;
            try {
                authorizerAccessToken = getWxOpenComponentService().getWxMpServiceByAppid(site.getAppId()).getAccessToken(refresh);
            } catch (WxErrorException e) {
                e.printStackTrace();
                throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
            }
            //更新公众号JS-SDK权限验证的签名
            String jsapiTicket;
            try {
                jsapiTicket = getWxOpenComponentService().getWxMpServiceByAppid(site.getAppId()).getJsapiTicket(refresh);
            } catch (WxErrorException e) {
                e.printStackTrace();
                throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
            }
            if (StrUtil.isNotBlank(authorizerAccessToken) || StrUtil.isNotBlank(jsapiTicket)) {
                WechatAuthorSite update = new WechatAuthorSite();
                update.setId(site.getId());
                update.setAccessToken(authorizerAccessToken);
                update.setJsapiTicket(jsapiTicket);
                count += wechatAuthorSiteMapper.normalUpdateById(update);
                RedisUtil.redis().delete("getAuthorSite" + site.getAppId());
            }
        }
        return count;
    }

    public String templateMsgSend(TemplateMsgSendParam param) {
        WxMpTemplateMessage message = new WxMpTemplateMessage();
        message.setTemplateId(param.getTemplateId());
        message.setToUser(param.getOpenId());
        message.setUrl(param.getUrl());
        message.setData(param.getData());
        WxMpTemplateMessage.MiniProgram miniProgram = new WxMpTemplateMessage.MiniProgram();
        miniProgram.setAppid(param.getMiniAppId());
        miniProgram.setPagePath(param.getPagePath());
        message.setMiniProgram(miniProgram);
        try {
            return getWxOpenComponentService().getWxMpServiceByAppid(param.getAppId()).getTemplateMsgService().sendTemplateMsg(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    /*============== 公众号操作 end ==============*/

    /*============== 小程序操作 start ==============*/

    public String miniCodeUnLimitGet(MiniProgramQrCodeUnLimitGetParam param) {
        try {
            File file = getWxOpenComponentService().getWxMaServiceByAppid(param.getAppId()).getQrcodeService()
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

    public void miniUniformMsgSend(MiniProgramUniformMsgSentParam param) {
        WxMaUniformMessage message = new WxMaUniformMessage();
        if (param.getSendMpMessage()) {
            message.setAppid(param.getMpAppId());
            if (StrUtil.isNotBlank(param.getPagePath())) {
                WxMaUniformMessage.MiniProgram miniProgram = new WxMaUniformMessage.MiniProgram();
                miniProgram.setAppid(param.getAppId());
                miniProgram.setPagePath(param.getPagePath());
                message.setMiniProgram(miniProgram);
            }
            message.setUrl(param.getUrl());
        } else {
            message.setPage(param.getPagePath());
            message.setFormId(param.getFormId());
            message.setEmphasisKeyword(param.getEmphasisKeyword());
        }
        message.setTemplateId(param.getTemplateId());
        message.setToUser(param.getOpenId());
        message.setMpTemplateMsg(param.getSendMpMessage());
        message.setData(param.getData());
        try {
            getWxOpenComponentService().getWxMaServiceByAppid(param.getAppId()).getMsgService().sendUniformMsg(message);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

    /*============== 小程序操作 end ==============*/

    /*==================== 相关微信操作 end ====================*/
}
