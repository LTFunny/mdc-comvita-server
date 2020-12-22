package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.aquilaflycloud.mdc.mapper.MemberEventLogMapper;
import com.aquilaflycloud.mdc.model.member.MemberEventLog;
import com.aquilaflycloud.mdc.param.member.MemberEventAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberEventParam;
import com.aquilaflycloud.mdc.param.member.MemberEventStatisticsParam;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberEventLogResult;
import com.aquilaflycloud.mdc.result.member.MemberEventSexAndAgeResult;
import com.aquilaflycloud.mdc.result.member.MemberEventStatisticsResult;
import com.aquilaflycloud.mdc.service.MemberEventLogService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.gitee.sop.servercommon.param.ServiceParamValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * MemberEventLogServiceImpl
 *
 * @author star
 * @date 2019-11-19
 */
@Service
@Slf4j
public class MemberEventLogServiceImpl implements MemberEventLogService {
    @Resource
    private MemberEventLogMapper memberEventLogMapper;

    private ServiceParamValidator serviceParamValidator = new ServiceParamValidator();

    private void insertRecord(Long maxSize, MemberEventLog memberEventLog) {
        String key = "businessRecord";
        if (memberEventLog != null) {
            RedisUtil.listRedis().rightPush(key, memberEventLog);
        }
        Long size = RedisUtil.listRedis().size(key);
        if (size >= maxSize) {
            RedisUtil.syncLoad("insertBusinessRecord", () -> {
                List<MemberEventLog> list = RedisUtil.<MemberEventLog>listRedis().range(key, 0, -1);
                if (list.size() > 0) {
                    for (MemberEventLog log : list) {
                        log.setCreateTime(DateTime.now());
                    }
                    memberEventLogMapper.normalInsertAllBatch(list);
                    RedisUtil.redis().delete(key);
                }
                return null;
            });
        }
    }

    private void pushRecord(MemberEventParam param) {
        MemberEventLog memberEventLog = new MemberEventLog();
        BeanUtil.copyProperties(param, memberEventLog);
        memberEventLog.setMemberId(MdcUtil.getCurrentMemberId());
        memberEventLog.setEventTime(DateTime.now());
        memberEventLog.setTenantId(MdcUtil.getCurrentTenantId());
        memberEventLog.setSubTenantId(MdcUtil.getCurrentSubTenantId());
        memberEventLog.setAppKey(MdcUtil.getAppKey());
        insertRecord(100L, memberEventLog);
    }

    private boolean loadNum(MemberEventParam param) {
        String lockName = "eventNumLock_" + SecureUtil.md5(JSONUtil.toJsonStr(param));
        String key = "eventNum_" + SecureUtil.md5(JSONUtil.toJsonStr(param));
        if (!RedisUtil.redis().hasKey(key)) {
            return RedisUtil.syncLoad(lockName, () -> {
                List<MemberEventLogResult> list = memberEventLogMapper.selectLogCount(Wrappers.<MemberEventLog>lambdaQuery()
                        .eq(MemberEventLog::getBusinessType, param.getBusinessType())
                        .eq(MemberEventLog::getEventType, param.getEventType())
                        .groupBy(MemberEventLog::getBusinessId)
                        .orderByDesc(MemberEventLog::getEventTime)
                );
                if (list.size() > 0) {
                    for (MemberEventLogResult event : list) {
                        RedisUtil.zSetRedis().add(key, event.getBusinessType() + "_" + event.getBusinessId(), event.getCountNum());
                    }
                } else {
                    RedisUtil.zSetRedis().add(key, "000", 0);
                }
                RedisUtil.redis().expire(key, 7, TimeUnit.DAYS);
                return true;
            });
        } else {
            return true;
        }
    }

