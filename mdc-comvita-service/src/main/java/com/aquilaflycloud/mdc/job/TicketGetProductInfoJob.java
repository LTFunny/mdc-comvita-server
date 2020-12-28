package com.aquilaflycloud.mdc.job;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.service.TicketProductInfoNormalService;
import com.aquilaflycloud.mdc.service.TicketProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Zengqingjie
 * @Description: 获取产品信息
 * @date 2019-11-18
 */
@Component
@Slf4j
public class TicketGetProductInfoJob extends JavaProcessor {
    @Resource
    private TicketProductInfoNormalService ticketProductInfoNormalService;

    @Resource
    private TicketProductInfoService ticketProductInfoService;
    @Override
    public ProcessResult process(JobContext jobContext) {
        log.info("Job TicketGetProductInfoJob Start...");
        try {
//            ticketProductInfoNormalService.getInterfaceProductInfo();
            ticketProductInfoService.getNormalInterfaceProductInfo();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Job TicketGetProductInfoJob Error");
            return new ProcessResult(false, e.getMessage());
        }

        log.info("Job TicketGetProductInfoJob End...");
        return new ProcessResult(true);
    }
}
