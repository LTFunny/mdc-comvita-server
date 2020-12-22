package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.aquilafly.easypay.enums.FrpCodeEnum;
import com.aquilafly.easypay.notify.RefundNotify;
import com.aquilafly.easypay.notify.UnionPayNotify;
import com.aquilafly.easypay.req.QueryOrderReq;
import com.aquilafly.easypay.req.RefundReq;
import com.aquilafly.easypay.req.UnionPayReq;
import com.aquilafly.easypay.resp.QueryOrderResp;
import com.aquilafly.easypay.resp.RefundResp;
import com.aquilafly.easypay.resp.UnionPayResp;
import com.aquilafly.easypay.util.EasyPayUtil;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.easypay.EasyPayAccountTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.OrderTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.PayTypeEnum;
import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.extra.alipay.notify.AlipayNotify;
import com.aquilaflycloud.mdc.extra.alipay.request.TradeCreateRequest;
import com.aquilaflycloud.mdc.extra.alipay.request.TradeQueryRequest;
import com.aquilaflycloud.mdc.extra.alipay.request.TradeRefundRequest;
import com.aquilaflycloud.mdc.extra.alipay.service.AlipayOpenPlatformService;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatPayService;
import com.aquilaflycloud.mdc.mapper.EasypayPaymentRecordMapper;
import com.aquilaflycloud.mdc.mapper.EasypayRefundRecordMapper;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.param.easypay.OrderParam;
import com.aquilaflycloud.mdc.param.easypay.RefundParam;
import com.aquilaflycloud.mdc.param.ticket.OrderInfoWechatPayParam;
import com.aquilaflycloud.mdc.param.ticket.OrderInfoWechatUpdateParam;
import com.aquilaflycloud.mdc.result.system.AlipayDirectPayConfig;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.aquilaflycloud.mdc.result.system.WechatDirectPayConfig;
import com.aquilaflycloud.mdc.service.*;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.bean.ServiceContext;
import com.gitee.sop.servercommon.exception.ServiceException;
import com.gitee.sop.servercommon.param.ParamValidator;
import com.gitee.sop.servercommon.param.ServiceParamValidator;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

import static com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum.*;

/**
 * EasyPayServiceImpl
 *
 * @author star
 * @date 2019-12-07
 */
@Slf4j
@Service
public class EasyPayServiceImpl implements EasyPayService {

    private final ParamValidator paramValidator = new ServiceParamValidator();
    @Resource
    private EasypayPaymentRecordMapper easypayPaymentRecordMapper;
    @Resource
    private EasypayRefundRecordMapper easypayRefundRecordMapper;
    @Resource
    private SystemTenantConfigService systemTenantConfigService;
    @Resource
    private AlipayOpenPlatformService alipayOpenPlatformService;
    @Resource
    private WechatPayService wechatPayService;
    @Resource
    private TicketOrderInfoService ticketOrderInfoService;
    @Resource
    private ParkingOrderService parkingOrderService;
    @Resource
    private ExchangeService exchangeService;

    private String getEasyPayConfigId(EasyPayAccountTypeEnum easyPayAccountType) {
        String configId = MdcUtil.getCurrentTenantId().toString();
        switch (easyPayAccountType) {
            case QUERY:{
                configId += EASYPAYQUERY.getType();
                break;
            }
            case TICKET:{
                configId += EASYPAYTICKET.getType();
                break;
            }
            case PARKING:{
                configId += EASYPAYPARKING.getType();
                break;
            }
            case EXCHANGE:{
                configId += EASYPAYEXCHANGE.getType();
                break;
            }
            default:
        }
        return configId;
    }

