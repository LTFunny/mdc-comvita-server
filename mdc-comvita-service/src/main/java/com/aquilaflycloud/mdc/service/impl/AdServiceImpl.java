package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.auth.enums.UserTypeEnum;
import com.aquilaflycloud.dataAuth.common.AuthParam;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.ad.AdPlacementEnum;
import com.aquilaflycloud.mdc.enums.ad.AdStateEnum;
import com.aquilaflycloud.mdc.enums.ad.EffectiveModeEnum;
import com.aquilaflycloud.mdc.enums.ad.EffectivePeriodEnum;
import com.aquilaflycloud.mdc.enums.common.AuditStateEnum;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.aquilaflycloud.mdc.mapper.AdInfoMapper;
import com.aquilaflycloud.mdc.model.ad.AdInfo;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.param.ad.*;
import com.aquilaflycloud.mdc.param.member.MemberEventAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberEventStatisticsParam;
import com.aquilaflycloud.mdc.result.ad.AdInfoResult;
import com.aquilaflycloud.mdc.result.ad.EventStatisticsResult;
import com.aquilaflycloud.mdc.result.ad.StatisticsResult;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberEventSexAndAgeResult;
import com.aquilaflycloud.mdc.result.member.MemberEventStatisticsResult;
import com.aquilaflycloud.mdc.service.AdService;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.MemberEventLogService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * AdServiceImpl
 *
 * @author star
 * @date 2019-11-18
 */
@Service
public class AdServiceImpl implements AdService {

    @Resource
    private AdInfoMapper adInfoMapper;

    @Resource
    private MemberEventLogService memberEventLogService;

    @Resource
    private FolksonomyService folksonomyService;

    private AdInfo stateHandler(AdInfo adInfo) {
        if (adInfo == null) {
            throw new ServiceException("广告不存在");
        }
        DateTime now = DateTime.now();
        if (adInfo.getState() != AdStateEnum.DISABLE) {
            if (adInfo.getAuditState() != AuditStateEnum.APPROVE) {
                adInfo.setState(AdStateEnum.PENDING);
            } else if (adInfo.getEffectiveMode() == EffectiveModeEnum.CUSTOM) {
                if (adInfo.getOnlineTime() != null && now.isBefore(adInfo.getOnlineTime())) {
                    adInfo.setState(AdStateEnum.PENDING);
                } else if (adInfo.getOfflineTime() != null && now.isAfter(adInfo.getOfflineTime())) {
                    adInfo.setState(AdStateEnum.EXPIRED);
                }
            }
        }
        return adInfo;
    }

