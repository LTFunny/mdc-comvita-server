package com.aquilaflycloud.mdc.aspectj;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.mission.MissionTypeEnum;
import com.aquilaflycloud.mdc.mapper.CouponMemberRelMapper;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.service.MissionService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

/**
 * MemberMissionAspect
 * 会员任务
 *
 * @author star
 * @date 2020-05-08
 */
@Aspect
@Component
public class MemberMissionAspect {
    @Resource
    private MissionService missionService;
    @Resource
    private CouponMemberRelMapper couponMemberRelMapper;

    @Pointcut("@annotation(com.gitee.sop.servercommon.annotation.ApiMapping)")
    public void resultPointcut() {
    }

    private final static String[] LOGIN_METHOD = new String[]{
            "comvita.member.info.register",
            "comvita.member.info.login",
            "comvita.mini.member.login",
    };

    private final static String[] FINISH_METHOD = new String[]{
            "comvita.member.info.edit",
            "comvita.folksonomy.memberRel.add",
            "comvita.member.phoneNumber.edit",
            "comvita.member.phone.edit",
            "comvita.sign.info.add",
            "comvita.coupon.rel.add",
            "comvita.coupon.rel.use",
            "backend.comvita.coupon.rel.use",
    };

    @Around(value = "resultPointcut()")
    public Object missionHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        ApiMapping annotation = getAnnotation(joinPoint);
        if (annotation != null) {
            if (ArrayUtil.contains(LOGIN_METHOD, annotation.value()[0])) {
                //会员接受任务
                if (result != null) {
                    if (result instanceof BaseResult) {
                        BaseResult<String> baseResult = Convert.convert(BaseResult.class, result);
                        MemberInfo memberInfo = RedisUtil.<MemberInfoResult>valueRedis().get(StrUtil.emptyIfNull(baseResult.getResult()));
                        missionService.acceptMission(memberInfo);
                    }
                }
            } else if (ArrayUtil.contains(FINISH_METHOD, annotation.value()[0])) {
                Long memberId = MdcUtil.getCurrentMemberId();
                //会员完成任务
                switch (annotation.value()[0]) {
                    case "comvita.member.info.edit": {
                        missionService.checkMission(memberId, MissionTypeEnum.COMPLETE);
                        break;
                    }
                    case "comvita.folksonomy.memberRel.add": {
                        missionService.checkMission(memberId, MissionTypeEnum.FOLKSONOMY);
                        break;
                    }
                    case "comvita.member.phoneNumber.edit":
                    case "comvita.member.phone.edit": {
                        missionService.checkMission(memberId, MissionTypeEnum.BINDINGPHONE);
                        break;
                    }
                    case "comvita.sign.info.add": {
                        missionService.checkMission(memberId, MissionTypeEnum.TOTALSIGN);
                        break;
                    }
                    case "comvita.coupon.rel.add": {
                        missionService.checkMission(memberId, MissionTypeEnum.RECEIVECOUPON);
                        break;
                    }
                    case "comvita.coupon.rel.use":
                    case "backend.comvita.coupon.rel.use": {
                        Object param = joinPoint.getArgs()[0];
                        JSONObject paramJson = JSONUtil.parseObj(param);
                        String verificateCode = paramJson.getStr("verificateCode");
                        CouponMemberRel rel = couponMemberRelMapper.selectOne(Wrappers.<CouponMemberRel>lambdaQuery()
                                .select(CouponMemberRel::getMemberId)
                                .eq(CouponMemberRel::getVerificateCode, verificateCode)
                        );
                        if (rel != null) {
                            missionService.checkMission(rel.getMemberId(), MissionTypeEnum.USECOUPON);
                        }
                        break;
                    }
                    default:
                }
            }
        }
        return result;
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
