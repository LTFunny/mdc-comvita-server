package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VoucherTemplateDeleteRequest
 *
 * @author star
 * @date 2020-06-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VoucherTemplateDeleteRequest extends AlipayBaseRequest {
    /**
     * 券模板ID
     */
    private String templateId;
}
