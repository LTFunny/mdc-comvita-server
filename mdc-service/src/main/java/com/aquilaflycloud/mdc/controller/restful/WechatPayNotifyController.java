package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.json.JSONObject;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatBusinessMallTradeNotifyService;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatPayService;
import com.aquilaflycloud.mdc.service.EasyPayService;
import com.aquilaflycloud.util.RedisUtil;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    @Resource
    private WechatBusinessMallTradeNotifyService wechatBusinessMallTradeNotifyService;

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

    @RequestMapping(value = "wechatBusinessMallTrade/{mallPath}", method = RequestMethod.POST)
    public ResponseEntity<Object> wechatBusinessMallTradeHandler(@PathVariable(value = "mallPath") String mallPath, HttpServletRequest request, HttpServletResponse response) {
        String serial = request.getHeader("Wechatpay-Serial");
        log.info("serial: {}", serial);
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        log.info("timestamp: {}", timestamp);
        String nonce = request.getHeader("Wechatpay-Nonce");
        log.info("nonce: {}", nonce);
        String signature = request.getHeader("Wechatpay-Signature");
        log.info("signature: {}", signature);
        String body;
        try {
            body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
            log.info("body: {}", body);
        } catch (IOException e) {
            log.error("获取requestBody失败", e);
            return null;
        }
        WxPayConfig payConfig = wechatPayService.getPayConfigByMallPath(mallPath);
        boolean validate = wechatPayService.validate(payConfig.getAppId(), body, serial, signature, timestamp, nonce);
        if (!validate) {
            return null;
        }
        JSONObject jsonObject = RedisUtil.syncLoad("wechatBusinessMallTrade" + new JSONObject(body).getStr("id"), () -> {
            //处理请求数据
            return wechatBusinessMallTradeNotifyService.saveNotifyInfo(body, mallPath);
        });

        //成功或失败都设置code和message，成功需设置HttpStatus值为200
        String code = jsonObject.getStr("code");
        String message = jsonObject.getStr("message");
        //设置返回结果
        JSONObject result = new JSONObject();
        result.set("code", code);
        result.set("message", message);

        //设置结果和http状态
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
