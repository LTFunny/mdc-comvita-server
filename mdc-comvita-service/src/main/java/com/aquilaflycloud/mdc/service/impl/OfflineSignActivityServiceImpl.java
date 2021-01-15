package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.sign.LimitTypeEnum;
import com.aquilaflycloud.mdc.enums.sign.SignStateEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.CouponMemberRelMapper;
import com.aquilaflycloud.mdc.mapper.OfflineSignActivityMapper;
import com.aquilaflycloud.mdc.mapper.OfflineSignMemberRecordMapper;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.sign.OfflineSignActivity;
import com.aquilaflycloud.mdc.model.sign.OfflineSignMemberRecord;
import com.aquilaflycloud.mdc.param.sign.*;
import com.aquilaflycloud.mdc.param.wechat.MiniProgramQrCodeUnLimitGetParam;
import com.aquilaflycloud.mdc.result.sign.*;
import com.aquilaflycloud.mdc.service.ClientConfigService;
import com.aquilaflycloud.mdc.service.CouponInfoService;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.OfflineSignActivityService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * OfflineSignActivityServiceImpl
 *
 * @author star
 * @date 2020-05-07
 */
@Slf4j
@Service
public class OfflineSignActivityServiceImpl implements OfflineSignActivityService {
    @Resource
    private OfflineSignActivityMapper offlineSignActivityMapper;
    @Resource
    private OfflineSignMemberRecordMapper offlineSignMemberRecordMapper;
    @Resource
    private CouponMemberRelMapper couponMemberRelMapper;
    @Resource
    private CouponInfoService couponInfoService;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private ClientConfigService clientConfigService;
    @Resource
    private WechatMiniService wechatMiniService;

    private OfflineSignActivity stateHandler(OfflineSignActivity offlineSignActivity) {
        if (offlineSignActivity == null) {
            throw new ServiceException("打卡活动不存在");
        }
        DateTime now = DateTime.now();
        if (offlineSignActivity.getState() != SignStateEnum.DISABLE) {
            if (now.isBefore(offlineSignActivity.getStartTime())) {
                offlineSignActivity.setState(SignStateEnum.PENDING);
            } else if (now.isAfter(offlineSignActivity.getEndTime())) {
                offlineSignActivity.setState(SignStateEnum.EXPIRED);
            }
        }
        return offlineSignActivity;
    }

