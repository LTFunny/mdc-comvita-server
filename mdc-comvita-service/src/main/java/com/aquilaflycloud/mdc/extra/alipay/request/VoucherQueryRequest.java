package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VoucherQueryRequest
 *
 * @author star
 * @date 2020-06-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherQueryRequest extends AlipayBaseRequest {
    /**
     * 券ID(券唯一标识, 发券接口返回参数)
     */
    private String voucherId;
}
