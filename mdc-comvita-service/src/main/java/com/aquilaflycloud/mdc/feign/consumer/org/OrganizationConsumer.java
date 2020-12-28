package com.aquilaflycloud.mdc.feign.consumer.org;

import com.aquilaflycloud.org.service.IOrganizationProvider;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * OrganizationConsumer
 *
 * @author star
 * @date 2019-11-22
 */
@FeignClient("org-comvita-server")
public interface OrganizationConsumer extends IOrganizationProvider {
}
