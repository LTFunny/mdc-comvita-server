package com.aquilaflycloud.mdc.controller.restful;

import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayOpenPlatformService;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatOpenPlatformService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthorSiteCodeController
 * 微信或支付宝第三方平台回调接收处理实现
 *
 * @author huangxing
 * @date 2018-09-06
 */
@RestController
@Slf4j
public class AuthorSiteCodeController {
    @Resource
    private WechatOpenPlatformService wechatOpenPlatformService;
    @Resource
    private AlipayOpenPlatformService alipayOpenPlatformService;

    @RequestMapping(value = "authorSiteCodeHandler/{state}", method = RequestMethod.GET)
    public Object authorSiteCodeHandler(@PathVariable("state") String state,
                                        @RequestParam("auth_code") String authCode,
                                        HttpServletResponse response) throws IOException {
        log.info("微信自定义参数{}, 授权码{}", state, authCode);
        try {
            wechatOpenPlatformService.saveWeChatAuthorSite(state, authCode, false);
            response.sendRedirect(MdcUtil.getConfigValue(ConfigTypeEnum.MDC_WECHAT_SUCCESS_RETURN_URL));
            return null;
        } catch (ServiceException e) {
            log.error("微信授权失败", e);
            return "授权失败";
        }
    }

    @RequestMapping(value = "alipayAuthorSiteCodeHandler", method = RequestMethod.GET)
    public Object alipayAuthorSiteCodeHandler(@RequestParam("state") String state,
                                              @RequestParam("app_auth_code") String authCode,
                                              HttpServletResponse response) throws IOException {
        log.info("支付宝自定义参数{}, 授权码{}", state, authCode);
        try {
            alipayOpenPlatformService.saveAlipayAuthorSite(state, authCode);
            response.sendRedirect(MdcUtil.getConfigValue(ConfigTypeEnum.MDC_WECHAT_SUCCESS_RETURN_URL));
            return null;
        } catch (ServiceException e) {
            log.error("支付宝授权失败", e);
            return "授权失败";
        }
    }
}
