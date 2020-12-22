package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniLiveInfo;
import com.aquilaflycloud.mdc.param.wechat.WechatAuthorSiteGetParam;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniLiveInfoResult;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * WechatMiniLiveService
 *
 * @author star
 * @date 2020-04-27
 */
public interface WechatMiniLiveService {
    void loadLiveInfo(WechatAuthorSiteGetParam param);

    IPage<WechatMiniLiveInfoResult> pageLive(PageParam<WechatMiniLiveInfo> param);
}

