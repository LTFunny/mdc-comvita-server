package com.aquilaflycloud.mdc.job;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.aquilaflycloud.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ExchangeOrderStateUpdateJob
 *
 * @author star
 * @date 2020-03-25
 */
@Slf4j
@Component
public class ExchangeOrderStateUpdateJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job ExchangeOrderStateUpdateJob Start...");
        ProcessResult processResult = new ProcessResult(false);
        try {
            ExchangeService exchangeService = SpringUtil.getBean(ExchangeService.class);
            exchangeService.updateOrderState();
            processResult = new ProcessResult(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Job ExchangeOrderStateUpdateJob Error...");
        }
        log.info("Job ExchangeOrderStateUpdateJob End...");
        return processResult;
    }
}
