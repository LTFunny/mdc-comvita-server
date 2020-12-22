package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.constant.MdcConstant;
import com.aquilaflycloud.mdc.enums.ticket.*;
import com.aquilaflycloud.mdc.extra.docom.component.DoComConfig;
import com.aquilaflycloud.mdc.extra.docom.util.DoComHttpUtil;
import com.aquilaflycloud.mdc.mapper.TicketOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.TicketProductInfoDetailMapper;
import com.aquilaflycloud.mdc.mapper.TicketProductInfoMapper;
import com.aquilaflycloud.mdc.model.ticket.*;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.ticket.*;
import com.aquilaflycloud.mdc.service.TicketInterfaceAccountInfoService;
import com.aquilaflycloud.mdc.service.TicketProductInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 产品信息服务实现类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-18
 */
@Service
@Slf4j
public class TicketProductInfoServiceImpl implements TicketProductInfoService {
    @Resource
    private TicketProductInfoMapper ticketProductInfoMapper;

    @Resource
    private TicketProductInfoDetailMapper ticketProductInfoDetailMapper;

    @Resource
    private TicketInterfaceAccountInfoService ticketInterfaceAccountInfoService;

    @Resource
    private TicketOrderInfoMapper ticketOrderInfoMapper;

    @Override
    @Transactional
    public void getInterfaceProductInfo() {
        try {
            //获取数据库配置的第三方接口(道控)账号相关信息
            List<TicketInterfaceAccountInfoResult> interfaceAccountInfo = ticketInterfaceAccountInfoService.getInterfaceAccountInfo();

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
                    int updateCount = ticketProductInfoMapper.update(productInfo, Wrappers.<TicketProductInfo>lambdaQuery()
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
                Map<String, JSONObject> productInfoMapResult = parseInterfaceResult(accountInfo, products);


                //获取对应商户编号的产品信息数据
                List<TicketProductInfo> currentDbProductInfos = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery().eq(TicketProductInfo::getMerchantCode, accountInfo.getMerchantCode()));

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
                            ticketProductInfoDetailMapper.delete(Wrappers.<TicketProductInfoDetail>lambdaQuery().eq(ObjectUtil.isNotNull(dbTicketProductInfo.getId()), TicketProductInfoDetail::getProductInfoId, dbTicketProductInfo.getId()));

                            TicketProductInfo productInfo = jsonObject.get("productInfo", TicketProductInfo.class);
                            productInfo.setId(dbTicketProductInfo.getId());
                            //去掉不更新的字段
                            productInfo.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                            productInfo.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                            productInfo.setIsRecommend(null);
                            productInfo.setIsTop(null);
                            productInfo.setType(null);
                            productInfo.setIsUpper(null);
                            productInfo.setBuyIntroduce(null);

                            ticketProductInfoMapper.updateById(productInfo);

                            for (int k = 0; k < productInfoDetails.size(); k++) {
                                JSONObject item = new JSONObject(productInfoDetails.get(k));
                                TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail();
                                BeanUtil.copyProperties(item, ticketProductInfoDetail);
                                ticketProductInfoDetail.setProductInfoId(dbTicketProductInfo.getId());
                                ticketProductInfoDetail.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                                ticketProductInfoDetail.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                                ticketProductInfoDetailMapper.insert(ticketProductInfoDetail);
                            }
                        } else if (ObjectUtil.isNull(jsonObject) || jsonObject.size() <= 0) {
                            TicketProductInfo productInfo = new TicketProductInfo();
                            productInfo.setId(dbTicketProductInfo.getId());
                            productInfo.setState(ProductInfoStateEnum.OUT);
                            // 数据库有-接口没有：更新产品信息的状态为过期
                            ticketProductInfoMapper.updateById(productInfo);
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
                            //设置默认为下架
                            productInfo.setIsUpper(ProductInfoUpperEnum.NOTUPPER);
                            //设置默认购买说明json串
                            productInfo.setBuyIntroduce(MdcConstant.PRODUCT_BUY_INTRODUCE.replace("sellMax", productInfo.getSellMax()+"").replace("sellMin", productInfo.getSellMin()+""));
                            ticketProductInfoMapper.insert(productInfo);

                            JSONArray productInfoDetails = jsonObject.getJSONArray("productInfoDetails");
                            for (int k = 0; k < productInfoDetails.size(); k++) {
                                JSONObject item = new JSONObject(productInfoDetails.get(k));
                                TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail();
                                BeanUtil.copyProperties(item, ticketProductInfoDetail);
                                ticketProductInfoDetail.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                                ticketProductInfoDetail.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                                ticketProductInfoDetailMapper.insert(ticketProductInfoDetail);
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
                        //默认为取消游玩时间
                        productInfo.setIsSetPlayTime(ProductInfoSetPlayTimeEnum.CANCEL_PLAY_TIME);
                        //设置默认为下架
                        productInfo.setIsUpper(ProductInfoUpperEnum.NOTUPPER);
                        //设置默认购买说明json串
                        productInfo.setBuyIntroduce(MdcConstant.PRODUCT_BUY_INTRODUCE.replace("sellMax", productInfo.getSellMax()+"").replace("sellMin", productInfo.getSellMin()+""));
                        int productInsertCount = ticketProductInfoMapper.insert(productInfo);

                        JSONArray productInfoDetails = jsonObject.getJSONArray("productInfoDetails");

                        if (ObjectUtil.isNotNull(productInfoDetails) && productInfoDetails.size() > 0) {
                            for (int j = 0; j < productInfoDetails.size(); j++) {
                                JSONObject item = new JSONObject(productInfoDetails.get(j));
                                TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail();
                                BeanUtil.copyProperties(item, ticketProductInfoDetail);
                                ticketProductInfoDetail.setCreatorOrgIds(accountInfo.getCreatorOrgIds());
                                ticketProductInfoDetail.setDesignateOrgIds(accountInfo.getDesignateOrgIds());
                                int detailCount = ticketProductInfoDetailMapper.insert(ticketProductInfoDetail);
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

    @Override
    public IPage<ProductInfoResult> listProductInfo(ProductInfoListParam param) {
        //设置获取的产品状态为正常的
        param.setState(ProductInfoStateEnum.NORMAL);

        //查询状态为正常的产品列表
        IPage<TicketProductInfo> ticketProductInfos = ticketProductInfoMapper.selectPage(param.page(),
                Wrappers.<TicketProductInfo>lambdaQuery()
                        .eq(TicketProductInfo::getState, param.getState())
                        .eq(ObjectUtil.isNotNull(param.getScenicSpotType()), TicketProductInfo::getScenicSpotType, param.getScenicSpotType())
                        .like(ObjectUtil.isNotNull(param.getProductId()), TicketProductInfo::getProductId, param.getProductId())
                        .like(StrUtil.isNotBlank(param.getProductName()), TicketProductInfo::getProductName, param.getProductName())
                        .like(StrUtil.isNotBlank(param.getCreatorName()), TicketProductInfo::getCreatorName, param.getCreatorName())
                        .like(StrUtil.isNotBlank(param.getLastOperatorName()), TicketProductInfo::getLastOperatorName, param.getLastOperatorName())
                        .ge(ObjectUtil.isNotNull(param.getStartTicketPrice()), TicketProductInfo::getTicketPrice, param.getStartTicketPrice())
                        .le(ObjectUtil.isNotNull(param.getEndTicketPrice()), TicketProductInfo::getTicketPrice, param.getEndTicketPrice())
                        .ge(ObjectUtil.isNotNull(param.getStartProductSellPrice()), TicketProductInfo::getProductSellPrice, param.getStartProductSellPrice())
                        .le(ObjectUtil.isNotNull(param.getEndProductSellPrice()), TicketProductInfo::getProductSellPrice, param.getEndProductSellPrice())
                        .ge(ObjectUtil.isNotNull(param.getStartEffectiveTime()), TicketProductInfo::getStartTime, param.getStartEffectiveTime())
                        .le(ObjectUtil.isNotNull(param.getEndEffectiveTime()), TicketProductInfo::getStartTime, param.getEndEffectiveTime())
                        .ge(ObjectUtil.isNotNull(param.getStartFailureTime()), TicketProductInfo::getEndTime, param.getStartFailureTime())
                        .le(ObjectUtil.isNotNull(param.getEndFailureTime()), TicketProductInfo::getEndTime, param.getEndFailureTime())
                        .eq(ObjectUtil.isNotNull(param.getScenicSpotType()), TicketProductInfo::getScenicSpotType, param.getScenicSpotType())
                        .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        //获取根据符合条件的产品信息id查询产品详情列表
        List<TicketProductInfoDetail> TicketProductInfoDetailList = null;
        if (ObjectUtil.isNotEmpty(ticketProductInfos.getRecords()) && ticketProductInfos.getRecords().size() > 0) {
            LambdaQueryWrapper<TicketProductInfoDetail> detailWrapper = new LambdaQueryWrapper<TicketProductInfoDetail>();
            detailWrapper.in(TicketProductInfoDetail::getProductInfoId, ticketProductInfos.getRecords().stream().map(w -> w.getId()).collect(toList()));
            TicketProductInfoDetailList = ticketProductInfoDetailMapper.selectList(detailWrapper);
        } else {
            //没有产品信息直接返回
            return null;
        }

        //保存产品对应的详情(key:产品id, value:对应产品的详情)
        Map<Long, List<TicketProductInfoDetail>> detailMap = new HashMap<>();

        //根据产品ids查询产品详情，迭代成map集合
        if (ObjectUtil.isNotEmpty(TicketProductInfoDetailList) && TicketProductInfoDetailList.size() > 0) {
            for (int i = 0; i < TicketProductInfoDetailList.size(); i++) {
                TicketProductInfoDetail item = TicketProductInfoDetailList.get(i);

                List<TicketProductInfoDetail> productInfoDetails = detailMap.get(item.getProductInfoId());
                if (ObjectUtil.isEmpty(productInfoDetails)) {
                    //map中没有对应产品id的详情，则创建详情集合，将当前的详情加入
                    List<TicketProductInfoDetail> details = new ArrayList<>();
                    details.add(item);
                    detailMap.put(item.getProductInfoId(), details);
                } else {
                    //map中有对应产品id的详情，则在原先基础加当前的详情加入
                    productInfoDetails.add(item);
                    detailMap.put(item.getProductInfoId(), productInfoDetails);
                }
            }
        }

        //将产品信息和详情信息封装成返回对象
        List<ProductInfoResult> records = new ArrayList<>();
        for (int i = 0; i < ticketProductInfos.getRecords().size(); i++) {
            ProductInfoResult item = new ProductInfoResult();
            TicketProductInfo ticketProductInfo = ticketProductInfos.getRecords().get(i);
            BeanUtil.copyProperties(ticketProductInfo, item);

            //不是预售产品将预售时间设置为null
            if (ObjectUtil.equal(item.getIsPreSell(), ProductInfoPreSellEnum.NOT_PRE_SELL)) {
                item.setPreSellStartDate(null);
                item.setPreSellEndDate(null);
            }
            item.setProductInfoDetailList(detailMap.get(item.getId()));

            //获取联票枚举值集合
            StringBuffer sb = new StringBuffer();
            if (ObjectUtil.equal(item.getType(), ProductInfoTypeEnum.COMPOSITEPRODUCT)) {
                String scenicSpotTypes = ticketProductInfo.getScenicSpotTypes();
                if (StrUtil.isNotEmpty(scenicSpotTypes)) {
                    List<ScenicSpotTypeEnum> scenicSpotTypeEnums = new ArrayList<>();
                    String[] split = scenicSpotTypes.split(",");
                    for (int j = 0; j < split.length; j++) {
                        int type = Integer.valueOf(split[j]);
                        ScenicSpotTypeEnum scenicSpotTypeEnum = EnumUtil.likeValueOf(ScenicSpotTypeEnum.class, type);
                        scenicSpotTypeEnums.add(scenicSpotTypeEnum);
                        sb.append(scenicSpotTypeEnum.getName() + "+");
                    }

                    item.setScenicSpotTypeEnums(scenicSpotTypeEnums);
                }
            }

            item.setTypeStr(sb.length() > 0
                    ? item.getType().getName() + "(" + sb.substring(0, sb.length() - 1) + ")"
                    : item.getType().getName());

            records.add(item);
        }

        IPage<ProductInfoResult> resultIPage = new Page<>(ticketProductInfos.getCurrent(), ticketProductInfos.getSize(), ticketProductInfos.getTotal());
        resultIPage.setRecords(records);

        return resultIPage;
    }

    //处理预售票：0为非预售票，全部显示；1为预售票：当前时间在预售期内则显示
    private static List<TicketProductInfo> parPreTicket(List<TicketProductInfo> dbList) {
        List<TicketProductInfo> resultList = new ArrayList<TicketProductInfo>();
        if (ObjectUtil.isNotNull(dbList) && dbList.size() > 0) {
            DateTime now = DateUtil.date();
            resultList = dbList.stream().filter(item -> ObjectUtil.equal(item.getIsPreSell(), ProductInfoPreSellEnum.NOT_PRE_SELL)
                            || (
                            (ObjectUtil.equal(item.getIsPreSell(), ProductInfoPreSellEnum.PRE_SELL)
                                    && (DateUtil.compare(now, item.getPreSellStartDate()) >= 0 && DateUtil.compare(now, item.getPreSellEndDate()) <= 0)
                            )
                    )
            ).collect(toList());
        }

        return resultList;
    }

    @Override
    public WechatGetProductInfoResult wechatGetProductInfo(WechatGetProductInfoParam param) {
        //查询热门推荐产品：推荐-置顶时间降序-创建时间降序
        //SELECT * FROM ticket_product_info WHERE state='1' AND is_recommend='1' ORDER BY create_time DESC, is_top DESC
        List<TicketProductInfo> recommendDbList = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
                .eq(TicketProductInfo::getState, ProductInfoStateEnum.NORMAL)
                .eq(TicketProductInfo::getIsUpper, ProductInfoUpperEnum.UPPER)
                .eq(TicketProductInfo::getIsRecommend, ProductInfoRecommendEnum.RECOMMEND)
                .orderByDesc(TicketProductInfo::getTopTime)
                .orderByDesc(TicketProductInfo::getCreateTime)
        );

        List<TicketProductInfo> recommendList = parPreTicket(recommendDbList);

        //查询海洋馆
        List<TicketProductInfo> oceanDbList = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
                .eq(TicketProductInfo::getState, ProductInfoStateEnum.NORMAL)
                .eq(TicketProductInfo::getIsUpper, ProductInfoUpperEnum.UPPER)
                .eq(TicketProductInfo::getScenicSpotType, ScenicSpotTypeEnum.OCEAN)
                .eq(TicketProductInfo::getType, ProductInfoTypeEnum.NOMALPRODUCTS)
                .orderByDesc(TicketProductInfo::getTopTime)
                .orderByDesc(TicketProductInfo::getCreateTime)
        );

        List<TicketProductInfo> oceanList = parPreTicket(oceanDbList);

        //查询雨林馆
        List<TicketProductInfo> rainforestDbList = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
                .eq(TicketProductInfo::getState, ProductInfoStateEnum.NORMAL)
                .eq(TicketProductInfo::getIsUpper, ProductInfoUpperEnum.UPPER)
                .eq(TicketProductInfo::getScenicSpotType, ScenicSpotTypeEnum.RAINFOREST)
                .eq(TicketProductInfo::getType, ProductInfoTypeEnum.NOMALPRODUCTS)
                .orderByDesc(TicketProductInfo::getTopTime)
                .orderByDesc(TicketProductInfo::getCreateTime)
        );

        List<TicketProductInfo> rainforestList = parPreTicket(rainforestDbList);

        //查询博物馆
        List<TicketProductInfo> museumDbList = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
                .eq(TicketProductInfo::getState, ProductInfoStateEnum.NORMAL)
                .eq(TicketProductInfo::getIsUpper, ProductInfoUpperEnum.UPPER)
                .eq(TicketProductInfo::getScenicSpotType, ScenicSpotTypeEnum.MUSEUM)
                .eq(TicketProductInfo::getType, ProductInfoTypeEnum.NOMALPRODUCTS)
                .orderByDesc(TicketProductInfo::getTopTime)
                .orderByDesc(TicketProductInfo::getCreateTime)
        );

        List<TicketProductInfo> museumList = parPreTicket(museumDbList);

        LambdaQueryWrapper<TicketProductInfo> compositeQuery = Wrappers.<TicketProductInfo>lambdaQuery();
        //指定查询哪个馆联票
        if (ObjectUtil.isNotNull(param.getScenicSpotType())) {
            compositeQuery.like(TicketProductInfo::getScenicSpotTypes, param.getScenicSpotType().getType());
//            compositeQuery.eq(TicketProductInfo::getScenicSpotType, param.getScenicSpotType());
        }

        //查询联票
        List<TicketProductInfo> compositeDbList = ticketProductInfoMapper.selectList(
                compositeQuery.eq(TicketProductInfo::getState, ProductInfoStateEnum.NORMAL)
                        .eq(TicketProductInfo::getIsUpper, ProductInfoUpperEnum.UPPER)
                        .eq(TicketProductInfo::getType, ProductInfoTypeEnum.COMPOSITEPRODUCT)
                        .orderByDesc(TicketProductInfo::getTopTime)
                        .orderByDesc(TicketProductInfo::getCreateTime)
        );

        List<TicketProductInfo> compositeList = parPreTicket(compositeDbList);

        WechatGetProductInfoResult result = new WechatGetProductInfoResult();
        result.setRecommendList(recommendList);
        result.setOceanList(oceanList);
        result.setRainforestList(rainforestList);
        result.setMuseumList(museumList);
        result.setCompositeList(compositeList);

        return result;
    }

    @Override
    public TicketProductInfoDetailResult wechatGetProductInfoById(ProductInfoGetByIdParam param) {
        TicketProductInfo ticketProductInfo = ticketProductInfoMapper.selectById(param.getId());
        if (ObjectUtil.isNull(ticketProductInfo)) {
            throw new ServiceException("未查询到对应产品信息，请重试");
        }

        List<TicketProductInfoDetail> details = ticketProductInfoDetailMapper.selectList(Wrappers.<TicketProductInfoDetail>lambdaQuery()
                .eq(TicketProductInfoDetail::getProductInfoId, ticketProductInfo.getId())
        );

        return new TicketProductInfoDetailResult(ticketProductInfo, details);
    }

    @Override
    public TicketProductSaleResult salesProductInfo(ProductInfoSalesParam param) {
        try {
            List<TicketProductInfo> productInfos = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
//                    .eq(TicketProductInfo::getState, ProductInfoStateEnum.NORMAL)
//                            .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
            );

            //联票产品id
            List<Integer> unitProductIds = productInfos.stream()
                    .filter(item -> ObjectUtil.equal(item.getType(), ProductInfoTypeEnum.COMPOSITEPRODUCT))
                    .map(TicketProductInfo::getProductId).collect(toList());

            //海洋馆产品id
            List<Integer> oceanProductids = productInfos.stream()
                    .filter(item -> ObjectUtil.equal(item.getScenicSpotType(), ScenicSpotTypeEnum.OCEAN))
                    .filter(item -> ObjectUtil.equal(item.getType(), ProductInfoTypeEnum.NOMALPRODUCTS))
                    .map(TicketProductInfo::getProductId).collect(toList());
            //雨林馆产品id
            List<Integer> rainforestProductIds = productInfos.stream()
                    .filter(item -> ObjectUtil.equal(item.getScenicSpotType(), ScenicSpotTypeEnum.RAINFOREST))
                    .filter(item -> ObjectUtil.equal(item.getType(), ProductInfoTypeEnum.NOMALPRODUCTS))
                    .map(TicketProductInfo::getProductId).collect(toList());
            //博物馆产品id
            List<Integer> museumProductIds = productInfos.stream()
                    .filter(item -> ObjectUtil.equal(item.getScenicSpotType(), ScenicSpotTypeEnum.MUSEUM))
                    .filter(item -> ObjectUtil.equal(item.getType(), ProductInfoTypeEnum.NOMALPRODUCTS))
                    .map(TicketProductInfo::getProductId).collect(toList());

            //过滤未付款和已作废
            List<TicketOrderInfo> orderInfoList = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                    .notIn(TicketOrderInfo::getStatus, OrderInfoStatusEnum.UN_PAID, OrderInfoStatusEnum.INVALID)
                    .ge(ObjectUtil.isNotNull(param.getStartDate()), TicketOrderInfo::getCreateTime, param.getStartDate())
                    .le(ObjectUtil.isNotNull(param.getEndDate()), TicketOrderInfo::getCreateTime, param.getEndDate())
                    .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
            );

            //获取全部订单总数
            Long allOrderCount = new Long(orderInfoList.stream().collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size());

            //获取全部销售金额
            BigDecimal allAmount = orderInfoList.stream().map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            //指定门票类型的销售金额
            BigDecimal amount = new BigDecimal(0);
            //指定门票类型的订单数
            Long orderCount = 0L;
            //购票人数
            Long personNumber = 0L;
            //人均消费金额
            double average = 0.00;

            if (ObjectUtil.isNull(param.getProductType())) {
                //门票类型为空，则设置为全部订单的和
                orderCount = allOrderCount;
                amount = allAmount;
                personNumber = (long) orderInfoList.stream().collect(Collectors.groupingBy(TicketOrderInfo::getMemberId)).size();
                if (personNumber > 0) {
                    average = amount.divide(new BigDecimal(personNumber), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            } else if (ObjectUtil.isNotNull(param.getProductType())) {
                //设置的为联票类型的订单
                if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.UNIT)) {
                    orderCount = (long) orderInfoList.stream()
                            .filter(item -> unitProductIds.contains(item.getProductId()))
                            .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();
                    amount = orderInfoList.stream()
                            .filter(item -> unitProductIds.contains(item.getProductId()))
                            .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    personNumber = (long) orderInfoList.stream()
                            .filter(item -> unitProductIds.contains(item.getProductId()))
                            .collect(Collectors.groupingBy(TicketOrderInfo::getMemberId)).size();

                    if (personNumber > 0) {
                        average = amount.divide(new BigDecimal(personNumber), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                } else {

                    //区别各个景区数据
                    if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.OCEAN)) {
                        orderCount = (long) orderInfoList.stream()
                                .filter(item -> oceanProductids.contains(item.getProductId()))
                                .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();
                        amount = orderInfoList.stream()
                                .filter(item -> oceanProductids.contains(item.getProductId()))
                                .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        personNumber = (long) orderInfoList.stream()
                                .filter(item -> oceanProductids.contains(item.getProductId()))
                                .collect(Collectors.groupingBy(TicketOrderInfo::getMemberId)).size();
                    } else if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.MUSEUM)) {
                        orderCount = (long) orderInfoList.stream()
                                .filter(item -> museumProductIds.contains(item.getProductId()))
                                .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();
                        amount = orderInfoList.stream()
                                .filter(item -> museumProductIds.contains(item.getProductId()))
                                .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        personNumber = (long) orderInfoList.stream()
                                .filter(item -> museumProductIds.contains(item.getProductId()))
                                .collect(Collectors.groupingBy(TicketOrderInfo::getMemberId)).size();
                    } else if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.RAINFOREST)) {
                        orderCount = (long) orderInfoList.stream()
                                .filter(item -> rainforestProductIds.contains(item.getProductId()))
                                .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();
                        amount = orderInfoList.stream()
                                .filter(item -> rainforestProductIds.contains(item.getProductId()))
                                .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                        personNumber = (long) orderInfoList.stream()
                                .filter(item -> rainforestProductIds.contains(item.getProductId()))
                                .collect(Collectors.groupingBy(TicketOrderInfo::getMemberId)).size();
                    }

