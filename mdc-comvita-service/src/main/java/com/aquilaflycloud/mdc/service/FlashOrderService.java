package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreFlashOrderInfo;
import com.aquilaflycloud.mdc.param.pre.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author zly
 */
public interface FlashOrderService {

   void getFlashOrderInfo(FlashConfirmOrderParam param);

    IPage<PreActivityInfo> pageMemberFlash(MemberFlashPageParam param);

    void verificationFlashOrder(FlashWriteOffOrderParam param);

    BaseResult<String> getFlashOrderCode(QueryFlashCodeParam param);

    IPage<PreFlashOrderInfo> pageFlashOrderInfo(FlashPageParam param);
}
