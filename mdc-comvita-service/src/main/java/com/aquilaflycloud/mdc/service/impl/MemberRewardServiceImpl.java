package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.component.event.AfterCommitEvent;
import com.aquilaflycloud.mdc.enums.common.StateEnum;
import com.aquilaflycloud.mdc.enums.member.RewardSourceEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.member.RuleTypeEnum;
import com.aquilaflycloud.mdc.enums.member.UnifiedColNameEnum;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.mapper.MemberInfoMapper;
import com.aquilaflycloud.mdc.mapper.MemberRewardRecordMapper;
import com.aquilaflycloud.mdc.mapper.MemberRewardRuleMapper;
import com.aquilaflycloud.mdc.message.MemberErrorEnum;
import com.aquilaflycloud.mdc.message.RewardErrorEnum;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.member.MemberRewardRecord;
import com.aquilaflycloud.mdc.model.member.MemberRewardRule;
import com.aquilaflycloud.mdc.param.member.*;
import com.aquilaflycloud.mdc.result.member.*;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.aquilaflycloud.mdc.service.MemberGradeService;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.SystemTenantConfigService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * MemberRewardServiceImpl
 *
 * @author star
 * @date 2019-11-14
 */
@Slf4j
@Service
public class MemberRewardServiceImpl implements MemberRewardService {
    @Resource
    private MemberRewardRuleMapper memberRewardRuleMapper;
    @Resource
    private MemberRewardRecordMapper memberRewardRecordMapper;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private MemberGradeService memberGradeService;
    @Resource
    private SystemTenantConfigService systemTenantConfigService;

