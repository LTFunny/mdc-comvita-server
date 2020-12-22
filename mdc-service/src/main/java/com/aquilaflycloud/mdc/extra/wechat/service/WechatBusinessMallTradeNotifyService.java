package com.aquilaflycloud.mdc.extra.wechat.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilafly.easypay.req.QueryOtherOrderByTopReq;
import com.aquilafly.easypay.resp.QueryOtherOrderResp;
import com.aquilafly.easypay.util.EasyPayUtil;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.member.MemberScanTypeEnum;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.feign.consumer.org.ShopConsumer;
import com.aquilaflycloud.mdc.mapper.MemberScanRecordMapper;
import com.aquilaflycloud.mdc.mapper.WechatBusinessMallTradeMapper;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.member.MemberScanRecord;
import com.aquilaflycloud.mdc.model.wechat.WechatBusinessMallTradeInfo;
import com.aquilaflycloud.mdc.result.member.MemberScanRewardResult;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.MemberService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.org.service.provider.entity.PShopInfo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.v3.util.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum.EASYPAYQUERY;

/**
 * WechatBusinessMallTradeNotifyService
 *
 * @author Zengqingjie
 * @date 202-06-02
 */
@Service
@Slf4j
public class WechatBusinessMallTradeNotifyService {
    @Resource
    private WechatBusinessMallTradeMapper wechatBusinessMallTradeMapper;
    @Resource
    private MemberScanRecordMapper memberScanRecordMapper;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private MemberService memberService;
    @Resource
    private WechatPayService wechatPayService;
    @Resource
    private ShopConsumer shopConsumer;

    private Long getFormatId(String merchantNo) {
        //获取商户业态id
        Long formatId = null;
        if (StrUtil.isNotBlank(merchantNo)) {
            PShopInfo pShopInfo = shopConsumer.getByPaymentSystemNumber(merchantNo);
            if (pShopInfo != null) {
                formatId = pShopInfo.getFormatId();
            }
        }
        return formatId;
    }

