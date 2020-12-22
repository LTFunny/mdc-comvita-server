package com.aquilaflycloud.mdc.service;

import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 产品信息服务类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
public interface TicketProductInfoService {
    /**
     * 获取第三方接口产品数据列表，并保存
     */
    void getInterfaceProductInfo();

    /**
     * 获取分页产品信息
     * @param param
     * @return
     */
    IPage<ProductInfoResult> listProductInfo(ProductInfoListParam param);

    /**
     * 微信小程序购票页面获取所有产品信息
     * @return
     */
    WechatGetProductInfoResult wechatGetProductInfo(WechatGetProductInfoParam param);

    /**
     * 根据id获取产品信息
     * @param param
     * @return
     */
    TicketProductInfoDetailResult wechatGetProductInfoById(ProductInfoGetByIdParam param);

    /**
     * 获取渠道售卖情况
     * @param param
     * @return
     */
    TicketProductSaleResult salesProductInfo(ProductInfoSalesParam param);

    /**
     * 获取渠道售卖-订单详情
     * @param param
     * @return
     */
    IPage<ProductInfoSaleOrderSumResult> salesProductOrderInfo(ProductInfoSalesOrderParam param);

    /**
     * 根据id获取产品详情
     * @param param
     * @return
     */
    TicketProductInfoDetailByIdResult getProductInfo(ProductInfoGetParam param);

    /**
     * 新增最新推荐模糊查询产品列表
     * @param param
     * @return
     */
    IPage<ProductInfoResult> listActivityProductInfo(ProductInfoActivityListParam param);

    /**
     * 是否推荐
     * @param param
     */
    void updateProductInfoRecommend(ProductInfoUpdateRecommendParam param);

    /**
     * 是否置顶
     * @param param
     */
    void updateProductInfoTop(ProductInfoUpdateTopParam param);

    /**
     * 更新产品信息中指定游玩日期
     * @param param
     */
    void updateProductInfoPlayTime(ProductInfoUpdatePlayTimeParam param);

    /**
     * 更新产品信息是否联票
     * @param param
     */
    void updateProductInfoComposite(ProductInfoUpdateCompositeParam param);

    /**
     * 更新产品信息购买须知
     * @param param
     */
    void updateProductInfoBuyIntroduce(ProductInfoUpdateBuyIntroduceParam param);

    /**
     * 获取第三方接口产品数据列表，并保存(定时器使用)
     */
    void getNormalInterfaceProductInfo();

    /**
     * 更新产品信息是否上下架
     * @param param
     */
    void updateProductInfoUpper(ProductInfoUpdateUpperParam param);
}
