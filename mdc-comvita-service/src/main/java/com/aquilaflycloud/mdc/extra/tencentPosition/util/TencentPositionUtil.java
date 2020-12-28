package com.aquilaflycloud.mdc.extra.tencentPosition.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.extra.tencentPosition.req.SuggestionListReq;
import com.aquilaflycloud.mdc.extra.tencentPosition.resp.SuggestionListResp;
import com.aquilaflycloud.mdc.result.system.TencentPositionAccountResult;
import com.aquilaflycloud.mdc.service.SystemAccountService;
import com.aquilaflycloud.util.SpringUtil;
import com.gitee.sop.servercommon.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TencentPositionUtil {

    private static final String APIDOMAIN = "https://apis.map.qq.com";

    private static TencentPositionAccountResult getAccount() {
        SystemAccountService service = SpringUtil.getBean(SystemAccountService.class);
        return service.getTencentPositionAccount();
    }

    public static List<SuggestionListResp> getSuggestionList(SuggestionListReq req) {
        TencentPositionAccountResult account = getAccount();
        Map<String, Object> params = BeanUtil.beanToMap(req, true, true);
        params.put("key", account.getKey());
        Map<String, Object> sortParams = new TreeMap<>(params);
        String preSign = req.getMethod() + "?" + URLUtil.decode(HttpUtil.toParams(sortParams));
        String sign = SecureUtil.md5(preSign + account.getSecret());
        params.put("sig", sign);
        String result = HttpUtil.get(APIDOMAIN + req.getMethod(), params);
        JSONObject resultJson = JSONUtil.parseObj(result);
        if (resultJson.getInt("status") == 0) {
            return JSONUtil.toList(resultJson.getJSONArray("data"), SuggestionListResp.class);
        }
        throw new ServiceException(resultJson.getStr("message"));
    }
}
