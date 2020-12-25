package com.aquilaflycloud.mdc.feign.consumer.org;

import com.aquilaflycloud.org.service.IAuthProvider;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * AuthConsumer
 *
 * @author star
 * @date 2019-11-22
 */
@FeignClient("org-comvita-server")
public interface AuthConsumer extends IAuthProvider {
}
