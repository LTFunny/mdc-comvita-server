package com.aquilaflycloud.mdc.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.service.WechatAuthorSiteService;
import com.aquilaflycloud.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WechatMiniAnalysisJob 小程序数据分析
 *
 * @author star
 * @date 2020/8/14
 */
@Slf4j
@Component
public class WechatMiniAnalysisJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job WechatMiniAnalysisJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        String attr = context.getJobParameters();
        try {
            // 获取授权公众号的用户分析数据
            WechatAuthorSiteService wechatAuthorSiteService = SpringUtil.getBean(WechatAuthorSiteService.class);
            String appId = null;
            DateTime begin = DateUtil.yesterday();
            DateTime end = DateUtil.yesterday();
            if (StrUtil.isNotBlank(attr)) {
                String[] attrs = attr.split(";");
                begin = DateUtil.parse(attrs[0]);
                end = DateUtil.parse(attrs[1]);
                if (attrs.length == 3) {
                    appId = attrs[2];
                }
            }
            wechatAuthorSiteService.addWechatMiniAnalysis(appId, begin, end);
            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job WechatMiniAnalysisJob Error...");
        }
        log.info("Job WechatMiniAnalysisJob End...");
        return processResult;
    }
}
