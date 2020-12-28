package com.aquilaflycloud.mdc.api;

import com.aquilaflycloud.mdc.model.catalog.CatalogInfo;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoods;
import com.aquilaflycloud.mdc.model.exchange.ExchangeGoodsSkuInfo;
import com.aquilaflycloud.mdc.param.exchange.*;
import com.aquilaflycloud.mdc.result.exchange.*;
import com.aquilaflycloud.mdc.service.CatalogService;
import com.aquilaflycloud.mdc.service.ExchangeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gitee.sop.servercommon.annotation.ApiMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ExchangeApi
 *
 * @author star
 * @date 2020-03-15
 */
@RestController
@Api(tags = "兑换商城相关接口")
public class ExchangeApi {
    @Resource
    private CatalogService catalogService;
    @Resource
    private ExchangeService exchangeService;

    @ApiOperation(value = "兑换商品分类列表查询", notes = "兑换商品分类列表查询")
    @ApiMapping(value = "comvita.exchange.catalog.list", method = RequestMethod.POST)
    public List<CatalogInfo> listExchangeCatalog() {
        return catalogService.listExchangeCatalog();
    }

    @ApiOperation(value = "兑换商品列表查询(分页)", notes = "兑换商品列表查询(分页)")
    @ApiMapping(value = "comvita.exchange.goods.page", method = RequestMethod.POST)
    public IPage<ExchangeGoodsResult> pageGoods(ExchangeGoodsPageParam param) {
        return exchangeService.pageExchangeGoods(param);
    }

    @ApiOperation(value = "推荐商品列表查询(分页)", notes = "推荐商品列表查询(分页)")
    @ApiMapping(value = "comvita.exchange.recommend.page", method = RequestMethod.POST)
    public IPage<ExchangeGoodsResult> pageRecommend(RecommendPageParam param) {
        return exchangeService.pageRecommendGoods(param);
    }

    @ApiOperation(value = "获取兑换商品详情", notes = "获取兑换商品详情")
    @ApiMapping(value = "comvita.exchange.goods.get", method = RequestMethod.POST)
    public ExchangeGoodsResult getGoods(GoodsGetParam param) {
        return exchangeService.getExchangeGoods(param);
    }

    @ApiOperation(value = "获取物流商品规格和规格值的配置信息", notes = "获取物流商品规格和规格值的配置信息")
    @ApiMapping(value = "comvita.exchange.goods.getSpecValue", method = RequestMethod.POST)
    public ExchangeGoodsSpecValueResult getGoodsSpecValue(GoodsGetParam param) {
        return exchangeService.getGoodsSpecValue(param);
    }

    @ApiOperation(value = "获取选择的物流商品的sku信息", notes = "获取选择的物流商品的sku信息")
    @ApiMapping(value = "comvita.exchange.goods.getSku", method = RequestMethod.POST)
    public ExchangeGoodsSkuInfo getSkuByGoodsId(GoodsGetSkuInfoParam param) {
        return exchangeService.getSkuByGoodsId(param);
    }

    @ApiOperation(value = "新增兑换商品订单", notes = "新增兑换商品订单(返回支付信息)")
    @ApiMapping(value = "comvita.exchange.order.add", method = RequestMethod.POST)
    public ExchangeOrderPayResult addOrder(OrderAddParam param) {
        return exchangeService.addOrder(param);
    }

    @ApiOperation(value = "支付商品订单", notes = "支付商品订单(返回支付信息)")
    @ApiMapping(value = "comvita.exchange.order.pay", method = RequestMethod.POST)
    public ExchangeOrderPayResult payOrder(OrderPayParam param) {
        return exchangeService.payOrder(param);
    }

    @ApiOperation(value = "商品订单列表(分页)", notes = "商品订单列表(分页)")
    @ApiMapping(value = "comvita.exchange.order.page", method = RequestMethod.POST)
    public IPage<ExchangeOrderPageResult> pageOrder(ExchangeOrderPageParam param) {
        return exchangeService.pageExchangeOrder(param);
    }

    @ApiOperation(value = "商品订单详情", notes = "商品订单详情")
    @ApiMapping(value = "comvita.exchange.order.get", method = RequestMethod.POST)
    public ExchangeOrderResult getOrder(OrderGetParam param) {
        return exchangeService.getExchangeOrder(param);
    }

    @ApiOperation(value = "订单确认收货", notes = "订单确认收货")
    @ApiMapping(value = "comvita.exchange.order.receipt", method = RequestMethod.POST)
    public void receipt(OrderGetParam param) {
        exchangeService.receipt(param);
    }

    @ApiOperation(value = "订单撤销退款", notes = "订单撤销退款")
    @ApiMapping(value = "comvita.exchange.order.revoke", method = RequestMethod.POST)
    public void revokeRefund(OrderGetParam param) {
        exchangeService.revokeRefund(param);
    }

    @ApiOperation(value = "订单发起退款", notes = "订单发起退款")
    @ApiMapping(value = "comvita.exchange.order.refund", method = RequestMethod.POST)
    public void refund(OrderRefundParam param) {
        exchangeService.refund(param);
    }

    @ApiOperation(value = "根据商品类型获取商圈店铺商品信息", notes = "根据商品类型获取商圈店铺商品信息")
    @ApiMapping(value = "comvita.exchange.goods.pageShopGood", method = RequestMethod.POST)
    public IPage<ExchangeGoods> pageShopGood(ExchangeShopGoodsPageParam param) {
        return exchangeService.pageShopGood(param);
    }
}
