package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.PageParam;
import com.aquilaflycloud.mdc.model.wechat.WechatMiniLiveInfo;
import com.aquilaflycloud.mdc.result.wechat.WechatMiniLiveInfoResult;
import com.aquilaflycloud.mdc.service.WechatMiniLiveService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * MiniLiveInfoApi
 *
 * @author star
 * @date 2020-04-27
 */
@RestController
@Api(tags = "小程序直播相关接口")
public class MiniLiveInfoApi {
    @Resource
    private WechatMiniLiveService wechatMiniLiveService;

    @ApiOperation(value = "小程序直播间列表(分页)", notes = "小程序直播间列表(分页)")
    @ApiMapping(value = "mdc.wechat.miniLive.page", method = RequestMethod.POST)
    public IPage<WechatMiniLiveInfoResult> pageLive(PageParam<WechatMiniLiveInfo> param) {
        return wechatMiniLiveService.pageLive(param);
    }
}
