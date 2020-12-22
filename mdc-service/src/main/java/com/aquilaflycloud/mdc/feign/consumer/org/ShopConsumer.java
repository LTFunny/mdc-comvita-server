package com.aquilaflycloud.mdc.feign.consumer.org;

import com.aquilaflycloud.org.service.IShopProvider;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * ShopConsumer
 *
 * @author zengqingjie
 * @date 2020-04-17
 */
@FeignClient("org-server")
public interface ShopConsumer extends IShopProvider {
}
