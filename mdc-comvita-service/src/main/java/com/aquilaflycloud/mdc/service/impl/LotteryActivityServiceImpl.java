package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.apply.ApplyStateEnum;
import com.aquilaflycloud.mdc.enums.common.CreateSourceEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.coupon.CouponStateEnum;
import com.aquilaflycloud.mdc.enums.coupon.ReceiveSourceEnum;
import com.aquilaflycloud.mdc.enums.lottery.FreeLimitEnum;
import com.aquilaflycloud.mdc.enums.lottery.LotteryStateEnum;
import com.aquilaflycloud.mdc.enums.lottery.PrizeTypeEnum;
import com.aquilaflycloud.mdc.mapper.CouponMemberRelMapper;
import com.aquilaflycloud.mdc.mapper.LotteryActivityMapper;
import com.aquilaflycloud.mdc.mapper.LotteryMemberRecordMapper;
import com.aquilaflycloud.mdc.mapper.LotteryPrizeMapper;
import com.aquilaflycloud.mdc.message.LotteryErrorEnum;
import com.aquilaflycloud.mdc.model.coupon.CouponInfo;
import com.aquilaflycloud.mdc.model.coupon.CouponMemberRel;
import com.aquilaflycloud.mdc.model.lottery.LotteryActivity;
import com.aquilaflycloud.mdc.model.lottery.LotteryMemberRecord;
import com.aquilaflycloud.mdc.model.lottery.LotteryPrize;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.param.coupon.CouponGetParam;
import com.aquilaflycloud.mdc.param.lottery.*;
import com.aquilaflycloud.mdc.result.lottery.*;
import com.aquilaflycloud.mdc.service.CouponInfoService;
import com.aquilaflycloud.mdc.service.LotteryActivityService;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * LotteryActivityServiceImpl
 *
 * @author star
 * @date 2020-04-06
 */
