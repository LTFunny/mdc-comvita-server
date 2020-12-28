package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.URLUtil;
import com.aquilafly.easypay.notify.RefundNotify;
import com.aquilafly.easypay.notify.UnionPayNotify;
import com.aquilaflycloud.mdc.service.EasyPayService;
import com.aquilaflycloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * EasyPayNotifyController
 *
 * @author star
 * @date 2019-12-07
 */
@RestController
@Slf4j
public class EasyPayNotifyController {

    @Resource
    private EasyPayService easyPayService;

    @RequestMapping(value = "unionPayNotify")
    public Object unionPayNotifyHandler(@RequestParam Map map) {
        log.info("支付成功回调:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        UnionPayNotify notify = BeanUtil.toBeanIgnoreError(map, UnionPayNotify.class);
        notify.setRa_PayTime(URLUtil.decode(notify.getRa_PayTime()));
        notify.setRb_DealTime(URLUtil.decode(notify.getRb_DealTime()));
        RedisUtil.syncLoad("unionPayNotify" + notify.getR2_OrderNo(), () -> {
            easyPayService.finishOrder(notify);
            return null;
        });
        return "success";
    }

    @RequestMapping(value = "refundNotify")
    public Object refundNotifyHandler(@RequestParam Map map) {
        log.info("退款成功回调:{}", map);
        if (CollUtil.isEmpty(map)) {
            return "failed";
        }
        RefundNotify notify = BeanUtil.toBeanIgnoreError(map, RefundNotify.class);
        RedisUtil.syncLoad("refundNotify" + notify.getR2_OrderNo(), () -> {
            easyPayService.finishRefundOrder(notify);
            return null;
        });
        return "success";
    }
}
