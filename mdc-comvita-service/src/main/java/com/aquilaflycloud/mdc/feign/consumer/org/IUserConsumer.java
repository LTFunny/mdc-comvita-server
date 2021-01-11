package com.aquilaflycloud.mdc.feign.consumer.org;

import com.aquilaflycloud.org.service.IUserProvider;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author pengyongliang
 * @Date 2021/1/11 15:56
 * @Version 1.0
 */
@FeignClient("org-comvita-server")
public interface IUserConsumer extends IUserProvider {
    
}
