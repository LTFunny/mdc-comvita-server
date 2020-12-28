package com.aquilaflycloud.mdc.extra.alipay.request;

import lombok.Data;

import java.io.Serializable;

/**
 * AlipayBaseRequest
 *
 * @author star
 * @date 2020-04-10
 */
@Data
public abstract class AlipayBaseRequest implements Serializable {
    private String appId;

    private static final long serialVersionUID = 1L;
}