    private boolean loadMemberNum(MemberEventParam param) {
        String lockName = "eventMemberNumLock_" + SecureUtil.md5(JSONUtil.toJsonStr(param));
        String key = "eventMemberNum_" + SecureUtil.md5(JSONUtil.toJsonStr(param));
        if (!RedisUtil.redis().hasKey(key)) {
            return RedisUtil.syncLoad(lockName, () -> {
                List<MemberEventLogResult> list = memberEventLogMapper.selectLogCount(Wrappers.<MemberEventLog>lambdaQuery()
                        .eq(MemberEventLog::getBusinessType, param.getBusinessType())
                        .eq(MemberEventLog::getEventType, param.getEventType())
                        .eq(MemberEventLog::getBusinessId, param.getBusinessId())
                        .groupBy(MemberEventLog::getMemberId)
                        .orderByDesc(MemberEventLog::getEventTime)
                );
                if (list.size() > 0) {
                    for (MemberEventLogResult event : list) {
                        RedisUtil.zSetRedis().add(key, event.getMemberId(), event.getCountNum());
                    }
                } else {
                    RedisUtil.zSetRedis().add(key, "000", 0);
                }
                RedisUtil.redis().expire(key, 7, TimeUnit.DAYS);
                return true;
            });
        } else {
            return true;
        }
    }

    @Override
    public Long getBusinessNum(MemberEventParam param) {
        serviceParamValidator.validateBizParam(param);
        boolean flag = loadNum(param);
        if (flag) {
            String key = "eventNum_" + param.getBusinessType() + param.getEventType();
            String value = param.getBusinessType() + "_" + param.getBusinessId();
            Double number = RedisUtil.zSetRedis().score(key, value);
            return Convert.toLong(number, 0L);
        }
        throw new ServiceException("获取记录数pv失败");
    }