    @Override
    public IPage<AdInfo> pageAd(AdPageParam param) {
        AdStateEnum state = param.getState();
        DateTime now = DateTime.now();
        return adInfoMapper.selectPage(param.page(), Wrappers.<AdInfo>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getAdName()), AdInfo::getAdName, param.getAdName())
                .eq(StrUtil.isNotBlank(param.getAppId()), AdInfo::getAppId, param.getAppId())
                .eq(param.getAdType() != null, AdInfo::getAdType, param.getAdType())
                .eq(param.getAdPlacement() != null, AdInfo::getAdPlacement, param.getAdPlacement())
                .eq(param.getEffectiveMode() != null, AdInfo::getEffectiveMode, param.getEffectiveMode())
                .ge(param.getOnlineTime() != null, AdInfo::getOnlineTime, param.getOnlineTime())
                .le(param.getOfflineTime() != null, AdInfo::getOfflineTime, param.getOfflineTime())
                .and(state == AdStateEnum.NORMAL, i -> i.eq(AdInfo::getState, AdStateEnum.NORMAL)
                        .and(j -> j.and(k -> k.le(AdInfo::getOnlineTime, now)
                                .ge(AdInfo::getOfflineTime, now)
                                .eq(AdInfo::getEffectiveMode, EffectiveModeEnum.CUSTOM))
                                .or().eq(AdInfo::getEffectiveMode, EffectiveModeEnum.FOREVER))
                )
                .eq(state == AdStateEnum.DISABLE, AdInfo::getState, AdStateEnum.DISABLE)
                .and(state == AdStateEnum.PENDING, i -> i.eq(AdInfo::getState, AdStateEnum.NORMAL)
                        .and(j -> j.and(k -> k.gt(AdInfo::getOnlineTime, now)
                                .eq(AdInfo::getEffectiveMode, EffectiveModeEnum.CUSTOM))
                                .or().ne(AdInfo::getAuditState, AuditStateEnum.APPROVE)))
                .and(state == AdStateEnum.EXPIRED, i -> i.eq(AdInfo::getState, AdStateEnum.NORMAL)
                        .lt(AdInfo::getOfflineTime, now).eq(AdInfo::getEffectiveMode, EffectiveModeEnum.CUSTOM))
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
                .orderByDesc(AdInfo::getCreateTime)
        ).convert(this::stateHandler);
    }

    @Override
    public AdInfoResult getAd(AdGetParam param) {
        AdInfo adInfo = adInfoMapper.selectById(param.getId());
        adInfo = stateHandler(adInfo);
        AdInfoResult result = Convert.convert(AdInfoResult.class, adInfo);
        List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.AD, adInfo.getId());
        result.setFolksonomyInfoList(folksonomyInfoList);
        return result;
    }

    @Override
    public void deleteAd(AdGetParam param) {
        AdInfo adInfo = adInfoMapper.selectById(param.getId());
        if (adInfo == null) {
            throw new ServiceException("广告不存在");
        }
        if (adInfo.getAuditState() == AuditStateEnum.APPROVE) {
            if (adInfo.getState() == AdStateEnum.PENDING || adInfo.getState() == AdStateEnum.NORMAL) {
                throw new ServiceException("广告状态为" + adInfo.getState().getName() + ",不能删除");
            }
        }
        int count = adInfoMapper.deleteById(param.getId());
        if (count <= 0) {
            throw new ServiceException("删除广告失败");
        }
    }

    @Override
    public void add(AdAddParam param) {
        AdInfo adInfo = new AdInfo();
        BeanUtil.copyProperties(param, adInfo);
        adInfo.setState(AdStateEnum.NORMAL);
        int count = adInfoMapper.insert(adInfo);
        if (count <= 0) {
            throw new ServiceException("新增广告失败");
        }
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.AD, adInfo.getId(), param.getFolksonomyIds());
    }

    @Override
    public void edit(AdEditParam param) {
        AdInfo adInfo = adInfoMapper.selectById(param.getId());
        if (adInfo == null) {
            throw new ServiceException("广告不存在");
        }
        AdInfo update = new AdInfo();
        BeanUtil.copyProperties(param, update);
        if (MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE) {
            adInfo.setAuditState(AuditStateEnum.PENDING);
        }
        int count = adInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑广告失败");
        }
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.AD, param.getId(), param.getFolksonomyIds());
    }

    @Override
    public void toggleState(AdGetParam param) {
        AdInfo adInfo = adInfoMapper.selectById(param.getId());
        AdInfo update = new AdInfo();
        update.setId(param.getId());
        update.setState(adInfo.getState() == AdStateEnum.NORMAL ? AdStateEnum.DISABLE : AdStateEnum.NORMAL);
        if (MdcUtil.getCurrentUser().getType() == UserTypeEnum.SHOPEMPLOYEE) {
            update.setAuditState(AuditStateEnum.PENDING);
        }
        int count = adInfoMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public StatisticsResult getStatistics(AuthParam param) {
        return adInfoMapper.selectMaps(new QueryWrapper<AdInfo>()
                .select("count(1) effectiveTotal,"
                        + "coalesce(sum(case when ad_placement=" + AdPlacementEnum.BANNER.getType() + " then 1 else 0 end), 0) bannerEffectiveTotal")
//                        + "coalesce(sum(case when ad_placement=" + AdPlacementEnum.POPUPS.getType() + " then 1 else 0 end), 0) popupsEffectiveTotal")
                .lambda()
                .and(i -> i.eq(AdInfo::getState, AdStateEnum.NORMAL)
                        .apply("case when effective_mode=" + EffectiveModeEnum.FOREVER.getType() + " then 1 else now() between online_time and offline_time end"))
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams()))
                .stream().map((map) -> {
                    map.put("total", adInfoMapper.selectCount(Wrappers.<AdInfo>lambdaQuery()
                            .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())));
                    return BeanUtil.fillBeanWithMap(map, new StatisticsResult(), true,
                            CopyOptions.create().ignoreError());
                }).collect(Collectors.toList()).get(0);
    }

    @Override
    public void audit(AdAuditParam param) {
        AdInfo adInfo = adInfoMapper.selectById(param.getId());
        stateHandler(adInfo);
        if (adInfo.getAuditState() == AuditStateEnum.PENDING) {
            AdInfo update = new AdInfo();
            update.setId(param.getId());
            update.setAuditState(param.getIsApprove() ? AuditStateEnum.APPROVE : AuditStateEnum.REJECT);
            update.setAuditRemark(param.getAuditRemark());
            int count = adInfoMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("审核失败");
            }
        } else {
            throw new ServiceException("该广告不需审核");
        }
    }

    @Override
    public EventStatisticsResult getAdEventStatistics(AdEventStatisticsGetParam param) {
        EventStatisticsResult statisticsResult = new EventStatisticsResult();
        Set<EventTypeEnum> eventTypeSet = new HashSet<>();
        eventTypeSet.add(EventTypeEnum.SHOW);
        eventTypeSet.add(EventTypeEnum.CLICK);
        List<MemberEventStatisticsResult> list = memberEventLogService.selectLogStatistics(new MemberEventStatisticsParam()
                .setBusinessId(param.getId()).setBusinessType(BusinessTypeEnum.AD).setEventTypes(eventTypeSet)
                .setStartTime(param.getStartTime()).setEndTime(param.getEndTime()));
        for (MemberEventStatisticsResult result : list) {
            if (result.getEventType() == EventTypeEnum.SHOW) {
                statisticsResult.setShowPv(result.getPv());
                statisticsResult.setShowUv(result.getUv());
            } else if (result.getEventType() == EventTypeEnum.CLICK) {
                statisticsResult.setClickPv(result.getPv());
                statisticsResult.setClickUv(result.getUv());
            }
        }
        if (statisticsResult.getShowUv() != 0) {
            statisticsResult.setConversionRate(NumberUtil.formatPercent(
                    NumberUtil.div(statisticsResult.getClickUv(), statisticsResult.getShowUv()).doubleValue(), 2));
        }
        return statisticsResult;
    }


    @Override
    public List<MemberEventAnalysisResult> getAdEventAnalysis(AdEventAnalysisGetParam param) {
        MemberEventAnalysisParam analysisParam = new MemberEventAnalysisParam();
        BeanUtil.copyProperties(param, analysisParam);
        analysisParam.setBusinessId(param.getId()).setBusinessType(BusinessTypeEnum.AD);
        switch (param.getAnalysisType()) {
            case SHOW_PV:
                analysisParam.setEventType(EventTypeEnum.SHOW).setIsPv(true);
                break;
            case SHOW_UV:
                analysisParam.setEventType(EventTypeEnum.SHOW).setIsPv(false);
                break;
            case CLICK_PV:
                analysisParam.setEventType(EventTypeEnum.CLICK).setIsPv(true);
                break;
            case CLICK_UV:
                analysisParam.setEventType(EventTypeEnum.CLICK).setIsPv(false);
                break;
            default:
        }
        return memberEventLogService.selectLogAnalysis(analysisParam);
    }

    @Override
    public MemberEventSexAndAgeResult getAdEventDistribution(AdEventDistributionGetParam param) {
        MemberEventAnalysisParam analysisParam = new MemberEventAnalysisParam();
        BeanUtil.copyProperties(param, analysisParam);
        analysisParam.setBusinessId(param.getId()).setBusinessType(BusinessTypeEnum.AD);
        switch (param.getDistributionType()) {
            case SHOW_PV:
                analysisParam.setEventType(EventTypeEnum.SHOW).setIsPv(true);
                break;
            case SHOW_UV:
                analysisParam.setEventType(EventTypeEnum.SHOW).setIsPv(false);
                break;
            case CLICK_PV:
                analysisParam.setEventType(EventTypeEnum.CLICK).setIsPv(true);
                break;
            case CLICK_UV:
                analysisParam.setEventType(EventTypeEnum.CLICK).setIsPv(false);
                break;
            default:
        }
        return memberEventLogService.selectLogSexAndAge(analysisParam);
    }

    @Override
    public List<AdInfoResult> listAd(AdListParam param) {
        return selectShowAd(param.getAdPlacement(), param.getLimit());
    }

    private List<AdInfoResult> selectShowAd(AdPlacementEnum adPlacement, Integer limit) {
        DateTime now = DateTime.now();
        String otherAppId = MdcUtil.getOtherAppId();
        return adInfoMapper.selectList(Wrappers.<AdInfo>lambdaQuery()
                .and(i -> i.eq(StrUtil.isNotBlank(otherAppId), AdInfo::getAppId, otherAppId)
                        .or().eq(AdInfo::getAppId, MdcConstant.UNIVERSAL_APP_ID)
                )
                .eq(AdInfo::getAdPlacement, adPlacement)
                .eq(AdInfo::getState, AdStateEnum.NORMAL)
                .and(i -> i.and(j -> j.le(AdInfo::getOnlineTime, now)
                        .ge(AdInfo::getOfflineTime, now))
                        .or().eq(AdInfo::getEffectiveMode, EffectiveModeEnum.FOREVER))
                .and(i -> i.and(j -> j.le(AdInfo::getEffectiveStartTime, now.toTimeStr())
                        .ge(AdInfo::getEffectiveEndTime, now.toTimeStr()))
                        .or(j -> j.eq(AdInfo::getEffectiveMode, EffectiveModeEnum.CUSTOM)
                                .eq(AdInfo::getEffectivePeriod, EffectivePeriodEnum.ALLDAY))
                        .or(j -> j.eq(AdInfo::getEffectiveMode, EffectiveModeEnum.FOREVER)))
                .orderByDesc(AdInfo::getPriority, AdInfo::getOnlineTime, AdInfo::getCreateTime)
                .last("limit " + limit)
        ).stream().map((adInfo) -> Convert.convert(AdInfoResult.class, adInfo)).collect(Collectors.toList());
    }

    @Override
    public AdInfo getAdInfo(AdGetParam param) {
        AdInfo adInfo = adInfoMapper.selectById(param.getId());
        adInfo = stateHandler(adInfo);
        if (adInfo.getState() != AdStateEnum.NORMAL) {
            throw new ServiceException("广告不存在");
        }
        return adInfo;
    }
}