    @Transactional
    public JSONObject saveNotifyInfo(String requestBody, String mallPath) {
        JSONObject result = decryptBodyData(requestBody, mallPath);
        String code = result.getStr("code");
        String message = null;

        try {
            //解析失败返回
            if ("FAIL".equals(code)) {
                return result;
            }
            //保存数据
            //判断是否查找到对应的微信支付订单号
            JSONObject decryptJson = result.getJSONObject("decryptJson");
            JSONObject bodyJson = result.getJSONObject("bodyJson");
            JSONObject resource = result.getJSONObject("resource");
            String transactionId = decryptJson.getStr("transaction_id");
            String eventType = new JSONObject(requestBody).getStr("event_type");

            List<WechatBusinessMallTradeInfo> infos = wechatBusinessMallTradeMapper.normalSelectList(Wrappers.<WechatBusinessMallTradeInfo>lambdaQuery()
                    .eq(WechatBusinessMallTradeInfo::getTransactionId, transactionId)
                    .eq(WechatBusinessMallTradeInfo::getEventType, eventType)
            );

            if (null != infos && infos.size() > 0) {
                log.info("信息重复：{transactionId=" + transactionId + ", eventType=" + eventType + "}");
                code = "SUCCESS";
                message = "操作成功";
            } else {
                WechatBusinessMallTradeInfo info = new WechatBusinessMallTradeInfo();
                //复制body未加密字段
                BeanUtil.copyProperties(bodyJson, info, "id", "createTime");
                //复制resource未加密字段
                BeanUtil.copyProperties(resource, info);
                info.setAlgorithmType(resource.getStr("algorithm"));
                //复制解密后数据
                BeanUtil.copyProperties(decryptJson, info);
                info.setId(IdWorker.getId());
                info.setNotifyId(bodyJson.getStr("id"));
                info.setNotifyCreateTime(bodyJson.getStr("create_time"));
                MemberInfo memberInfo = memberService.getUnifiedMember(Wrappers.<MemberInfo>lambdaQuery()
                                .eq(MemberInfo::getWxAppId, info.getAppid())
                                .eq(MemberInfo::getOpenId, info.getOpenid())
                        , true);
                if (memberInfo != null && StrUtil.isNotBlank(memberInfo.getPhoneNumber())) {
                    //设置此次请求租户id
                    ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, memberInfo.getTenantId());
                    ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, memberInfo.getSubTenantId());
                } else {
                    code = "FAIL";
                    message = memberInfo == null ? "会员不存在" : "会员未绑定手机号";
                    log.error(message);
                    result.set("code", code);
                    result.set("message", message);
                    return result;
                }

                //支付成功回调，获取奖励数据
                if ("MALL_TRANSACTION.SUCCESS".equals(info.getEventType())) {
                    Integer scanCount = memberScanRecordMapper.selectCount(Wrappers.<MemberScanRecord>lambdaQuery()
                            .eq(MemberScanRecord::getTopOrderNo, info.getTransactionId())
                    );
                    int count = 0;
                    if (scanCount <= 0) {
                        //获取积分
                        BigDecimal amount = NumberUtil.div(info.getAmount(), Integer.valueOf(100), 2, RoundingMode.DOWN);
                        MemberScanRecord record = new MemberScanRecord();
                        MdcUtil.setMemberInfo(record, memberInfo);
                        Long formatId = null;
                        if ("正佳广场".equals(info.getShopName())) {
                            QueryOtherOrderResp resp = EasyPayUtil.queryOtherOrderByTop(new QueryOtherOrderByTopReq().setP1_topOrderNo(info.getTransactionId()),
                                    MdcUtil.getCurrentTenantId().toString() + EASYPAYQUERY.getType());
                            if (StrUtil.equals(resp.getRa_Code(), "100")) {
                                scanCount = memberScanRecordMapper.selectCount(Wrappers.<MemberScanRecord>lambdaQuery()
                                        .eq(MemberScanRecord::getOrderNo, resp.getR01_OrderNo())
                                );
                                if (scanCount <= 0) {
                                    formatId = getFormatId(resp.getR00_MerchantNo());
                                    record.setMerchantNo(resp.getR00_MerchantNo());
                                    record.setOrderNo(resp.getR01_OrderNo());
                                    record.setTopOrderNo(resp.getR11_topOrderNo());
                                    record.setPayNo(resp.getR05_TrxNo());
                                    record.setCardNo(resp.getR07_CardNo());
                                    record.setProductName(resp.getR04_ProductName());
                                    record.setActualMoney(amount);
                                    record.setPayMoney(new BigDecimal(resp.getR03_Amount()));
                                    record.setPayTime(DateUtil.parse(resp.getR06_PaySuccessTime()));
                                    record.setPayType(EnumUtil.likeValueOf(PaymentTypeEnum.class, Convert.toInt(resp.getR02_PaymentType())));
                                }
                            } else {
                                log.error("查询惠云支付信息失败: " + resp.getRb_CodeMsg());
                                code = "FAIL";
                                message = "查询惠云支付信息失败";
                                result.set("code", code);
                                result.set("message", message);
                                return result;
                            }
                        } else {
                            record.setMerchantNo(info.getMchid());
                            record.setTopOrderNo(info.getTransactionId());
                            record.setPayNo(info.getTransactionId());
                            record.setProductName(info.getShopName());
                            record.setShopName(info.getShopName());
                            record.setActualMoney(amount);
                            record.setPayTime(DateUtil.parse(info.getTimeEnd()));
                            record.setPayType(PaymentTypeEnum.WECHAT);
                        }
                        if (StrUtil.isNotBlank(record.getMerchantNo())) {
                            Map<RewardTypeEnum, MemberScanRewardResult> rewardResult = memberRewardService
                                    .addScanRewardRecord(memberInfo, formatId, amount, true);
                            JSONObject rewardObject = JSONUtil.parseObj(rewardResult);
                            info.setRewardValueContent(rewardObject.toString());
                            List<MemberScanRewardResult> list = CollUtil.newArrayList(rewardResult.values());
                            record.setRewardValueContent(JSONUtil.toJsonStr(list));
                            record.setType(MemberScanTypeEnum.AUTO);
                            count = memberScanRecordMapper.insert(record);
                        }
                    }
                    if (count <= 0) {
                        log.info("信息重复：{transactionId=" + transactionId + ", eventType=" + eventType + "}");
                        code = "SUCCESS";
                        message = "操作成功";
                    } else {
                        code = "SUCCESS";
                        message = "保存数据成功";
                    }
                }

                int count = wechatBusinessMallTradeMapper.insert(info);
                if (count <= 0) {
                    log.error("保存数据失败: {transactionId=" + transactionId + ", eventType=" + eventType + "}");
                    code = "FAIL";
                    message = "保存数据失败";
                }

                //消费信息接收成功，延迟执行通知积分变动
                if ("MALL_TRANSACTION.SUCCESS".equals(info.getEventType()) && "SUCCESS".equals(code)) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            notifyWechatScoreChange(info);
                        }
                    }, 5000);// 这里毫秒
                }
            }
        } catch (Exception e) {
            log.error("回调失败,抛出异常信息: " + e.getMessage());
            code = "FAIL";
            message = "保存数据异常";
        }
        result.set("code", code);
        result.set("message", message);
        return result;
    }

    /**
     * 解析请求数据
     *
     * @param requestBody
     * @return
     */
    private JSONObject decryptBodyData(String requestBody, String mallPath) {
        JSONObject result = new JSONObject();
        String code = "SUCCESS";
        String message = "回调成功";
        JSONObject decryptJson = null;

        try {
            if (StrUtil.isBlank(requestBody)) {
                log.error("请求体为空");
                code = "FAIL";
                message = "请求体为空";
            }
            //获取apiV3Key
            WxPayConfig payConfig = wechatPayService.getPayConfigByMallPath(mallPath);
            String apiV3Key = payConfig.getApiV3Key();
            //验证签名字段
            JSONObject bodyJson = new JSONObject(requestBody);
            JSONObject resource = bodyJson.getJSONObject("resource");
            String nonce = resource.getStr("nonce");
            String associatedData = resource.getStr("associated_data");
            String ciphertext = resource.getStr("ciphertext");

            if (null == resource || StrUtil.isBlank(nonce) || StrUtil.isBlank(associatedData) || StrUtil.isBlank(ciphertext)) {
                log.error("关键参数存在空: {notifyId=" + bodyJson.getStr("id") + "}");
                code = "FAIL";
                message = "参数有误";
            }

            //解码ciphertext
            if ("SUCCESS".equals(code)) {
                result.set("bodyJson", bodyJson);
                result.set("resource", resource);
                byte[] key = apiV3Key.getBytes(StandardCharsets.UTF_8);
                AesUtils aesUtils = new AesUtils(key);
                String decryptString = aesUtils.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), ciphertext);

                if (null != decryptString && StrUtil.isNotBlank(decryptString)) {
                    decryptJson = new JSONObject(decryptString);
                    result.set("decryptJson", decryptJson);
                } else {
                    log.error("解码ciphertext失败: {notifyId=" + bodyJson.getStr("id") + "}");
                    code = "FAIL";
                    message = "解码ciphertext失败";
                }
            }

            //判断是否存在微信支付订单号
            if ("SUCCESS".equals(code)) {
                String transactionId = decryptJson.getStr("transaction_id");
                if (null == transactionId || StrUtil.isBlank(transactionId)) {
                    log.error("ciphertext中不存在微信支付订单号: {notifyId=" + bodyJson.getStr("id") + "}");
                    code = "FAIL";
                    message = "ciphertext中不存在微信支付订单号";
                }
            }
        } catch (Exception e) {
            code = "FAIL";
            message = "保存数据异常";
            log.error("回调失败,抛出异常信息: " + e.getMessage());
        }

        result.set("code", code);
        result.set("message", message);

        return result;
    }

    private void notifyWechatScoreChange(WechatBusinessMallTradeInfo info) {
        try {
            int increasedPoints = 0;
            //获取积分奖励值
            if (StrUtil.isNotBlank(info.getRewardValueContent())) {
                JSONObject pointsJson = new JSONObject(info.getRewardValueContent());
                JSONObject scoreJson = pointsJson.getJSONObject(RewardTypeEnum.SCORE.name());
                if (null != scoreJson && null != scoreJson.getInt("canReward")) {
                    increasedPoints = scoreJson.getInt("canReward");
                }
            }
            String pointsUpdateTime = DateUtil.format(new Date(), "yyy-MM-dd'T'HH:mm:ss.SSSXXX");
            JSONObject params = new JSONObject();
            params.set("sub_mchid", info.getMchid());
            params.set("transaction_id", info.getTransactionId());
            params.set("appid", info.getAppid());
            params.set("openid", info.getOpenid());
            params.set("earn_points", true);
            params.set("increased_points", increasedPoints);
            params.set("points_update_time", pointsUpdateTime);
            String url = "https://api.mch.weixin.qq.com/v3/businesscircle/points/notify";
            WxPayService wxPayService = wechatPayService.getWxPayServiceByAppId(info.getAppid());
            String res = wxPayService.postV3WithWechatpaySerial(url, params.toString());
            //返回{}为成功
            if (StrUtil.isNotBlank(res) && "{}".equals(res)) {
                log.info("商圈积分通知成功: {notifyId=" + info.getNotifyId() + ", transactionId=" + info.getTransactionId() + "}");
            } else {
                log.error("商圈积分通知失败: {res=" + res + "}");
                log.error("商圈积分通知失败: {notifyId=" + info.getNotifyId() + ", transactionId=" + info.getTransactionId() + "}");
            }
        } catch (WxPayException e) {
            log.error("商圈积分通知失败: {notifyId=" + info.getNotifyId() + ", transactionId=" + info.getTransactionId() + "}");
            log.error("商圈积分通知失败,抛出异常信息: " + e.getMessage());
        }
    }
}