    private Long changeBusinessNum(MemberEventParam param) {
        long num = getBusinessNum(param);
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId != null) {
            String key = "eventNum_" + param.getBusinessType() + param.getEventType();
            String value = param.getBusinessType() + "_" + param.getBusinessId();
            num++;
            RedisUtil.zSetRedis().incrementScore(key, value, 1);
        }
        return num;
    }

    @Override
    public Long increaseBusinessNum(MemberEventParam param) {
        long num = changeBusinessNum(param);
        changeBusinessMemberNum(param);
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId != null) {
            pushRecord(param);
        }
        return num;
    }

    @Override
    public Long getBusinessMemberNum(MemberEventParam param) {
        serviceParamValidator.validateBizParam(param);
        boolean flag = loadMemberNum(param);
        if (flag) {
            String key = "eventMemberNum_" + param.getBusinessType() + param.getEventType() + param.getBusinessId();
            Long number = RedisUtil.zSetRedis().size(key);
            if (RedisUtil.zSetRedis().score(key, "000") != null) {
                number--;
            }
            return Convert.toLong(number, 0L);
        }
        throw new ServiceException("获取记录数uv失败");
    }

    private Long changeBusinessMemberNum(MemberEventParam param) {
        long num = getBusinessMemberNum(param);
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId != null) {
            String key = "eventMemberNum_" + param.getBusinessType() + param.getEventType() + param.getBusinessId();
            if (RedisUtil.zSetRedis().score(key, "000") != null) {
                if (num > 1) {
                    RedisUtil.zSetRedis().remove(key, "000");
                }
                num--;
                Double d = RedisUtil.zSetRedis().incrementScore(key, memberId, 1);
                num += d > 1 ? 0 : 1;
            }
        }
        return num;
    }

    @Override
    public Long increaseBusinessMemberNum(MemberEventParam param) {
        long num = changeBusinessMemberNum(param);
        changeBusinessNum(param);
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId != null) {
            pushRecord(param);
        }
        return num;
    }

    @Override
    public List<MemberEventStatisticsResult> selectLogStatistics(MemberEventStatisticsParam param) {
        serviceParamValidator.validateBizParam(param);
        //把缓存数据插入到数据库后再查询分析
        insertRecord(1L, null);
        return memberEventLogMapper.selectMaps(new QueryWrapper<MemberEventLog>()
                .select("business_id, business_type, event_type, count(distinct member_id) uv, count(1) pv")
                .lambda()
                .eq(MemberEventLog::getBusinessType, param.getBusinessType())
                .eq(MemberEventLog::getBusinessId, param.getBusinessId())
                .in(CollUtil.isNotEmpty(param.getEventTypes()), MemberEventLog::getEventType, param.getEventTypes())
                .le(param.getStartTime() != null, MemberEventLog::getEventTime, param.getStartTime())
                .lt(param.getEndTime() != null, MemberEventLog::getEventTime, param.getEndTime())
                .groupBy(MemberEventLog::getEventType))
                .stream().map((map) -> {
                    map.put("business_type", EnumUtil.likeValueOf(BusinessTypeEnum.class, map.get("business_type")));
                    map.put("event_type", EnumUtil.likeValueOf(EventTypeEnum.class, map.get("event_type")));
                    return BeanUtil.fillBeanWithMap(map, new MemberEventStatisticsResult(), true,
                            CopyOptions.create().ignoreError());
                }).collect(Collectors.toList());
    }

    @Override
    public List<MemberEventAnalysisResult> selectLogAnalysis(MemberEventAnalysisParam param) {
        serviceParamValidator.validateBizParam(param);
        //把缓存数据插入到数据库后再查询分析
        insertRecord(1L, null);
        Map<String, MemberEventAnalysisResult> analysis = memberEventLogMapper.selectMaps(new QueryWrapper<MemberEventLog>()
                .select("date_format(event_time,'%Y-%m-%d') event_date, count("
                        + (param.getIsPv() ? "1" : "distinct member_id")
                        + ") event_count")
                .groupBy("event_date")
                .lambda()
                .eq(MemberEventLog::getBusinessType, param.getBusinessType())
                .eq(MemberEventLog::getBusinessId, param.getBusinessId())
                .eq(MemberEventLog::getEventType, param.getEventType())
                .ge(param.getStartTime() != null, MemberEventLog::getEventTime, param.getStartTime())
                .lt(param.getEndTime() != null, MemberEventLog::getEventTime, param.getEndTime()))
                .stream().map((map) -> {
                    map.put("business_type", EnumUtil.likeValueOf(BusinessTypeEnum.class, map.get("business_type")));
                    map.put("event_type", EnumUtil.likeValueOf(EventTypeEnum.class, map.get("event_type")));
                    return BeanUtil.fillBeanWithMap(map, new MemberEventAnalysisResult(), true,
                            CopyOptions.create().ignoreError());
                }).collect(Collectors.toMap(MemberEventAnalysisResult::getEventDate, result -> result));
        return DateUtil.rangeToList(param.getStartTime(), param.getEndTime(), DateField.DAY_OF_YEAR).stream().map(dateTime -> {
            MemberEventAnalysisResult result = analysis.get(dateTime.toDateStr());
            if (result == null) {
                result = new MemberEventAnalysisResult();
                result.setEventDate(dateTime.toDateStr());
                result.setEventCount(0L);
            }
            return result;
        }).collect(Collectors.toList());
    }

    @Override
    public MemberEventSexAndAgeResult selectLogSexAndAge(MemberEventAnalysisParam param) {
        serviceParamValidator.validateBizParam(param);
        //把缓存数据插入到数据库后再查询分析
        insertRecord(1L, null);
        return memberEventLogMapper.selectLogSexAndAge(Wrappers.<MemberEventLog>lambdaQuery()
                .eq(MemberEventLog::getBusinessType, param.getBusinessType())
                .eq(MemberEventLog::getBusinessId, param.getBusinessId())
                .eq(MemberEventLog::getEventType, param.getEventType())
                .ge(param.getStartTime() != null, MemberEventLog::getEventTime, param.getStartTime())
                .lt(param.getEndTime() != null, MemberEventLog::getEventTime, param.getEndTime())
                .groupBy(!param.getIsPv(), MemberEventLog::getMemberId)
        );
    }

}
