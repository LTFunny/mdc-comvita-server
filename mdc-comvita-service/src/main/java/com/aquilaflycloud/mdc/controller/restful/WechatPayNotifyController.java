package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatPayService;
import com.aquilaflycloud.mdc.service.EasyPayService;
import com.aquilaflycloud.util.RedisUtil;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * WechatPayNotifyController
 *
 * @author star
 * @date 2020/8/6
 */
@RestController
@Slf4j
public class WechatPayNotifyController {
    @Resource
    private EasyPayService easyPayService;
    @Resource
    private WechatPayService wechatPayService;

    @RequestMapping(value = "wechatPayNotify")
    public Object wechatPayNotifyHandler(@RequestBody String xmlData) {
        log.info("微信支付成功回调:{}", xmlData);
        if (StrUtil.isBlank(xmlData)) {
            return WxPayNotifyResponse.fail("回调参数错误");
        }
        Map<String, Object> map = XmlUtil.xmlToMap(xmlData);
        String appId = Convert.toStr(map.get("appid"));
        try {
            WxPayOrderNotifyResult notify = wechatPayService.getWxPayServiceByAppId(appId).parseOrderNotifyResult(xmlData);
            RedisUtil.syncLoad("wechatPayNotify" + notify.getOutTradeNo(), () -> {
                easyPayService.finishOrder(notify);
                return null;
            });
            return WxPayNotifyResponse.success("成功");
        } catch (WxPayException e) {
            log.error("回调微信支付失败", e);
            return WxPayNotifyResponse.fail(e.getErrCodeDes());
        } catch (ServiceException e) {
            log.error("回调微信支付判断失败", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }

    @RequestMapping(value = "wechatRefundNotify")
    public Object wechatRefundNotifyHandler(@RequestBody String xmlData) {
        log.info("微信退款成功回调:{}", xmlData);
        if (StrUtil.isBlank(xmlData)) {
            return WxPayNotifyResponse.fail("回调参数错误");
        }
        Map<String, Object> map = XmlUtil.xmlToMap(xmlData);
        String appId = Convert.toStr(map.get("appid"));
        try {
            WxPayRefundNotifyResult notify = wechatPayService.getWxPayServiceByAppId(appId).parseRefundNotifyResult(xmlData);
            RedisUtil.syncLoad("wechatRefundNotify" + notify.getNonceStr(), () -> {
                easyPayService.finishRefundOrder(notify);
                return null;
            });
            return WxPayNotifyResponse.success("成功");
        } catch (WxPayException e) {
            log.error("回调微信退款失败", e);
            return WxPayNotifyResponse.fail(e.getErrCodeDes());
        } catch (ServiceException e) {
            log.error("回调微信退款判断失败", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
    }
}