    private String order(OrderParam param, EasyPayAccountTypeEnum easyPayAccountType, OrderTypeEnum orderType) {
        paramValidator.validateBizParam(param);
        FrpCodeEnum frpCode = param.getFrpCode();
        SystemTenantConfigResult result = systemTenantConfigService.getConfig(TenantConfigTypeEnum.ALIPAYDIRECTPAY);
        if (result != null) {
            AlipayDirectPayConfig config = result.getAlipayDirectPayConfig();
            if (BooleanUtil.isTrue(config.getEffective()) && frpCode == FrpCodeEnum.ALIPAY_NATIVE) {
                String sysServiceProviderId = config.getSysServiceProviderId();
                String timeoutExpress = param.getTimeExpire() == null ? null : param.getTimeExpire() + "m";
                TradeCreateRequest request = new TradeCreateRequest();
                request.setAppId(param.getAppId());
                request.setOutTradeNo(param.getOrderNo());
                request.setTotalAmount(param.getAmount().toString());
                request.setBuyerId(param.getUserId());
                request.setSubject(param.getProductName());
                request.setBody(param.getProductDesc());
                request.setTimeoutExpress(timeoutExpress);
                request.setSysServiceProviderId(sysServiceProviderId);
                AlipayTradeCreateResponse response = alipayOpenPlatformService.createAlipayTrade(request);
                EasypayPaymentRecord record = new EasypayPaymentRecord();
                record.setPayType(PayTypeEnum.ALIPAY);
                record.setOrderType(orderType);
                record.setOrderId(param.getId());
                record.setAppId(param.getAppId());
                record.setUserId(param.getUserId());
                record.setOrderNo(param.getOrderNo());
                record.setAmount(param.getAmount());
                record.setFrpCode(frpCode.getType());
                String code, msg;
                if (StrUtil.isBlank(response.getSubCode())) {
                    code = response.getCode();
                    msg = response.getMsg();
                } else {
                    code = response.getSubCode();
                    msg = response.getSubMsg();
                }
                record.setCode(code);
                record.setCodeMsg(msg);
                record.setTrxNo(response.getTradeNo());
                record.setPayParamContent(JSONUtil.toJsonStr(request));
                record.setPayResultContent(JSONUtil.toJsonStr(response));
                record.setResult(response.getTradeNo());
                int count = easypayPaymentRecordMapper.insert(record);
                if (count > 0 && StrUtil.isBlank(response.getSubCode())) {
                    return response.getTradeNo();
                }
                throw new ServiceException("发起支付宝支付失败");
            }
        }
        result = systemTenantConfigService.getConfig(TenantConfigTypeEnum.WECHATDIRECTPAY);
        if (result != null) {
            WechatDirectPayConfig config = result.getWechatDirectPayConfig();
            if (BooleanUtil.isTrue(config.getEffective()) && frpCode == FrpCodeEnum.APPLET_PAY) {
                WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
                request.setTradeType(WxPayConstants.TradeType.JSAPI);
                request.setSignType(WxPayConstants.SignType.MD5);
                request.setBody(param.getProductName());
                request.setDetail(param.getProductDesc());
                request.setOpenid(param.getOpenId());
                request.setOutTradeNo(param.getOrderNo());
                request.setTotalFee(param.getAmount().multiply(new BigDecimal("100")).intValue());
                request.setTimeExpire(DateTime.now().offset(DateField.MINUTE, param.getTimeExpire()).toString(DatePattern.PURE_DATETIME_FORMAT));
                request.setSpbillCreateIp(NetUtil.getLocalhostStr());
                String notifyUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                        .append("/rest/").append(MdcUtil.getServerName()).append("/wechatPayNotify").toString();
                request.setNotifyUrl(notifyUrl);
                WxPayUnifiedOrderResult orderResult;
                try {
                    orderResult = wechatPayService.getWxPayServiceByAppId(param.getAppId()).unifiedOrder(request);
                } catch (WxPayException e) {
                    log.error("发起微信直联支付失败", e);
                    throw new ServiceException(e.getMessage());
                }
                EasypayPaymentRecord record = new EasypayPaymentRecord();
                record.setPayType(PayTypeEnum.WECHATPAY);
                record.setOrderType(orderType);
                record.setOrderId(param.getId());
                record.setAppId(param.getAppId());
                record.setOpenId(param.getOpenId());
                record.setOrderNo(param.getOrderNo());
                record.setAmount(param.getAmount());
                record.setFrpCode(frpCode.getType());
                String code, msg;
                if (StrUtil.equalsAny("SUCCESS", orderResult.getReturnCode(), orderResult.getResultCode())) {
                    code = orderResult.getReturnCode();
                    msg = orderResult.getResultCode();
                } else if (StrUtil.equals("FAIL", orderResult.getResultCode())) {
                    code = orderResult.getErrCode();
                    msg = orderResult.getErrCodeDes();
                } else {
                    code = orderResult.getReturnCode();
                    msg = orderResult.getReturnMsg();
                }
                record.setCode(code);
                record.setCodeMsg(msg);
                record.setTrxNo(orderResult.getPrepayId());
                record.setPayParamContent(JSONUtil.toJsonStr(request));
                record.setPayResultContent(JSONUtil.toJsonStr(orderResult));
                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                WxPayMpOrderResult payResult = WxPayMpOrderResult.builder()
                        .appId(orderResult.getAppid())
                        .timeStamp(timestamp)
                        .nonceStr(orderResult.getNonceStr())
                        .packageValue("prepay_id=" + orderResult.getPrepayId())
                        .signType(request.getSignType())
                        .build();
                String mchKey = wechatPayService.getWxPayServiceByAppId(param.getAppId()).getConfig().getMchKey();
                payResult.setPaySign(SignUtils.createSign(payResult, request.getSignType(), mchKey, null));
                JSONObject payResultJson = JSONUtil.parseObj(payResult);
                payResultJson.remove("packageValue");
                payResultJson.set("package", payResult.getPackageValue());
                record.setResult(payResultJson.toString());
                int count = easypayPaymentRecordMapper.insert(record);
                if (count > 0 && StrUtil.isNotBlank(orderResult.getPrepayId())) {
                    return payResultJson.toString();
                }
                throw new ServiceException("发起微信支付失败");
            }
        }
        UnionPayReq req = new UnionPayReq();
        req.setP2_OrderNo(param.getOrderNo()).setP3_Amount(param.getAmount()).setQ1_FrpCode(frpCode.getType())
                .setP5_ProductName(param.getProductName()).setP6_ProductDesc(param.getProductDesc())
                .setP7_Mp(param.getMp()).setQ7_AppId(param.getAppId()).setQ5_OpenId(param.getOpenId())
                .setQ6_AuthCode(param.getAuthCode()).setQ9_TimeExpire(Convert.toStr(param.getTimeExpire()));
        /*if (frpCode == FrpCodeEnum.ALIPAY_NATIVE || frpCode == FrpCodeEnum.WEIXIN_NATIVE || frpCode == FrpCodeEnum.UNIONPAY_NATIVE
                || frpCode == FrpCodeEnum.QQ_NATIVE || frpCode == FrpCodeEnum.JD_NATIVE || frpCode == FrpCodeEnum.BAIDU_NATIVE) {
            req.setQ4_ShowPic("1");
        }*/
        if (frpCode != FrpCodeEnum.ALIPAY_APP && frpCode != FrpCodeEnum.WEIXIN_APP && frpCode != FrpCodeEnum.APPLET_PAY
                && frpCode != FrpCodeEnum.JD_APP && frpCode != FrpCodeEnum.QQ_APP && frpCode != FrpCodeEnum.UNIONPAY_APP) {
            req.setQ7_AppId(null);
        }
        UnionPayResp resp = EasyPayUtil.unionPay(req, getEasyPayConfigId(easyPayAccountType));
        EasypayPaymentRecord record = new EasypayPaymentRecord();
        record.setPayType(PayTypeEnum.EASYPAY);
        record.setOrderType(orderType);
        record.setOrderId(param.getId());
        record.setAccountType(easyPayAccountType);
        record.setMerchantNo(req.getP1_MerchantNo());
        record.setOrderNo(req.getP2_OrderNo());
        record.setAmount(req.getP3_Amount());
        record.setFrpCode(req.getQ1_FrpCode());
        record.setTrxNo(resp.getR7_TrxNo());
        record.setAppId(param.getAppId());
        record.setOpenId(param.getOpenId());
        record.setUserId(param.getUserId());
        record.setCode(resp.getRa_Code());
        record.setCodeMsg(resp.getRb_CodeMsg());
        record.setResult(resp.getRc_Result());
        //record.setPic(resp.getRd_Pic());
        record.setPayParamContent(JSONUtil.toJsonStr(req));
        record.setPayResultContent(JSONUtil.toJsonStr(resp));
        int count = easypayPaymentRecordMapper.insert(record);
        if (count > 0 && "100".equals(resp.getRa_Code())) {
            return resp.getRc_Result();
        }
        throw new ServiceException("发起支付失败");
    }

