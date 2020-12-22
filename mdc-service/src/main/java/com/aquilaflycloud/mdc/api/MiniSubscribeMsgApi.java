package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.param.wechat.MiniMessageTmplIdListParam;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * MiniSubscribeMsgApi
 *
 * @author star
 * @date 2020-03-30
 */
@RestController
@Api(tags = "小程序订阅消息接口")
public class MiniSubscribeMsgApi {
    @Resource
    private WechatMiniProgramSubscribeMessageService wechatMiniProgramSubscribeMessageService;

    @ApiOperation(value = "小程序订阅消息模板id列表", notes = "小程序订阅消息模板id列表")
    @ApiMapping(value = "mdc.wechat.messageTmplId.list", method = RequestMethod.POST)
    public List<String> listTmplId(MiniMessageTmplIdListParam param) {
        return wechatMiniProgramSubscribeMessageService.listTmplId(param);
    }
}
