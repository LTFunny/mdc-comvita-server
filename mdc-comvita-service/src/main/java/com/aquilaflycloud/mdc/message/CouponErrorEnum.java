package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * CouponErrorEnum
 *
 * @author star
 * @date 2020-03-15
 */
public enum CouponErrorEnum {
    ;

    private ServiceErrorMeta errorMeta;

    CouponErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("mdc.coupon_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
