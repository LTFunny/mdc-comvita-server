package com.aquilaflycloud.mdc.extra.wechat.handler;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author star
 */
@Component
@Slf4j
public class MassSendJobHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) {
        String msgId = wxMessage.getMsgId().toString();
        String appId = wxMpService.getWxMpConfigStorage().getAppId();
        String status = wxMessage.getStatus();
        String totalCount = wxMessage.getTotalCount().toString();
        String filterCount = wxMessage.getFilterCount().toString();
        String sentCount = wxMessage.getSentCount().toString();
        String errorCount = wxMessage.getErrorCount().toString();
        Map copyrightCheckResult = (Map) wxMessage.getAllFieldsMap().get("CopyrightCheckResult");
        String checkState = (String) copyrightCheckResult.get("CheckState");
        String count = (String) copyrightCheckResult.get("Count");
        String resultListString = JSONUtil.toJsonStr(copyrightCheckResult.get("ResultList"));
        log.info("微信群发结果通知: " + appId + ",状态: " + status);
        /*WechatPushResultModel resultModel = new WechatPushResultModel();
        resultModel.setAppId(appId);
        resultModel.setMsgId(msgId);
        resultModel.setStatus(status);
        resultModel.setTotalCount(totalCount);
        resultModel.setFilterCount(filterCount);
        resultModel.setSentCount(sentCount);
        resultModel.setErrorCount(errorCount);
        resultModel.setCheckState(checkState);
        resultModel.setCount(count);
        resultModel.setResultList(resultListString);
        WechatPushResultReq resultReq = new WechatPushResultReq();
        resultReq.setWechatPushResult(resultModel);
        IWechatPushRpcServices wechatPushRpcServices = SpringContextHolder.getRpcBean("wechatPushRpcServices");
        wechatPushRpcServices.updatePushResult(resultReq);*/
        return null;
    }

}
