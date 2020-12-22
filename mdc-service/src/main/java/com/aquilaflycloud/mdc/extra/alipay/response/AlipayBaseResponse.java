package com.aquilaflycloud.mdc.extra.alipay.response;

import lombok.Data;

import java.io.Serializable;

/**
 * AlipayBaseResponse
 *
 * @author star
 * @date 2020-03-30
 */
@Data
public abstract class AlipayBaseResponse implements Serializable {
    private String code;

    private String msg;

    private String subCode;

    private String subMsg;

    private static final long serialVersionUID = 1L;
}
