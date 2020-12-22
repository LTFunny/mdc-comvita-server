package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.apply.ApplyStateEnum;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.member.BusinessTypeEnum;
import com.aquilaflycloud.mdc.enums.member.EventTypeEnum;
import com.aquilaflycloud.mdc.enums.recommendation.RecommendationStateEnum;
import com.aquilaflycloud.mdc.enums.recommendation.ReleaseTypeEnum;
import com.aquilaflycloud.mdc.mapper.RecommendationMapper;
import com.aquilaflycloud.mdc.model.folksonomy.FolksonomyInfo;
import com.aquilaflycloud.mdc.model.recommendation.Recommendation;
import com.aquilaflycloud.mdc.param.member.MemberEventAnalysisParam;
import com.aquilaflycloud.mdc.param.member.MemberEventStatisticsParam;
import com.aquilaflycloud.mdc.param.recommendation.*;
import com.aquilaflycloud.mdc.result.member.MemberEventAnalysisResult;
import com.aquilaflycloud.mdc.result.member.MemberEventStatisticsResult;
import com.aquilaflycloud.mdc.result.recommendation.EventStatisticsResult;
import com.aquilaflycloud.mdc.result.recommendation.RecommendationApiResult;
import com.aquilaflycloud.mdc.result.recommendation.RecommendationResult;
import com.aquilaflycloud.mdc.service.FolksonomyService;
import com.aquilaflycloud.mdc.service.MemberEventLogService;
import com.aquilaflycloud.mdc.service.RecommendationService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RecommendationServiceImpl
 *
 * @author star
 * @date 2020-03-27
 */
