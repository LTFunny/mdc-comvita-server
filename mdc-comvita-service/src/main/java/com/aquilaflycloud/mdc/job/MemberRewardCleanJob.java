package com.aquilaflycloud.mdc.job;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MemberRewardCleanJob 会员奖励清零规则
 *
 * @author star
 * @date 2020-03-06
 */
@Slf4j
@Component
public class MemberRewardCleanJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job MemberRewardCleanJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        try {
            MemberRewardService memberRewardService = SpringUtil.getBean(MemberRewardService.class);
            memberRewardService.addCleanRewardRecord();
            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job MemberRewardCleanJob Error...");
        }
        log.info("Job MemberRewardCleanJob End...");
        return processResult;
    }
}
