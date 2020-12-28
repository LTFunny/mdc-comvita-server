package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * ExchangeErrorEnum
 *
 * @author star
 * @date 2020-03-15
 */
public enum ExchangeErrorEnum {
    ;

    private ServiceErrorMeta errorMeta;

    ExchangeErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("mdc.exchange_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
