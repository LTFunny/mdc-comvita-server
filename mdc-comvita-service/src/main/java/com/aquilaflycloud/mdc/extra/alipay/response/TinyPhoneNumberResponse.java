package com.aquilaflycloud.mdc.extra.alipay.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TinyPhoneNumberResponse
 *
 * @author star
 * @date 2020-04-09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TinyPhoneNumberResponse extends AlipayBaseResponse {
    /**
     * 小程序用户手机号码
     */
    private String mobile;
}
