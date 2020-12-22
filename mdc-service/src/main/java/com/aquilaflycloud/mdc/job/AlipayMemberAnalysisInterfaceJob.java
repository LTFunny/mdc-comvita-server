package com.aquilaflycloud.mdc.job;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.exception.OceanException;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.model.alipay.AlipayAuthorSite;
import com.aquilaflycloud.mdc.model.alipay.AlipayMemberDailyDataInfo;
import com.aquilaflycloud.mdc.param.member.MemberDailyDataListParam;
import com.aquilaflycloud.mdc.service.AlipayAuthorSiteService;
import com.aquilaflycloud.mdc.service.MemberDailyDataInfoService;
import com.aquilaflycloud.util.SpringUtil;
import com.umeng.umini.param.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AlipayMemberAnalysisInterfaceJob
 * 支付宝小程序友盟统计活跃用户数、新增用户数、总用户数、启动数
 *
 * @author Zengqingjie
 * @date 2020-05-16
 */
@Slf4j
@Component
public class AlipayMemberAnalysisInterfaceJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job AlipayMemberAnalysisInterfaceJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        String serverHost = "gateway.open.umeng.com";

        try {
            AlipayAuthorSiteService alipayAuthorSiteService = SpringUtil.getBean(AlipayAuthorSiteService.class);

            //获取友盟appKey非空的授权列表
            List<AlipayAuthorSite> authorSite = alipayAuthorSiteService.listAuthor();

            if (null != authorSite && authorSite.size() > 0) {

                for (int i = 0; i < authorSite.size(); i++) {
                    AlipayAuthorSite item = authorSite.get(i);
                    ApiExecutor apiExecutor = new ApiExecutor(item.getUmengApiKey(), item.getUmengApiSecKey());
                    apiExecutor.setServerHost(serverHost);
                    //获取今天时间
                    String date = DateUtil.date().toString("yyyy-MM-dd");
                    AlipayMemberDailyDataInfo info = umengUminiGetOverview(apiExecutor, item.getUmengAppKey(), date);
                    Long totalUser = umengUminiGetTotalUser(apiExecutor, item.getUmengAppKey(), date);
                    //处理信息：保存或者更新数据库
                    handleInfo(date, info, totalUser, item.getAppId());
                }
            }

            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job AlipayMemberAnalysisInterfaceJob Error...");
        }

        log.info("Job AlipayMemberAnalysisInterfaceJob End...");
        return processResult;
    }

    /**
     * 获取应用的累计用户数
     *
     * @param apiExecutor
     * @param umengAppKey
     * @param date
     * {"code": 200,"data": [{"dateTime": "2020-05-21","totalUser": 11}, {"dateTime": "2020-05-20", "totalUser": 0} ], "msg": "SUCCESS","success": true}
     */
    private Long umengUminiGetTotalUser(ApiExecutor apiExecutor, String umengAppKey, String date) throws OceanException {
        UmengUminiGetTotalUserParam param = new UmengUminiGetTotalUserParam();
        param.setDataSourceId(umengAppKey);
        //获取当天数据，分页参数都为1
        param.setFromDate(date);
        param.setToDate(date);
        param.setPageIndex(1);
        param.setPageSize(1);

        UmengUminiGetTotalUserResult result = apiExecutor.execute(param);
        if (result.getSuccess()) {
            UmnegUminiTotalUserDTO[] data = result.getData();
            if (null != data && data.length > 0) {
                //存在数据
                return data[0].getTotalUser();
            }
        } else {
            log.error("Job AlipayMemberAnalysisInterfaceJob Error: interface=umengUminiGetTotalUser, errorMessage=" + result.getMsg() + ", errorCode=" + result.getCode() );
        }

        return null;
    }

    /**
     * 获取应用概况数据:新增、活跃、启动、访次、次均停留时长、人均停留时长
     * {"code":200,"data":{"currentPage":1,"data":[{"activeUser":6,"dailyDuration":"00:02:16","dateTime":"2020-05-21","launch":16,"newUser":11,"onceDuration":"00:00:51","visitTimes":37}],"totalCount":1},"msg":"SUCCESS","success":true}
     *
     * @param apiExecutor
     * @param umengAppKey
     * @param date
     */
    private AlipayMemberDailyDataInfo umengUminiGetOverview(ApiExecutor apiExecutor, String umengAppKey, String date) throws OceanException {
        UmengUminiGetOverviewParam param = new UmengUminiGetOverviewParam();
        param.setDataSourceId(umengAppKey);
        param.setFromDate(date);
        param.setToDate(date);
        param.setTimeUnit("day");
        //新增、活跃、启动、访次、次均停留时长、人均停留时长
        param.setIndicators("newUser,activeUser,launch,visitTimes,onceDuration,dailyDuration");
        param.setPageIndex(1);
        param.setPageSize(1);

        UmengUminiGetOverviewResult result = apiExecutor.execute(param);

        if (result.getSuccess()) {
            UmengUminiOverviewDTO data = result.getData();
            if (null != data && null != data.getData() && data.getData().length > 0) {
                //存在数据
                AlipayMemberDailyDataInfo returnResult = new AlipayMemberDailyDataInfo();
                BeanUtil.copyProperties(data.getData()[0], returnResult);
                return returnResult;
            }
        } else {
            log.error("Job AlipayMemberAnalysisInterfaceJob Error: interface=umengUminiGetOverview, errorMessage=" + result.getMsg() + ", errorCode=" + result.getCode() );
        }

        return null;
    }

    /**
     * 处理信息：保存或者更新数据库
     * @param date
     * @param info
     * @param totalUser
     */
    private void handleInfo(String date, AlipayMemberDailyDataInfo info, Long totalUser, String appId) {
        MemberDailyDataInfoService memberDailyDataInfoService = SpringUtil.getBean(MemberDailyDataInfoService.class);

        if (null == info) {
            return;
        }

        if (null == totalUser) {
            totalUser = 0L;
        }

        info.setDate(date);
        info.setTotalUser(totalUser);
        info.setAppId(appId);

        //查询数据库是否存在对应时间的数据
        MemberDailyDataListParam param = new MemberDailyDataListParam();
        param.setDate(date);
        param.setAppId(appId);
        List<AlipayMemberDailyDataInfo> dataInfos = memberDailyDataInfoService.listInfos(param);

        if (null != dataInfos && dataInfos.size() > 1) {
            log.error("Job AlipayMemberAnalysisInterfaceJob Error: 存在多条数据("+ dataInfos.size() +")。appId=" + appId + ", date=" + date);
            return;
        }

        int count = 0;

        if (null == dataInfos || dataInfos.size() == 0) {
            //保存数据
            count = memberDailyDataInfoService.handleInfos(info, "1");
        } else if (dataInfos.size() == 1) {
            //更新数据
            info.setId(dataInfos.get(0).getId());
            count = memberDailyDataInfoService.handleInfos(info, "2");
        }

        log.info("Job AlipayMemberAnalysisInterfaceJob 保存或更新信息: appId=" + appId + ", date=" + date + ", count=" + count);
    }
}