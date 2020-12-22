package com.aquilaflycloud.mdc.job;

import com.alibaba.schedulerx.worker.domain.JobContext;
import com.alibaba.schedulerx.worker.processor.JavaProcessor;
import com.alibaba.schedulerx.worker.processor.ProcessResult;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 微信第三方平台
 *
 * @author star
 * @date 2019-06-04
 */
@Slf4j
@Component
public class WechatOpenUpdateJob extends JavaProcessor {

    @Override
    public ProcessResult process(JobContext context) {
        log.info("Job WechatOpenUpdateJob Start...");
        try {
            WechatOpenPlatformService wechatOpenPlatformService = SpringUtil.getBean(WechatOpenPlatformService.class);
            //更新第三方平台AccessToken
            int count = wechatOpenPlatformService.saveComponentAccessToken(true);
            if (count > 0) {
                log.info("componentAccessToken刷新成功");
                //更新授权公众号
                count = wechatOpenPlatformService.saveAuthorSite(true);
                if (count > 0) {
                    log.info("authorSite刷新成功");
                } else {
                    log.info("authorSite刷新失败");
                    return new ProcessResult(false, "authorSite刷新失败");
                }
            } else {
                log.error("componentAccessToken刷新失败");
                return new ProcessResult(false, "componentAccessToken刷新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Job WechatOpenUpdateJob Error");
            return new ProcessResult(false, e.getMessage());
        }
        log.info("Job WechatOpenUpdateJob End...");
        return new ProcessResult(true);
    }
}
