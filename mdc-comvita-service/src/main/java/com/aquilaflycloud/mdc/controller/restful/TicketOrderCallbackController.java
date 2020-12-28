package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.json.JSONObject;
import com.aquilaflycloud.mdc.service.TicketCallbackFunctionRecordService;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 微信第三方平台回调接收处理实现
 *
 * @author huangxing
 * @date 2019-11-27
 */
@RestController
@Slf4j
public class TicketOrderCallbackController {
    @Resource
    private TicketCallbackFunctionRecordService ticketCallbackFunctionRecordService;

    @RequestMapping(value = "ticketOrderCallbackHandler", method = RequestMethod.POST)
    public JSONObject ticketOrderCallbackHandler(@RequestParam("OrderNo") String orderNo,
                                                 @RequestParam("OTAOrderNo") String otaOrderNo,
                                                 @RequestParam("OrderInfo")  String orderInfoStr) {
        try {
            return ticketCallbackFunctionRecordService.ticketOrderCallbackHandler(orderNo, otaOrderNo, orderInfoStr);
        } catch (ServiceException e) {
            System.out.println("回调控制器抛出的异常信息");
            log.error("订单回调失败", e);
            JSONObject result = new JSONObject();
            result.set("IsTrue", "false");
            result.set("ResultCode", 205);
            result.set("ResultMsg", e);
            return result;
        }
    }
}
