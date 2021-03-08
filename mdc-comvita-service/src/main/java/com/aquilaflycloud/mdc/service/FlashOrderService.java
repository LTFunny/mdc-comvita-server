package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.pre.FlashConfirmOrderParam;
import com.aquilaflycloud.mdc.param.pre.FlashWriteOffOrderParam;

/**
 * @Author zly
 */
public interface FlashOrderService {

   String getFlashOrderInfo(FlashConfirmOrderParam param);

    void verificationFlashOrder(FlashWriteOffOrderParam param);
}
