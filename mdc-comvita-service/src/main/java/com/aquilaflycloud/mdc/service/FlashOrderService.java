package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.param.pre.FlashConfirmOrderParam;
import com.aquilaflycloud.mdc.param.pre.FlashWriteOffOrderParam;
import com.aquilaflycloud.mdc.param.pre.MemberFlashPageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface FlashOrderService {

   String getFlashOrderInfo(FlashConfirmOrderParam param);

    IPage<PreActivityInfo> pageMemberFlash(MemberFlashPageParam param);

    void verificationFlashOrder(FlashWriteOffOrderParam param);
}
