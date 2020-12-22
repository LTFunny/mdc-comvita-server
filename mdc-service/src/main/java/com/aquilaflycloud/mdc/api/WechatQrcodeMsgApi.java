package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.param.wechat.QrCodeMsgForScanAddParam;
import com.aquilaflycloud.mdc.service.WechatAuthorSiteService;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * WechatQrcodeMsgApi
 *
 * @author star
 * @date 2020-04-02
 */
@RestController
@Api(tags = "微信公众号二维码回复接口")
public class WechatQrcodeMsgApi {
    @Resource
    private WechatAuthorSiteService wechatAuthorSiteService;

    @ApiOperation(value = "生成可扫码奖励的小票二维码", notes = "生成可扫码奖励的小票二维码")
    @ApiMapping(value = "mdc.wechat.qrCodeMsgForScan.add", method = RequestMethod.POST, permission = true)
    public BaseResult<String> addQrCodeMsgForScan(QrCodeMsgForScanAddParam param) {
        return wechatAuthorSiteService.addQrCodeMsgForScan(param);
    }
}
