package com.aquilaflycloud.mdc.controller.restful;

import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.util.MdcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * AuthorSiteOauthController
 *
 * @author star
 * @date 2019-10-08
 */
@RestController
@Slf4j
public class ParkingAjbQrcodeController {
    @RequestMapping("scanParking/{encryptParam}/ajb/{action}/{fileName}")
    public Object scanParking(@PathVariable("encryptParam") String encryptParam, @PathVariable("action") String action, @PathVariable("fileName") String fileName,
                              HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = new HashMap<>();
        String paramArray = MdcUtil.getCryption(Long.valueOf(encryptParam));
        for (String param : StrUtil.split(paramArray, ";")) {
            if (StrUtil.isNotBlank(param)) {
                String[] p = StrUtil.split(param, "=");
                params.put(p[0], p[1]);
            }
        }
        String name = params.get("wechatVerifyName");
        String content = params.get("wechatVerifyContent");
        if (StrUtil.equals(fileName, name.split(".")[0])) {
            return content;
        }
        return null;
    }

    @RequestMapping("scanParking/{encryptParam}/ajb/{action}")
    public Object scanParking(@PathVariable("encryptParam") String encryptParam, @PathVariable("action") String action,
                              HttpServletRequest request, HttpServletResponse response) {
        if (StrUtil.isNotBlank(encryptParam) && StrUtil.isNotBlank(action)) {
            Map<String, String> params = new HashMap<>();
            String paramArray = MdcUtil.getCryption(Long.valueOf(encryptParam));
            if (StrUtil.isBlank(paramArray)) {
                return null;
            }
            for (String param : StrUtil.split(paramArray, ";")) {
                if (StrUtil.isNotBlank(param)) {
                    String[] p = StrUtil.split(param, "=");
                    params.put(p[0], p[1]);
                }
            }
            Map<String, String> otherParams = getParams(request);
            otherParams.remove("encryptParam");
            params.putAll(otherParams);
            String userAgent = request.getHeader("user-agent").toLowerCase();
            log.info("客户端类型: " + userAgent);
            String encrypt = null;
            String url = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME)).append("/rest/").append(MdcUtil.getServerName()).toString();
            String redirectUrl;
            if (userAgent.contains("micromessenger")) {//微信客户端
                if (StrUtil.equals(action, "carIn")) {
                    encrypt = params.get("wechatCarIn");
                } else if (StrUtil.equalsAny(action, "charge", "if")) {
                    encrypt = params.get("wechatCharge");
                }
                redirectUrl = String.format(url + "/wechatAuthorSiteOauthUrl?encryptParam=%s&d=%s", encrypt, URLUtil.encode(params.get("d")));
            } else {
                if (StrUtil.equals(action, "carIn")) {
                    encrypt = params.get("alipayCarIn");
                } else if (StrUtil.equalsAny(action, "charge", "if")) {
                    encrypt = params.get("alipayCharge");
                }
                redirectUrl = String.format(url + "/alipayAuthorSiteOauthUrl?encryptParam=%s&d=%s", encrypt, URLUtil.encode(params.get("d")));
            }
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                log.error("重定向失败:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    private Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    map.put(paramName, paramValue);
                }
            }
        }
        return map;
    }
}
