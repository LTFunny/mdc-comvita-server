package com.aquilaflycloud.mdc.aspectj;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.mapper.AlipayAuthorSiteMapper;
import com.aquilaflycloud.mdc.mapper.WechatAuthorSiteMapper;
import com.aquilaflycloud.mdc.message.AuthorSiteErrorEnum;
import com.aquilaflycloud.mdc.model.alipay.AlipayAuthorSite;
import com.aquilaflycloud.mdc.model.wechat.WechatAuthorSite;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AuthorSiteParamAspect
 * 处理入参里有微信appId或支付宝appId时的合法租户判断
 *
 * @author star
 * @date 2019-10-30
 */
@Aspect
@Component
public class AuthorSiteParamAspect {

    @Resource
    private WechatAuthorSiteMapper wechatAuthorSiteMapper;

    @Resource
    private AlipayAuthorSiteMapper alipayAuthorSiteMapper;

    @Pointcut("@annotation(com.gitee.sop.servercommon.annotation.ApiMapping)")
    public void paramPointcut() {
    }

    @Before(value = "paramPointcut()")
    public void authBefore(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length > 0) {
            Object param = joinPoint.getArgs()[0];
            JSONObject jsonObject = JSONUtil.parseObj(param);
            String appId = jsonObject.getStr("appId");
            String appIds = MdcUtil.getConfigValue(ConfigTypeEnum.IGNORE_JUDGMENT_NORMAL_APPID);
            if (StrUtil.isBlank(appId) || StrUtil.contains(appIds, appId)) {
                return;
            }
            ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_FILTER);
            try {
                if (StrUtil.startWith(appId, "wx")) {
                    if (!MdcConstant.UNIVERSAL_APP_ID.equals(appId)) {
                        Integer count = wechatAuthorSiteMapper.selectCount(Wrappers.<WechatAuthorSite>lambdaQuery()
                                .eq(WechatAuthorSite::getAppId, appId)
                        );
                        if (count <= 0) {
                            throw AuthorSiteErrorEnum.AUTHOR_SITE_ERROR_10101.getErrorMeta().getException();
                        }
                    }
                } else {
                    if (!MdcConstant.UNIVERSAL_APP_ID.equals(appId)) {
                        Integer count = alipayAuthorSiteMapper.selectCount(Wrappers.<AlipayAuthorSite>lambdaQuery().eq(AlipayAuthorSite::getAppId, appId));
                        if (count <= 0) {
                            throw AuthorSiteErrorEnum.AUTHOR_SITE_ERROR_10101.getErrorMeta().getException();
                        }
                    }
                }
            } finally {
                ServiceContext.getCurrentContext().remove(DataAuthConstant.SUB_TENANT_FILTER);
            }
        }
    }
}
