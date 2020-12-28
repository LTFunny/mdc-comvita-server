package com.aquilaflycloud.mdc.aspectj;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CommonParamAspect 对参数处理验证
 *
 * @author star
 * @date 2020-03-31
 */
@Aspect
@Component
public class CommonParamAspect {

    @Pointcut("@annotation(com.gitee.sop.servercommon.annotation.ApiMapping)")
    public void paramPointcut() {
    }

    @Before(value = "paramPointcut()")
    public void authBefore(JoinPoint joinPoint) {
        if (joinPoint.getArgs().length > 0) {
            Object param = joinPoint.getArgs()[0];
            //处理查询时间段,start默认00:00:00,end默认23:59:59
            String className = param.getClass().getSimpleName();
            if (StrUtil.containsAnyIgnoreCase(className, "add", "edit", "save")) {
                return;
            }
            Map<String, Object> map = BeanUtil.beanToMap(param);
            Set<String> keys = map.keySet().stream().filter(key ->
                    StrUtil.containsAnyIgnoreCase(key, "start", "end")).collect(Collectors.toSet());
            if (keys.size() > 0) {
                DynaBean bean = DynaBean.create(param);
                for (String key : keys) {
                    Object value = bean.safeGet(key);
                    if (value instanceof Date) {
                        if (StrUtil.containsIgnoreCase(key, "start")) {
                            bean.set(key, DateUtil.beginOfDay((Date) value));
                        } else if (StrUtil.containsIgnoreCase(key, "end")) {
                            bean.set(key, DateUtil.endOfDay((Date) value));
                        }
                    }
                }
            }
            if (map.get("exportParam") != null) {
                JSONObject json = JSONUtil.parseObj(map.get("exportParam"));
                keys = json.keySet().stream().filter(key ->
                        StrUtil.containsAnyIgnoreCase(key, "start", "end")).collect(Collectors.toSet());
                if (keys.size() > 0) {
                    for (String key : keys) {
                        Date value = json.getDate(key);
                        if (value != null) {
                            if (StrUtil.containsIgnoreCase(key, "start")) {
                                json.set(key, DateUtil.beginOfDay(value));
                            } else if (StrUtil.containsIgnoreCase(key, "end")) {
                                json.set(key, DateUtil.endOfDay(value));
                            }
                        }
                    }
                    DynaBean bean = DynaBean.create(param);
                    bean.set("exportParam", json.toString());
                }
            }
        }
    }
}
