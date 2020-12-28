package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * ParkingErrorEnum
 *
 * @author star
 * @date 2020-01-16
 */
public enum ParkingErrorEnum {
    /**
     * 停车券领取超过限制
     */
    PARKING_ERROR_10501("10501"),
    /**
     * 停车券使用超过限制
     */
    PARKING_ERROR_10502("10502"),
    /**
     * 停车计费不需支付
     */
    PARKING_ERROR_10503("10503"),
    /**
     * 停车全免券只能单独使用
     */
    PARKING_ERROR_10504("10504"),
    ;
    private ServiceErrorMeta errorMeta;

    ParkingErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("mdc.parking_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