    @Override
    public List<MemberRewardRule> listRewardRule(RewardRuleListParam param) {
        return memberRewardRuleMapper.selectList(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberRewardRule::getAppId, param.getAppId())
                .in(CollUtil.isNotEmpty(param.getRuleTypeList()), MemberRewardRule::getRuleType, param.getRuleTypeList()));
    }

    private void saveRule(RewardSaveParam param, RuleTypeEnum ruleType) {
        Object result;
        switch (ruleType) {
            case CLEAN: {
                result = new CleanRewardRuleResult();
                break;
            }
            case SIGN: {
                result = new SignRewardRuleResult();
                break;
            }
            case SCAN: {
                result = new ScanRewardRuleResult();
                break;
            }
            default:
                throw new ServiceException("保存规则失败:转换失败");
        }
        BeanUtil.copyProperties(param, result);
        MemberRewardRule rule = memberRewardRuleMapper.selectOne(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getRuleType, ruleType)
                .eq(MemberRewardRule::getRewardType, param.getRewardType())
                .eq(MemberRewardRule::getAppId, param.getAppId())
        );
        int count;
        if (rule == null) {
            rule = new MemberRewardRule();
            BeanUtil.copyProperties(param, rule);
            rule.setRuleType(ruleType);
            rule.setState(StateEnum.NORMAL);
            rule.setRuleContent(JSONUtil.toJsonStr(result));
            count = memberRewardRuleMapper.insert(rule);
        } else {
            MemberRewardRule update = new MemberRewardRule();
            BeanUtil.copyProperties(param, update);
            update.setState(StateEnum.NORMAL);
            update.setRuleContent(JSONUtil.toJsonStr(result));
            update.setId(rule.getId());
            count = memberRewardRuleMapper.updateById(update);
        }
        if (count <= 0) {
            throw new ServiceException("保存规则失败");
        }
    }

    @Override
    public void saveRewardSign(RewardSignSaveParam param) {
        saveRule(param, RuleTypeEnum.SIGN);
    }

    @Override
    public SignRewardRuleResult getRewardSign(RewardRuleGetParam param) {
        MemberRewardRule rule = memberRewardRuleMapper.selectOne(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.SIGN)
                .eq(MemberRewardRule::getAppId, param.getAppId())
                .eq(MemberRewardRule::getRewardType, param.getRewardType()));
        return rule == null ? null : JSONUtil.toBean(rule.getRuleContent(), SignRewardRuleResult.class);
    }

    @Override
    public void saveRewardScan(RewardScanSaveParam param) {
        saveRule(param, RuleTypeEnum.SCAN);
    }

    @Override
    public ScanRewardRuleResult getRewardScan(RewardRuleGetParam param) {
        MemberRewardRule rule = memberRewardRuleMapper.selectOne(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.SCAN)
                .eq(MemberRewardRule::getAppId, param.getAppId())
                .eq(MemberRewardRule::getRewardType, param.getRewardType()));
        return rule == null ? null : JSONUtil.toBean(rule.getRuleContent(), ScanRewardRuleResult.class);
    }

    @Override
    public void saveRewardClean(RewardCleanSaveParam param) {
        saveRule(param, RuleTypeEnum.CLEAN);
    }

    @Override
    public CleanRewardRuleResult getRewardClean(RewardRuleGetParam param) {
        MemberRewardRule rule = memberRewardRuleMapper.selectOne(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.CLEAN)
                .eq(MemberRewardRule::getAppId, param.getAppId())
                .eq(MemberRewardRule::getRewardType, param.getRewardType()));
        return rule == null ? null : JSONUtil.toBean(rule.getRuleContent(), CleanRewardRuleResult.class);
    }

    @Override
    public void toggleState(RewardRuleIdGetParam param) {
        MemberRewardRule rule = memberRewardRuleMapper.selectById(param.getId());
        MemberRewardRule update = new MemberRewardRule();
        update.setId(param.getId());
        update.setState(rule.getState() == StateEnum.NORMAL ? StateEnum.DISABLE : StateEnum.NORMAL);
        int count = memberRewardRuleMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public IPage<MemberRewardRecord> page(RewardRecordPageParam param) {
        return memberRewardRecordMapper.selectPage(param.page(), Wrappers.<MemberRewardRecord>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getPhoneNumber()), MemberRewardRecord::getPhoneNumber, param.getPhoneNumber())
                .like(StrUtil.isNotBlank(param.getNickName()), MemberRewardRecord::getNickName, param.getNickName())
                .eq(StrUtil.isNotBlank(param.getAppId()), MemberRewardRecord::getAppId, param.getAppId())
                .ge(param.getCreateTimeStart() != null, MemberRewardRecord::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, MemberRewardRecord::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getMemberId() != null, MemberRewardRecord::getMemberId, param.getMemberId())
                .eq(param.getRewardType() != null, MemberRewardRecord::getRewardType, param.getRewardType())
                .eq(param.getRewardSource() != null, MemberRewardRecord::getRewardSource, param.getRewardSource())
                .ge(param.getMinRewardValue() != null, MemberRewardRecord::getRewardValue, param.getMinRewardValue())
                .le(param.getMaxRewardValue() != null, MemberRewardRecord::getRewardValue, param.getMaxRewardValue())
                .like(StrUtil.isNotBlank(param.getCreatorName()), MemberRewardRecord::getCreatorName, param.getCreatorName())
                .orderByDesc(MemberRewardRecord::getCreateTime)
        );
    }

    @Override
    public Integer getMemberTotalReward(Long memberId, RewardTypeEnum rewardType) {
        if (memberId == null || rewardType == null) {
            return 0;
        }
        MemberRewardRecord record = memberRewardRecordMapper.selectOne(new QueryWrapper<MemberRewardRecord>()
                .select("coalesce(sum(reward_value), 0) reward_value")
                .lambda()
                .eq(MemberRewardRecord::getRewardType, rewardType)
                .eq(MemberRewardRecord::getMemberId, memberId)
                .groupBy(MemberRewardRecord::getMemberId)
        );
        return record == null ? 0 : Convert.toInt(record.getRewardValue(), 0);
    }

    @Transactional
    @Override
    public void addRecord(RewardRecordAddParam param) {
        if (param.getRewardValue() == 0) {
            throw new ServiceException("奖励值不能为0");
        }
        MemberInfo memberInfo = memberInfoMapper.selectById(param.getMemberId());
        if (memberInfo == null) {
            throw MemberErrorEnum.MEMBER_ERROR_10002.getErrorMeta().getException();
        }
        addMemberRewardRecord(memberInfo, param.getRewardType(), param.getRewardValue(), RewardSourceEnum.SYSTEM, param.getRemark());
    }

    private void refreshMemberReward(String appId, Long memberId, RewardTypeEnum rewardType) {
        addRecordCache(null, null, appId, memberId, rewardType);
        DateTime now = DateTime.now();
        addRecordCache(DateUtil.beginOfYear(now), DateUtil.endOfYear(now), appId, memberId, rewardType);
        addRecordCache(DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now), appId, memberId, rewardType);
    }

    private void addRecordCache(DateTime start, DateTime end, String appId, Long memberId, RewardTypeEnum rewardTypeEnum) {
        Set<RewardTypeEnum> rewardTypeSet = new HashSet<>();
        if (rewardTypeEnum == null) {
            rewardTypeSet.addAll(CollUtil.toList(RewardTypeEnum.values()));
        } else {
            rewardTypeSet.add(rewardTypeEnum);
        }
        for (RewardTypeEnum rewardType : rewardTypeSet) {
            String key = getRankCacheKey(start, end, appId, rewardType);
            RedisUtil.syncLoad("rewardRankLock" + rewardType.getType() + memberId, () -> {
                List<RewardRankResult> list = memberRewardRecordMapper.normalSelectRank(Wrappers.<MemberRewardRecord>lambdaQuery()
                        .ge(start != null, MemberRewardRecord::getCreateTime, start)
                        .le(end != null, MemberRewardRecord::getCreateTime, end)
                        .eq(MemberRewardRecord::getMemberId, memberId)
                        .eq(MemberRewardRecord::getRewardType, rewardType)
                );
                if (CollUtil.isNotEmpty(list)) {
                    RewardRankResult result = list.get(0);
                    String s = "0." + result.getCreateTime().getTime();
                    double score = NumberUtil.add(result.getTotalReward(), NumberUtil.sub(1, Convert.toNumber(s))).doubleValue();
                    MemberRewardRankResult.RankResult rankResult = new MemberRewardRankResult.RankResult();
                    BeanUtil.copyProperties(result, rankResult);
                    RedisUtil.<Long>zSetRedis().add(key, memberId, score);
                    RedisUtil.<Long, MemberRewardRankResult.RankResult>hashRedis().put(key + "hash", memberId, rankResult);
                    RedisUtil.redis().expire(key, 30, TimeUnit.DAYS);
                    RedisUtil.redis().expire(key + "hash", 30, TimeUnit.DAYS);
                    return result.getTotalReward();
                }
                return null;
            });
        }
    }

    @Override
    public List<RewardRankResult> listRewardRank(RewardRankListParam param) {
        String dateNum = param.getDateNum();
        DateTime start = null, end = null;
        try {
            if (param.getRankType() == RewardRankListParam.RankTypeEnum.YEAR) {
                if (dateNum == null) {
                    dateNum = StrUtil.toString(DateUtil.thisYear());
                }
                start = DateUtil.parse(dateNum, "yyyy");
                end = DateUtil.endOfYear(start);
            } else if (param.getRankType() == RewardRankListParam.RankTypeEnum.MONTH) {
                if (dateNum == null) {
                    dateNum = StrUtil.toString(DateUtil.thisYear()) + StrUtil.toString(DateUtil.thisMonth() + 1);
                }
                start = DateUtil.parse(dateNum, "yyyy-MM");
                end = DateUtil.endOfMonth(start);
            }
        } catch (Exception e) {
            log.error("转换日期失败", e);
            throw new ServiceException("转换日期失败");
        }
        boolean isUnifiedMember = false;
        Set<String> appIdSet = null;
        SystemTenantConfigResult configResult = systemTenantConfigService.getConfig(TenantConfigTypeEnum.UNIFIEDMEMBER);
        if (configResult != null && configResult.getUnifiedMemberConfig() != null && configResult.getUnifiedMemberConfig().getUnified()
                && configResult.getUnifiedMemberConfig().getUnifiedCol() == UnifiedColNameEnum.APPID
                && configResult.getUnifiedMemberConfig().getIncludeValue().contains(param.getAppId())) {
            isUnifiedMember = true;
            appIdSet = configResult.getUnifiedMemberConfig().getIncludeValue();
        }
        String key = StrUtil.join("_", "memberRank1000", MdcUtil.getCurrentTenantId(), SecureUtil.md5(JSONUtil.toJsonStr(param)));
        List<Long> memberIds = RedisUtil.<Long>listRedis().range(key, 0, -1);
        List<RewardRankResult> list = memberRewardRecordMapper.selectRank(Wrappers.<MemberRewardRecord>lambdaQuery()
                .ge(start != null, MemberRewardRecord::getCreateTime, start)
                .le(end != null, MemberRewardRecord::getCreateTime, end)
                .eq(!isUnifiedMember, MemberRewardRecord::getAppId, param.getAppId())
                .in(isUnifiedMember && CollUtil.isNotEmpty(appIdSet), MemberRewardRecord::getAppId, appIdSet)
                .eq(MemberRewardRecord::getRewardType, param.getRewardType())
                .in(CollUtil.isNotEmpty(memberIds) && memberIds.size() >= param.getLimit(), MemberRewardRecord::getMemberId, memberIds)
        );
        if (CollUtil.isNotEmpty(list)) {
            list = CollUtil.sub(list, 0, param.getLimit());
            memberIds = list.stream().map(RewardRankResult::getMemberId).collect(Collectors.toList());
            RedisUtil.redis().delete(key);
            RedisUtil.<Long>listRedis().leftPushAll(key, memberIds);
            RedisUtil.redis().expire(key, 1, TimeUnit.HOURS);
            AtomicLong i = new AtomicLong(1L);
            list = list.stream().peek(result -> result.setRankNo(i.getAndIncrement())).collect(Collectors.toList());
            if (isUnifiedMember) {
                list = list.parallelStream().peek(result -> result.setGradeTitle(memberGradeService.getRewardGrade(configResult.getUnifiedMemberConfig()
                        .getUnifiedValue(), result.getRewardType(), result.getTotalReward()).getGradeTitle())).collect(Collectors.toList());
            } else {
                list = list.parallelStream().peek(result -> result.setGradeTitle(memberGradeService.getRewardGrade(result
                        .getAppId(), result.getRewardType(), result.getTotalReward()).getGradeTitle())).collect(Collectors.toList());
            }
        }
        return list;
    }

    @Override
    public void initRewardRank(RewardRankInitParam param) {
        MdcUtil.getTtlExecutorService().submit(() -> initRewardCache(null, null, param));
        DateTime now = DateTime.now();
        MdcUtil.getTtlExecutorService().submit(() -> initRewardCache(DateUtil.beginOfYear(now), DateUtil.endOfYear(now), param));
        MdcUtil.getTtlExecutorService().submit(() -> initRewardCache(DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now), param));
    }

    private void initRewardCache(DateTime start, DateTime end, RewardRankInitParam param) {
        String key = getRankCacheKey(start, end, param.getAppId(), param.getRewardType());
        List<RewardRankResult> list = memberRewardRecordMapper.selectRank(Wrappers.<MemberRewardRecord>lambdaQuery()
                .ge(start != null, MemberRewardRecord::getCreateTime, start)
                .le(end != null, MemberRewardRecord::getCreateTime, end)
                .eq(CollUtil.isEmpty(param.getAppIdList()), MemberRewardRecord::getAppId, param.getAppId())
                .in(CollUtil.isNotEmpty(param.getAppIdList()), MemberRewardRecord::getAppId, param.getAppIdList())
                .eq(MemberRewardRecord::getRewardType, param.getRewardType())
        );
        Set<ZSetOperations.TypedTuple<Long>> tuples = new HashSet<>();
        Map<Long, MemberRewardRankResult.RankResult> map = new HashMap<>();
        for (RewardRankResult result : list) {
            String s = "0." + result.getCreateTime().getTime();
            double score = NumberUtil.add(result.getTotalReward(), NumberUtil.sub(1, Convert.toNumber(s))).doubleValue();
            ZSetOperations.TypedTuple<Long> tuple = new DefaultTypedTuple<>(result.getMemberId(), score);
            tuples.add(tuple);
            MemberRewardRankResult.RankResult rankResult = new MemberRewardRankResult.RankResult();
            BeanUtil.copyProperties(result, rankResult);
            map.put(result.getMemberId(), rankResult);
        }
        if (tuples.size() > 0) {
            RedisUtil.redis().delete(key);
            RedisUtil.redis().delete(key + "hash");
            RedisUtil.<Long>zSetRedis().add(key, tuples);
            RedisUtil.<Long, MemberRewardRankResult.RankResult>hashRedis().putAll(key + "hash", map);
            RedisUtil.redis().expire(key, 30, TimeUnit.DAYS);
            RedisUtil.redis().expire(key + "hash", 30, TimeUnit.DAYS);
        }
    }

    @Override
    public String getRankCacheKey(DateTime start, DateTime end, String appId, RewardTypeEnum rewardType) {
        String date = "all";
        if (start != null && end != null) {
            date = start.toString(DatePattern.PURE_DATE_FORMAT) + "-" + end.toString(DatePattern.PURE_DATE_FORMAT);
        }
        return StrUtil.join("_", "getRewardRank", appId, rewardType.getType(), date, MdcUtil.getCurrentTenantId());
    }

    @Override
    public MemberRewardRankResult getRewardRank(MemberRewardRankGetParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        String appId = MdcUtil.getMemberAppId(memberInfo);
        DateTime start = null, end = null, now = DateTime.now();
        if (param.getRankType() == MemberRewardRankGetParam.RankTypeEnum.YEAR) {
            start = DateUtil.beginOfYear(now);
            end = DateUtil.endOfYear(now);
        } else if (param.getRankType() == MemberRewardRankGetParam.RankTypeEnum.MONTH) {
            start = DateUtil.beginOfMonth(now);
            end = DateUtil.endOfMonth(now);
        }
        MemberRewardRankResult.RankResult emptyMemberRank = new MemberRewardRankResult.RankResult();
        MdcUtil.setMemberInfo(emptyMemberRank, memberInfo);
        emptyMemberRank.setRewardType(param.getRewardType());
        emptyMemberRank.setTotalReward(0);
        MemberRewardRankResult result = new MemberRewardRankResult();
        String key = getRankCacheKey(start, end, appId, param.getRewardType());
        Set<Long> memberIds = RedisUtil.<Long>zSetRedis().reverseRange(key, 0, param.getLimit() - 1);
        if (memberIds.size() <= 0) {
            result.setMemberRank(emptyMemberRank);
            return result;
        }
        List<MemberRewardRankResult.RankResult> list = RedisUtil.<Long, MemberRewardRankResult.RankResult>hashRedis().multiGet(key + "hash", memberIds);
        CollUtil.forEach(list.iterator(), (value, index) -> {
            value.setRankNo(Convert.toLong(index + 1));
            value.setGradeTitle(memberGradeService.getRewardGrade(appId, value.getRewardType(), value.getTotalReward()).getGradeTitle());
        });
        MemberRewardRankResult.RankResult memberRank = RedisUtil.<Long, MemberRewardRankResult.RankResult>hashRedis().get(key + "hash", memberInfo.getId());
        if (memberRank == null) {
            result.setMemberRank(emptyMemberRank);
            result.setMemberRankList(list);
            return result;
        }
        Long rank = RedisUtil.<Long>zSetRedis().reverseRank(key, memberInfo.getId());
        memberRank.setRankNo(rank == null ? null : Convert.toLong(rank, 0L) + 1);
        memberRank.setGradeTitle(memberGradeService.getRewardGrade(appId, memberRank.getRewardType(), memberRank.getTotalReward()).getGradeTitle());
        memberRank.setNickName(memberInfo.getNickName());
        memberRank.setAvatarUrl(memberInfo.getAvatarUrl());
        result.setMemberRank(memberRank);
        result.setMemberRankList(list);
        return result;
    }

    @Override
    public MemberSummaryRewardResult getSummaryReward(MemberSummaryRewardGetParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        String appId = MdcUtil.getMemberAppId(memberInfo);
        String key = getRankCacheKey(null, null, appId, param.getRewardType());
        Double score = RedisUtil.<Long>zSetRedis().score(key, memberInfo.getId());
        Integer total = Convert.toInt(score, 0);
        MemberSummaryRewardResult result = new MemberSummaryRewardResult();
        result.setRewardType(param.getRewardType());
        result.setTotalReward(total);
        MemberRewardRule rule = memberRewardRuleMapper.selectOne(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getAppId, appId)
                .eq(MemberRewardRule::getRewardType, param.getRewardType())
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.CLEAN)
                .eq(MemberRewardRule::getState, StateEnum.NORMAL)
        );
        if (rule != null) {
            Map<String, DateTime> dateResult = getCleanDate(rule);
            DateTime cleanDate = dateResult.get("cleanDate");
            DateTime cleanBefore = dateResult.get("cleanBefore");
            List<Map<String, Object>> list = memberRewardRecordMapper.selectMaps(new QueryWrapper<MemberRewardRecord>()
                    .select("coalesce(sum(reward_value), 0) total, member_id")
                    .lambda()
                    .eq(MemberRewardRecord::getRewardType, rule.getRewardType())
                    .eq(MemberRewardRecord::getAppId, rule.getAppId())
                    .eq(MemberRewardRecord::getMemberId, memberInfo.getId())
                    .nested(i -> i.le(MemberRewardRecord::getCreateTime, cleanBefore)
                            .or(j -> j.le(MemberRewardRecord::getRewardValue, 0)
                                    .gt(MemberRewardRecord::getCreateTime, cleanBefore)
                                    .le(MemberRewardRecord::getCreateTime, DateTime.now())
                            )
                    )
                    .groupBy(MemberRewardRecord::getMemberId)
            );
            Integer expireTotal = 0;
            if (list.size() > 0) {
                expireTotal = Convert.toInt(list.get(0).get("total"));
                expireTotal = NumberUtil.max(expireTotal, 0);
            }
            result.setExpireDate(cleanDate);
            result.setExpireReward(expireTotal);
        }
        return result;
    }

    @Override
    public IPage<MemberRewardRecord> pageMemberReward(MemberRewardPageParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return memberRewardRecordMapper.selectPage(param.page(), Wrappers.<MemberRewardRecord>lambdaQuery()
                .eq(MemberRewardRecord::getRewardType, param.getRewardType())
                .eq(MemberRewardRecord::getMemberId, memberId)
                .orderByDesc(MemberRewardRecord::getCreateTime)
        );
    }

    @Override
    public List<SignRewardRuleResult> getRewardSignList(String appId) {
        return memberRewardRuleMapper.selectList(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.SIGN)
                .eq(MemberRewardRule::getAppId, appId)
                .eq(MemberRewardRule::getState, StateEnum.NORMAL)
        ).stream().map(rule -> JSONUtil.toBean(rule.getRuleContent(), SignRewardRuleResult.class)).collect(Collectors.toList());
    }

    @Override
    public Map<RewardTypeEnum, MemberRewardResult> addSignRewardRecord(MemberInfo memberInfo, Integer continueCount) {
        String appId = MdcUtil.getMemberAppId(memberInfo);
        List<MemberRewardRule> ruleList = memberRewardRuleMapper.selectList(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getAppId, appId)
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.SIGN)
                .eq(MemberRewardRule::getState, StateEnum.NORMAL)
        );
        Map<RewardTypeEnum, MemberRewardResult> result = new HashMap<>();
        for (MemberRewardRule rule : ruleList) {
            SignRewardRuleResult ruleResult = JSONUtil.toBean(rule.getRuleContent(), SignRewardRuleResult.class);
            if (ruleResult != null) {
                MemberRewardResult rewardResult = new MemberRewardResult();
                rewardResult.setRewardType(rule.getRewardType());
                Integer rewardValue = ruleResult.getSignReward();
                if (ruleResult.getTimes() != null && continueCount != 0 && continueCount % ruleResult.getTimes() == 0) {
                    rewardValue += ruleResult.getExtReward();
                }
                MemberRewardRecord rewardRecord = new MemberRewardRecord();
                MdcUtil.setMemberInfo(rewardRecord, memberInfo);
                rewardRecord.setRewardSource(RewardSourceEnum.SIGN);
                rewardRecord.setRewardType(rule.getRewardType());
                rewardRecord.setRewardValue(rewardValue);
                rewardRecord.setRemark(RewardSourceEnum.SIGN.getName());
                int count = memberRewardRecordMapper.insert(rewardRecord);
                if (count <= 0) {
                    throw new ServiceException("新增奖励失败");
                }
                rewardResult.setRewardValue(rewardValue);
                result.put(rule.getRewardType(), rewardResult);
            }
        }
        MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("签到后刷新奖励值缓存_" + memberInfo.getId(), () -> {
            for (MemberRewardResult reward : result.values()) {
                refreshMemberReward(appId, memberInfo.getId(), reward.getRewardType());
            }
        }));
        return result;
    }

    @Override
    public Map<RewardTypeEnum, MemberScanRewardResult> addScanRewardRecord(MemberInfo memberInfo, Long formatId, BigDecimal amount, Boolean ignore) {
        String appId = MdcUtil.getMemberAppId(memberInfo);
        List<MemberRewardRule> ruleList = memberRewardRuleMapper.selectList(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getAppId, appId)
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.SCAN)
                .eq(MemberRewardRule::getState, StateEnum.NORMAL)
        );
        Map<RewardTypeEnum, MemberScanRewardResult> result = new HashMap<>();
        for (MemberRewardRule rule : ruleList) {
            ScanRewardRuleResult ruleResult = JSONUtil.toBean(rule.getRuleContent(), ScanRewardRuleResult.class);
            if (ruleResult != null) {
                MemberScanRewardResult rewardResult = new MemberScanRewardResult();
                rewardResult.setOverLimit(false);
                rewardResult.setRewardType(rule.getRewardType());
                BigDecimal consumeMoney = ruleResult.getConsumeMoney();
                Integer scanReward = ruleResult.getScanReward();
                if (formatId != null && CollUtil.isNotEmpty(ruleResult.getFormatRewardList())) {
                    Optional<ScanRewardRuleResult.FormatReward> optional = ruleResult.getFormatRewardList().stream()
                            .filter(format -> formatId.equals(format.getFormatId())).findFirst();
                    if (optional.isPresent()) {
                        ScanRewardRuleResult.FormatReward formatReward = optional.get();
                        consumeMoney = formatReward.getFormatConsumeMoney();
                        scanReward = formatReward.getFormatReward();
                    }
                }
                Integer rewardValue = NumberUtil.mul(amount, NumberUtil.div(scanReward, consumeMoney)).intValue();
                if (CollUtil.isNotEmpty(ruleResult.getExtRewardList())) {
                    CollUtil.sort(ruleResult.getExtRewardList(), (o1, o2) -> o2.getExtConsumeMoney().compareTo(o1.getExtConsumeMoney()));
                    for (ScanRewardRuleResult.ExtReward extReward : ruleResult.getExtRewardList()) {
                        if (amount.compareTo(extReward.getExtConsumeMoney()) >= 0) {
                            rewardValue += extReward.getExtReward();
                            break;
                        }
                    }
                }
                Integer canReward = rewardValue;
                if (ruleResult.getPreMaxReward() != null && ruleResult.getPreMaxReward() >= 0 && rewardValue > ruleResult.getPreMaxReward()) {
                    if (!ignore) {
                        rewardResult.setOverLimit(true);
                        rewardResult.setPreMaxReward(ruleResult.getPreMaxReward());
                    }
                    canReward = ruleResult.getPreMaxReward();
                }
                if (ruleResult.getDayMaxReward() != null && ruleResult.getDayMaxReward() >= 0) {
                    DateTime now = DateTime.now();
                    List<Map<String, Object>> maps = memberRewardRecordMapper.selectMaps(new QueryWrapper<MemberRewardRecord>()
                            .select("coalesce(sum(reward_value), 0) total_reward")
                            .lambda()
                            .eq(MemberRewardRecord::getRewardType, rule.getRewardType())
                            .eq(MemberRewardRecord::getRewardSource, RewardSourceEnum.SCAN)
                            .eq(MemberRewardRecord::getMemberId, memberInfo.getId())
                            .ge(MemberRewardRecord::getCreateTime, DateUtil.beginOfDay(now))
                            .lt(MemberRewardRecord::getCreateTime, DateUtil.endOfDay(now))
                    );
                    if (CollUtil.isNotEmpty(maps)) {
                        Integer dayMax = Convert.toInt(maps.get(0).get("total_reward"), 0);
                        if (dayMax + canReward > ruleResult.getDayMaxReward()) {
                            if (!ignore) {
                                rewardResult.setOverLimit(true);
                                rewardResult.setDayMaxReward(ruleResult.getDayMaxReward());
                                rewardResult.setAlreadyDayReward(dayMax);
                            }
                            canReward = ruleResult.getDayMaxReward() - dayMax;
                        }
                    }
                }
                rewardResult.setRewardValue(rewardValue);
                rewardResult.setCanReward(NumberUtil.max(canReward, 0));
                result.put(rule.getRewardType(), rewardResult);
            }
        }
        //超过限制的数量为0则增加奖励
        if (result.values().stream().filter(MemberScanRewardResult::getOverLimit).count() <= 0) {
            for (MemberScanRewardResult rewardResult : result.values()) {
                MemberRewardRecord rewardRecord = new MemberRewardRecord();
                MdcUtil.setMemberInfo(rewardRecord, memberInfo);
                rewardRecord.setRewardSource(RewardSourceEnum.SCAN);
                rewardRecord.setRewardType(rewardResult.getRewardType());
                rewardRecord.setRewardValue(rewardResult.getCanReward());
                rewardRecord.setRemark(RewardSourceEnum.SCAN.getName());
                int count = memberRewardRecordMapper.insert(rewardRecord);
                if (count <= 0) {
                    throw new ServiceException("新增奖励失败");
                }
            }
            MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("扫码奖励后刷新奖励值缓存_" + memberInfo.getId(), () -> {
                for (MemberScanRewardResult reward : result.values()) {
                    refreshMemberReward(appId, memberInfo.getId(), reward.getRewardType());
                }
            }));
        }
        return result;
    }

    private void addMemberRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue, RewardSourceEnum rewardSource, String remark) {
        int memberReward = getMemberTotalReward(memberInfo.getId(), rewardType);
        if (memberReward + rewardValue < 0) {
            throw RewardErrorEnum.REWARD_ERROR_10602.getErrorMeta().getException(rewardType.getName());
        }
        MemberRewardRecord rewardRecord = new MemberRewardRecord();
        MdcUtil.setMemberInfo(rewardRecord, memberInfo);
        rewardRecord.setRewardSource(rewardSource);
        rewardRecord.setRewardType(rewardType);
        rewardRecord.setRewardValue(rewardValue);
        rewardRecord.setRemark(StrUtil.isNotBlank(remark) ? remark : rewardSource.getName());
        int count = memberRewardRecordMapper.insert(rewardRecord);
        if (count <= 0) {
            throw new ServiceException("新增奖励失败");
        }
        MdcUtil.publishTransactionalEvent(AfterCommitEvent.build(rewardSource.getName() + "后刷新奖励值缓存",
                () -> refreshMemberReward(MdcUtil.getMemberAppId(memberInfo), memberInfo.getId(), rewardType)));
    }

    @Override
    public void addExchangeRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue) {
        addMemberRewardRecord(memberInfo, rewardType, rewardValue, RewardSourceEnum.EXCHANGE, null);
    }

    @Override
    public void addLotteryRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue) {
        addMemberRewardRecord(memberInfo, rewardType, rewardValue, RewardSourceEnum.LOTTERY, null);
    }

    @Override
    public void addMissionRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue) {
        addMemberRewardRecord(memberInfo, rewardType, rewardValue, RewardSourceEnum.MISSION, null);
    }

    @Override
    public void addOfflineSignRewardRecord(MemberInfo memberInfo, RewardTypeEnum rewardType, Integer rewardValue) {
        addMemberRewardRecord(memberInfo, rewardType, rewardValue, RewardSourceEnum.OFFLINESIGN, null);
    }

    private Map<String, DateTime> getCleanDate(MemberRewardRule rule) {
        DateTime now = DateUtil.beginOfDay(DateTime.now());
        DateTime cleanDate = DateUtil.dateNew(now).setMutable(true);
        CleanRewardRuleResult ruleResult = JSONUtil.toBean(rule.getRuleContent(), CleanRewardRuleResult.class);
        String[] cleanDateStr = ruleResult.getCleanDate().split("-");
        if (cleanDateStr.length == 2) {
            int month = Convert.toInt(cleanDateStr[0]) - 1;
            month = Math.min(month, 11);
            int day = Convert.toInt(cleanDateStr[1]);
            cleanDate.setField(DateField.YEAR, now.getField(DateField.YEAR))
                    .setField(DateField.MONTH, month);
            int maxDay = DateUtil.getEndValue(cleanDate.toCalendar(), DateField.DAY_OF_MONTH.getValue());
            if (day > maxDay) {
                day = maxDay;
            }
            cleanDate.setField(DateField.DAY_OF_MONTH, day);
            DateTime cleanBefore = DateUtil.dateNew(cleanDate);
            if (ruleResult.getDateUnitType() != null && ruleResult.getCleanDelay() != null) {
                switch (ruleResult.getDateUnitType()) {
                    case YEAR: {
                        cleanDate.offset(DateField.YEAR, ruleResult.getCleanDelay()).setField(DateField.YEAR, DateUtil.thisYear());
                        cleanBefore = DateUtil.dateNew(cleanDate).offset(DateField.YEAR, -ruleResult.getCleanDelay());
                        break;
                    }
                    case MONTH: {
                        cleanDate.offset(DateField.MONTH, ruleResult.getCleanDelay()).setField(DateField.YEAR, DateUtil.thisYear());
                        cleanBefore = DateUtil.dateNew(cleanDate).offset(DateField.MONTH, -ruleResult.getCleanDelay());
                        break;
                    }
                    case DAY: {
                        cleanDate.offset(DateField.DAY_OF_YEAR, ruleResult.getCleanDelay()).setField(DateField.YEAR, DateUtil.thisYear());
                        cleanBefore = DateUtil.dateNew(cleanDate).offset(DateField.DAY_OF_YEAR, -ruleResult.getCleanDelay());
                        break;
                    }
                    default:
                }
            }
            if (cleanDate.before(now)) {
                cleanDate.offset(DateField.YEAR, 1);
                cleanBefore.offset(DateField.YEAR, 1);
            }
            Map<String, DateTime> result = CollUtil.newHashMap();
            result.put("cleanDate", cleanDate);
            result.put("cleanBefore", cleanBefore);
            return result;
        } else {
            throw new ServiceException("清零规则日期格式错误");
        }
    }

    @Transactional
    @Override
    public void addCleanRewardRecord() {
        List<MemberRewardRule> ruleList = memberRewardRuleMapper.normalSelectList(Wrappers.<MemberRewardRule>lambdaQuery()
                .eq(MemberRewardRule::getRuleType, RuleTypeEnum.CLEAN)
                .eq(MemberRewardRule::getState, StateEnum.NORMAL)
        );
        DateTime now = DateUtil.beginOfDay(DateTime.now());
        for (MemberRewardRule rule : ruleList) {
            Map<String, DateTime> result = getCleanDate(rule);
            DateTime cleanDate = result.get("cleanDate");
            DateTime cleanBefore = result.get("cleanBefore");
            if (cleanDate.equals(now)) {
                //设置此次请求租户id
                ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, rule.getTenantId());
                ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, rule.getSubTenantId());
                List<Map<String, Object>> list = memberRewardRecordMapper.selectMaps(new QueryWrapper<MemberRewardRecord>()
                        .select("coalesce(sum(reward_value), 0) total, member_id")
                        .lambda()
                        .eq(MemberRewardRecord::getRewardType, rule.getRewardType())
                        .eq(MemberRewardRecord::getAppId, rule.getAppId())
                        .nested(i -> i.le(MemberRewardRecord::getCreateTime, cleanBefore)
                                .or(j -> j.le(MemberRewardRecord::getRewardValue, 0)
                                        .gt(MemberRewardRecord::getCreateTime, cleanBefore)
                                        .le(MemberRewardRecord::getCreateTime, DateTime.now())
                                )
                        )
                        .groupBy(MemberRewardRecord::getMemberId)
                );
                for (Map<String, Object> map : list) {
                    Long memberId = Convert.toLong(map.get("member_id"));
                    Integer total = Convert.toInt(map.get("total"));
                    total = NumberUtil.max(total, 0);
                    if (total > 0) {
                        MemberRewardRecord rewardRecord = new MemberRewardRecord();
                        rewardRecord.setRewardSource(RewardSourceEnum.CLEAN);
                        rewardRecord.setAppId(rule.getAppId());
                        rewardRecord.setMemberId(memberId);
                        rewardRecord.setRewardType(rule.getRewardType());
                        rewardRecord.setRewardValue(-total);
                        rewardRecord.setRemark(RewardSourceEnum.CLEAN.getName());
                        int count = memberRewardRecordMapper.insert(rewardRecord);
                        if (count <= 0) {
                            throw new ServiceException("新增奖励失败");
                        }
                    }
                }
                //全部更新后再刷新缓存,以防事务回滚而缓存没回滚
                MdcUtil.publishTransactionalEvent(AfterCommitEvent.build("清零积分任务后刷新奖励值缓存", () -> {
                    for (Map<String, Object> map : list) {
                        Long memberId = Convert.toLong(map.get("member_id"));
                        refreshMemberReward(rule.getAppId(), memberId, rule.getRewardType());
                    }
                }));
            }
        }
    }
}
