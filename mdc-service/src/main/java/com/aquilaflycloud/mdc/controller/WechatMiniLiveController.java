package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.mdc.param.wechat.WechatAuthorSiteGetParam;
import com.aquilaflycloud.mdc.service.WechatMiniLiveService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * WechatMiniLiveController
 *
 * @author star
 * @date 2020-04-27
 */
@Api(tags = "微信小程序直播相关接口")
@RestController
public class WechatMiniLiveController {

    @Resource
    private WechatMiniLiveService wechatMiniLiveService;

    @ApiOperation(value = "全量加载直播间列表", notes = "全量加载直播间列表")
    @PreAuthorize("hasAuthority('mdc:wechat:liveListLoad')")
    @ApiMapping(value = "backend.comvita.wechat.miniLive.load", method = RequestMethod.POST, permission = true)
    public void list(WechatAuthorSiteGetParam param) {
        wechatMiniLiveService.loadLiveInfo(param);
    }

}
