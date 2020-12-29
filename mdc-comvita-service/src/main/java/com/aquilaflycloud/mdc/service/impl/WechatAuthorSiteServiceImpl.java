package com.aquilaflycloud.mdc.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaAnalysisService;
import cn.binarywang.wx.miniapp.bean.analysis.WxMaRetainInfo;
import cn.binarywang.wx.miniapp.bean.analysis.WxMaSummaryTrend;
import cn.binarywang.wx.miniapp.bean.analysis.WxMaVisitTrend;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.AnalysisTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.QrcodeHandlerTypeEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteSourceEnum;
import com.aquilaflycloud.mdc.enums.wechat.SiteStateEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.wechat.*;
import com.aquilaflycloud.mdc.param.wechat.*;
import com.aquilaflycloud.mdc.result.wechat.WechatUserAnalysisSumResult;
import com.aquilaflycloud.mdc.service.WechatAuthorSiteService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * WechatAuthorSiteServiceImpl
 *
 * @author star
 * @date 2019-10-08
 */
@Service
public class WechatAuthorSiteServiceImpl implements WechatAuthorSiteService {
    @Resource
    private WechatAuthorSiteMapper wechatAuthorSiteMapper;
    @Resource
    private WechatAuthorSiteTemplateMapper wechatAuthorSiteTemplateMapper;
    @Resource
    private WechatAuthorSiteQrcodeMsgMapper wechatAuthorSiteQrcodeMsgMapper;
    @Resource
    private WechatMiniRetainAnalysisMapper wechatMiniRetainAnalysisMapper;
    @Resource
    private WechatMiniVisitTrendAnalysisMapper wechatMiniVisitTrendAnalysisMapper;
    @Resource
    private WechatMiniSummaryAnalysisMapper wechatMiniSummaryAnalysisMapper;
    @Resource
    private WechatUserAnalysisMapper wechatUserAnalysisMapper;
    @Resource
    private WechatMiniService wechatMiniService;

    @Override
    public IPage<WechatAuthorSite> pageAuthor(WechatAuthorSitePageParam param) {
        return wechatAuthorSiteMapper.selectPage(param.page(), Wrappers.<WechatAuthorSite>lambdaQuery()
                .eq(WechatAuthorSite::getState, SiteStateEnum.AUTHORIZED)
                .eq(WechatAuthorSite::getIsShow, WhetherEnum.YES)
                .eq(param.getSource() != null, WechatAuthorSite::getSource, param.getSource())
                .like(StrUtil.isNotBlank(param.getNickName()), WechatAuthorSite::getNickName, param.getNickName())
                .orderByDesc(WechatAuthorSite::getCreateTime)
        );
    }

    @Override
    public WechatAuthorSiteTemplate getWechatTemplateByType(String appId, String type) {
        return wechatAuthorSiteTemplateMapper.selectOne(Wrappers.<WechatAuthorSiteTemplate>lambdaQuery()
                .eq(WechatAuthorSiteTemplate::getAppId, appId)
                .eq(WechatAuthorSiteTemplate::getTemplateType, type)
                .orderByAsc(WechatAuthorSiteTemplate::getSubTenantId)
                .last("limit 1")
        );
    }

