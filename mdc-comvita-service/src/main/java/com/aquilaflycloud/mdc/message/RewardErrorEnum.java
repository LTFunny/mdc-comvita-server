package com.aquilaflycloud.mdc.message;

import com.gitee.sop.servercommon.message.ServiceErrorMeta;

/**
 * RewardErrorEnum
 *
 * @author star
 * @date 2020-05-09
 */
public enum RewardErrorEnum {
    /**
     * 该订单已领过奖励
     */
    REWARD_ERROR_10601("10601"),
    /**
     * 奖励值不足
     */
    REWARD_ERROR_10602("10602"),
    ;

    private ServiceErrorMeta errorMeta;

    RewardErrorEnum(String subCode) {
        this.errorMeta = new ServiceErrorMeta("comvita.reward_error_", subCode);
    }

    public ServiceErrorMeta getErrorMeta() {
        return errorMeta;
    }
}