                    if (personNumber > 0) {
                        average = amount.divide(new BigDecimal(personNumber), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                }
            }

            List<String> days = DateUtil.rangeToList(param.getStartDate(), param.getEndDate(), DateField.DAY_OF_YEAR)
                    .stream().map(item -> DateUtil.format(item, "yyyy-MM-dd")).collect(toList());

            List<Long> orderCountList = new ArrayList<>();
            List<Double> sellAmountList = new ArrayList<>();

            for (int i = 0; i < days.size(); i++) {
                String itemDay = days.get(i);
                BigDecimal sum = null;
                Long count = null;

                if (ObjectUtil.isNull(param.getProductType())) {
                    //获取当前日期的订单金额和
                    sum = orderInfoList.stream().filter(
                            item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                            .map(TicketOrderInfo::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    count = (long) orderInfoList.stream().filter(
                            item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                            .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();


                } else if (ObjectUtil.isNotNull(param.getProductType())) {
                    if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.UNIT)) {
                        sum = orderInfoList.stream()
                                .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                .filter(item -> unitProductIds.contains(item.getProductId()))
                                .map(TicketOrderInfo::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        count = (long) orderInfoList.stream()
                                .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                .filter(item -> unitProductIds.contains(item.getProductId()))
                                .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();

                    } else {
                        //区别各个景区数据
                        if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.OCEAN)) {
                            sum = orderInfoList.stream()
                                    .filter(item -> oceanProductids.contains(item.getProductId()))
                                    .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                    .map(TicketOrderInfo::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            count = (long) orderInfoList.stream()
                                    .filter(item -> oceanProductids.contains(item.getProductId()))
                                    .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                    .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();
                        } else if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.MUSEUM)) {
                            sum = orderInfoList.stream()
                                    .filter(item -> museumProductIds.contains(item.getProductId()))
                                    .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                    .map(TicketOrderInfo::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            count = (long) orderInfoList.stream()
                                    .filter(item -> museumProductIds.contains(item.getProductId()))
                                    .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                    .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();
                        } else if (ObjectUtil.equal(param.getProductType(), ProductInfoAllTypeEnum.RAINFOREST)) {
                            sum = orderInfoList.stream()
                                    .filter(item -> rainforestProductIds.contains(item.getProductId()))
                                    .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                    .map(TicketOrderInfo::getAmount)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                            count = (long) orderInfoList.stream()
                                    .filter(item -> rainforestProductIds.contains(item.getProductId()))
                                    .filter(item -> DateUtil.format(item.getCreateTime(), "yyyy-MM-dd").equals(itemDay))
                                    .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId)).size();
                        }
                    }
                }

                if (ObjectUtil.isNull(sum)) {
                    sum = new BigDecimal(0);
                }

                if (ObjectUtil.isNull(count)) {
                    count = 0L;
                }

                orderCountList.add(count);
                sellAmountList.add(sum.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }

            //统计各类型的订单数
            //海洋馆订单数
            Long oceanOrderCount = (long) orderInfoList.stream()
                    .filter(item -> oceanProductids.contains(item.getProductId()))
                    .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId))
                    .size();

            //雨林馆订单数
            Long rainforestOrderCount = (long) orderInfoList.stream()
                    .filter(item -> rainforestProductIds.contains(item.getProductId()))
                    .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId))
                    .size();

            //博物馆订单数
            Long museumOrderCount = (long) orderInfoList.stream()
                    .filter(item -> museumProductIds.contains(item.getProductId()))
                    .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId))
                    .size();

            //联票订单数
            Long unitOrderCount = (long) orderInfoList.stream()
                    .filter(item -> unitProductIds.contains(item.getProductId()))
                    .collect(Collectors.groupingBy(TicketOrderInfo::getOrdernoOtaordernoRelationId))
                    .size();

            List<TicketProductPieChartResult> productOrderCountList = new ArrayList<>();
            productOrderCountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.OCEAN.getType(), ProductInfoAllTypeEnum.OCEAN.getName(), oceanOrderCount));
            productOrderCountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.RAINFOREST.getType(), ProductInfoAllTypeEnum.RAINFOREST.getName(), rainforestOrderCount));
            productOrderCountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.MUSEUM.getType(), ProductInfoAllTypeEnum.MUSEUM.getName(), museumOrderCount));
            productOrderCountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.UNIT.getType(), ProductInfoAllTypeEnum.UNIT.getName(), unitOrderCount));


            //统计各类型的销售金额
            //海洋馆销售金额
            Double oceanOrderSum = orderInfoList.stream()
                    .filter(item -> oceanProductids.contains(item.getProductId()))
                    .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            //雨林馆销售金额
            Double rainforestOrderSum = orderInfoList.stream()
                    .filter(item -> rainforestProductIds.contains(item.getProductId()))
                    .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            //博物馆销售金额
            Double museumOrderSum = orderInfoList.stream()
                    .filter(item -> museumProductIds.contains(item.getProductId()))
                    .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            //联票销售金额
            Double unitOrderSum = orderInfoList.stream()
                    .filter(item -> unitProductIds.contains(item.getProductId()))
                    .map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            List<TicketProductPieChartResult> productSellAmountList = new ArrayList<>();
            productSellAmountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.OCEAN.getType(), ProductInfoAllTypeEnum.OCEAN.getName(), oceanOrderSum));
            productSellAmountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.RAINFOREST.getType(), ProductInfoAllTypeEnum.RAINFOREST.getName(), rainforestOrderSum));
            productSellAmountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.MUSEUM.getType(), ProductInfoAllTypeEnum.MUSEUM.getName(), museumOrderSum));
            productSellAmountList.add(new TicketProductPieChartResult(ProductInfoAllTypeEnum.UNIT.getType(), ProductInfoAllTypeEnum.UNIT.getName(), unitOrderSum));

            TicketProductSaleResult ticketProductSaleResult = new TicketProductSaleResult();
            ticketProductSaleResult.setTicketProductSaleHeaderResult(new TicketProductSaleHeaderResult(amount, orderCount, personNumber, average));
            ticketProductSaleResult.setDays(days);
            ticketProductSaleResult.setOrderCountList(orderCountList);
            ticketProductSaleResult.setSellAmountList(sellAmountList);
            ticketProductSaleResult.setProductOrderCountList(productOrderCountList);
            ticketProductSaleResult.setProductSellAmountList(productSellAmountList);
            return ticketProductSaleResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("获取数据失败");
        }

    }

    @Override
    public IPage<ProductInfoSaleOrderSumResult> salesProductOrderInfo(ProductInfoSalesOrderParam param) {
        String authSql = param.getDataAuthParam().getAliasCompleteSql("o");
        return ticketProductInfoMapper.getSalesProductOrderInfo(param.page(), param, authSql);
    }

    @Override
    public TicketProductInfoDetailByIdResult getProductInfo(ProductInfoGetParam param) {
        //获取产品信息
        TicketProductInfo result = ticketProductInfoMapper.selectById(param.getId());
        ProductInfoByIdResult ticketOrderInfo = new ProductInfoByIdResult();
        BeanUtil.copyProperties(result, ticketOrderInfo);

        //获取联票枚举值集合
        StringBuffer sb = new StringBuffer();
        if (ObjectUtil.equal(ticketOrderInfo.getType(), ProductInfoTypeEnum.COMPOSITEPRODUCT)) {
            String scenicSpotTypes = ticketOrderInfo.getScenicSpotTypes();
            if (StrUtil.isNotEmpty(scenicSpotTypes)) {
                List<ScenicSpotTypeEnum> scenicSpotTypeEnums = new ArrayList<>();
                String[] split = scenicSpotTypes.split(",");
                for (int j = 0; j < split.length; j++) {
                    int type = Integer.valueOf(split[j]);
                    ScenicSpotTypeEnum scenicSpotTypeEnum = EnumUtil.likeValueOf(ScenicSpotTypeEnum.class, type);
                    scenicSpotTypeEnums.add(scenicSpotTypeEnum);
                    sb.append(scenicSpotTypeEnum.getName() + "+");
                }

                ticketOrderInfo.setScenicSpotTypeEnums(scenicSpotTypeEnums);
            }
        }

        ticketOrderInfo.setTypeStr(sb.length() > 0 ?
                ticketOrderInfo.getType().getName() + "(" + sb.substring(0, sb.length() - 1) + ")"
                : ticketOrderInfo.getType().getName());

        //获取产品详情信息
        List<TicketProductInfoDetail> details = ticketProductInfoDetailMapper.selectList(Wrappers.<TicketProductInfoDetail>lambdaQuery()
                .eq(ObjectUtil.isNotNull(ticketOrderInfo.getId()), TicketProductInfoDetail::getProductInfoId, ticketOrderInfo.getId())
        );
        return new TicketProductInfoDetailByIdResult(ticketOrderInfo, details);
    }

    @Override
    public IPage<ProductInfoResult> listActivityProductInfo(ProductInfoActivityListParam param) {
        //设置获取的产品状态为正常的
        param.setState(ProductInfoStateEnum.NORMAL);

        //查询状态为正常的产品列表
        IPage<TicketProductInfo> ticketProductInfos = ticketProductInfoMapper.selectPage(param.page(),
                Wrappers.<TicketProductInfo>lambdaQuery()
                        .like(StrUtil.isNotBlank(param.getKeyWord()), TicketProductInfo::getProductId, param.getKeyWord())
                        .or().like(StrUtil.isNotBlank(param.getKeyWord()), TicketProductInfo::getProductName, param.getKeyWord())
                        .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        );

        //获取根据符合条件的产品信息id查询产品详情列表
        List<TicketProductInfoDetail> TicketProductInfoDetailList = null;
        if (ObjectUtil.isNotEmpty(ticketProductInfos.getRecords()) && ticketProductInfos.getRecords().size() > 0) {
            LambdaQueryWrapper<TicketProductInfoDetail> detailWrapper = new LambdaQueryWrapper<TicketProductInfoDetail>();
            detailWrapper.in(TicketProductInfoDetail::getProductInfoId, ticketProductInfos.getRecords().stream().map(w -> w.getId()).collect(toList())


            );
            TicketProductInfoDetailList = ticketProductInfoDetailMapper.selectList(detailWrapper);
        } else {
            //没有产品信息直接返回
            return null;
        }

        //保存产品对应的详情(key:产品id, value:对应产品的详情)
        Map<Long, List<TicketProductInfoDetail>> detailMap = new HashMap<>();

        //根据产品ids查询产品详情，迭代成map集合
        if (ObjectUtil.isNotEmpty(TicketProductInfoDetailList) && TicketProductInfoDetailList.size() > 0) {
            for (int i = 0; i < TicketProductInfoDetailList.size(); i++) {
                TicketProductInfoDetail item = TicketProductInfoDetailList.get(i);

                List<TicketProductInfoDetail> productInfoDetails = detailMap.get(item.getProductInfoId());
                if (ObjectUtil.isEmpty(productInfoDetails)) {
                    //map中没有对应产品id的详情，则创建详情集合，将当前的详情加入
                    List<TicketProductInfoDetail> details = new ArrayList<>();
                    details.add(item);
                    detailMap.put(item.getProductInfoId(), details);
                } else {
                    //map中有对应产品id的详情，则在原先基础加当前的详情加入
                    productInfoDetails.add(item);
                    detailMap.put(item.getProductInfoId(), productInfoDetails);
                }
            }
        }

        //将产品信息和详情信息封装成返回对象
        List<ProductInfoResult> records = new ArrayList<>();
        for (int i = 0; i < ticketProductInfos.getRecords().size(); i++) {
            ProductInfoResult item = new ProductInfoResult();
            BeanUtil.copyProperties(ticketProductInfos.getRecords().get(i), item);
            item.setProductInfoDetailList(detailMap.get(item.getId()));

            records.add(item);
        }

        IPage<ProductInfoResult> resultIPage = new Page<>(ticketProductInfos.getCurrent(), ticketProductInfos.getSize(), ticketProductInfos.getTotal());
        resultIPage.setRecords(records);

        return resultIPage;
    }

    @Override
    public void updateProductInfoTop(ProductInfoUpdateTopParam param) {
        TicketProductInfo item = new TicketProductInfo();
        BeanUtil.copyProperties(param, item);

        //当设置置顶的时候，更新置顶时间，不置顶，则设置为null
        if (ObjectUtil.equal(param.getIsTop(), ProductInfoTopEnum.TOP)) {
            item.setTopTime(new Date());
        } else if (ObjectUtil.equal(param.getIsTop(), ProductInfoTopEnum.NOTTOP)) {
            item.setTopTime(null);
        }

        ticketProductInfoMapper.updateById(item);
    }

    @Override
    public void updateProductInfoPlayTime(ProductInfoUpdatePlayTimeParam param) {
        //判断设置自定义日期时需要开始和结束时间
        if (ObjectUtil.equal(param.getIsSetPlayTime(), ProductInfoSetPlayTimeEnum.APPOINT_PLAY_TIME)
                && ObjectUtil.equal(param.getPlayTimeType(), ProductInfoPlayTimeType.CUSTOM_DAY)) {
            if (ObjectUtil.isNull(param.getPlayStartTime()) || ObjectUtil.isNull(param.getPlayEndTime())) {
                throw new ServiceException("自定义可选游玩时间需要设置游玩开始时间和游玩结束时间");
            }
        }

        TicketProductInfo ticketProductInfo = new TicketProductInfo();
        BeanUtil.copyProperties(param, ticketProductInfo);
        ticketProductInfoMapper.updateById(ticketProductInfo);

    }

    @Override
    public void updateProductInfoRecommend(ProductInfoUpdateRecommendParam param) {
        TicketProductInfo item = new TicketProductInfo();
        BeanUtil.copyProperties(param, item);
        ticketProductInfoMapper.updateById(item);
    }

    @Override
    public void updateProductInfoComposite(ProductInfoUpdateCompositeParam param) {
        if (ObjectUtil.equal(param.getType(), ProductInfoTypeEnum.COMPOSITEPRODUCT)) {
            if (StrUtil.isEmpty(param.getScenicSpotTypes())) {
                throw new ServiceException("指定联票时需选择景区");
            }
        }

        TicketProductInfo item = new TicketProductInfo();
        BeanUtil.copyProperties(param, item);

        ticketProductInfoMapper.updateById(item);
    }

    @Override
    public void updateProductInfoBuyIntroduce(ProductInfoUpdateBuyIntroduceParam param) {
        TicketProductInfo item = new TicketProductInfo();
        BeanUtil.copyProperties(param, item);
        ticketProductInfoMapper.updateById(item);
    }

    /**
     * 封装数据(key:客户编号-产品id-产品编号;value:包含产品信息和详情的json对象)
     *
     * @param accountInfo
     * @param products
     * @return
     */
    private static Map<String, JSONObject> parseInterfaceResult(TicketInterfaceAccountInfoResult accountInfo, JSONArray products) {
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
                        accountInfo.getDesignateOrgNames(),
                        accountInfo.getScenicSpotId(),
                        accountInfo.getType(),
                        accountInfo.getScenicSpotName()
                );

                List<TicketProductInfoDetail> productInfoDetails = new ArrayList<>();
                //获取产品详情
                Object calendarPlanData = item.getObj("CalendarPlanData");
                //判断是否存在产品明细详情
                if (ObjectUtil.isNotNull(calendarPlanData)) {
                    JSONArray productDetails = new JSONArray(calendarPlanData);
                    if (ObjectUtil.isNotNull(productDetails) && productDetails.size() > 0) {
                        for (int j = 0; j < productDetails.size(); j++) {
                            JSONObject detail = JSONUtil.parseObj(productDetails.get(j));
                            if (ObjectUtil.isNull(detail) || detail.size() <= 0) {
                                continue;
                            }
                            TicketProductInfoDetail ticketProductInfoDetail = new TicketProductInfoDetail(IdWorker.getId(),
                                    StrUtil.isEmpty(detail.getStr("PriceDate")) ? null : DateUtil.parse(detail.getStr("PriceDate"), "yyyy-MM-dd"),
                                    detail.getBigDecimal("ProductPrice"),
                                    detail.getBigDecimal("ProductSellPrice"),
                                    detail.getBigDecimal("TicketPrice"),
                                    detail.getStr("TotalInventory"),
                                    detail.getStr("UseInventory"),
                                    ticketProductInfo.getId());

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

    @Override
    @Transactional
    public void getNormalInterfaceProductInfo() {
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
                            productInfo.setIsUpper(null);
                            productInfo.setBuyIntroduce(null);

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
                            //设置默认为下架
                            productInfo.setIsUpper(ProductInfoUpperEnum.NOTUPPER);
                            //设置默认购买说明json串,替换最大销售量和最小销售量
                            productInfo.setBuyIntroduce(MdcConstant.PRODUCT_BUY_INTRODUCE.replace("sellMax", productInfo.getSellMax()+"").replace("sellMin", productInfo.getSellMin()+""));
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
                        //设置默认为下架
                        productInfo.setIsUpper(ProductInfoUpperEnum.NOTUPPER);
                        //设置默认购买说明json串
                        productInfo.setBuyIntroduce(MdcConstant.PRODUCT_BUY_INTRODUCE.replace("sellMax", productInfo.getSellMax()+"").replace("sellMin", productInfo.getSellMin()+""));
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

    @Override
    public void updateProductInfoUpper(ProductInfoUpdateUpperParam param) {
        TicketProductInfo item = new TicketProductInfo();
        BeanUtil.copyProperties(param, item);
        ticketProductInfoMapper.updateById(item);
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
                        accountInfo.getDesignateOrgNames(),
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
