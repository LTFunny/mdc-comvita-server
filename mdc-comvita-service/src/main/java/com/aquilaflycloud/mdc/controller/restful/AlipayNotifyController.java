package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.extra.alipay.notify.AlipayCashVoucherNotify;
import com.aquilaflycloud.mdc.extra.alipay.notify.AlipayCashVoucherPay;
import com.aquilaflycloud.mdc.extra.alipay.notify.AlipayNotify;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayBusinessMallTradeNotifyService;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayBusinessMallTradeRefundNotifyService;
import com.aquilaflycloud.mdc.model.alipay.AlipayBusinessMallTradeInfo;
import com.aquilaflycloud.mdc.model.alipay.AlipayBusinessMallTradeRefundInfo;
import com.aquilaflycloud.mdc.service.CouponInfoService;
import com.aquilaflycloud.mdc.service.EasyPayService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * AlipayNotifyController
 *
 * @author star
 * @date 2020-04-09
 */
@RestController
@Slf4j
public class AlipayNotifyController {

    @Resource
    private EasyPayService easyPayService;

    @Resource
    private CouponInfoService couponInfoService;

    @Resource
    private AlipayBusinessMallTradeNotifyService alipayBusinessMallTradeNotifyService;

    @Resource
    private AlipayBusinessMallTradeRefundNotifyService alipayBusinessMallTradeRefundNotifyService;

    @RequestMapping(value = "alipayNotify")
    public Object alipayNotifyHandler(@RequestParam Map map) {
        log.info("支付宝支付成功回调:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        AlipayNotify notify = BeanUtil.toBeanIgnoreError(map, AlipayNotify.class);
        RedisUtil.syncLoad("alipayNotify" + notify.getOutTradeNo(), () -> {
            easyPayService.finishOrder(notify);
            return null;
        });
        return "success";
    }

    @RequestMapping(value = "alipay/cashVoucherTemplate/pay")
    public Object cashVoucherTemplatePayHandler(@RequestParam Map map, HttpServletResponse response) {
        log.info("支付宝现金抵扣券支付成功重定向:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        AlipayCashVoucherPay notify = BeanUtil.toBeanIgnoreError(map, AlipayCashVoucherPay.class);
        Long couponId = RedisUtil.syncLoad("cashVoucherTemplatePay" + notify.getOutRequestNo(),
                () -> couponInfoService.updateThirdCouponStatus(notify));
        String route = MdcUtil.getConfigValue(ConfigTypeEnum.ALIPAY_CASH_VOUCHER_REDIRECT_ROUTE_NAME);
        if (StrUtil.isNotBlank(route)) {
            String redirectUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.MDC_URL_DOMAIN_NAME))
                    .append(route).append(couponId).toString();
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                log.error("重定向失败:" + e.getMessage());
                return "success";
            }
        }
        return "success";
    }

    @RequestMapping(value = "alipay/cashVoucherTemplate/notify")
    public Object cashVoucherTemplateNotifyHandler(@RequestParam Map map) {
        log.info("支付宝现金抵扣券状态回调:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        String notifyId = Convert.toStr(map.get("notify_id"));
        String status = Convert.toStr(map.get("status"));
        RedisUtil.syncLoad("cashVoucherTemplateNotify" + notifyId, () -> {
            String bizContent = Convert.toStr(map.get("biz_content"));
            Map<String, String> bizContentMap = JSONUtil.toBean(bizContent, Map.class);
            AlipayCashVoucherNotify notify = new AlipayCashVoucherNotify();
            BeanUtil.fillBeanWithMap(bizContentMap, notify, true, true);
            if (StrUtil.equals("V_USE", status)) {
                //核销优惠券更新状态
                couponInfoService.updateThirdCouponRelStatus(notify);
            }
            return null;
        });
        return "success";
    }

    @RequestMapping(value = "alipayBusinessMallTradeSuccess", method = RequestMethod.GET)
    public Object alipayBusinessMallTradeHandler(
            @RequestParam("charset") String charset,
            @RequestParam("biz_content") String bizContent,
            @RequestParam("utc_timestamp") String utcTimestamp,
            @RequestParam("version") String version,
            @RequestParam("sign_type") String signType,
            @RequestParam("notify_id") String notifyId,
            @RequestParam("app_id") String appId,
            @RequestParam("sign") String sign) {

        log.info("支付宝商圈交易成功信息订阅回调报文:{charset:" + charset + ", utc_timestamp:" + utcTimestamp + ", version:" + version
                + ", sign_type:" + signType + ", notify_id:" + notifyId+ ", app_id:" + appId+ ", sign:" + sign+ ", biz_content:" + bizContent+"}");
        if (StringUtils.isBlank(bizContent) || StringUtils.isBlank(appId) || StringUtils.isBlank(notifyId)) {
            return "failed";
        }

        RedisUtil.syncLoad("alipayBusinessMallTrade" + notifyId, () -> {
            //封装数据
            JSONObject contentJson = new JSONObject(bizContent);
            AlipayBusinessMallTradeInfo notify = new AlipayBusinessMallTradeInfo(charset, utcTimestamp, version, signType, notifyId, appId, sign);
            BeanUtil.copyProperties(contentJson, notify);
            alipayBusinessMallTradeNotifyService.saveNotifyInfo(notify);
            return null;
        });

        return "success";
    }

    @RequestMapping(value = "alipayBusinessMallTradeRefund", method = RequestMethod.GET)
    public Object alipayBusinessMallTradeRefundedHandler(
            @RequestParam("charset") String charset,
            @RequestParam("biz_content") String bizContent,
            @RequestParam("utc_timestamp") String utcTimestamp,
            @RequestParam("version") String version,
            @RequestParam("sign_type") String signType,
            @RequestParam("notify_id") String notifyId,
            @RequestParam("app_id") String appId,
            @RequestParam("sign") String sign) {
        log.info("支付宝商圈交易退款信息订阅回调:{charset:" + charset + ", utc_timestamp:" + utcTimestamp + ", version:" + version
                + ", sign_type:" + signType + ", notify_id:" + notifyId+ ", app_id:" + appId+ ", sign:" + sign+ ", biz_content:" + bizContent+"}");
        if (StringUtils.isBlank(bizContent) || StringUtils.isBlank(appId) || StringUtils.isBlank(notifyId)) {
            return "failed";
        }

        RedisUtil.syncLoad("alipayBusinessMallTradeRefund" + notifyId, () -> {
            //封装数据
            JSONObject contentJson = new JSONObject(bizContent);
            AlipayBusinessMallTradeRefundInfo notify = new AlipayBusinessMallTradeRefundInfo(charset, utcTimestamp, version, signType, notifyId, appId, sign);
            BeanUtil.copyProperties(contentJson, notify);
            alipayBusinessMallTradeRefundNotifyService.saveRefundNotifyInfo(notify);
            return null;
        });

        return "success";
    }
}