@Slf4j
@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {
    @Resource
    private LotteryActivityMapper lotteryActivityMapper;
    @Resource
    private LotteryPrizeMapper lotteryPrizeMapper;
    @Resource
    private LotteryMemberRecordMapper lotteryMemberRecordMapper;
    @Resource
    private CouponMemberRelMapper couponMemberRelMapper;
    @Resource
    private CouponInfoService couponInfoService;
    @Resource
    private MemberRewardService memberRewardService;

    private LotteryActivity stateHandler(LotteryActivity lotteryActivity) {
        if (lotteryActivity == null) {
            throw new ServiceException("抽奖活动不存在");
        }
        DateTime now = DateTime.now();
        if (lotteryActivity.getState() != LotteryStateEnum.DISABLE) {
            if (now.isBefore(lotteryActivity.getStartTime())) {
                lotteryActivity.setState(LotteryStateEnum.PENDING);
            } else if (now.isAfter(lotteryActivity.getEndTime())) {
                lotteryActivity.setState(LotteryStateEnum.EXPIRED);
            }
        }
        return lotteryActivity;
    }

    private PrizeResult convertResult(LotteryPrize prize) {
        if (prize == null) {
            return null;
        }
        PrizeResult prizeResult = Convert.convert(PrizeResult.class, prize);
        prizeResult.setAlgorithm(JSONUtil.toBean(prizeResult.getAlgorithmContent(), Algorithm.class));
        switch (prizeResult.getPrizeType()) {
            case REWARD: {
                prizeResult.setLotteryReward(JSONUtil.toBean(prizeResult.getRelContent(), LotteryReward.class));
                break;
            }
            case COUPON: {
                prizeResult.setLotteryCoupon(JSONUtil.toBean(prizeResult.getRelContent(), LotteryCoupon.class));
                break;
            }
            default:
        }
        return prizeResult;
    }

    @Override
    public IPage<LotteryActivity> pageLottery(LotteryPageParam param) {
        if (param.getValid() == WhetherEnum.YES) {
            param.setState(null);
        }
        DateTime now = DateTime.now();
        LotteryStateEnum state = param.getState();
        return lotteryActivityMapper.selectPage(param.page(), Wrappers.<LotteryActivity>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getLotteryName()), LotteryActivity::getLotteryName, param.getLotteryName())
                .eq(StrUtil.isNotBlank(param.getAppId()), LotteryActivity::getAppId, param.getAppId())
                .like(StrUtil.isNotBlank(param.getCreatorName()), LotteryActivity::getCreatorName, param.getCreatorName())
                .ge(param.getCreateTimeStart() != null, LotteryActivity::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, LotteryActivity::getCreateTime, param.getCreateTimeEnd())
                .eq(param.getLotteryMode() != null, LotteryActivity::getLotteryMode, param.getLotteryMode())
                .and(param.getValid() == WhetherEnum.YES, i -> i
                        .eq(LotteryActivity::getState, LotteryStateEnum.NORMAL)
                        .ge(LotteryActivity::getEndTime, now))
                .and(state == LotteryStateEnum.NORMAL, i -> i
                        .eq(LotteryActivity::getState, LotteryStateEnum.NORMAL)
                        .le(LotteryActivity::getStartTime, now)
                        .ge(LotteryActivity::getEndTime, now))
                .eq(state == LotteryStateEnum.DISABLE, LotteryActivity::getState, LotteryStateEnum.DISABLE)
                .and(state == LotteryStateEnum.PENDING, i -> i
                        .eq(LotteryActivity::getState, LotteryStateEnum.NORMAL)
                        .gt(LotteryActivity::getStartTime, now))
                .and(state == LotteryStateEnum.EXPIRED, i -> i
                        .eq(LotteryActivity::getState, LotteryStateEnum.NORMAL)
                        .lt(LotteryActivity::getEndTime, now))
                .orderByDesc(LotteryActivity::getCreateTime)
        ).convert(this::stateHandler);
    }

    @Override
    public LotteryResult getLottery(LotteryGetParam param) {
        LotteryActivity lottery = lotteryActivityMapper.selectById(param.getId());
        lottery = stateHandler(lottery);
        LotteryResult result = Convert.convert(LotteryResult.class, lottery);
        int lotteryNum = lotteryMemberRecordMapper.selectCount(Wrappers.<LotteryMemberRecord>lambdaQuery()
                .eq(LotteryMemberRecord::getLotteryId, param.getId())
        );
        int wonNum = lotteryMemberRecordMapper.selectCount(Wrappers.<LotteryMemberRecord>lambdaQuery()
                .eq(LotteryMemberRecord::getLotteryId, param.getId())
                .isNotNull(LotteryMemberRecord::getPrizeType)
        );
        int prizeNum = lotteryPrizeMapper.selectMaps(new QueryWrapper<LotteryPrize>()
                .select("coalesce(sum(inventory), 0) total")
                .lambda()
                .eq(LotteryPrize::getLotteryId, param.getId())
        ).stream().map(map -> Convert.toInt(map.get("total"))).collect(toList()).get(0);
        List<PrizeResult> prizeResultList = lotteryPrizeMapper.selectList(Wrappers.<LotteryPrize>lambdaQuery()
                .eq(LotteryPrize::getLotteryId, param.getId())
                .orderByAsc(LotteryPrize::getPrizeOrder)
                .orderByDesc(LotteryPrize::getCreateTime)
        ).stream().map(prize -> {
            PrizeResult prizeResult = Convert.convert(PrizeResult.class, prize);
            prizeResult.setAlgorithm(JSONUtil.toBean(prizeResult.getAlgorithmContent(), Algorithm.class));
            switch (prizeResult.getPrizeType()) {
                case REWARD: {
                    prizeResult.setLotteryReward(JSONUtil.toBean(prizeResult.getRelContent(), LotteryReward.class));
                    break;
                }
                case COUPON: {
                    CouponInfo couponInfo = couponInfoService.getCoupon(prizeResult.getRelId());
                    LotteryCoupon lotteryCoupon = new LotteryCoupon();
                    BeanUtil.copyProperties(couponInfo, lotteryCoupon);
                    prizeResult.setLotteryCoupon(lotteryCoupon);
                    break;
                }
                default:
            }
            return prizeResult;
        }).collect(toList());
        result.setLotteryNum(Convert.toLong(lotteryNum));
        result.setWonNum(Convert.toLong(wonNum));
        result.setPrizeNum(Convert.toLong(prizeNum));
        result.setLotteryRule(JSONUtil.toBean(result.getLotteryRuleContent(), LotteryRule.class));
        result.setAlgorithm(JSONUtil.toBean(result.getAlgorithmContent(), Algorithm.class));
        result.setPrizeList(prizeResultList);
        return result;
    }

    private void addPrizeList(Long lotteryId, List<PrizeAddParam> prizeAddParamList) {
        List<LotteryPrize> prizeList = new ArrayList<>();
        for (PrizeAddParam addParam : prizeAddParamList) {
            LotteryPrize prize = new LotteryPrize();
            BeanUtil.copyProperties(addParam, prize);
            prize.setWonCount(0);
            prize.setLotteryId(lotteryId);
            prize.setAlgorithmContent(JSONUtil.toJsonStr(addParam.getAlgorithm()));
            switch (addParam.getPrizeType()) {
                case REWARD: {
                    LotteryReward lotteryReward = new LotteryReward();
                    lotteryReward.setRewardType(addParam.getRewardType());
                    lotteryReward.setRewardValue(addParam.getRewardValue());
                    prize.setRelContent(JSONUtil.toJsonStr(lotteryReward));
                    break;
                }
                case COUPON: {
                    CouponInfo couponInfo = couponInfoService.getCoupon(addParam.getRelId());
                    if (couponInfo.getState() != CouponStateEnum.NORMAL
                            && couponInfo.getState() != CouponStateEnum.EXPIRING) {
                        throw new ServiceException("优惠券不可用");
                    }
                    LotteryCoupon lotteryCoupon = new LotteryCoupon();
                    BeanUtil.copyProperties(couponInfo, lotteryCoupon);
                    prize.setRelId(couponInfo.getId());
                    prize.setRelContent(JSONUtil.toJsonStr(lotteryCoupon));
                    break;
                }
                default:
            }
            prizeList.add(prize);
        }
        if (prizeList.size() > 0) {
            int count = lotteryPrizeMapper.insertAllBatch(prizeList);
            if (count <= 0) {
                throw new ServiceException("保存奖品失败");
            }
        }
    }

    private void editPrizeList(Long lotteryId, List<PrizeEditParam> prizeEditParamList) {
        for (PrizeEditParam editParam : prizeEditParamList) {
            LotteryPrize prize = lotteryPrizeMapper.selectOne(Wrappers.<LotteryPrize>lambdaQuery()
                    .eq(LotteryPrize::getId, editParam.getId())
                    .eq(LotteryPrize::getLotteryId, lotteryId)
            );
            if (prize == null) {
                continue;
            }
            Algorithm algorithm = JSONUtil.toBean(prize.getAlgorithmContent(), Algorithm.class);
            LotteryPrize update = new LotteryPrize();
            BeanUtil.copyProperties(editParam, update);
            BeanUtil.copyProperties(editParam.getAlgorithm(), algorithm, CopyOptions.create().ignoreNullValue());
            update.setAlgorithmContent(JSONUtil.toJsonStr(algorithm));
            update.setInventory(prize.getInventory() + editParam.getInventoryIncrease());
            switch (prize.getPrizeType()) {
                case REWARD: {
                    if (editParam.getRewardType() != null && editParam.getRewardValue() != null) {
                        LotteryReward lotteryReward = new LotteryReward();
                        lotteryReward.setRewardType(editParam.getRewardType());
                        lotteryReward.setRewardValue(editParam.getRewardValue());
                        prize.setRelContent(JSONUtil.toJsonStr(lotteryReward));
                    }
                    break;
                }
                case COUPON: {
                    if (editParam.getRelId() != null) {
                        CouponInfo couponInfo = couponInfoService.getCoupon(editParam.getRelId());
                        if (couponInfo.getState() != CouponStateEnum.NORMAL
                                && couponInfo.getState() != CouponStateEnum.EXPIRING) {
                            throw new ServiceException("优惠券不可用");
                        }
                        LotteryCoupon lotteryCoupon = new LotteryCoupon();
                        BeanUtil.copyProperties(couponInfo, lotteryCoupon);
                        update.setRelId(couponInfo.getId());
                        update.setRelContent(JSONUtil.toJsonStr(lotteryCoupon));
                    }
                    break;
                }
                default:
            }
            lotteryPrizeMapper.updateById(update);
        }
    }

    @Transactional
    @Override
    public void addLottery(LotteryAddParam param) {
        LotteryActivity lottery = new LotteryActivity();
        BeanUtil.copyProperties(param, lottery);
        lottery.setState(LotteryStateEnum.NORMAL);
        if (param.getLotteryRule().getFreeLimit() == FreeLimitEnum.NONE) {
            param.getLotteryRule().setFreeLimitContent(null);
        }
        lottery.setLotteryRuleContent(JSONUtil.toJsonStr(param.getLotteryRule()));
        lottery.setAlgorithmContent(JSONUtil.toJsonStr(param.getAlgorithm()));
        int count = lotteryActivityMapper.insert(lottery);
        if (count <= 0) {
            throw new ServiceException("新增抽奖活动失败");
        }
        addPrizeList(lottery.getId(), param.getPrizeList());
    }

    @Transactional
    @Override
    public void editLottery(LotteryEditParam param) {
        LotteryActivity lottery = lotteryActivityMapper.selectById(param.getId());
        lottery = stateHandler(lottery);
        LotteryRule lotteryRule = JSONUtil.toBean(lottery.getLotteryRuleContent(), LotteryRule.class);
        Algorithm algorithm = JSONUtil.toBean(lottery.getAlgorithmContent(), Algorithm.class);
        LotteryActivity update = new LotteryActivity();
        BeanUtil.copyProperties(param, update);
        BeanUtil.copyProperties(param.getLotteryRule(), lotteryRule, CopyOptions.create().ignoreNullValue());
        if (lotteryRule.getFreeLimit() == FreeLimitEnum.NONE) {
            lotteryRule.setFreeLimitContent(null);
        }
        BeanUtil.copyProperties(param.getAlgorithm(), algorithm, CopyOptions.create().ignoreNullValue());
        update.setLotteryRuleContent(JSONUtil.toJsonStr(lotteryRule));
        update.setAlgorithmContent(JSONUtil.toJsonStr(algorithm));
        int count = lotteryActivityMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("修改抽奖活动失败");
        }
        if (CollUtil.isNotEmpty(param.getPrizeAddList()) || CollUtil.isNotEmpty(param.getPrizeEditList()) || CollUtil.isNotEmpty(param.getPrizeDeleteList())) {
            RedisUtil.transactionalLock("changePrize" + param.getId());
            addPrizeList(param.getId(), param.getPrizeAddList());
            editPrizeList(param.getId(), param.getPrizeEditList());
            if (param.getPrizeDeleteList().size() > 0) {
                List<LotteryPrize> prizeList = lotteryPrizeMapper.selectList(Wrappers.<LotteryPrize>lambdaQuery()
                        .select(LotteryPrize::getId)
                        .eq(LotteryPrize::getLotteryId, param.getId())
                );
                List<Long> ids = prizeList.stream().map(LotteryPrize::getId).collect(toList());
                lotteryPrizeMapper.deleteBatchIds(ids);
            }
        }
    }

    @Override
    public void toggleState(LotteryGetParam param) {
        LotteryActivity lottery = lotteryActivityMapper.selectById(param.getId());
        if (lottery == null) {
            throw new ServiceException("抽奖活动不存在");
        }
        LotteryActivity update = new LotteryActivity();
        update.setId(param.getId());
        update.setState(lottery.getState() == LotteryStateEnum.NORMAL ? LotteryStateEnum.DISABLE : LotteryStateEnum.NORMAL);
        int count = lotteryActivityMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public void releaseLottery(LotteryGetParam param) {
        LotteryActivity lottery = lotteryActivityMapper.selectById(param.getId());
        lottery = stateHandler(lottery);
        if (lottery.getState() != LotteryStateEnum.PENDING) {
            throw new ServiceException("抽奖活动" + lottery.getState().getName() + ", 不能发布");
        }
        LotteryActivity update = new LotteryActivity();
        update.setId(param.getId());
        update.setStartTime(DateTime.now());
        int count = lotteryActivityMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("发布失败");
        }
    }

    @Override
    public StatisticsResult getStatistics() {
        StatisticsResult result = new StatisticsResult();
        int total = lotteryActivityMapper.selectCount(Wrappers.lambdaQuery());
        int effectiveTotal = lotteryActivityMapper.selectCount(Wrappers.<LotteryActivity>lambdaQuery()
                .eq(LotteryActivity::getState, ApplyStateEnum.NORMAL)
                .ge(LotteryActivity::getEndTime, DateTime.now())
        );
        int lotteryTotal = lotteryMemberRecordMapper.selectCount(Wrappers.lambdaQuery());
        int wonTotal = lotteryMemberRecordMapper.selectCount(Wrappers.<LotteryMemberRecord>lambdaQuery()
                .isNotNull(LotteryMemberRecord::getPrizeType)
        );
        result.setTotal(Convert.toLong(total));
        result.setEffectiveTotal(Convert.toLong(effectiveTotal));
        result.setLotteryTotal(Convert.toLong(lotteryTotal));
        result.setWonTotal(Convert.toLong(wonTotal));
        return result;
    }

    @Override
    public IPage<LotteryMemberRecord> pageRecord(RecordPageParam param) {
        return lotteryMemberRecordMapper.selectPage(param.page(), Wrappers.<LotteryMemberRecord>lambdaQuery()
                .eq(LotteryMemberRecord::getLotteryId, param.getLotteryId())
                .like(StrUtil.isNotBlank(param.getNickName()), LotteryMemberRecord::getNickName, param.getNickName())
                .like(StrUtil.isNotBlank(param.getPrizeLevel()), LotteryMemberRecord::getPrizeLevel, param.getPrizeLevel())
                .ge(param.getCreateTimeStart() != null, LotteryMemberRecord::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, LotteryMemberRecord::getCreateTime, param.getCreateTimeEnd())
                .orderByDesc(LotteryMemberRecord::getCreateTime)
        );
    }

    @Override
    public IPage<LotteryActivity> pageLotteryActivity(PageParam<LotteryActivity> param) {
        return lotteryActivityMapper.selectPage(param.page(), Wrappers.<LotteryActivity>lambdaQuery()
                .eq(LotteryActivity::getState, LotteryStateEnum.NORMAL)
                .orderByDesc(LotteryActivity::getCreateTime)
        ).convert(this::stateHandler);
    }

    @Override
    public LotteryActivityResult getLotteryActivity(LotteryGetParam param) {
        LotteryActivity lottery = lotteryActivityMapper.selectById(param.getId());
        lottery = stateHandler(lottery);
        LotteryActivityResult result = Convert.convert(LotteryActivityResult.class, lottery);
        result.setLotteryRule(JSONUtil.toBean(result.getLotteryRuleContent(), LotteryRule.class));
        List<PrizeResult> prizeResultList = lotteryPrizeMapper.selectList(Wrappers.<LotteryPrize>lambdaQuery()
                .eq(LotteryPrize::getLotteryId, param.getId())
                .orderByAsc(LotteryPrize::getPrizeOrder)
                .orderByDesc(LotteryPrize::getCreateTime)
        ).stream().map(this::convertResult).collect(toList());
        result.setPrizeList(prizeResultList);
        Integer surplusLotteryNum = null;
        Long memberId = MdcUtil.getCurrentMemberId();
        if (memberId != null) {
            DateTime now = DateTime.now();
            surplusLotteryNum = Convert.toInt(surplusLotteryNum, 0);
            int dailyCount = lotteryMemberRecordMapper.selectCount(Wrappers.<LotteryMemberRecord>lambdaQuery()
                    .eq(LotteryMemberRecord::getLotteryId, param.getId())
                    .eq(LotteryMemberRecord::getMemberId, memberId)
                    .ge(LotteryMemberRecord::getCreateTime, DateUtil.beginOfDay(now))
                    .le(LotteryMemberRecord::getCreateTime, DateUtil.endOfDay(now))
            );
            int totalCount = lotteryMemberRecordMapper.selectCount(Wrappers.<LotteryMemberRecord>lambdaQuery()
                    .eq(LotteryMemberRecord::getLotteryId, param.getId())
                    .eq(LotteryMemberRecord::getMemberId, memberId)
            );
            LotteryRule rule = result.getLotteryRule();
            if (rule.getDailyLimit()) {
                surplusLotteryNum = rule.getDailyCount() - dailyCount;
            }
            if (rule.getTotalLimit()) {
                if (surplusLotteryNum > rule.getTotalCount() - totalCount) {
                    surplusLotteryNum = rule.getTotalCount() - totalCount;
                }
            }
            surplusLotteryNum = NumberUtil.max(surplusLotteryNum, 0);
        }
        result.setSurplusLotteryNum(surplusLotteryNum);
        return result;
    }

    @Override
    public List<LotteryMemberRecord> listLotteryRecord(LotteryGetParam param) {
        String key = "lotteryRecordList" + param.getId();
        if (RedisUtil.redis().hasKey(key)) {
            return RedisUtil.<LotteryMemberRecord>listRedis().range(key, 0, 10);
        } else {
            List<LotteryMemberRecord> prizeList = lotteryMemberRecordMapper.selectList(Wrappers.<LotteryMemberRecord>lambdaQuery()
                    .eq(LotteryMemberRecord::getLotteryId, param.getId())
                    .isNotNull(LotteryMemberRecord::getPrizeId)
                    .orderByDesc(LotteryMemberRecord::getCreateTime)
                    .last("limit 10")
            );
            if (CollUtil.isNotEmpty(prizeList)) {
                RedisUtil.<LotteryMemberRecord>listRedis().leftPushAll(key, prizeList);
                RedisUtil.redis().expire(key, 7, TimeUnit.DAYS);
            }
            return prizeList;
        }
    }

    @Transactional
    @Override
    public PrizeResult addLotteryRecord(LotteryGetParam param) {
        RedisUtil.transactionalLock("changePrize" + param.getId());
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        LotteryActivity lottery = lotteryActivityMapper.selectById(param.getId());
        lottery = stateHandler(lottery);
        if (lottery.getState() != LotteryStateEnum.NORMAL) {
            throw new ServiceException("该抽奖活动" + lottery.getState().getName() + ", 不能抽奖");
        }
        LotteryRule lotteryRule = JSONUtil.toBean(lottery.getLotteryRuleContent(), LotteryRule.class);
        Algorithm algorithm = JSONUtil.toBean(lottery.getAlgorithmContent(), Algorithm.class);
        boolean needCost = true;
        if (lotteryRule.getFreeLimit() != null) {
            if (lotteryRule.getFreeLimit() == FreeLimitEnum.AUTHTIME && memberInfo.getAuthTime() != null) {
                String[] content = lotteryRule.getFreeLimitContent().split(";");
                String start = content[0];
                String end = content[1];
                if (StrUtil.isAllNotBlank(start, end)) {
                    DateTime startTime = DateUtil.parse(start);
                    DateTime endTime = DateUtil.parse(end);
                    if (DateTime.of(memberInfo.getAuthTime()).isIn(startTime, endTime)) {
                        needCost = false;
                    }
                } else if (StrUtil.isNotBlank(start)) {
                    DateTime time = DateUtil.parse(start);
                    if (time.isBeforeOrEquals(memberInfo.getAuthTime())) {
                        needCost = false;
                    }
                } else if (StrUtil.isNotBlank(end)) {
                    DateTime time = DateUtil.parse(end);
                    if (time.isAfterOrEquals(memberInfo.getAuthTime())) {
                        needCost = false;
                    }
                }
            }
        }
        if (needCost && lotteryRule.getRewardType() != null && lotteryRule.getConsumeReward() != null && lotteryRule.getConsumeReward() > 0) {
            memberRewardService.addLotteryRewardRecord(memberInfo, lotteryRule.getRewardType(), -lotteryRule.getConsumeReward(), lottery.getId());
        }
        DateTime now = DateTime.now();
        if (lotteryRule.getTotalLimit()) {
            int count = lotteryMemberRecordMapper.selectCount(Wrappers.<LotteryMemberRecord>lambdaQuery()
                    .eq(LotteryMemberRecord::getMemberId, memberInfo.getId())
                    .eq(LotteryMemberRecord::getLotteryId, param.getId())
            );
            if (count + 1 > lotteryRule.getTotalCount()) {
                throw LotteryErrorEnum.LOTTERY_ERROR_10702.getErrorMeta().getException("总");
            }
        }
        if (lotteryRule.getDailyLimit()) {
            int count = lotteryMemberRecordMapper.selectCount(Wrappers.<LotteryMemberRecord>lambdaQuery()
                    .eq(LotteryMemberRecord::getMemberId, memberInfo.getId())
                    .eq(LotteryMemberRecord::getLotteryId, param.getId())
                    .ge(LotteryMemberRecord::getCreateTime, DateUtil.beginOfDay(now))
                    .lt(LotteryMemberRecord::getCreateTime, DateUtil.endOfDay(now))
            );
            if (count + 1 > lotteryRule.getDailyCount()) {
                throw LotteryErrorEnum.LOTTERY_ERROR_10702.getErrorMeta().getException("每日");
            }
        }
        if (algorithm.getWinningOdds() < RandomUtil.randomInt(1, 100)) {
            addLotteryRecord(param.getId(), null, null, memberInfo);
            return null;
        }
        List<LotteryPrize> prizeList = lotteryPrizeMapper.selectList(Wrappers.<LotteryPrize>lambdaQuery()
                .eq(LotteryPrize::getLotteryId, param.getId())
                .orderByAsc(LotteryPrize::getPrizeOrder)
                .orderByDesc(LotteryPrize::getCreateTime)
        );
        JSONArray array = new JSONArray();
        int total = 1;
        for (LotteryPrize prize : prizeList) {
            int start = total;
            int end = total;
            Algorithm prizeAlgorithm = JSONUtil.toBean(prize.getAlgorithmContent(), Algorithm.class);
            if (prizeAlgorithm != null && prizeAlgorithm.getWinningOdds() != null) {
                end += prizeAlgorithm.getWinningOdds();
            } else {
                int canWin = prize.getInventory() - Convert.toInt(prize.getWonCount(), 0);
                end += Math.max(canWin, 0);
            }
            if (end > 1) {
                total = end;
                JSONObject prizeJson = new JSONObject();
                prizeJson.set("id", prize.getId());
                prizeJson.set("start", start);
                prizeJson.set("end", end);
                array.add(prizeJson);
            }
        }
        Long recordId = MdcUtil.getSnowflakeId();
        Map<Long, LotteryPrize> prizeMap = prizeList.stream().collect(Collectors.toMap(LotteryPrize::getId, prize -> prize));
        if (total <= 1) {
            addLotteryRecord(param.getId(), null, null, memberInfo);
            return null;
        }
        int luck = RandomUtil.randomInt(1, total);
        LotteryPrize luckPrize = null;
        for (JSONObject prizeJson : array.jsonIter()) {
            if (luck >= prizeJson.getInt("start") && luck < prizeJson.getInt("end")) {
                luckPrize = prizeMap.get(prizeJson.getLong("id"));
                switch (luckPrize.getPrizeType()) {
                    case REWARD: {
                        LotteryReward reward = JSONUtil.toBean(luckPrize.getRelContent(), LotteryReward.class);
                        memberRewardService.addLotteryRewardRecord(memberInfo, reward.getRewardType(), reward.getRewardValue(), lottery.getId());
                        break;
                    }
                    case COUPON: {
                        try {
                            couponInfoService.addRel(new CouponGetParam().setId(luckPrize.getRelId()), 1, memberInfo,
                                    ReceiveSourceEnum.LOTTERY, recordId, CreateSourceEnum.NORMAL);
                        } catch (ServiceException e) {
                            //领取失败当作未中奖
                            luckPrize = null;
                        }
                        break;
                    }
                    default:
                }
            }
        }
        LotteryMemberRecord record = addLotteryRecord(param.getId(), recordId, luckPrize, memberInfo);
        PrizeResult prizeResult = null;
        if (luckPrize != null) {
            luckPrize.setWonCount(luckPrize.getWonCount() + 1);
            int count = lotteryPrizeMapper.update(null, Wrappers.<LotteryPrize>lambdaUpdate()
                    .set(LotteryPrize::getWonCount, luckPrize.getWonCount())
                    .eq(LotteryPrize::getId, luckPrize.getId())
            );
            if (count <= 0) {
                throw new ServiceException("更新奖品库存失败");
            }
            prizeResult = convertResult(luckPrize);
            String key = "lotteryRecordList" + param.getId();
            if (RedisUtil.redis().hasKey(key)) {
                RedisUtil.<LotteryMemberRecord>listRedis().leftPush(key, record);
            }
        }
        return prizeResult;
    }

    private LotteryMemberRecord addLotteryRecord(Long lotteryId, Long recordId, LotteryPrize prize, MemberInfo memberInfo) {
        LotteryMemberRecord record = new LotteryMemberRecord();
        Long prizeId = null;
        if (prize == null) {
            record.setPrizeLevel("未中奖");
        } else {
            BeanUtil.copyProperties(prize, record, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
            prizeId = prize.getId();
        }
        MdcUtil.setMemberInfo(record, memberInfo);
        record.setId(recordId);
        record.setPrizeId(prizeId);
        record.setLotteryId(lotteryId);
        int count = lotteryMemberRecordMapper.insert(record);
        if (count <= 0) {
            throw new ServiceException("新增抽奖记录失败");
        }
        return record;
    }

    @Override
    public IPage<RecordResult> pageLotteryRecord(LotteryRecordPageParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        return lotteryMemberRecordMapper.selectPage(param.page(), Wrappers.<LotteryMemberRecord>lambdaQuery()
                .eq(LotteryMemberRecord::getMemberId, memberId)
                .eq(LotteryMemberRecord::getLotteryId, param.getLotteryId())
                .isNotNull(LotteryMemberRecord::getPrizeId)
                .orderByDesc(LotteryMemberRecord::getCreateTime)
        ).convert(record -> {
            RecordResult result = Convert.convert(RecordResult.class, record);
            if (result.getPrizeType() == PrizeTypeEnum.COUPON) {
                List<CouponMemberRel> relList = couponMemberRelMapper.selectList(Wrappers.<CouponMemberRel>lambdaQuery()
                        .select(CouponMemberRel::getId)
                        .eq(CouponMemberRel::getReceiveSourceId, result.getId())
                );
                if (relList.size() > 0) {
                    result.setCouponRelId(relList.get(0).getId());
                }
            }
            return result;
        });
    }

    @Override
    public IPage<LotteryMemberRecordResult> pageLotteryMemberRecord(PageParam<LotteryMemberRecord> param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        return lotteryMemberRecordMapper.selectPage(param.page(), Wrappers.<LotteryMemberRecord>lambdaQuery()
                .eq(LotteryMemberRecord::getMemberId, memberId)
                .groupBy(LotteryMemberRecord::getLotteryId)
                .orderByDesc(LotteryMemberRecord::getCreateTime)
        ).convert(record -> {
            LotteryMemberRecordResult result = Convert.convert(LotteryMemberRecordResult.class, record);
            LotteryActivity lottery = lotteryActivityMapper.selectById(result.getLotteryId());
            lottery = stateHandler(lottery);
            result.setLotteryActivity(lottery);
            return result;
        });
    }
}
