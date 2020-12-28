package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.dataAuth.constant.DataAuthConstant;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.easypay.*;
import com.aquilaflycloud.mdc.enums.system.ConfigTypeEnum;
import com.aquilaflycloud.mdc.enums.system.TenantConfigTypeEnum;
import com.aquilaflycloud.mdc.extra.wechat.service.WechatPayService;
import com.aquilaflycloud.mdc.mapper.EasypayPaymentRecordMapper;
import com.aquilaflycloud.mdc.mapper.EasypayRefundRecordMapper;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.param.easypay.OrderParam;
import com.aquilaflycloud.mdc.param.easypay.RefundParam;
import com.aquilaflycloud.mdc.result.system.SystemTenantConfigResult;
import com.aquilaflycloud.mdc.result.system.WechatDirectPayConfig;
import com.aquilaflycloud.mdc.service.EasyPayService;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.aquilaflycloud.mdc.service.SystemTenantConfigService;
import com.aquilaflycloud.mdc.util.MdcUtil;
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

import static com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum.EASYPAYEXCHANGE;
import static com.aquilaflycloud.mdc.enums.system.AccountSubTypeEnum.EASYPAYQUERY;

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
    private WechatPayService wechatPayService;
    @Resource
    private ExchangeService exchangeService;

    private String getEasyPayConfigId(EasyPayAccountTypeEnum easyPayAccountType) {
        String configId = MdcUtil.getCurrentTenantId().toString();
        switch (easyPayAccountType) {
            case QUERY:{
                configId += EASYPAYQUERY.getType();
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
        SystemTenantConfigResult result = systemTenantConfigService.getConfig(TenantConfigTypeEnum.WECHATDIRECTPAY);
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
        throw new ServiceException("发起支付失败");
    }

    private void refund(RefundParam param, EasyPayAccountTypeEnum easyPayAccountType, OrderTypeEnum orderType) {
        paramValidator.validateBizParam(param);
        if (param.getPayType() == PayTypeEnum.WECHATPAY) {
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
        }
        throw new ServiceException("发起退款申请失败");
    }

    @Override
    public Boolean checkOrder(EasypayPaymentRecord record) {
        boolean isPaid = false;
        String status = null;
        Object query = null;
        if (record.getPayType() == PayTypeEnum.WECHATPAY) {
            try {
                WxPayOrderQueryResult result = wechatPayService.getWxPayServiceByAppId(record.getAppId()).queryOrder(null, record.getOrderNo());
                status = result.getTradeState();
                isPaid = StrUtil.equalsAny("SUCCESS", result.getReturnCode(), result.getResultCode(), result.getTradeState());
                query = result;
            } catch (WxPayException e) {
                throw new ServiceException(e.getResultCode());
            }
        }
        if (isPaid && record.getPayState() != WhetherEnum.YES) {
            EasypayPaymentRecord update = new EasypayPaymentRecord();
            update.setId(record.getId());
            update.setStatus(status);
            DateTime now = DateTime.now();
            update.setPayTime(now);
            update.setDealTime(now);
            update.setPayState(WhetherEnum.YES);
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
    public String orderExchange(OrderParam param) {
        return order(param, EasyPayAccountTypeEnum.EXCHANGE, OrderTypeEnum.EXCHANGE);
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
        if (notify instanceof WxPayOrderNotifyResult) {
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
        if (notify instanceof WxPayOrderNotifyResult) {
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
        if (notify instanceof WxPayRefundNotifyResult) {
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
        if (notify instanceof WxPayRefundNotifyResult) {
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
            case EXCHANGE: {
                exchangeService.finishRefund(isSuccess, record);
                break;
            }
            default:
        }
    }
}