    @Override
    public IPage<OfflineSignActivity> pageSign(SignPageParam param) {
        if (param.getValid() == WhetherEnum.YES) {
            param.setState(null);
        }
        DateTime now = DateTime.now();
        SignStateEnum state = param.getState();
        return offlineSignActivityMapper.selectPage(param.page(), Wrappers.<OfflineSignActivity>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getSignName()), OfflineSignActivity::getSignName, param.getSignName())
                .eq(StrUtil.isNotBlank(param.getAppId()), OfflineSignActivity::getAppId, param.getAppId())
                .like(StrUtil.isNotBlank(param.getCreatorName()), OfflineSignActivity::getCreatorName, param.getCreatorName())
                .ge(param.getCreateTimeStart() != null, OfflineSignActivity::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, OfflineSignActivity::getCreateTime, param.getCreateTimeEnd())
                .and(param.getValid() == WhetherEnum.YES, i -> i
                        .eq(OfflineSignActivity::getState, SignStateEnum.NORMAL)
                        .ge(OfflineSignActivity::getEndTime, now))
                .and(state == SignStateEnum.NORMAL, i -> i
                        .eq(OfflineSignActivity::getState, SignStateEnum.NORMAL)
                        .le(OfflineSignActivity::getStartTime, now)
                        .ge(OfflineSignActivity::getEndTime, now))
                .eq(state == SignStateEnum.DISABLE, OfflineSignActivity::getState, SignStateEnum.DISABLE)
                .and(state == SignStateEnum.PENDING, i -> i
                        .eq(OfflineSignActivity::getState, SignStateEnum.NORMAL)
                        .gt(OfflineSignActivity::getStartTime, now))
                .and(state == SignStateEnum.EXPIRED, i -> i
                        .eq(OfflineSignActivity::getState, SignStateEnum.NORMAL)
                        .lt(OfflineSignActivity::getEndTime, now))
                .orderByDesc(OfflineSignActivity::getCreateTime)
        ).convert(sign -> {
            sign = stateHandler(sign);
            sign.setSignDetail(null);
            return sign;
        });
    }

    @Override
    public SignResult getSign(SignGetParam param) {
        OfflineSignActivity sign = offlineSignActivityMapper.selectById(param.getId());
        sign = stateHandler(sign);
        SignResult result = Convert.convert(SignResult.class, sign);
        switch (result.getSignRewardType()) {
            case REWARD: {
                result.setSignReward(JSONUtil.toBean(result.getRelContent(), SignReward.class));
                break;
            }
            /*case COUPON: {
                CouponInfo couponInfo = couponInfoService.getCoupon(result.getRelId());
                SignCoupon signCoupon = new SignCoupon();
                BeanUtil.copyProperties(couponInfo, signCoupon);
                result.setSignCoupon(signCoupon);
                break;
            }*/
            default:
        }
        List<Map<String, Object>> todayCountList = offlineSignMemberRecordMapper.selectMaps(new QueryWrapper<OfflineSignMemberRecord>()
                .select("count(1) signCount, count(distinct member_id) signMemberCount")
                .lambda()
                .eq(OfflineSignMemberRecord::getSignId, param.getId())
        );
        if (todayCountList.size() > 0) {
            result.setSignCount(Convert.toInt(todayCountList.get(0).get("signCount")));
            result.setSignMember(Convert.toInt(todayCountList.get(0).get("signMemberCount")));
        }
        DateTime yesterday = DateUtil.yesterday();
        List<Map<String, Object>> yesterdayCountList = offlineSignMemberRecordMapper.selectMaps(new QueryWrapper<OfflineSignMemberRecord>()
                .select("count(1) signCount, count(distinct member_id) signMemberCount")
                .lambda()
                .eq(OfflineSignMemberRecord::getSignId, param.getId())
                .ge(OfflineSignMemberRecord::getCreateTime, DateUtil.beginOfDay(yesterday))
                .le(OfflineSignMemberRecord::getCreateTime, DateUtil.endOfDay(yesterday))
        );
        if (yesterdayCountList.size() > 0) {
            result.setYesterdaySignCount(Convert.toInt(yesterdayCountList.get(0).get("signCount")));
            result.setYesterdaySignMember(Convert.toInt(yesterdayCountList.get(0).get("signMemberCount")));
        }
        return result;
    }

    @Override
    public void addSign(SignAddParam param) {
        OfflineSignActivity sign = new OfflineSignActivity();
        BeanUtil.copyProperties(param, sign);
        MdcUtil.setOrganInfo(sign);
        sign.setState(SignStateEnum.NORMAL);
        switch (param.getSignRewardType()) {
            case REWARD: {
                SignReward signReward = new SignReward();
                signReward.setRewardType(param.getRewardType());
                signReward.setRewardValue(param.getRewardValue());
                sign.setRelContent(JSONUtil.toJsonStr(signReward));
                break;
            }
            /*case COUPON: {
                CouponInfo couponInfo = couponInfoService.getCoupon(param.getRelId());
                if (couponInfo.getState() != CouponStateEnum.NORMAL
                        && couponInfo.getState() != CouponStateEnum.EXPIRING
                        && couponInfo.getState() != CouponStateEnum.NOTACTIVE) {
                    throw new ServiceException("优惠券不可用");
                }
                SignCoupon signCoupon = new SignCoupon();
                BeanUtil.copyProperties(couponInfo, signCoupon);
                sign.setRelId(couponInfo.getId());
                sign.setRelContent(JSONUtil.toJsonStr(signCoupon));
                break;
            }*/
            default:
        }
        Long id = MdcUtil.getSnowflakeId();
        sign.setId(id);
        String path = clientConfigService.getItemByName(param.getAppId(), "offlineSignPage");
        path = Convert.toStr(path, "pages/home/home");
        String queryParam = "id=" + id + "&scan=true";
        String codeUrl = null;
        if (StrUtil.startWith(param.getAppId(), "wx")) {
            codeUrl = wechatMiniService.miniCodeUnLimitGet(new MiniProgramQrCodeUnLimitGetParam().setAppId(param.getAppId())
                    .setScene(queryParam).setPagePath(path));
        }
        sign.setCodeUrl(codeUrl);
        int count = offlineSignActivityMapper.insert(sign);
        if (count <= 0) {
            throw new ServiceException("新增打卡活动失败");
        }
    }

    @Transactional
    @Override
    public void editSign(SignEditParam param) {
        OfflineSignActivity sign = offlineSignActivityMapper.selectById(param.getId());
        if (sign == null) {
            throw new ServiceException("打卡活动不存在");
        }
        OfflineSignActivity update = new OfflineSignActivity();
        BeanUtil.copyProperties(param, update);
        update.setId(null);
        LambdaUpdateWrapper<OfflineSignActivity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(OfflineSignActivity::getId, param.getId());
        switch (param.getSignRewardType()) {
            case REWARD: {
                SignReward signReward = new SignReward();
                signReward.setRewardType(param.getRewardType());
                signReward.setRewardValue(param.getRewardValue());
                update.setRelContent(JSONUtil.toJsonStr(signReward));
                break;
            }
            /*case COUPON: {
                CouponInfo couponInfo = couponInfoService.getCoupon(param.getRelId());
                if (couponInfo.getState() != CouponStateEnum.NORMAL
                        && couponInfo.getState() != CouponStateEnum.EXPIRING
                        && couponInfo.getState() != CouponStateEnum.NOTACTIVE) {
                    throw new ServiceException("优惠券不可用");
                }
                SignCoupon signCoupon = new SignCoupon();
                BeanUtil.copyProperties(couponInfo, signCoupon);
                update.setRelId(couponInfo.getId());
                update.setRelContent(JSONUtil.toJsonStr(signCoupon));
                break;
            }*/
            default:
        }
        if (param.getDesignateOrgIds() != null && !StrUtil.equals(param.getDesignateOrgIds(), sign.getDesignateOrgIds())) {
            //若designateOrgIds为空字符串,则设置为null
            if (StrUtil.isBlank(param.getDesignateOrgIds())) {
                updateWrapper.set(OfflineSignActivity::getDesignateOrgIds, null);
                updateWrapper.set(OfflineSignActivity::getDesignateOrgNames, null);
                offlineSignMemberRecordMapper.update(new OfflineSignMemberRecord(), Wrappers.<OfflineSignMemberRecord>lambdaUpdate()
                        .eq(OfflineSignMemberRecord::getSignId, param.getId())
                        .set(OfflineSignMemberRecord::getDesignateOrgIds, sign.getCreatorOrgIds())
                        .set(OfflineSignMemberRecord::getDesignateOrgNames, sign.getCreatorOrgNames())
                );
            } else {
                //设置部门信息
                MdcUtil.setOrganInfo(update);
                OfflineSignMemberRecord recordUpdate = new OfflineSignMemberRecord();
                recordUpdate.setDesignateOrgIds(param.getDesignateOrgIds());
                MdcUtil.setOrganInfo(recordUpdate);
                offlineSignMemberRecordMapper.update(recordUpdate, Wrappers.<OfflineSignMemberRecord>lambdaUpdate()
                        .eq(OfflineSignMemberRecord::getSignId, param.getId())
                );
            }
        } else if (param.getDesignateOrgIds() == null) {
            update.setDesignateOrgIds(null);
            update.setDesignateOrgNames(null);
        }
        int count = offlineSignActivityMapper.update(update, updateWrapper);
        if (count <= 0) {
            throw new ServiceException("修改打卡活动失败");
        }
    }

    @Override
    public void toggleState(SignGetParam param) {
        OfflineSignActivity sign = offlineSignActivityMapper.selectById(param.getId());
        if (sign == null) {
            throw new ServiceException("打卡活动不存在");
        }
        OfflineSignActivity update = new OfflineSignActivity();
        update.setId(param.getId());
        update.setState(sign.getState() == SignStateEnum.NORMAL ? SignStateEnum.DISABLE : SignStateEnum.NORMAL);
        int count = offlineSignActivityMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public IPage<OfflineSignRecordResult> pageRecord(RecordPageParam param) {
        return offlineSignMemberRecordMapper.selectPage(param.page(), Wrappers.<OfflineSignMemberRecord>lambdaQuery()
                .eq(OfflineSignMemberRecord::getSignId, param.getSignId())
                .like(StrUtil.isNotBlank(param.getNickName()), OfflineSignMemberRecord::getNickName, param.getNickName())
                .ge(param.getCreateTimeStart() != null, OfflineSignMemberRecord::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, OfflineSignMemberRecord::getCreateTime, param.getCreateTimeEnd())
                .orderByDesc(OfflineSignMemberRecord::getCreateTime)
        ).convert(record -> {
            OfflineSignRecordResult result = BeanUtil.copyProperties(record, OfflineSignRecordResult.class);
            OfflineSignRewardResult signReward = new OfflineSignRewardResult();
            result.setSignReward(signReward);
            switch (record.getSignRewardType()) {
                case REWARD: {
                    SignReward reward = JSONUtil.toBean(record.getRelContent(), SignReward.class);
                    signReward.setSignReward(reward);
                    break;
                }
                /*case COUPON: {
                    SignCoupon coupon = JSONUtil.toBean(finalSign.getRelContent(), SignCoupon.class);
                    result.setSignCoupon(coupon);
                    break;
                }*/
                default:
            }
            return result;
        });
    }

    @Transactional
    @Override
    public OfflineSignRewardResult addOfflineSignRecord(SignGetParam param) {
        MemberInfo memberInfo = MdcUtil.getRequireCurrentMember();
        OfflineSignActivity sign = offlineSignActivityMapper.selectById(param.getId());
        sign = stateHandler(sign);
        if (sign.getState() != SignStateEnum.NORMAL) {
            throw new ServiceException("打卡活动" + sign.getState().getName() + ", 不能打卡");
        }
        LimitTypeEnum limitType = sign.getLimitType();
        DateTime now = DateTime.now();
        OfflineSignActivity finalSign = sign;
        return RedisUtil.syncLoad("offlineSign" + param.getId(), () -> {
            if (limitType == LimitTypeEnum.DAILY) {
                int count = offlineSignMemberRecordMapper.selectCount(Wrappers.<OfflineSignMemberRecord>lambdaQuery()
                        .eq(OfflineSignMemberRecord::getMemberId, memberInfo.getId())
                        .eq(OfflineSignMemberRecord::getSignId, param.getId())
                        .ge(OfflineSignMemberRecord::getCreateTime, DateUtil.beginOfDay(now))
                        .lt(OfflineSignMemberRecord::getCreateTime, DateUtil.endOfDay(now))
                );
                if (count + 1 > 1) {
                    throw new ServiceException("超过每日打卡次数");
                }
            } else if (limitType == LimitTypeEnum.TOTAL) {
                int count = offlineSignMemberRecordMapper.selectCount(Wrappers.<OfflineSignMemberRecord>lambdaQuery()
                        .eq(OfflineSignMemberRecord::getMemberId, memberInfo.getId())
                        .eq(OfflineSignMemberRecord::getSignId, param.getId())
                );
                if (count + 1 > 1) {
                    throw new ServiceException("超过活动打卡次数");
                }
            }
            OfflineSignRewardResult result = new OfflineSignRewardResult();
            Long recordId = MdcUtil.getSnowflakeId();
            OfflineSignMemberRecord record = new OfflineSignMemberRecord();
            BeanUtil.copyProperties(finalSign, record, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
            MdcUtil.setMemberInfo(record, memberInfo);
            record.setId(recordId);
            record.setSignId(finalSign.getId());
            switch (finalSign.getSignRewardType()) {
                case REWARD: {
                    SignReward reward = JSONUtil.toBean(finalSign.getRelContent(), SignReward.class);
                    memberRewardService.addOfflineSignRewardRecord(memberInfo, reward.getRewardType(), reward.getRewardValue(), finalSign.getId());
                    result.setSignReward(reward);
                    break;
                }
                /*case COUPON: {
                    try {
                        couponInfoService.addRel(new CouponGetParam().setId(finalSign.getRelId()), 1, memberInfo,
                                ReceiveSourceEnum.OFFLINESIGN, recordId, CreateSourceEnum.NORMAL);
                        SignCoupon coupon = JSONUtil.toBean(finalSign.getRelContent(), SignCoupon.class);
                        result.setSignCoupon(coupon);
                    } catch (ServiceException e) {
                        //领取失败当作无奖励
                        record.setRelId(null);
                        record.setRelContent(null);
                    }
                    break;
                }*/
                default:
            }
            int count = offlineSignMemberRecordMapper.insert(record);
            if (count <= 0) {
                throw new ServiceException("新增打卡记录失败");
            }
            return result;
        });
    }

    @Override
    public IPage<OfflineSignActivity> pageOfflineSign(PageParam<OfflineSignActivity> param) {
        return offlineSignActivityMapper.selectPage(param.page(), Wrappers.<OfflineSignActivity>lambdaQuery()
                .ne(OfflineSignActivity::getState, SignStateEnum.DISABLE)
                .orderByDesc(OfflineSignActivity::getCreateTime)
        ).convert(sign -> {
            sign = stateHandler(sign);
            sign.setSignDetail(null);
            return sign;
        });
    }

    @Override
    public OfflineSignResult getOfflineSign(SignGetParam param) {
        OfflineSignActivity sign = offlineSignActivityMapper.selectById(param.getId());
        sign = stateHandler(sign);
        OfflineSignResult result = BeanUtil.copyProperties(sign, OfflineSignResult.class);
        List<Map<String, Object>> todayCountList = offlineSignMemberRecordMapper.selectMaps(new QueryWrapper<OfflineSignMemberRecord>()
                .select("count(1) signCount, count(distinct member_id) signMemberCount")
                .lambda()
                .eq(OfflineSignMemberRecord::getSignId, param.getId())
        );
        if (todayCountList.size() > 0) {
            result.setSignCount(Convert.toInt(todayCountList.get(0).get("signCount")));
            result.setSignMember(Convert.toInt(todayCountList.get(0).get("signMemberCount")));
        }
        return result;
    }

    @Override
    public IPage<RecordResult> pageOfflineSignRecord(SignRecordPageParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return offlineSignMemberRecordMapper.selectPage(param.page(), Wrappers.<OfflineSignMemberRecord>lambdaQuery()
                .eq(OfflineSignMemberRecord::getMemberId, memberId)
                .eq(OfflineSignMemberRecord::getSignId, param.getSignId())
                .orderByDesc(OfflineSignMemberRecord::getCreateTime)
        ).convert(record -> {
            RecordResult result = BeanUtil.copyProperties(record, RecordResult.class);
            /*if (result.getSignRewardType() == SignRewardTypeEnum.COUPON) {
                List<CouponMemberRel> relList = couponMemberRelMapper.selectList(Wrappers.<CouponMemberRel>lambdaQuery()
                        .select(CouponMemberRel::getId)
                        .eq(CouponMemberRel::getReceiveSourceId, result.getId())
                );
                if (relList.size() > 0) {
                    result.setCouponRelId(relList.get(0).getId());
                }
            }*/
            return result;
        });
    }

    @Override
    public IPage<SignRecordResult> pageOfflineSignMemberRecord(PageParam<OfflineSignMemberRecord> param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return offlineSignMemberRecordMapper.selectPage(param.page(), Wrappers.<OfflineSignMemberRecord>lambdaQuery()
                .eq(OfflineSignMemberRecord::getMemberId, memberId)
                .groupBy(OfflineSignMemberRecord::getSignId)
                .orderByDesc(OfflineSignMemberRecord::getCreateTime)
        ).convert(record -> {
            SignRecordResult result = BeanUtil.copyProperties(record, SignRecordResult.class);
            OfflineSignActivity sign = offlineSignActivityMapper.selectById(result.getSignId());
            sign = stateHandler(sign);
            result.setOfflineSignActivity(sign);
            return result;
        });
    }
}