    private void refund(RefundParam param, EasyPayAccountTypeEnum easyPayAccountType, OrderTypeEnum orderType) {
        paramValidator.validateBizParam(param);
        if (param.getPayType() == PayTypeEnum.ALIPAY) {
            if (StrUtil.isBlank(param.getAppId())) {
                throw new ServiceException("appId不能为空");
            }
            TradeRefundRequest request = new TradeRefundRequest();
            request.setAppId(param.getAppId());
            request.setOutTradeNo(param.getOrderNo());
            request.setOutRequestNo(param.getRefundOrderNo());
            request.setRefundAmount(param.getRefundAmount().toString());
            request.setRefundReason(param.getRefundReason());
            AlipayTradeRefundResponse response = alipayOpenPlatformService.refundAlipayTrade(request);
            EasypayRefundRecord record = new EasypayRefundRecord();
            record.setOrderType(orderType);
            record.setOrderId(param.getId());
            record.setAccountType(easyPayAccountType);
            record.setOrderNo(param.getOrderNo());
            record.setRefundOrderNo(param.getRefundOrderNo());
            record.setRefundAmount(param.getRefundAmount());
            record.setRefundReason(param.getRefundReason());
            String code, msg;
            if (StrUtil.isBlank(response.getSubCode())) {
                code = response.getCode();
                msg = response.getMsg();
            } else {
                code = response.getSubCode();
                msg = response.getSubMsg();
            }
            record.setCode(code);
            record.setCodeMsg(msg);
            record.setRefundParamContent(JSONUtil.toJsonStr(request));
            record.setRefundResultContent(JSONUtil.toJsonStr(response));
            //支付宝直联退款没有退款回调,直接判断是否退款成功
            if (StrUtil.equals(response.getFundChange(), "Y")) {
                record.setRefundState(WhetherEnum.YES);
            } else {
                record.setRefundState(WhetherEnum.NO);
            }
            int count = easypayRefundRecordMapper.insert(record);
            if (count > 0) {
                MdcUtil.getTtlExecutorService().submit(() -> finishRefundOrderHandler(record.getRefundState() == WhetherEnum.YES, record));
                return;
            }
        } else if (param.getPayType() == PayTypeEnum.WECHATPAY) {
            if (StrUtil.isBlank(param.getAppId())) {
                throw new ServiceException("appId不能为空");
            }
            WxPayRefundRequest request = new WxPayRefundRequest();
            request.setOutTradeNo(param.getOrderNo());
            request.setOutRefundNo(param.getRefundOrderNo());
            request.setRefundFee(param.getRefundAmount().multiply(new BigDecimal("100")).intValue());
            request.setTotalFee(param.getRefundAmount().multiply(new BigDecimal("100")).intValue());
            request.setRefundDesc(param.getRefundReason());
            String notifyUrl = StrBuilder.create().append(MdcUtil.getConfigValue(ConfigTypeEnum.OPEN_API_DOMAIN_NAME))
                    .append("/rest/").append(MdcUtil.getServerName()).append("/wechatRefundNotify").toString();
            request.setNotifyUrl(notifyUrl);
            WxPayRefundResult refundResult;
            try {
                refundResult = wechatPayService.getWxPayServiceByAppId(param.getAppId()).refund(request);
            } catch (WxPayException e) {
                log.error("发起微信直联退款失败", e);
                throw new ServiceException(e.getMessage());
            }
            EasypayRefundRecord record = new EasypayRefundRecord();
            record.setOrderType(orderType);
            record.setOrderId(param.getId());
            record.setAccountType(easyPayAccountType);
            record.setOrderNo(param.getOrderNo());
            record.setRefundOrderNo(param.getRefundOrderNo());
            record.setRefundAmount(param.getRefundAmount());
            record.setRefundReason(param.getRefundReason());
            String code, msg;
            if (StrUtil.equalsAny("SUCCESS", refundResult.getReturnCode(), refundResult.getResultCode())) {
                code = refundResult.getReturnCode();
                msg = refundResult.getResultCode();
            } else if (StrUtil.equals("FAIL", refundResult.getResultCode())) {
                code = refundResult.getErrCode();
                msg = refundResult.getErrCodeDes();
            } else {
                code = refundResult.getReturnCode();
                msg = refundResult.getReturnMsg();
            }
            record.setCode(code);
            record.setCodeMsg(msg);
            record.setRefundParamContent(JSONUtil.toJsonStr(request));
            record.setRefundResultContent(JSONUtil.toJsonStr(refundResult));
            int count = easypayRefundRecordMapper.insert(record);
            if (count > 0) {
                return;
            }
        } else {
            RefundReq req = new RefundReq();
            req.setP2_OrderNo(param.getOrderNo()).setP3_RefundOrderNo(param.getRefundOrderNo())
                    .setP4_RefundAmount(param.getRefundAmount()).setP5_RefundReason(param.getRefundReason());
            RefundResp resp = EasyPayUtil.refund(req, getEasyPayConfigId(easyPayAccountType));
            EasypayRefundRecord record = new EasypayRefundRecord();
            record.setOrderType(orderType);
            record.setOrderId(param.getId());
            record.setAccountType(easyPayAccountType);
            record.setMerchantNo(req.getP1_MerchantNo());
            record.setOrderNo(req.getP2_OrderNo());
            record.setRefundOrderNo(req.getP3_RefundOrderNo());
            record.setRefundAmount(req.getP4_RefundAmount());
            record.setRefundReason(req.getP5_RefundReason());
            record.setStatus(resp.getRa_Status());
            record.setCode(resp.getRb_Code());
            record.setCodeMsg(resp.getRc_CodeMsg());
            record.setRefundParamContent(JSONUtil.toJsonStr(req));
            record.setRefundResultContent(JSONUtil.toJsonStr(resp));
            int count = easypayRefundRecordMapper.insert(record);
            if (count > 0 && "100".equals(resp.getRb_Code())) {
                return;
            }
        }
        throw new ServiceException("发起退款申请失败");
    }