@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Resource
    private RecommendationMapper recommendationMapper;

    @Resource
    private MemberEventLogService memberEventLogService;

    @Resource
    private FolksonomyService folksonomyService;

    private Recommendation stateHandler(Recommendation recommendation) {
        if (recommendation == null) {
            throw new ServiceException("最新推荐不存在");
        }
        if (recommendation.getState() != RecommendationStateEnum.NORMAL) {
            if (DateTime.now().isBefore(recommendation.getReleaseTime())) {
                recommendation.setState(RecommendationStateEnum.NOTPUBLISH);
            }
        }
        return recommendation;
    }

    @Override
    public IPage<Recommendation> pageRecommendation(RecommendationPageParam param) {
        DateTime now = DateTime.now();
        if (param.getValid() == WhetherEnum.YES) {
            param.setState(null);
        }
        return recommendationMapper.selectPage(param.page(), new QueryWrapper<Recommendation>()
                .orderByDesc("is_top", "case when is_top = 1 then set_top_time else create_time end")
                .lambda()
                .like(StrUtil.isNotBlank(param.getTitle()), Recommendation::getTitle, param.getTitle())
                .eq(StrUtil.isNotBlank(param.getAppId()), Recommendation::getAppId, param.getAppId())
                .ge(param.getCreateTimeStart() != null, Recommendation::getCreateTime, param.getCreateTimeStart())
                .lt(param.getCreateTimeEnd() != null, Recommendation::getCreateTime, param.getCreateTimeEnd())
                .and(param.getState() == RecommendationStateEnum.NORMAL, i -> i
                        .eq(Recommendation::getState, RecommendationStateEnum.NORMAL)
                        .le(Recommendation::getReleaseTime, now))
                .eq(param.getState() == RecommendationStateEnum.DISABLE,
                        Recommendation::getState, RecommendationStateEnum.DISABLE)
                .and(param.getState() == RecommendationStateEnum.NOTPUBLISH, i -> i
                        .eq(Recommendation::getState, RecommendationStateEnum.NORMAL)
                        .gt(Recommendation::getReleaseTime, now))
                .and(param.getValid() == WhetherEnum.YES, i -> i
                        .eq(Recommendation::getState, ApplyStateEnum.NORMAL)
                        .le(Recommendation::getReleaseTime, now))
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(this::stateHandler);
    }

    @Override
    public void addRecommendation(RecommendationAddParam param) {
        Recommendation recommendation = new Recommendation();
        BeanUtil.copyProperties(param, recommendation);
        DateTime now = DateTime.now();
        if (ReleaseTypeEnum.IMMEDIATELY == param.getReleaseType()) {
            recommendation.setReleaseTime(now);
        }
        if (WhetherEnum.YES == param.getIsTop()) {
            recommendation.setSetTopTime(now);
        }
        recommendation.setState(RecommendationStateEnum.NORMAL);
        int count = recommendationMapper.insert(recommendation);
        if (count <= 0) {
            throw new ServiceException("新增最新推荐失败");
        }
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.RECOMMEND, recommendation.getId(), param.getFolksonomyIds());
    }

    @Override
    public void editRecommendation(RecommendationEditParam param) {
        Recommendation recommendation = recommendationMapper.selectById(param.getId());
        recommendation = stateHandler(recommendation);
        Recommendation update = new Recommendation();
        BeanUtil.copyProperties(param, update);
        DateTime now = DateTime.now();
        if (param.getReleaseType() == ReleaseTypeEnum.IMMEDIATELY && recommendation.getReleaseType() != ReleaseTypeEnum.IMMEDIATELY) {
            if (now.isBefore(recommendation.getReleaseTime())) {
                update.setReleaseTime(now);
            } else {
                update.setReleaseTime(null);
            }
        }
        if (param.getIsTop() == WhetherEnum.YES && recommendation.getIsTop() != WhetherEnum.YES) {
            update.setSetTopTime(now);
        }
        int count = recommendationMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("编辑最新推荐失败");
        }
        //保存业务功能标签
        folksonomyService.saveFolksonomyBusinessRel(BusinessTypeEnum.RECOMMEND, recommendation.getId(), param.getFolksonomyIds());
    }

    @Override
    public void toggleState(RecommendationGetParam param) {
        Recommendation recommendation = recommendationMapper.selectById(param.getId());
        Recommendation update = new Recommendation();
        update.setId(param.getId());
        update.setState(recommendation.getState() == RecommendationStateEnum.NORMAL ? RecommendationStateEnum.DISABLE : RecommendationStateEnum.NORMAL);
        int count = recommendationMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public void toggleIsTop(RecommendationGetParam param) {
        Recommendation recommendation = recommendationMapper.selectById(param.getId());
        Recommendation update = new Recommendation();
        update.setId(param.getId());
        update.setIsTop(recommendation.getIsTop() == WhetherEnum.YES ? WhetherEnum.NO : WhetherEnum.YES);
        if (update.getIsTop() == WhetherEnum.YES && recommendation.getIsTop() != WhetherEnum.YES) {
            update.setSetTopTime(DateTime.now());
        }
        int count = recommendationMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public void releaseRecommendation(RecommendationGetParam param) {
        Recommendation recommendation = recommendationMapper.selectById(param.getId());
        recommendation = stateHandler(recommendation);
        if (recommendation.getState() != RecommendationStateEnum.NOTPUBLISH) {
            throw new ServiceException("最新推荐" + recommendation.getState().getName() + ", 不需发布");
        }
        Recommendation update = new Recommendation();
        update.setId(param.getId());
        update.setReleaseType(ReleaseTypeEnum.IMMEDIATELY);
        update.setReleaseTime(DateTime.now());
        int count = recommendationMapper.updateById(update);
        if (count <= 0) {
            throw new ServiceException("发布失败");
        }
    }

    @Override
    public RecommendationResult getRecommendation(RecommendationGetParam param) {
        Recommendation recommendation = recommendationMapper.selectById(param.getId());
        recommendation = stateHandler(recommendation);
        RecommendationResult result = Convert.convert(RecommendationResult.class, recommendation);
        List<FolksonomyInfo> folksonomyInfoList = folksonomyService.getFolksonomyBusinessList(BusinessTypeEnum.RECOMMEND, recommendation.getId());
        result.setFolksonomyInfoList(folksonomyInfoList);
        return result;
    }

    @Override
    public List<MemberEventAnalysisResult> getEventAnalysis(EventAnalysisGetParam param) {
        MemberEventAnalysisParam analysisParam = new MemberEventAnalysisParam();
        BeanUtil.copyProperties(param, analysisParam);
        analysisParam.setBusinessId(param.getId()).setBusinessType(BusinessTypeEnum.RECOMMEND);
        switch (param.getAnalysisType()) {
            case READ_PV:
                analysisParam.setEventType(EventTypeEnum.READ).setIsPv(true);
                break;
            case READ_UV:
                analysisParam.setEventType(EventTypeEnum.READ).setIsPv(false);
                break;
            case CLICK_PV:
                analysisParam.setEventType(EventTypeEnum.CLICK).setIsPv(true);
                break;
            case CLICK_UV:
                analysisParam.setEventType(EventTypeEnum.CLICK).setIsPv(false);
                break;
            case SHARE_PV:
                analysisParam.setEventType(EventTypeEnum.SHARE).setIsPv(true);
                break;
            case SHARE_UV:
                analysisParam.setEventType(EventTypeEnum.SHARE).setIsPv(false);
                break;
            default:
        }
        return memberEventLogService.selectLogAnalysis(analysisParam);
    }

    @Override
    public EventStatisticsResult getEventStatistics(EventStatisticsParam param) {
        EventStatisticsResult statisticsResult = new EventStatisticsResult();
        Set<EventTypeEnum> eventTypeSet = new HashSet<>();
        eventTypeSet.add(EventTypeEnum.READ);
        eventTypeSet.add(EventTypeEnum.CLICK);
        eventTypeSet.add(EventTypeEnum.SHARE);
        List<MemberEventStatisticsResult> list = memberEventLogService.selectLogStatistics(new MemberEventStatisticsParam()
                .setBusinessId(param.getId()).setBusinessType(BusinessTypeEnum.RECOMMEND).setEventTypes(eventTypeSet)
                .setStartTime(param.getStartTime()).setEndTime(param.getEndTime()));
        for (MemberEventStatisticsResult result : list) {
            if (result.getEventType() == EventTypeEnum.READ) {
                statisticsResult.setReadPv(result.getPv());
                statisticsResult.setReadUv(result.getUv());
            } else if (result.getEventType() == EventTypeEnum.CLICK) {
                statisticsResult.setClickPv(result.getPv());
                statisticsResult.setClickUv(result.getUv());
            } else if (result.getEventType() == EventTypeEnum.SHARE) {
                statisticsResult.setSharePv(result.getPv());
                statisticsResult.setShareUv(result.getUv());
            }
        }
        return statisticsResult;
    }

    @Override
    public IPage<Recommendation> page(PageParam param) {
        String otherAppId = MdcUtil.getOtherAppId();
        return recommendationMapper.selectPage(param.page(), new QueryWrapper<Recommendation>()
                .orderByDesc("is_top", "case when is_top = 1 then set_top_time else create_time end").lambda()
                .and(i -> i.eq(StrUtil.isNotBlank(otherAppId), Recommendation::getAppId, otherAppId)
                        .or().eq(Recommendation::getAppId, MdcConstant.UNIVERSAL_APP_ID)
                )
                .eq(Recommendation::getState, RecommendationStateEnum.NORMAL)
                .le(Recommendation::getReleaseTime, DateTime.now())
        );
    }

    @Override
    public RecommendationApiResult get(RecommendationGetParam param) {
        Recommendation recommendation = recommendationMapper.selectById(param.getId());
        recommendation = stateHandler(recommendation);
        if (recommendation.getState() != RecommendationStateEnum.NORMAL) {
            throw new ServiceException("文章不存在");
        }
        return Convert.convert(RecommendationApiResult.class, recommendation);
    }
}
