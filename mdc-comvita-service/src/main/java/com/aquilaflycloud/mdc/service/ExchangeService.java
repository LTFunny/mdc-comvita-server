package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.enums.easypay.PaymentTypeEnum;
import com.aquilaflycloud.mdc.enums.exchange.GoodsTypeEnum;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoodsSkuInfo;
import com.aquilaflycloud.mdc.model.exchange.ExchangeOrder;
import com.aquilaflycloud.mdc.param.exchange.*;
import com.aquilaflycloud.mdc.result.exchange.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface ExchangeService {
    IPage<GoodsResult> pageGoods(GoodsPageParam param);

    void addGoods(GoodsAddParam param);

    void editGoods(GoodsEditParam param);

    void toggleGoods(GoodsGetParam param);

    void auditGoods(GoodsAuditParam param, GoodsTypeEnum goodsType, Long relId);

    void auditGoods(GoodsAuditParam param);

    GoodsResult getGoods(GoodsGetParam param);

    GoodsRankResult getGoodsRank(GoodsRankGetParam param);

    OrderStatisticsResult getOrderStatistics(OrderStatisticsGetParam param);

    List<OrderAnalysisResult> getOrderAnalysis(OrderAnalysisGetParam param);

    IPage<ExchangeOrder> pageOrder(OrderPageParam param);

    OrderResult getOrder(OrderGetParam param);

    void editOrderReceive(OrderReceiveEditParam param);

    void editOrder(OrderEditParam param);

    void finishExchangeOrder(OrderGetParam param);

    void refundOrder(OrderGetParam param);

    void confirmOrder(OrderGetParam param);

    void deliverOrder(OrderDeliverParam param);

    void auditOrderRefund(OrderRefundAuditParam param);

    void enforceRefundOrder(OrderEnforceRefundParam param);

    IPage<RecommendGoodsResult> pageRecommend(RecommendGoodsPageParam param);

    void addRecommend(RecommendGoodsAddParam param);

    void editRecommend(RecommendGoodsEditParam param);

    void deleteRecommend(RecommendGoodsDeleteParam param);

    IPage<ExchangeGoodsResult> pageExchangeGoods(ExchangeGoodsPageParam param);

    IPage<ExchangeGoodsResult> pageRecommendGoods(RecommendPageParam param);

    ExchangeGoodsResult getExchangeGoods(GoodsGetParam param);

    ExchangeOrderPayResult addOrder(OrderAddParam param);

    ExchangeOrder addExchangeOrder(OrderAddParam param);

    ExchangeOrderPayResult payOrder(OrderPayParam param);

    void finishOrder(Boolean isSuccess, PaymentTypeEnum paymentType, EasypayPaymentRecord record);

    void finishOrder(ExchangeOrder exchangeOrder);

    void updateFailedState(ExchangeOrder order, String reason);

    void updateSuccessState(Long orderId);

    void finishRefund(Boolean isSuccess, EasypayRefundRecord record);

    IPage<ExchangeOrderPageResult> pageExchangeOrder(ExchangeOrderPageParam param);

    ExchangeOrderResult getExchangeOrder(OrderGetParam param);

    void receipt(OrderGetParam param);

    void revokeRefund(OrderGetParam param);

    void refund(OrderRefundParam param);

    void updateOrderState();

    void autoRefundExpireOrder();

    IPage<ExchangeGoods> pageShopGood(ExchangeShopGoodsPageParam param);

    ExchangeGoodsSpecValueResult getGoodsSpecValue(GoodsGetParam param);

    ExchangeGoodsSkuInfo getSkuByGoodsId(GoodsGetSkuInfoParam param);
}