    @Override
    public Boolean checkOrder(EasypayPaymentRecord record) {
        boolean isPaid;
        String status;
        Object query;
        if (record.getPayType() == PayTypeEnum.ALIPAY) {
            TradeQueryRequest request = new TradeQueryRequest();
            request.setAppId(record.getAppId());
            request.setOutTradeNo(record.getOrderNo());
            AlipayTradeQueryResponse response = alipayOpenPlatformService.queryAlipayTrade(request);
            status = response.getTradeStatus();
            isPaid = StrUtil.equalsAny(status, "TRADE_SUCCESS", "TRADE_FINISHED");
            query = response;
        } else if (record.getPayType() == PayTypeEnum.WECHATPAY) {
            try {
                WxPayOrderQueryResult result = wechatPayService.getWxPayServiceByAppId(record.getAppId()).queryOrder(null, record.getOrderNo());
                status = result.getTradeState();
                isPaid = StrUtil.equalsAny("SUCCESS", result.getReturnCode(), result.getResultCode(), result.getTradeState());
                query = result;
            } catch (WxPayException e) {
                throw new ServiceException(e.getResultCode());
            }
        } else {
            QueryOrderResp resp = EasyPayUtil.queryOrder(new QueryOrderReq().setP1_MerchantNo(record.getMerchantNo())
                    .setP2_OrderNo(record.getOrderNo()), getEasyPayConfigId(record.getAccountType()));
            status = resp.getRa_Status();
            isPaid = StrUtil.equals(status, "100");
            query = resp;
        }
        if (isPaid && record.getPayState() != WhetherEnum.YES) {
            EasypayPaymentRecord update = new EasypayPaymentRecord();
            update.setId(record.getId());
            update.setStatus(status);
            DateTime now = DateTime.now();
            update.setPayTime(now);
            update.setDealTime(now);
            if (isPaid) {
                update.setPayState(WhetherEnum.YES);
            } else {
                update.setPayState(WhetherEnum.NO);
            }
            update.setPayQueryContent(JSONUtil.toJsonStr(query));
            int count = easypayPaymentRecordMapper.updateById(update);
            if (count <= 0) {
                throw new ServiceException("更新支付信息失败");
            }
        }
        return isPaid;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String orderTicket(OrderParam param) {
        return order(param, EasyPayAccountTypeEnum.TICKET, OrderTypeEnum.TICKET);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String orderParking(OrderParam param) {
        return order(param, EasyPayAccountTypeEnum.PARKING, OrderTypeEnum.PARKING);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public String orderExchange(OrderParam param) {
        return order(param, EasyPayAccountTypeEnum.EXCHANGE, OrderTypeEnum.EXCHANGE);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void refundTicketOrder(RefundParam param) {
        refund(param, EasyPayAccountTypeEnum.TICKET, OrderTypeEnum.TICKET);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void refundParkingOrder(RefundParam param) {
        refund(param, EasyPayAccountTypeEnum.PARKING, OrderTypeEnum.PARKING);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void refundExchangeOrder(RefundParam param) {
        refund(param, EasyPayAccountTypeEnum.EXCHANGE, OrderTypeEnum.EXCHANGE);
    }

    @Override
    public void finishOrder(Object notify) {
        String orderNo = null, status = null;
        DateTime payTime = null, dealTime = null;
        PaymentTypeEnum paymentType = null;
        PayTypeEnum payType = null;
        if (notify instanceof UnionPayNotify) {
            orderNo = ((UnionPayNotify) notify).getR2_OrderNo();
            status = ((UnionPayNotify) notify).getR6_Status();
            payTime = DateUtil.parse(((UnionPayNotify) notify).getRa_PayTime());
            dealTime = DateUtil.parse(((UnionPayNotify) notify).getRb_DealTime());
            paymentType = EnumUtil.likeValueOf(PaymentTypeEnum.class, Convert.toInt(((UnionPayNotify) notify).getR10_PaymentType()));
            payType = PayTypeEnum.EASYPAY;
        } else if (notify instanceof AlipayNotify) {
            orderNo = ((AlipayNotify) notify).getOutTradeNo();
            status = ((AlipayNotify) notify).getTradeStatus();
            payTime = DateUtil.parse(((AlipayNotify) notify).getGmtPayment());
            dealTime = DateUtil.parse(((AlipayNotify) notify).getNotifyTime());
            paymentType = PaymentTypeEnum.ALIPAY;
            payType = PayTypeEnum.ALIPAY;
        } else if (notify instanceof WxPayOrderNotifyResult) {
            orderNo = ((WxPayOrderNotifyResult) notify).getOutTradeNo();
            status = ((WxPayOrderNotifyResult) notify).getResultCode();
            payTime = dealTime = DateUtil.parse(((WxPayOrderNotifyResult) notify).getTimeEnd());
            paymentType = PaymentTypeEnum.WECHAT;
            payType = PayTypeEnum.WECHATPAY;
        }
        EasypayPaymentRecord record = easypayPaymentRecordMapper.normalSelectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderNo, orderNo)
                .eq(EasypayPaymentRecord::getPayType, payType)
        );
        if (record == null) {
            log.error("支付记录不存在");
            return;
        }
        if (record.getPayState() == WhetherEnum.YES) {
            log.error("支付已完成");
            return;
        }
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, record.getTenantId());
        ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, record.getSubTenantId());
        boolean verify = false;
        if (notify instanceof UnionPayNotify) {
            String hmac = ((UnionPayNotify) notify).getHmac();
            ((UnionPayNotify) notify).setHmac(null);
            verify = EasyPayUtil.verify(notify, hmac, getEasyPayConfigId(record.getAccountType()));
        } else if (notify instanceof AlipayNotify) {
            //验签
            Map<String, Object> contentMap = BeanUtil.beanToMap(notify, true, true);
            Map<String, String> params = Convert.toMap(String.class, String.class, contentMap);
            verify = alipayOpenPlatformService.rsaCheck(record.getAppId(), params);
        } else if (notify instanceof WxPayOrderNotifyResult) {
            verify = true;
        }
        if (verify) {
            Boolean isSuccess = StrUtil.equalsAny(status, "100", "TRADE_SUCCESS", "TRADE_FINISHED", "SUCCESS");
            EasypayPaymentRecord update = new EasypayPaymentRecord();
            update.setId(record.getId());
            update.setStatus(status);
            update.setPayTime(payTime);
            update.setDealTime(dealTime);
            if (isSuccess) {
                update.setPayState(WhetherEnum.YES);
            } else {
                update.setPayState(WhetherEnum.NO);
            }
            update.setPayNotifyContent(JSONUtil.toJsonStr(notify));
            int count = easypayPaymentRecordMapper.updateById(update);
            if (count > 0) {
                finishOrderHandler(isSuccess, paymentType, record);
            }
        }
    }

    /**
     * 处理业务订单状态
     *
     * @param isSuccess   是否支付成功
     * @param paymentType 支付类型
     * @param record      支付记录
     */
    private void finishOrderHandler(Boolean isSuccess, PaymentTypeEnum paymentType, EasypayPaymentRecord record) {
        switch (record.getOrderType()) {
            case TICKET: {
                try {
                    OrderInfoWechatPayParam orderInfoWechatPayParam = new OrderInfoWechatPayParam();
                    orderInfoWechatPayParam.setId(record.getOrderId());
                    orderInfoWechatPayParam.setPaymentType(paymentType);
                    orderInfoWechatPayParam.setIsSuccess(isSuccess);
                    ticketOrderInfoService.wechatPayOrder(orderInfoWechatPayParam);
                } catch (Exception e) {
                    //抛出异常，需要调用退款
                    //生成退款订单号
                    String refundOrderNo = IdWorker.getIdStr();
                    RefundParam param = new RefundParam();
                    param.setPayType(record.getPayType());
                    param.setId(record.getOrderId());
                    param.setOrderNo(record.getOrderNo());
                    param.setRefundAmount(record.getAmount());
                    param.setRefundOrderNo(refundOrderNo);
                    param.setAppId(record.getAppId());
                    this.refundTicketOrder(param);
                    log.error(e.getMessage());
                }
                break;
            }
            case PARKING: {
                parkingOrderService.finishOrder(isSuccess, paymentType, record);
                break;
            }
            case EXCHANGE: {
                exchangeService.finishOrder(isSuccess, paymentType, record);
                break;
            }
            default:
        }
    }

    @Override
    public void finishRefundOrder(Object notify) {
        String refundOrderNo = null, status = null;
        if (notify instanceof RefundNotify) {
            refundOrderNo = ((RefundNotify) notify).getR3_RefundOrderNo();
            status = ((RefundNotify) notify).getRa_Status();
        } else if (notify instanceof WxPayRefundNotifyResult) {
            refundOrderNo = ((WxPayRefundNotifyResult) notify).getReqInfo().getOutRefundNo();
            status = ((WxPayRefundNotifyResult) notify).getReqInfo().getRefundStatus();
        }
        EasypayRefundRecord record = easypayRefundRecordMapper.normalSelectOne(Wrappers.<EasypayRefundRecord>lambdaQuery()
                .eq(EasypayRefundRecord::getRefundOrderNo, refundOrderNo));
        if (record == null) {
            log.error("退款记录不存在");
            return;
        }
        if (record.getRefundState() == WhetherEnum.YES) {
            log.error("退款已完成");
            return;
        }
        //设置此次请求租户id
        ServiceContext.getCurrentContext().set(DataAuthConstant.TENANT_ID, record.getTenantId());
        ServiceContext.getCurrentContext().set(DataAuthConstant.SUB_TENANT_ID, record.getSubTenantId());
        boolean verify = false;
        if (notify instanceof RefundNotify) {
            String hmac = ((RefundNotify) notify).getHmac();
            ((RefundNotify) notify).setHmac(null);
            verify = EasyPayUtil.verify(notify, hmac, getEasyPayConfigId(record.getAccountType()));
        } else if (notify instanceof WxPayRefundNotifyResult) {
            verify = true;
        }
        if (verify) {
            Boolean isSuccess = StrUtil.equalsAny(status, "100", "SUCCESS");
            EasypayRefundRecord update = new EasypayRefundRecord();
            update.setId(record.getId());
            update.setNotifyStatus(status);
            if (isSuccess) {
                update.setRefundState(WhetherEnum.YES);
            } else {
                update.setRefundState(WhetherEnum.NO);
            }
            update.setRefundNotifyContent(JSONUtil.toJsonStr(notify));
            int count = easypayRefundRecordMapper.updateById(update);
            if (count > 0) {
                finishRefundOrderHandler(isSuccess, record);
            }
        }
    }

    /**
     * 处理业务订单状态
     *
     * @param isSuccess 是否退款成功
     * @param record    支付记录
     */
    private void finishRefundOrderHandler(Boolean isSuccess, EasypayRefundRecord record) {
        switch (record.getOrderType()) {
            case TICKET: {
                OrderInfoWechatUpdateParam param = new OrderInfoWechatUpdateParam();
                param.setIsSuccess(isSuccess);
                param.setId(record.getOrderId());
                ticketOrderInfoService.wechatRefundUpdateOrderInfo(param);
                break;
            }
            case PARKING: {
                parkingOrderService.finishRefund(isSuccess, record);
                break;
            }
            case EXCHANGE: {
                exchangeService.finishRefund(isSuccess, record);
                break;
            }
            default:
        }
    }
}
