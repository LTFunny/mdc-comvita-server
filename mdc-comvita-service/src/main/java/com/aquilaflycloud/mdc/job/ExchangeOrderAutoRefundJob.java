package com.aquilaflycloud.mdc.job;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.aquilaflycloud.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ExchangeOrderAutoRefundJob
 *
 * @author star
 * @date 2020-05-22
 */
@Slf4j
@Component
public class ExchangeOrderAutoRefundJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job ExchangeOrderAutoRefundJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        try {
            ExchangeService exchangeService = SpringUtil.getBean(ExchangeService.class);
            exchangeService.autoRefundExpireOrder();
            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job ExchangeOrderAutoRefundJob Error...");
        }
        log.info("Job ExchangeOrderAutoRefundJob End...");
        return processResult;
    }
}
