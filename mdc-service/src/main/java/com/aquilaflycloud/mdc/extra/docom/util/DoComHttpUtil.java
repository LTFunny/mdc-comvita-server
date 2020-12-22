package com.aquilaflycloud.mdc.extra.docom.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.schedulerx.worker.util.SpringContext;
import com.aquilaflycloud.mdc.extra.docom.component.DoComConfig;
import com.aquilaflycloud.mdc.mapper.TicketInterfaceReturnRecordMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketInterfaceReturnRecord;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Zeng.qingjie
 * @description 调用道控接口工具类
 * @date 2019/10/24 14:57
 */
@Slf4j
public class DoComHttpUtil {
    private final static String GET_PRODUCT = "GetProduct";
    private final static String QUERY_BALANCE = "QueryBalance";
    private final static String CREATE_ORDER = "CreateOrder";
    private final static String RELEASE_ORDER = "OrderRelease";
    private final static String PAY_ORDER = "PayOrder";
    private final static String CREATE_AND_PAY_ORDER = "CreateAndPayOrder";
    private final static String REFUND_ORDER = "RefundOrder";
    private final static String QUERY_ORDER = "QueryOrder";
    private final static Map<String, String> HEADER = new HashMap<String, String>() {{
        put("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
    }};

    /**
     * @param params 请求的参数
     * @param method 请求的方法名
     * @return 接口调用的json
     */
    private static JSONObject getRequest(DoComConfig doComConfig, Map<String, Object> params, String method) {
        JSONObject resultObject = new JSONObject();
        String isTrue = "true";
        int code = -1;
        String msg = "调用成功";
        String jsonStr = "null";

        String sortParamStr = getSortParamStr(doComConfig, params);
        log.info("get请求的接口参数: " + sortParamStr);
        //加入秘钥的参数字符串
        String encryParamStr = sortParamStr + "&Secret=" + doComConfig.getSecret();
        //进行md5加密
        String sign = SecureUtil.md5(encryParamStr);
        //将验证签名拼接到url
        String urlStr = doComConfig.getBaseUrl() + method + "?" + sortParamStr + "&Sign=" + sign;
        log.info("get请求的接口url: " + urlStr);
        String result = "";

        try {
            //请求第三方接口
            result = HttpUtil.createGet(urlStr).headerMap(HEADER, true).execute().charset("UTF-8").body();
            //解析请求结果
            JSONObject resultJsonObject = JSONUtil.parseObj(result);
            log.info("get请求的接口返回值: " + result);

            //解析接口调用结果
            isTrue = resultJsonObject.getStr("IsTrue");
            code = resultJsonObject.getInt("ResultCode");
            msg = resultJsonObject.getStr("ResultMsg");
            jsonStr = resultJsonObject.getStr("ResultJson");
            TicketInterfaceReturnRecordMapper bean = SpringContext.getBean(TicketInterfaceReturnRecordMapper.class);
            TicketInterfaceReturnRecord ticketInterfaceReturnRecord = new TicketInterfaceReturnRecord(isTrue, code, msg, jsonStr, urlStr);
            int count = bean.normalInsert(ticketInterfaceReturnRecord);
            log.info("此次保存请求结果条数：" + count);

            /*if ("false".equals(isTrue)) {
                //增加钉钉告警
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=0ac1903526d564be649d789adb688e34425a66a6e3f77ecc0c6567802f6b1bbf");
                OapiRobotSendRequest request = new OapiRobotSendRequest();
                request.setMsgtype("text");
                OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
                text.setContent("三馆购票系统调用道控接口告警：地址:" + urlStr + " ," + DateTime.now().toString() + ",返回接口数据:" + result +",报错信息:" + msg);
                request.setText(text);
                OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
                at.setAtMobiles(Arrays.asList("15118884685"));
                request.setAt(at);
                OapiRobotSendResponse response;
                response = client.execute(request);
                log.info(response.getErrorCode());

            }*/
        } catch (Exception e) {
            isTrue = "false";
            msg = "调用接口抛出异常：" + e.getMessage();
            log.error(msg);

            //增加钉钉告警
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=0ac1903526d564be649d789adb688e34425a66a6e3f77ecc0c6567802f6b1bbf");
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent("三馆购票系统调用道控接口告警：地址:" + urlStr + " ," + DateTime.now().toString() + ",返回接口数据:" + result + ",报错信息:" + e.getMessage());
            request.setText(text);
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(Arrays.asList("15118884685"));
            request.setAt(at);
            OapiRobotSendResponse response;
            try {
                response = client.execute(request);
                log.info(response.getErrorCode());
            } catch (ApiException ex) {
                ex.printStackTrace();
            }
        }

        //封装最终的返回结果
        resultObject.set("isTrue", isTrue);
        resultObject.set("code", code);
        resultObject.set("msg", msg);
        resultObject.set("jsonStr", jsonStr);
        return resultObject;
    }

    public static JSONObject getProduct(DoComConfig doComConfig) throws IOException {
        DateTime now = DateUtil.date();
        String startDate = DateUtil.format(now, "yyyy-MM-dd");
        //推后60天
        DateTime after = DateUtil.offset(now, DateField.DAY_OF_YEAR, 60);
        Map<String, Object> params = new HashMap<>();
        params.put("SDate", startDate);
        params.put("EDate", DateUtil.format(after, "yyyy-MM-dd"));
        return getRequest(doComConfig, params, GET_PRODUCT);
    }

    public static JSONObject queryBalance(DoComConfig doComConfig, Map<String, Object> params) throws IOException {
        return getRequest(doComConfig, params, QUERY_BALANCE);
    }

    public static JSONObject createOrder(DoComConfig doComConfig, Map<String, Object> params) throws IOException {
        return getRequest(doComConfig, params, CREATE_ORDER);
    }

    public static JSONObject releaseOrder(DoComConfig doComConfig, Map<String, Object> params) throws IOException {
        return getRequest(doComConfig, params, RELEASE_ORDER);
    }

    public static JSONObject payOrder(DoComConfig doComConfig, Map<String, Object> params) throws IOException {
        return getRequest(doComConfig, params, PAY_ORDER);
    }

    public static JSONObject createAndPayOrder(DoComConfig doComConfig, Map<String, Object> params) throws IOException {
        return getRequest(doComConfig, params, CREATE_AND_PAY_ORDER);
    }

    public static JSONObject refundOrder(DoComConfig doComConfig, Map<String, Object> params) throws IOException {
        return getRequest(doComConfig, params, REFUND_ORDER);
    }

    public static JSONObject queryOrder(DoComConfig doComConfig, Map<String, Object> params) throws IOException {
        return getRequest(doComConfig, params, QUERY_ORDER);
    }


    public static void main(String[] args) throws IOException {
//        DoComConfig config = new DoComConfig("S190917368", "yunyingota01", "d8fdcf8719a24efaac847f445f40ae8e", "http://otainterface.pay.x-jing.cn/Index/");
        //生产
//        DoComConfig config = new DoComConfig("S190722160", "zjhygxcx", "6d7d81eb53c54ee3b667ad96ea117fbb", "http://otainterface.core.x-jing.com/Index/");
//        DoComConfig config = new DoComConfig("S190701338", "zjbwgxcx", "3b8b38dd4aba4b0c8feec6378a5aa32f", "http://otainterface.core.x-jing.com/Index/");
        DoComConfig config = new DoComConfig("S190626765", "zjylgxcx", "34d9aaa3ab3f4ffb89fa04145064c1d1", "http://otainterface.core.x-jing.com/Index/");

        System.out.println(getProduct(config).toString());
        //2、测试创建订单接口
        /*JSONObject customer = new JSONObject();
        customer.put("CustomerName", "测试1");
        customer.put("CustomerPhone", "13800000000");

        JSONArray customerList = new JSONArray();
        customerList.add(customer);

        JSONObject goodsDetailInfo = new JSONObject();
        goodsDetailInfo.put("ProductId", "773");
        goodsDetailInfo.put("ProductCount", 2);
        goodsDetailInfo.put("ProductPrice", BigDecimal.valueOf(0.1));
        goodsDetailInfo.put("ProductSellPrice", BigDecimal.valueOf(0.2));
        goodsDetailInfo.put("CustomerData", customerList);

        JSONArray goodsDetails = new JSONArray();
        goodsDetails.add(goodsDetailInfo);

        Map<String, Object> params = new JSONObject();
        params.put("OTAOrderNo", "C20191028118009581");
        params.put("GoodsDetails", goodsDetails);
        params.put("PlayDate", "2019-12-12");
        params.put("Amount", "0.4");


        JSONObject result = createOrder(config, params);
        System.out.println(result.toString());*/


        //3、释放订单
        /*Map<String, Object> params = new HashMap<>();
        params.put("OTAOrderNo", "C20191028118009579");
        System.out.println(releaseOrder(config, params));*/

        //4、支付订单
        /*Map<String, Object> params = new HashMap<>();
        params.put("OTAOrderNo", "1205041183404085249");
        System.out.println(payOrder(config, params));*/

        //5、创建并支付订单
//        JSONObject customer = new JSONObject();
//        customer.put("CustomerName", "测试1");
//        customer.put("CustomerPhone", "13800000000");
//
//        JSONArray customerList = new JSONArray();
//        customerList.add(customer);
//
//        JSONObject goodsDetailInfo = new JSONObject();
//        goodsDetailInfo.put("ProductId", "773");
//        goodsDetailInfo.put("ProductCount", 2);
//        goodsDetailInfo.put("ProductPrice", BigDecimal.valueOf(0.01));
//        goodsDetailInfo.put("ProductSellPrice", BigDecimal.valueOf(0.01));
//        goodsDetailInfo.put("CustomerData", customerList);

        /*JSONObject customer2 = new JSONObject();
        customer2.put("CustomerName", "测试1");
        customer2.put("CustomerPhone", "13800000000");

        JSONArray customerList2 = new JSONArray();
        customerList2.add(customer2);

        JSONObject goodsDetailInfo2 = new JSONObject();
        goodsDetailInfo2.put("ProductId", "773");
        goodsDetailInfo2.put("ProductCount", 2);
        goodsDetailInfo2.put("ProductPrice", BigDecimal.valueOf(0.1));
        goodsDetailInfo2.put("ProductSellPrice", BigDecimal.valueOf(0.2));
        goodsDetailInfo2.put("CustomerData", customerList2);*/


//        JSONArray goodsDetails = new JSONArray();
//        goodsDetails.add(goodsDetailInfo);
//        goodsDetails.add(goodsDetailInfo2);

//        Map<String, Object> params = new JSONObject();
//        params.put("OTAOrderNo", "C20191028118009783");
//        params.put("GoodsDetails", goodsDetails);
//        params.put("PlayDate", "2019-12-12");
//        params.put("Amount", "0.02");
//
//        System.out.println(createAndPayOrder(config, params));

        //6、退款
        /*Map<String, Object> params = new HashMap<>();

        JSONArray goodsDetails = new JSONArray();
        JSONObject detail = new JSONObject();
        detail.put("ECode", "32152969831912182");
        detail.put("ProductCount", 2);

        goodsDetails.add(detail);

        params.put("OTAOrderNo", "C20191028118009781");
        params.put("GoodsDetails", goodsDetails);

        System.out.println(refundOrder(config, params));*/

        //7、订单查询
        /*Map<String, Object> params = new HashMap<>();
        params.put("OTAOrderNo", "C20191028118009700");
        params.put("ECode", "32100692421910571");
        System.out.println(queryOrder(params));*/

        //测试财务查询接口
        /*Map<String, Object> params = new HashMap<>();
        System.out.println(queryBalance(params));*/


    }


    /**
     * 将参数根据map中的key进行排序,并组装成url中get请求参数
     *
     * @param params
     * @return
     */
    public static String getSortParamStr(DoComConfig doComConfig, Map<String, Object> params) {
        //获取时间秒值
        long totalSeconds = System.currentTimeMillis() / 1000;
        //生成指定length的随机字符串(A-Z，a-z，0-9),用于防止重放攻击
        String randomString = RandomStringUtils.randomAlphanumeric(32);

        params.put("MerchantCode", doComConfig.getMerchantCode());
        params.put("InterFaceAccount", doComConfig.getInterfaceAccount());
        params.put("Timestamp", totalSeconds);
        params.put("Nonce", randomString);

        //对参数集合进行排序(字母表或数字表里递增顺序的排列次序,空值不参与排序)
        Set paramsSet = params.keySet();
        Object[] keyArr = paramsSet.toArray();
        Arrays.sort(keyArr);
        //拼接url中get参数
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < keyArr.length; i++) {
            String key = keyArr[i].toString();
            //排序第一个参数不添加&符号,其他需要添加&
            if (i > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(params.get(key));
        }

        //未加入秘钥的参数字符串
        String paramsStr = sb.toString();

        return paramsStr;
    }
}
