package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.pre.OrderExpressActionEnum;
import com.aquilaflycloud.mdc.param.pre.PreOrderExpressInfoParam;
import com.aquilaflycloud.mdc.result.pre.PreOrderExpressResult;
import com.aquilaflycloud.mdc.service.PreOrderExpressService;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PreOrderExpressServiceImpl
 *
 * @author zengqingjie
 * @date 2021-01-04
 */
@Service
@Slf4j
public class PreOrderExpressServiceImpl implements PreOrderExpressService {

    @Override
    public List<PreOrderExpressResult> queryTrackInfo(PreOrderExpressInfoParam param) {
        List<PreOrderExpressResult> result = new ArrayList<>();

        String queryStr = getOrderTracesByJson(param);

        if (StrUtil.isBlank(queryStr)) {
            log.error("PreOrderExpressServiceImpl: {expressOrder=" + param.getExpressOrder());
            return result;
        }

        return parseOrderTraces(queryStr);
    }

    public List<PreOrderExpressResult> parseOrderTraces (String queryStr) {
        List<PreOrderExpressResult> result = new ArrayList<>();

        JSONObject jsonObject = JSONUtil.parseObj(queryStr);
        Boolean success = jsonObject.getBool("Success");
        String state = jsonObject.getStr("State");

        JSONArray traces = jsonObject.getJSONArray("Traces");

        //没有物流信息
        if ("0".equals(state) && !success && null != traces && traces.size() == 0) {
            return result;
        }

        //有物流信息
        if (success && null != traces && traces.size() > 0) {
            for (int i = 0; i < traces.size(); i++) {
                JSONObject jsonItem = traces.getJSONObject(i);
                PreOrderExpressResult item = new PreOrderExpressResult();
                item.setData(jsonItem.getStr("AcceptTime"),
                        jsonItem.getStr("AcceptStation"),
                        jsonItem.getStr("Location"),
                        EnumUtil.likeValueOf(OrderExpressActionEnum.class, jsonItem.getStr("Action")));

                result.add(item);
            }
        } else {
            log.error("查询物流接口错误：" + queryStr);
            throw new ServiceException("查询物流失败");
        }

        return result;
    }

    /**
     * 请求获取物流数据
     * @param paramInfo
     * @return
     */
    public String getOrderTracesByJson(PreOrderExpressInfoParam paramInfo) {
        //TODO 上线修改
        //demo账号信息
        String eBusinessId = "test1693466";
        String appKey = "c17c0fe4-de83-4d83-beb3-1d575ee41f82";
        String reqUrl = "http://sandboxapi.kdniao.com:8080/kdniaosandbox/gateway/exterfaceInvoke.json";
        //prod账号信息
//        String eBusinessId = "1693466";
//        String appKey = "e9734857-c794-43a8-b892-cf6aacef34a5";
//        String reqUrl = "https://api.kdniao.com/Ebusiness/EbusinessOrderHandle.aspx";

        //构造请求参数
        JSONObject param = new JSONObject();
        //顺丰速递需传手机号后四位
        if ("SF".equals(paramInfo.getExpressName())) {
            String buyerPhone = paramInfo.getBuyerPhone();
            param.set("CustomerName", StrUtil.sub(buyerPhone, buyerPhone.length()-4, buyerPhone.length()));
        }

        param.set("OrderCode", "");
        param.set("ShipperCode", paramInfo.getExpressName());
        param.set("LogisticCode", paramInfo.getExpressOrder());

        String requestData= param.toString();

        //封装接口系统级参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("RequestData", URLUtil.encode(requestData));
        params.put("EBusinessID", eBusinessId);
        //TODO 上线修改
        //demo：1002；prod：8001
        params.put("RequestType", "1002");

        //构造请求签名
        String dataSign = "";
        if (null != appKey) {
            dataSign = Base64.encode(SecureUtil.md5(requestData + appKey));
        } else {
            Base64.encode(SecureUtil.md5(requestData));
        }
        params.put("DataSign", URLUtil.encode(dataSign));
        params.put("DataType", "2");

        //请求返回
        return HttpUtil.post(reqUrl, params);
    }
}
