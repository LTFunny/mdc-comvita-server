package com.aquilaflycloud.mdc.aspectj;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.aquilaflycloud.mdc.param.member.MemberEventParam;
import com.aquilaflycloud.mdc.service.MemberEventLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * MemberBusinessEventAspect
 * 记录并返回指定业务功能的记录数
 *
 * @author star
 * @date 2019-11-19
 */
@Aspect
@Component
public class MemberBusinessEventAspect {

    @Resource
    private MemberEventLogService memberEventLogService;

    @Pointcut("@annotation(com.gitee.sop.servercommon.annotation.ApiMapping)")
    public void resultPointcut() {
    }

    @Around(value = "resultPointcut()")
    public Object fillAfter(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        ApiMapping annotation = getAnnotation(joinPoint);
        if (annotation != null && result != null) {
            switch (annotation.value()[0]) {
                case "comvita.ad.info.list": {
                    result = addNum(result, BusinessTypeEnum.AD, EventTypeEnum.SHOW);
                    break;
                }
                case "comvita.recommendation.info.get": {
                    result = addNum(result, BusinessTypeEnum.RECOMMEND, EventTypeEnum.READ);
                    break;
                }
                case "comvita.apply.info.get": {
                    result = addNum(result, BusinessTypeEnum.APPLY, EventTypeEnum.READ);
                    break;
                }
                case "comvita.apply.info.page":
                case "backend.comvita.apply.info.page":
                case "backend.comvita.apply.info.get": {
                    result = getNum(result, BusinessTypeEnum.APPLY, EventTypeEnum.READ);
                    break;
                }
                default:
            }
        }
        return result;
    }

    private Object addNum(Object object, BusinessTypeEnum businessType, EventTypeEnum eventType) {
        return returnNum(object, businessType, eventType, true);
    }

    private Object getNum(Object object, BusinessTypeEnum businessType, EventTypeEnum eventType) {
        return returnNum(object, businessType, eventType, false);
    }

    private Object returnNum(Object object, BusinessTypeEnum businessType, EventTypeEnum eventType, boolean isIncrease) {
        if (object instanceof List) {
            List list = Convert.toList(object);
            for (Object o : list) {
                fill(o, businessType, eventType, isIncrease);
            }
            object = list;
        } else if (object instanceof IPage) {
            IPage page = Convert.convert(IPage.class,object);
            for (Object o : page.getRecords()) {
                fill(o, businessType, eventType, isIncrease);
            }
            object = page;
        } else {
            fill(object, businessType, eventType, isIncrease);
        }
        return object;
    }

    private void fill(Object object, BusinessTypeEnum businessType, EventTypeEnum eventType, boolean isIncrease) {
        if (ReflectUtil.hasField(object.getClass(), "id")) {
            DynaBean bean = DynaBean.create(object);
            Long id = Convert.toLong(bean.get("id"));
            Long num;
            MemberEventParam param = new MemberEventParam().setBusinessId(id).setBusinessType(businessType).setEventType(eventType);
            if (isIncrease) {
                num = increaseNum(param);
            } else {
                num = onlyGetNum(param);
            }
            if (ReflectUtil.hasField(object.getClass(), eventType.name().toLowerCase() + "Num")) {
                bean.set(eventType.name().toLowerCase() + "Num", num);
            }
        }
    }

    private Long increaseNum(MemberEventParam param) {
        return memberEventLogService.increaseBusinessNum(param);
    }

    private Long onlyGetNum(MemberEventParam param) {
        return memberEventLogService.getBusinessNum(param);
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
