package com.aquilaflycloud.mdc.controller;

import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatMiniService;
import com.aquilaflycloud.mdc.param.wechat.MiniQrcodeAddParam;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.AliOssUtil;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import com.gitee.sop.servercommon.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
    private WechatMiniService wechatMiniService;

    @ApiOperation(value = "根据内容生成小程序二维码", notes = "根据内容生成小程序二维码")
//    @PreAuthorize("hasAuthority('mdc:wechat:qrcode')")
    @ApiMapping(value = "backend.comvita.wechat.qrcode.add", method = RequestMethod.POST, permission = true)
    public BaseResult<String> addQrcode(MiniQrcodeAddParam param) {
        try {
            File file = wechatMiniService.getWxMaServiceByAppId(param.getAppId()).getQrcodeService().createQrcode(param.getPath(), param.getWidth());
            //上传文件至服务器并返回url
            try {
                String path = param.getAppId() + "/" + StrUtil.split(param.getPath(), "?")[0].replace("/", ".");
                String name = MdcUtil.getSnowflakeIdStr();
                String url = AliOssUtil.uploadFile(path, StrUtil.appendIfMissing(name, ".png"), new FileInputStream(file), AliOssUtil.MEMBER_STYLE);
                return BaseResult.buildResult(url);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new ServiceException("上传小程序二维码失败");
            }
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new ServiceException(String.valueOf(e.getError().getErrorCode()), e.getError().getErrorMsg());
        }
    }

}
