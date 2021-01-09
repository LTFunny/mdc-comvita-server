package com.aquilaflycloud.mdc.controller;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.wechat.MiniQrcodeGetParam;
import com.aquilaflycloud.mdc.service.WechatMiniFunctionService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * WechatMiniController
 *
 * @author star
 * @date 2021/1/8
 */
@Api(tags = "微信小程序相关接口")
@RestController
public class WechatMiniController {

    @Resource
    private WechatMiniFunctionService wechatMiniFunctionService;

    @ApiOperation(value = "根据内容生成小程序二维码", notes = "根据内容生成小程序二维码")
//    @PreAuthorize("hasAuthority('mdc:wechat:qrcode')")
    @ApiMapping(value = "backend.comvita.wechat.qrcode.get", method = RequestMethod.POST, permission = true)
    public BaseResult<String> getQrcode(MiniQrcodeGetParam param) {
        return wechatMiniFunctionService.getMiniQrcode(param);
    }

}
