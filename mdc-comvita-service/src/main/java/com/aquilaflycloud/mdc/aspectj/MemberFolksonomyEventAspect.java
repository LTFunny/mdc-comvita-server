package com.aquilaflycloud.mdc.aspectj;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * MemberFolksonomyEventAspect
 * 会员业务功能标签的关联
 *
 * @author star
 * @date 2019-11-28
 */
@Aspect
@Component
public class MemberFolksonomyEventAspect {

    @Resource
    private FolksonomyService folksonomyService;

    @Pointcut("@annotation(com.gitee.sop.servercommon.annotation.ApiMapping)")
    public void resultPointcut() {
    }

    @AfterReturning(value = "resultPointcut()")
    public void handlerAfter(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length > 0) {
            ApiMapping annotation = getAnnotation(joinPoint);
            if (annotation != null) {
                Object param = joinPoint.getArgs()[0];
                JSONObject paramJson = JSONUtil.parseObj(param);
                Long businessId = null;
                switch (annotation.value()[0]) {
                    case "mdc.member.adShare.add":
                    case "mdc.member.lotteryShare.add":
                    case "mdc.member.offlineSignShare.add":
                    case "mdc.member.applyShare.add":
                    case "mdc.member.recommendShare.add":
                    case "mdc.member.scenicSpotsShare.add":
                    case "mdc.member.adClick.add":
                    case "mdc.member.recommendClick.add": {
                        businessId = paramJson.getLong("businessId");
                        break;
                    }
                    default:
                }
                if (businessId != null) {
                    saveFolksonomyMemberRel(businessId);
                }
            }
        }
    }

    private void saveFolksonomyMemberRel(Long businessId) {
        folksonomyService.saveFolksonomyMemberRel(businessId);
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private ApiMapping getAnnotation(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(ApiMapping.class);
        }
        return null;
    }
}
