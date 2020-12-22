package com.aquilaflycloud.mdc.feign.consumer.org;

import com.aquilaflycloud.org.service.IDataAuthScopeProvider;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * DataAuthScopeConsumer
 *
 * @author star
 * @date 2019-11-22
 */
@FeignClient("org-server")
public interface DataAuthScopeConsumer extends IDataAuthScopeProvider {
}
