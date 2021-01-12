package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.wechat.MiniQrcodeGetParam;

/**
 * WechatMiniFunctionService
 *
 * @author star
 * @date 2021/1/9
 */
public interface WechatMiniFunctionService {
    BaseResult<String> getMiniQrcode(MiniQrcodeGetParam param);
}

