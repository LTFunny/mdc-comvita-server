package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VoucherListQueryRequest
 *
 * @author star
 * @date 2020-06-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherListQueryRequest extends AlipayBaseRequest {
    /**
     * 券模板ID
     */
    private String templateId;

    /**
     * 支付宝用户ID
     */
    private String userId;
}
