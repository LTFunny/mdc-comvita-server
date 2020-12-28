package com.aquilaflycloud.mdc.extra.alipay.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TinyRunDataResponse
 *
 * @author star
 * @date 2020-06-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TinyRunDataResponse extends AlipayBaseResponse {
    /**
     * 步数
     */
    private String count;

    /**
     * 查询日期
     */
    private String countDate;

    /**
     * 查询的时区
     */
    private String timeZone;
}