    @Override
    public String getOauth2Url(Oauth2UrlGetParam param) {
        String url = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME)).append("/rest/").append(MdcUtil.getServerName()).toString();
        if (param.getEncrypt()) {
            Long encryptParam = MdcUtil.setCryption("url=" + URLUtil.encode(param.getUrl())
                    + ";scopeType=" + param.getScopeType().name() + ";appId=" + param.getAppId());
            return String.format(url + "/wechatAuthorSiteOauthUrl?encryptParam=%s",
                    encryptParam);
        } else {
            return String.format(url + "/wechatAuthorSiteOauthUrl?url=%s&scopeType=%s&appId=%s",
                    URLUtil.encode(param.getUrl()), param.getScopeType().name(), param.getAppId());
        }
    }

    @Override
    public IPage<WechatAuthorSiteQrcodeMsg> pageQrCodeMsg(QrCodeMsgPageParam param) {
        return wechatAuthorSiteQrcodeMsgMapper.selectPage(param.page(), Wrappers.<WechatAuthorSiteQrcodeMsg>lambdaQuery()
                .eq(WechatAuthorSiteQrcodeMsg::getHandlerType, QrcodeHandlerTypeEnum.NORMAL)
                .eq(StrUtil.isNotBlank(param.getAppId()), WechatAuthorSiteQrcodeMsg::getAppId, param.getAppId())
                .like(StrUtil.isNotBlank(param.getSceneString()), WechatAuthorSiteQrcodeMsg::getSceneString, param.getSceneString())
                .eq(param.getExpireType() != null, WechatAuthorSiteQrcodeMsg::getExpireType, param.getExpireType())
                .eq(param.getMsgType() != null, WechatAuthorSiteQrcodeMsg::getMsgType, param.getMsgType())
        );
    }

    @Override
    public void editQrCodeMsg(QrCodeMsgEditParam param) {
        WechatAuthorSiteQrcodeMsg msg = wechatAuthorSiteQrcodeMsgMapper.selectById(param.getId());
        if (msg == null) {
            throw new ServiceException("消息不存在");
        }
        if (param.getMsgType() != null) {
            WechatAuthorSiteQrcodeMsg update = new WechatAuthorSiteQrcodeMsg();
            update.setId(msg.getId());
            String msgContent = null;
            switch (param.getMsgType()) {
                case TEXT: {
                    msgContent = JSONUtil.toJsonStr(param.getMsgText());
                    break;
                }
                case IMAGE: {
                    msgContent = JSONUtil.toJsonStr(param.getMsgImage());
                    break;
                }
                case VOICE: {
                    msgContent = JSONUtil.toJsonStr(param.getMsgVoice());
                    break;
                }
                case VIDEO: {
                    msgContent = JSONUtil.toJsonStr(param.getMsgVideo());
                    break;
                }
                case MUSIC: {
                    msgContent = JSONUtil.toJsonStr(param.getMsgMusic());
                    break;
                }
                case NEWS: {
                    msgContent = JSONUtil.toJsonStr(param.getMsgNewsList());
                    break;
                }
                default:
            }
            update.setMsgContent(msgContent);
            int count = wechatAuthorSiteQrcodeMsgMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("编辑消息失败");
            }
        }
    }

    @Override
    public List<WechatUserAnalysisSumResult> getWechatUserAnalysisSum(UserAnalysisSumGetParam param) {
        return wechatUserAnalysisMapper.selectMaps(new QueryWrapper<WechatUserAnalysis>()
                .select("ref_date," +
                        "app_id," +
                        "coalesce(sum( new_user ), 0) new_user," +
                        "coalesce(sum( cancel_user ), 0) cancel_user," +
                        "coalesce(sum( increase_user ), 0) increase_user," +
                        "coalesce(sum( cumulate_user ), 0) cumulate_user")
                .lambda()
                .eq(StrUtil.isNotBlank(param.getAppId()), WechatUserAnalysis::getAppId, param.getAppId())
                .ge(StrUtil.isNotBlank(param.getBeginDate()), WechatUserAnalysis::getRefDate, param.getBeginDate())
                .le(StrUtil.isNotBlank(param.getEndDate()), WechatUserAnalysis::getRefDate, param.getEndDate())
                .groupBy(WechatUserAnalysis::getRefDate).orderByDesc(WechatUserAnalysis::getRefDate)).stream().map((map) ->
                BeanUtil.fillBeanWithMap(map, new WechatUserAnalysisSumResult(), true,
                        CopyOptions.create().ignoreError())).collect(Collectors.toList());
    }

    @Override
    public void addWechatMiniAnalysis(String appId, DateTime begin, DateTime end) {
        List<WechatAuthorSite> siteList = wechatAuthorSiteMapper.normalSelectList(Wrappers.<WechatAuthorSite>lambdaQuery()
                .eq(StrUtil.isNotBlank(appId), WechatAuthorSite::getAppId, appId)
                .eq(WechatAuthorSite::getSource, SiteSourceEnum.MINIPRO)
        );
        begin = DateUtil.beginOfDay(begin);
        end = DateUtil.beginOfDay(end);
        DateTime now = DateUtil.beginOfDay(new DateTime());
        DateTime yesterday = DateUtil.beginOfDay(DateUtil.yesterday());
        if (end.isAfterOrEquals(now)) {
            end = yesterday;
        }
        for (WechatAuthorSite site : siteList) {
            ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, site.getTenantId());
            ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, site.getSubTenantId());
            //保存访问留存数据
            saveRetainAnalysisData(site, begin, end, now, yesterday);
            //保存访问趋势数据
            saveVisitTrendAnalysisData(site, begin, end, now, yesterday);
            //保存访问概况数据
            saveDailySummaryAnalysisData(site, begin, end);
        }
    }

    private final Integer[] dailyRetainArray = new Integer[]{1, 2, 3, 4, 5, 6, 7, 14, 30};
    private final Integer[] weeklyRetainArray = new Integer[]{1, 2, 3, 4};

    private void saveRetainAnalysisData(WechatAuthorSite site, DateTime begin, DateTime end, DateTime now, DateTime yesterday) {
        //日留存分析数据
        List<WechatMiniRetainAnalysis> addDailyRetainList = new ArrayList<>();
        List<WechatMiniRetainAnalysis> editDailyRetainList = new ArrayList<>();
        DateTime queryBegin = DateUtil.dateNew(begin);
        while (queryBegin.isBeforeOrEquals(end)) {
            addDailyRetainList.addAll(getRetainAnalysis(site.getAppId(), queryBegin, queryBegin, AnalysisTypeEnum.DAILY));
            if (begin.equals(yesterday)) {
                for (Integer day : dailyRetainArray) {
                    editDailyRetainList.addAll(getRetainAnalysis(site.getAppId(), queryBegin.offsetNew(DateField.DAY_OF_YEAR, -day),
                            queryBegin.offsetNew(DateField.DAY_OF_YEAR, -day), AnalysisTypeEnum.DAILY));
                }
            }
            queryBegin = queryBegin.offsetNew(DateField.DAY_OF_YEAR, 1);
        }
        //周留存分析数据
        List<WechatMiniRetainAnalysis> addWeeklyRetainList = new ArrayList<>();
        List<WechatMiniRetainAnalysis> editWeeklyRetainList = new ArrayList<>();
        DateTime weekBegin = DateUtil.beginOfWeek(begin);
        DateTime weekEnd = DateUtil.beginOfDay(DateUtil.endOfWeek(end));
        if (weekEnd.isAfterOrEquals(now)) {
            weekEnd.offset(DateField.WEEK_OF_YEAR, -1);
        }
        queryBegin = weekBegin;
        while (queryBegin.isBefore(weekEnd)) {
            DateTime queryEnd = DateUtil.beginOfDay(DateUtil.endOfWeek(queryBegin));
            addWeeklyRetainList.addAll(getRetainAnalysis(site.getAppId(), queryBegin, queryEnd, AnalysisTypeEnum.WEEKLY));
            if (begin.equals(yesterday)) {
                for (Integer week : weeklyRetainArray) {
                    editWeeklyRetainList.addAll(getRetainAnalysis(site.getAppId(), queryBegin.offsetNew(DateField.WEEK_OF_YEAR, -week),
                            queryEnd.offsetNew(DateField.WEEK_OF_YEAR, -week), AnalysisTypeEnum.WEEKLY));
                }
            }
            queryBegin.offset(DateField.WEEK_OF_YEAR, 1);
        }
        //月留存分析数据
        List<WechatMiniRetainAnalysis> addMonthlyRetainList = new ArrayList<>();
        List<WechatMiniRetainAnalysis> editMonthlyRetainList = new ArrayList<>();
        DateTime monthBegin = DateUtil.beginOfMonth(begin);
        DateTime monthEnd = DateUtil.beginOfDay(DateUtil.endOfMonth(end));
        if (monthEnd.isAfterOrEquals(now)) {
            monthEnd.offset(DateField.MONTH, -1);
        }
        queryBegin = monthBegin;
        while (queryBegin.isBefore(monthEnd)) {
            DateTime queryEnd = DateUtil.beginOfDay(DateUtil.endOfMonth(queryBegin));
            addMonthlyRetainList.addAll(getRetainAnalysis(site.getAppId(), queryBegin, queryEnd, AnalysisTypeEnum.MONTHLY));
            if (begin.equals(yesterday)) {
                editMonthlyRetainList.addAll(getRetainAnalysis(site.getAppId(), queryBegin.offsetNew(DateField.MONTH, -1),
                        queryEnd.offsetNew(DateField.MONTH, -1), AnalysisTypeEnum.MONTHLY));
            }
            queryBegin.offset(DateField.MONTH, 1);
        }
        if (addDailyRetainList.size() > 0) {
            wechatMiniRetainAnalysisMapper.insertIgnoreAllBatch(addDailyRetainList);
        }
        for (WechatMiniRetainAnalysis retainAnalysis : editDailyRetainList) {
            wechatMiniRetainAnalysisMapper.update(retainAnalysis, Wrappers.<WechatMiniRetainAnalysis>lambdaUpdate()
                    .eq(WechatMiniRetainAnalysis::getAppId, retainAnalysis.getAppId())
                    .eq(WechatMiniRetainAnalysis::getRefDate, retainAnalysis.getRefDate())
            );
        }
        if (addWeeklyRetainList.size() > 0) {
            wechatMiniRetainAnalysisMapper.insertIgnoreAllBatch(addWeeklyRetainList);
        }
        for (WechatMiniRetainAnalysis retainAnalysis : editWeeklyRetainList) {
            wechatMiniRetainAnalysisMapper.update(retainAnalysis, Wrappers.<WechatMiniRetainAnalysis>lambdaUpdate()
                    .eq(WechatMiniRetainAnalysis::getAppId, retainAnalysis.getAppId())
                    .eq(WechatMiniRetainAnalysis::getRefDate, retainAnalysis.getRefDate())
            );
        }
        if (addMonthlyRetainList.size() > 0) {
            wechatMiniRetainAnalysisMapper.insertIgnoreAllBatch(addWeeklyRetainList);
        }
        for (WechatMiniRetainAnalysis retainAnalysis : editMonthlyRetainList) {
            wechatMiniRetainAnalysisMapper.update(retainAnalysis, Wrappers.<WechatMiniRetainAnalysis>lambdaUpdate()
                    .eq(WechatMiniRetainAnalysis::getAppId, retainAnalysis.getAppId())
                    .eq(WechatMiniRetainAnalysis::getRefDate, retainAnalysis.getRefDate())
            );
        }
    }

    private List<WechatMiniRetainAnalysis> getRetainAnalysis(String appId, DateTime begin, DateTime end, AnalysisTypeEnum analysisType) {
        List<WechatMiniRetainAnalysis> analysisList = new ArrayList<>();
        try {
            WxMaAnalysisService wxMaAnalysisService = wechatMiniService.getWxMaServiceByAppId(appId).getAnalysisService();
            WxMaRetainInfo wxMaRetainInfo;
            switch (analysisType) {
                case DAILY: {
                    wxMaRetainInfo = wxMaAnalysisService.getDailyRetainInfo(begin, end);
                    break;
                }
                case WEEKLY: {
                    wxMaRetainInfo = wxMaAnalysisService.getWeeklyRetainInfo(begin, end);
                    break;
                }
                case MONTHLY: {
                    wxMaRetainInfo = wxMaAnalysisService.getMonthlyRetainInfo(begin, end);
                    break;
                }
                default:
                    throw new ServiceException("留存类型有误");
            }
            if (StrUtil.isNotBlank(wxMaRetainInfo.getRefDate())) {
                WechatMiniRetainAnalysis analysis = new WechatMiniRetainAnalysis();
                analysis.setAppId(appId);
                analysis.setAnalysisType(analysisType);
                analysis.setRefDate(wxMaRetainInfo.getRefDate());
                analysis.setVisitUvNew(wxMaRetainInfo.getVisitUvNew() != null ? wxMaRetainInfo.getVisitUvNew().get(0) : null);
                analysis.setVisitUv(wxMaRetainInfo.getVisitUv() != null ? wxMaRetainInfo.getVisitUv().get(0) : null);
                analysis.setVisitUvNewContent(JSONUtil.toJsonStr(wxMaRetainInfo.getVisitUvNew()));
                analysis.setVisitUvContent(JSONUtil.toJsonStr(wxMaRetainInfo.getVisitUv()));
                analysisList.add(analysis);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
        return analysisList;
    }

    private void saveVisitTrendAnalysisData(WechatAuthorSite site, DateTime begin, DateTime end, DateTime now, DateTime yesterday) {
        //日趋势分析数据
        List<WechatMiniVisitTrendAnalysis> addDailyVisitTrendList = new ArrayList<>();
        DateTime queryBegin = DateUtil.dateNew(begin);
        while (queryBegin.isBeforeOrEquals(end)) {
            addDailyVisitTrendList.addAll(getVisitTrendAnalysis(site.getAppId(), queryBegin, queryBegin, AnalysisTypeEnum.DAILY));
            queryBegin = queryBegin.offset(DateField.DAY_OF_YEAR, 1);
        }
        //周趋势分析数据
        List<WechatMiniVisitTrendAnalysis> addWeeklyVisitTrendList = new ArrayList<>();
        DateTime weekBegin = DateUtil.beginOfWeek(begin);
        DateTime weekEnd = DateUtil.beginOfDay(DateUtil.endOfWeek(end));
        if (weekEnd.isAfterOrEquals(now)) {
            weekEnd.offset(DateField.WEEK_OF_YEAR, -1);
        }
        queryBegin = weekBegin;
        while (queryBegin.isBefore(weekEnd)) {
            DateTime queryEnd = DateUtil.beginOfDay(DateUtil.endOfWeek(queryBegin));
            addWeeklyVisitTrendList.addAll(getVisitTrendAnalysis(site.getAppId(), queryBegin, queryEnd, AnalysisTypeEnum.WEEKLY));
            queryBegin.offset(DateField.WEEK_OF_YEAR, 1);
        }
        //月趋势分析数据
        List<WechatMiniVisitTrendAnalysis> addMonthlyVisitTrendList = new ArrayList<>();
        DateTime monthBegin = DateUtil.beginOfMonth(begin);
        DateTime monthEnd = DateUtil.beginOfDay(DateUtil.endOfMonth(end));
        if (monthEnd.isAfterOrEquals(now)) {
            monthEnd.offset(DateField.MONTH, -1);
        }
        queryBegin = monthBegin;
        while (queryBegin.isBefore(monthEnd)) {
            DateTime queryEnd = DateUtil.beginOfDay(DateUtil.endOfMonth(queryBegin));
            addMonthlyVisitTrendList.addAll(getVisitTrendAnalysis(site.getAppId(), queryBegin, queryEnd, AnalysisTypeEnum.MONTHLY));
            queryBegin.offset(DateField.MONTH, 1);
        }
        if (addDailyVisitTrendList.size() > 0) {
            wechatMiniVisitTrendAnalysisMapper.insertIgnoreAllBatch(addDailyVisitTrendList);
        }
        if (addWeeklyVisitTrendList.size() > 0) {
            wechatMiniVisitTrendAnalysisMapper.insertIgnoreAllBatch(addWeeklyVisitTrendList);
        }
        if (addMonthlyVisitTrendList.size() > 0) {
            wechatMiniVisitTrendAnalysisMapper.insertIgnoreAllBatch(addMonthlyVisitTrendList);
        }
    }

    private List<WechatMiniVisitTrendAnalysis> getVisitTrendAnalysis(String appId, DateTime begin, DateTime end, AnalysisTypeEnum analysisType) {
        List<WechatMiniVisitTrendAnalysis> analysisList = new ArrayList<>();
        try {
            WxMaAnalysisService wxMaAnalysisService = wechatMiniService.getWxMaServiceByAppId(appId).getAnalysisService();
            List<WxMaVisitTrend> visitTrendList;
            switch (analysisType) {
                case DAILY: {
                    visitTrendList = wxMaAnalysisService.getDailyVisitTrend(begin, end);
                    break;
                }
                case WEEKLY: {
                    visitTrendList = wxMaAnalysisService.getWeeklyVisitTrend(begin, end);
                    break;
                }
                case MONTHLY: {
                    visitTrendList = wxMaAnalysisService.getMonthlyVisitTrend(begin, end);
                    break;
                }
                default:
                    throw new ServiceException("留存类型有误");
            }
            for (WxMaVisitTrend visitTrend : visitTrendList) {
                WechatMiniVisitTrendAnalysis analysis = new WechatMiniVisitTrendAnalysis();
                analysis.setAppId(appId);
                analysis.setAnalysisType(analysisType);
                analysis.setRefDate(visitTrend.getRefDate());
                analysis.setSessionCnt(visitTrend.getSessionCnt());
                analysis.setVisitPv(visitTrend.getVisitPv());
                analysis.setVisitUv(visitTrend.getVisitUv());
                analysis.setVisitUvNew(visitTrend.getVisitUvNew());
                analysis.setStayTimeUv(NumberUtil.toBigDecimal(visitTrend.getStayTimeUv()));
                analysis.setStayTimeSession(NumberUtil.toBigDecimal(visitTrend.getStayTimeSession()));
                analysis.setVisitDepth(NumberUtil.toBigDecimal(visitTrend.getVisitDepth()));
                analysisList.add(analysis);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
        return analysisList;
    }

    private void saveDailySummaryAnalysisData(WechatAuthorSite site, DateTime begin, DateTime end) {
        //日趋势分析数据
        List<WechatMiniSummaryAnalysis> addSummaryList = new ArrayList<>();
        DateTime queryBegin = DateUtil.dateNew(begin);
        while (queryBegin.isBeforeOrEquals(end)) {
            addSummaryList.addAll(getDailySummaryAnalysis(site.getAppId(), queryBegin, queryBegin));
            queryBegin = queryBegin.offset(DateField.DAY_OF_YEAR, 1);
        }
        if (addSummaryList.size() > 0) {
            wechatMiniSummaryAnalysisMapper.insertIgnoreAllBatch(addSummaryList);
        }
    }

    private List<WechatMiniSummaryAnalysis> getDailySummaryAnalysis(String appId, DateTime begin, DateTime end) {
        List<WechatMiniSummaryAnalysis> analysisList = new ArrayList<>();
        try {
            WxMaAnalysisService wxMaAnalysisService = wechatMiniService.getWxMaServiceByAppId(appId).getAnalysisService();
            List<WxMaSummaryTrend> summaryTrendList = wxMaAnalysisService.getDailySummaryTrend(begin, end);
            for (WxMaSummaryTrend summaryTrend : summaryTrendList) {
                WechatMiniSummaryAnalysis analysis = new WechatMiniSummaryAnalysis();
                analysis.setAppId(appId);
                analysis.setRefDate(DateUtil.parse(summaryTrend.getRefDate()));
                analysis.setVisitTotal(summaryTrend.getVisitTotal());
                analysis.setSharePv(summaryTrend.getSharePv());
                analysis.setShareUv(summaryTrend.getShareUv());
                analysisList.add(analysis);
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
        return analysisList;
    }
}
