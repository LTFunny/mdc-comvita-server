package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * LotteryErrorEnum
 *
 * @author star
 * @date 2020-04-07
 */
public enum LotteryErrorEnum {
    /**
     * 抽奖次数不足
     */
    LOTTERY_ERROR_10702("10702"),
    ;

    private ServiceErrorMeta errorMeta;

    LotteryErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("mdc.lottery_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
