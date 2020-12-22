package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.ticket.*;
import com.aquilaflycloud.mdc.extra.docom.component.DoComConfig;
import com.aquilaflycloud.mdc.extra.docom.util.DoComHttpUtil;
import com.aquilaflycloud.mdc.mapper.TicketProductInfoDetailMapper;
import com.aquilaflycloud.mdc.mapper.TicketProductInfoMapper;
import com.aquilaflycloud.mdc.model.ticket.TicketProductInfo;
import com.aquilaflycloud.mdc.model.ticket.TicketProductInfoDetail;
import com.aquilaflycloud.mdc.result.ticket.TicketInterfaceAccountInfoResult;
import com.aquilaflycloud.mdc.service.TicketInterfaceAccountInfoService;
import com.aquilaflycloud.mdc.service.TicketProductInfoNormalService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 产品信息服务实现类(不包含租户解析)
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@Service
@Slf4j
public class TicketProductInfoNormalServiceImpl implements TicketProductInfoNormalService {
    @Resource
    private TicketProductInfoMapper ticketProductInfoMapper;

    @Resource
    private TicketProductInfoDetailMapper ticketProductInfoDetailMapper;

    @Resource
    private TicketInterfaceAccountInfoService ticketInterfaceAccountInfoService;

    @Override
    @Transactional
    public void getInterfaceProductInfo() {
        try {
            //获取数据库配置的第三方接口(道控)账号相关信息
            List<TicketInterfaceAccountInfoResult> interfaceAccountInfo = ticketInterfaceAccountInfoService.normalGetInterfaceAccountInfo();

            //判断是否有符合条件的账号信息
            if (ObjectUtil.isEmpty(interfaceAccountInfo)) {
                log.info("没有可用的第三方接口账号信息未配置，请配置后重试");
                throw new ServiceException("没有可用的第三方接口账号信息未配置，请配置后重试");
            }

            //保存循环调用的结果
            JSONArray results = new JSONArray();

            //循环调用获取对应的产品列表接口
            for (int i = 0; i < interfaceAccountInfo.size(); i++) {
                TicketInterfaceAccountInfoResult item = interfaceAccountInfo.get(i);
                //设置道控账号信息
                DoComConfig config = new DoComConfig(item.getMerchantCode(), item.getInterfaceAccount(), item.getSecret(), item.getBaseUrl());
                JSONObject result = DoComHttpUtil.getProduct(config);

                if (StrUtil.equals("true", result.getStr("isTrue")) && StrUtil.equals("200", result.getStr("code"))) {
                    //将账号信息设置到返回结果中
                    result.accumulate("accountInfo", item);
                    results.add(result);
                } else if (StrUtil.equals("false", result.getStr("isTrue")) && StrUtil.equals("204", result.getStr("code"))) {
                    TicketProductInfo productInfo = new TicketProductInfo();
                    productInfo.setState(ProductInfoStateEnum.OUT);
                    //没有获取到对应产品
                    int updateCount = ticketProductInfoMapper.normalUpdate(productInfo, Wrappers.<TicketProductInfo>lambdaQuery()
                            .eq(TicketProductInfo::getMerchantCode, item.getMerchantCode())
                            .eq(TicketProductInfo::getScenicSpotId, item.getScenicSpotId())
                    );

                    log.info("调用获取产品信息接口商户编号：" + item.getMerchantCode() + ";更新产品为过期的数量：" + updateCount);
                } else {
                    log.error("调用获取产品信息接口商户编号：" + item.getMerchantCode() + ";错误返回结果：" + result.toString());
                }
            }

            //解析循环调用的结果
            for (int i = 0; i < results.size(); i++) {
                //解析单个接口返回结果
                JSONObject result = new JSONObject(results.get(i));
                String jsonStr = result.getStr("jsonStr");
                TicketInterfaceAccountInfoResult accountInfo = result.get("accountInfo", TicketInterfaceAccountInfoResult.class);

                //存放封装好的产品信息和产品详情
                //将返回的产品信息字符串转为json数组
                JSONArray products = JSONUtil.parseArray(jsonStr);
                Map<String, JSONObject> productInfoMapResult = parseNormalInterfaceResult(accountInfo, products);


                //获取对应商户编号的产品信息数据
                List<TicketProductInfo> currentDbProductInfos = ticketProductInfoMapper.normalSelectList(Wrappers.<TicketProductInfo>lambdaQuery().eq(TicketProductInfo::getMerchantCode, accountInfo.getMerchantCode()));

                //数据库有值-接口有返回值
                if ((ObjectUtil.isNotNull(productInfoMapResult) && productInfoMapResult.size() > 0)
                        && (ObjectUtil.isNotNull(currentDbProductInfos) && currentDbProductInfos.size() > 0)) {
                    //以数据库为基础，与接口返回值对比
                    for (int j = 0; j < currentDbProductInfos.size(); j++) {
                        TicketProductInfo dbTicketProductInfo = currentDbProductInfos.get(j);
                        String key = dbTicketProductInfo.getMerchantCode() + "-" + dbTicketProductInfo.getProductId() + "-" + dbTicketProductInfo.getProductSeriesCode();

                        JSONObject jsonObject = productInfoMapResult.get(key);
                        //数据库和接口都有数据
                        if (ObjectUtil.isNotNull(jsonObject) && jsonObject.size() > 0) {
                            //数据库有-接口有：更新产品对应字段信息，删除产品详情，重新保存产品详情

                            JSONArray productInfoDetails = jsonObject.getJSONArray("productInfoDetails");
                            //删除产品详情
                            ticketProductInfoDetailMapper.normalDelete(Wrappers.<TicketProductInfoDetail>lambdaQuery().eq(ObjectUtil.isNotNull(dbTicketProductInfo.getId()), TicketProductInfoDetail::getProductInfoId, dbTicketProductInfo.getId()));

                            TicketProductInfo productInfo = jsonObject.get("productInfo", TicketProductInfo.class);
                            productInfo.setId(dbTicketProductInfo.getId());
                            //去掉不更新的字段
                            productInfo.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                            productInfo.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                            productInfo.setIsRecommend(null);
                            productInfo.setIsTop(null);
                            productInfo.setType(null);

                            ticketProductInfoMapper.normalUpdateById(productInfo);

                            for (int k = 0; k < productInfoDetails.size(); k++) {
                                JSONObject item = new JSONObject(productInfoDetails.get(k));
                                TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail();
                                BeanUtil.copyProperties(item, ticketProductInfoDetail);
                                ticketProductInfoDetail.setProductInfoId(dbTicketProductInfo.getId());
                                ticketProductInfoDetail.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                                ticketProductInfoDetail.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                                ticketProductInfoDetailMapper.normalInsert(ticketProductInfoDetail);
                            }
                        } else if (ObjectUtil.isNull(jsonObject) || jsonObject.size() <= 0) {
                            TicketProductInfo productInfo = new TicketProductInfo();
                            productInfo.setId(dbTicketProductInfo.getId());
                            productInfo.setState(ProductInfoStateEnum.OUT);
                            // 数据库有-接口没有：更新产品信息的状态为过期
                            ticketProductInfoMapper.normalUpdateById(productInfo);
                        }
                    }

                    List<String> keys = currentDbProductInfos.stream().map(dbTicketProductInfo -> dbTicketProductInfo.getMerchantCode() + "-" + dbTicketProductInfo.getProductId() + "-" + dbTicketProductInfo.getProductSeriesCode()).collect(toList());
                    // 数据库没有-接口有：插入产品信息和产品详情
                    for (String key : productInfoMapResult.keySet()) {
                        if (!keys.contains(key)) {
                            JSONObject jsonObject = productInfoMapResult.get(key);
                            TicketProductInfo productInfo = jsonObject.get("productInfo", TicketProductInfo.class);
                            productInfo.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                            productInfo.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                            //默认为取消游玩时间
                            productInfo.setIsSetPlayTime(ProductInfoSetPlayTimeEnum.CANCEL_PLAY_TIME);
                            ticketProductInfoMapper.normalInsert(productInfo);
                            JSONArray productInfoDetails = jsonObject.getJSONArray("productInfoDetails");
                            for (int k = 0; k < productInfoDetails.size(); k++) {
                                JSONObject item = new JSONObject(productInfoDetails.get(k));
                                TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail();
                                BeanUtil.copyProperties(item, ticketProductInfoDetail);
                                ticketProductInfoDetail.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                                ticketProductInfoDetail.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                                ticketProductInfoDetailMapper.normalInsert(ticketProductInfoDetail);
                            }
                        }
                    }

                } else if ((ObjectUtil.isNull(productInfoMapResult) || productInfoMapResult.size() <= 0)
                        && (ObjectUtil.isNotNull(currentDbProductInfos) && currentDbProductInfos.size() > 0)) {
                    //接口封装结果没有和数据库有值：更新数据库状态都为过期
                    List<TicketProductInfo> updateList = new ArrayList<>();
                    for (int j = 0; j < currentDbProductInfos.size(); j++) {
                        TicketProductInfo item = new TicketProductInfo();
                        item.setId(currentDbProductInfos.get(j).getId());
                        item.setState(ProductInfoStateEnum.OUT);
                        updateList.add(item);
                    }

                    int count = ticketProductInfoMapper.updateBatchByIds(updateList);

                } else if ((ObjectUtil.isNotNull(productInfoMapResult) && productInfoMapResult.size() > 0)
                        && (ObjectUtil.isNull(currentDbProductInfos) || currentDbProductInfos.size() <= 0)) {
                    //接口封装结果有值和数据库没有值：保存所有的值
                    for (String key : productInfoMapResult.keySet()) {
                        JSONObject jsonObject = new JSONObject(productInfoMapResult.get(key));
                        TicketProductInfo productInfo = jsonObject.get("productInfo", TicketProductInfo.class);
                        productInfo.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                        productInfo.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                        //默认为没有指定游玩时间
                        productInfo.setIsSetPlayTime(ProductInfoSetPlayTimeEnum.CANCEL_PLAY_TIME);
                        int productInsertCount = ticketProductInfoMapper.normalInsert(productInfo);

                        JSONArray productInfoDetails = jsonObject.getJSONArray("productInfoDetails");

                        if (ObjectUtil.isNotNull(productInfoDetails) && productInfoDetails.size() > 0) {
                            for (int j = 0; j < productInfoDetails.size(); j++) {
                                JSONObject item = new JSONObject(productInfoDetails.get(j));
                                TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail();
                                BeanUtil.copyProperties(item, ticketProductInfoDetail);
                                ticketProductInfoDetail.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                                ticketProductInfoDetail.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                                int detailCount = ticketProductInfoDetailMapper.normalInsert(ticketProductInfoDetail);
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            log.error("获取产品列表失败：" + e);
            throw new ServiceException("获取产品列表失败");
        }
    }


    /**
     * 封装数据(key:客户编号-产品id-产品编号;value:包含产品信息和详情的json对象)
     *
     * @param accountInfo
     * @param products
     * @return
     */
    private static Map<String, JSONObject> parseNormalInterfaceResult(TicketInterfaceAccountInfoResult accountInfo, JSONArray products) {
        //存放产品信息及产品详情信息
        Map<String, JSONObject> result = new HashMap<>();
        //将结果封装
        for (int i = 0; i < products.size(); i++) {
            JSONObject item = JSONUtil.parseObj(products.get(i));
            if (ObjectUtil.isNotEmpty(item) && item.size() > 0) {
                //存放产品信息到实体类
                JSONObject productItem = new JSONObject();
                TicketProductInfo ticketProductInfo = new TicketProductInfo(
                        IdWorker.getId(),
                        item.getStr("MerchantCode"),
                        item.getStr("ProductName"),
                        item.getInt("ProductType"),
                        item.getInt("ProductId"),
                        item.getStr("ProductSeriesCode"),
                        item.getStr("OfflineCode"),
                        item.getStr("TicketName"),
                        item.getBigDecimal("ProductPrice"),
                        item.getBigDecimal("ProductSellPrice"),
                        item.getBigDecimal("TicketPrice"),
                        item.getInt("ExpireNum"),
                        item.getInt("ExpiryDay"),
                        StrUtil.isEmpty(item.getStr("StartTime")) ? null : DateUtil.parse(item.getStr("StartTime"), "yyyy-MM-dd'T'HH:mm:ss"),
                        StrUtil.isEmpty(item.getStr("EndTime")) ? null : DateUtil.parse(item.getStr("EndTime"), "yyyy-MM-dd'T'HH:mm:ss"),
                        EnumUtil.likeValueOf(ProductInfoPreSellEnum.class, item.getInt("IsPreSell")),
                        StrUtil.isEmpty(item.getStr("PreSellStartDate")) ? null : DateUtil.parse(item.getStr("PreSellStartDate"), "yyyy-MM-dd'T'HH:mm:ss"),
                        StrUtil.isEmpty(item.getStr("PreSellEndDate")) ? null : DateUtil.parse(item.getStr("PreSellEndDate"), "yyyy-MM-dd'T'HH:mm:ss"),
                        item.getInt("IsDateLine"),
                        item.getInt("DateLineHour"),
                        item.getInt("IsSendMessage"),
                        item.getStr("IsTernimal"),
                        EnumUtil.likeValueOf(ProductInfoSellTodayEnum.class, item.getInt("IsSellToday")),
                        item.getInt("SellMax"),
                        item.getInt("SellMin"),
                        StrUtil.isEmpty(item.getStr("CheckStartTime")) ? null : DateUtil.parse(item.getStr("CheckStartTime"), "yyyy-MM-dd'T'HH:mm:ss"),
                        StrUtil.isEmpty(item.getStr("CheckEndTime")) ? null : DateUtil.parse(item.getStr("CheckEndTime"), "yyyy-MM-dd'T'HH:mm:ss"),
                        item.getStr("CheckAllowType"),
                        item.getInt("IsCanRefund"),
                        item.getInt("RefundCheckAfter"),
                        item.getInt("RefundCanToday"),
                        item.getInt("RefundShowAudit"),
                        EnumUtil.likeValueOf(ProductInfoBigTicketEnum.class, item.getInt("IsBigTicket")),
                        ProductInfoStateEnum.NORMAL,
                        ProductInfoRecommendEnum.NOTRECOMMEND,
                        ProductInfoTopEnum.NOTTOP,
                        ProductInfoTypeEnum.NOMALPRODUCTS,
                        StrUtil.isEmpty(item.getStr("BuyMinTime")) ? null : DateUtil.parse(item.getStr("BuyMinTime"), "yyyy-MM-dd'T'HH:mm:ss"),
                        accountInfo.getDesignateOrgIds(),
                        accountInfo.getScenicSpotId(),
                        accountInfo.getType(),
                        accountInfo.getScenicSpotName(),
                        accountInfo.getTenantId(),
                        accountInfo.getSubTenantId()
                );

                List<TicketProductInfoDetail> productInfoDetails = new ArrayList<>();
                //获取产品详情
                Object calendarPlanData = item.getObj("CalendarPlanData");
                //判断是否存在产品明细详情
                if (ObjectUtil.isNotNull(calendarPlanData)) {
                    JSONArray productDetails = new JSONArray(calendarPlanData);
                    if (ObjectUtil.isNotNull(productDetails) && productDetails.size() >0) {
                        for (int j = 0; j < productDetails.size(); j++) {
                            JSONObject detail = JSONUtil.parseObj(productDetails.get(j));
                            if (ObjectUtil.isNull(detail) || detail.size()<=0) {
                                continue;
                            }
                            TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail(IdWorker.getId(),
                                    StrUtil.isEmpty(detail.getStr("PriceDate")) ? null : DateUtil.parse(detail.getStr("PriceDate"), "yyyy-MM-dd"),
                                    detail.getBigDecimal("ProductPrice"),
                                    detail.getBigDecimal("ProductSellPrice"),
                                    detail.getBigDecimal("TicketPrice"),
                                    detail.getStr("TotalInventory"),
                                    detail.getStr("UseInventory"),
                                    ticketProductInfo.getId(),
                                    accountInfo.getTenantId(),
                                    accountInfo.getSubTenantId());

                            productInfoDetails.add(ticketProductInfoDetail);

                        }
                    }
                }
                String key = ticketProductInfo.getMerchantCode() + "-" + ticketProductInfo.getProductId() + "-" + ticketProductInfo.getProductSeriesCode();
                productItem.accumulate("productInfo", ticketProductInfo);
                productItem.accumulate("productInfoDetails", productInfoDetails);
                result.put(key, productItem);
            }
        }

        return result;
    }

}
