package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatPayService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.github.binarywang.wxpay.config.WxPayConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * WechatPayConfigApi
 *
 * @author zengqingjie
 * @date 2020-09-21
 */
@RestController
@Api(tags = "微信支付配置查询")
public class WechatPayConfigApi {
    @Resource
    private WechatPayService wechatPayService;

    @ApiOperation(value = "获取商圈号", notes = "获取商圈号")
    @ApiMapping(value = "comvita.wechat.mchid.get", method = RequestMethod.POST)
    public BaseResult<String> getMchid() {
        String appId = MdcUtil.getOtherAppId();
        WxPayConfig payConfig = wechatPayService.getPayConfig(appId);
        if (payConfig == null) {
            throw new ServiceException("配置不存在");
        }
        return BaseResult.buildResult(payConfig.getMchId());
    }
}
