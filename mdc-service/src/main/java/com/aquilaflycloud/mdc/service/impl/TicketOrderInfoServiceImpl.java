package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.aquilafly.easypay.enums.FrpCodeEnum;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.common.WhetherEnum;
import com.aquilaflycloud.mdc.enums.easypay.OrderTypeEnum;
import com.aquilaflycloud.mdc.enums.ticket.ChannelInfoStateEnum;
import com.aquilaflycloud.mdc.enums.ticket.OrderInfoStatusEnum;
import com.aquilaflycloud.mdc.enums.ticket.OrderPayStatusEnum;
import com.aquilaflycloud.mdc.extra.docom.component.DoComConfig;
import com.aquilaflycloud.mdc.extra.docom.util.DoComHttpUtil;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.message.TicketOrderErrorEnum;
import com.aquilaflycloud.mdc.model.easypay.EasypayPaymentRecord;
import com.aquilaflycloud.mdc.model.easypay.EasypayRefundRecord;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.ticket.*;
import com.aquilaflycloud.mdc.param.easypay.OrderParam;
import com.aquilaflycloud.mdc.param.easypay.RefundParam;
import com.aquilaflycloud.mdc.param.ticket.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.ticket.*;
import com.aquilaflycloud.mdc.service.TicketChannelInfoService;
import com.aquilaflycloud.mdc.service.TicketOrderInfoService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 订单信息表 服务实现类
 * </p>
 *
 * @author Zengqingjie
 * @since 2019-11-19
 */
@Service
@Slf4j
public class TicketOrderInfoServiceImpl implements TicketOrderInfoService {
    @Resource
    private EasyPayServiceImpl easyPayService;

    @Resource
    private TicketScenicSpotInfoMapper ticketScenicSpotInfoMapper;

    @Resource
    private TicketProductInfoMapper ticketProductInfoMapper;

    @Resource
    private TicketInterfaceAccountInfoMapper ticketInterfaceAccountInfoMapper;

    @Resource
    private TicketOrdernoOtaordernoRelationMapper ticketOrdernoOtaordernoRelationMapper;

    @Resource
    private TicketOrderInfoMapper ticketOrderInfoMapper;

    @Resource
    private TicketProductInfoDetailMapper ticketProductInfoDetailMapper;

    @Resource
    private TicketOrderCustomerInfoMapper ticketOrderCustomerInfoMapper;

    @Resource
    private TicketChannelInfoMapper ticketChannelInfoMapper;

    @Resource
    private MemberInfoMapper memberInfoMapper;

    @Resource
    private TicketChannelInfoService ticketChannelInfoService;

    @Resource
    private TicketVerificateInfoMapper ticketVerificateInfoMapper;

    @Resource
    private EasypayRefundRecordMapper easypayRefundRecordMapper;

    @Resource
    private EasypayPaymentRecordMapper easypayPaymentRecordMapper;

    @Resource
    private TicketOrderProductInfoMapper ticketOrderProductInfoMapper;

    @Resource
    private TicketOrderProductInfoDetailMapper ticketOrderProductInfoDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult<String> wechatCreateOrder(OrderInfoWechatCreateParam param) {
        //21:00不允许下当日的订单
        //获取当天日期和时间
        Date currentDate = new Date();
        String playDate = DateUtil.format(param.getPlayDate(), "yyyy-MM-dd");
        String date = DateUtil.format(currentDate, "yyyy-MM-dd");

        //游玩时间为当前日期，判断是否21:00
        if (StrUtil.equals(playDate, date) && DateUtil.hour(currentDate, true) >= 21) {
            throw new ServiceException("当天营业时间即将结束，线上售票窗口已关闭");
        }

        //存在渠道id，判断渠道信息是否停用，停用则去除渠道id，渠道统计时不计入计算
        if (ObjectUtil.isNotNull(param.getChannelId())) {
            TicketChannelInfo ticketChannelInfo = ticketChannelInfoMapper.selectById(param.getChannelId());

            //渠道信息为空或者停用，则去除渠道id
            if (ObjectUtil.isNull(ticketChannelInfo) || ObjectUtil.equal(ticketChannelInfo.getState(), ChannelInfoStateEnum.STOP)) {
                param.setChannelId(null);
            }
        }

        String payStr = "";
        List<TicketOrderInfo> returnResult = new ArrayList<>();
        //分析返回的订单信息
        TicketOrdernoOtaordernoRelation ticketOrdernoOtaordernoRelation = null;
        DoComConfig doComConfig = null;
        TicketScenicSpotInfo ticketScenicSpotInfo = null;
        try {
            MemberInfoResult requireCurrentMember = MdcUtil.getRequireCurrentMember();

            //获取对应景区的账号信息
            ticketScenicSpotInfo = ticketScenicSpotInfoMapper.selectById(param.getScenicSpotId());
            if (ObjectUtil.isNull(ticketScenicSpotInfo)) {
                throw new ServiceException("查询不到对应景区的信息，请重试");
            }

            //获取产品对应的账号信息
            TicketInterfaceAccountInfo account = ticketInterfaceAccountInfoMapper.selectById(ticketScenicSpotInfo.getAccountId());

            if (ObjectUtil.isNull(account)) {
                throw new ServiceException("查询不到景区对应的账号信息，请重试");
            }

            //创建账号信息对象
            doComConfig = new DoComConfig(account.getMerchantCode(), account.getInterfaceAccount(), account.getSecret(), account.getBaseUrl());

            Map<String, Object> params = new HashMap<>();
            params.put("OTAOrderNo", IdWorker.getId());
            params.put("PlayDate", DateUtil.format(param.getPlayDate(), "yyyy-MM-dd"));

            //保存产品集合
            JSONArray goodsDetails = new JSONArray();
            Map<Integer, List<TicketOrderCustomerInfo>> customerMap = new HashMap<>();
            List<OrderInfoDetailInfo> orderInfoDetailInfoList = param.getOrderInfoDetailInfoList();

            BigDecimal amount = BigDecimal.ZERO;
            for (OrderInfoDetailInfo infoDetailInfo : orderInfoDetailInfoList) {
                //单个产品信息封装
                JSONObject goodsDetailInfo = new JSONObject();
                goodsDetailInfo.set("ProductId", infoDetailInfo.getProductId());
                goodsDetailInfo.set("ProductCount", infoDetailInfo.getProductCount());
                goodsDetailInfo.set("ProductPrice", infoDetailInfo.getProductPrice());
                goodsDetailInfo.set("ProductSellPrice", infoDetailInfo.getProductSellPrice());
                amount = NumberUtil.mul(infoDetailInfo.getProductSellPrice(), infoDetailInfo.getProductCount());
                //封装顾客信息
                JSONArray customerList = new JSONArray();
                //保存出行人信息
                List<OrderInfoCustomerInfo> customerInfos = infoDetailInfo.getCustomerInfos();
                OrderInfoCustomerInfo customerInfo = infoDetailInfo.getCustomerInfo();

                JSONObject customer = new JSONObject();
                customer.set("CustomerName", customerInfo.getName());
                customer.set("CustomerPhone", customerInfo.getPhone());
                /*customer.put("CustomerCardId", customerData.getCardId());
                customer.put("CustomerImg", customerData.getCustomerImg());*/

                //保存出行人信息
                List<TicketOrderCustomerInfo> customerInfoList = new ArrayList<>();
                if (ObjectUtil.isNotNull(customerInfos) && customerInfos.size() > 0) {
                    for (OrderInfoCustomerInfo info : customerInfos) {
                        TicketOrderCustomerInfo item = new TicketOrderCustomerInfo();
                        item.setIdCardName(info.getName());
                        item.setMobile(info.getPhone());
                        item.setIdCardNo(info.getCardId());
                        item.setMemberId(requireCurrentMember.getId());
                        item.setProductId(infoDetailInfo.getProductId());
                        item.setCreatorOrgIds(account.getCreatorOrgIds());
                        item.setCreatorOrgNames(account.getCreatorOrgNames());
                        item.setDesignateOrgIds(account.getDesignateOrgIds());
                        item.setDesignateOrgNames(account.getDesignateOrgNames());
                        customerInfoList.add(item);
                    }
                }
                customerList.add(customer);
                goodsDetailInfo.set("CustomerData", customerList);
                customerMap.put(infoDetailInfo.getProductId(), customerInfoList);
                goodsDetails.add(goodsDetailInfo);
            }
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ServiceException("支付金额异常");
            }
            params.put("Amount", amount);
            params.put("GoodsDetails", goodsDetails);

            //调用第三方接口，创建订单没有订单详情查询
            JSONObject result = DoComHttpUtil.createOrder(doComConfig, params);
            String jsonStr = null;
            log.info("TicketOrderInfoServiceImpl->wechatCreate调用道控接口返回结果：" + result.toString());
            if (StrUtil.equals(result.getStr("isTrue"), "true") && StrUtil.equals(result.getStr("code"), "200")) {
                //处理成功结果
                jsonStr = result.getStr("jsonStr");

            } else {
                throw new ServiceException(result.getStr("msg"));
            }


            if (StrUtil.isNotEmpty(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Long id = IdWorker.getId();
                //保存商户订单号与OTA订单号关系对象(默认状态未付款)
                ticketOrdernoOtaordernoRelation = new TicketOrdernoOtaordernoRelation(id, jsonObject.getStr("OrderNo"), jsonObject.getStr("OTAOrderNo"), param.getChannelId(),
                        account.getCreatorOrgIds(), account.getCreatorOrgNames(), account.getDesignateOrgIds(), account.getDesignateOrgNames()
                        , requireCurrentMember.getId(), requireCurrentMember.getNickName(), OrderPayStatusEnum.UN_PAID);

                int relationCount = ticketOrdernoOtaordernoRelationMapper.insert(ticketOrdernoOtaordernoRelation);

                if (relationCount <= 0) {
                    throw new ServiceException("保存订单关系表失败，请重试");
                }

            } else {
                throw new ServiceException("创建订单接口调用信息为空，请重试");
            }

            //调用订单详情接口获取该订单详情
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("OTAOrderNo", ticketOrdernoOtaordernoRelation.getOtaOrderNo());
            JSONObject jsonObject = DoComHttpUtil.queryOrder(doComConfig, queryParams);
            if (StrUtil.equals(result.getStr("isTrue"), "true") && StrUtil.equals(result.getStr("code"), "200")) {
                JSONArray orderInfoDetail = jsonObject.getJSONArray("jsonStr");
                //封装接口返回的订单数据
                List<TicketOrderInfo> orderInfoList = parOrderInfos(orderInfoDetail);

                //支付参数
                OrderParam orderParam = new OrderParam();
                StringBuffer sbProductNames = new StringBuffer();

                for (int i = 0; i < orderInfoList.size(); i++) {
                    TicketOrderInfo ticketOrderInfo = orderInfoList.get(i);
                    //默认设置为未支付
                    ticketOrderInfo.setPlayDate(param.getPlayDate());
                    ticketOrderInfo.setOrdernoOtaordernoRelationId(ticketOrdernoOtaordernoRelation.getId());
                    ticketOrderInfo.setMemberId(MdcUtil.getRequireCurrentMemberId());
                    ticketOrderInfo.setChannelId(param.getChannelId());
                    ticketOrderInfo.setCreatorOrgIds(account.getCreatorOrgIds());
                    ticketOrderInfo.setCreatorOrgNames(account.getCreatorOrgNames());
                    ticketOrderInfo.setDesignateOrgIds(account.getDesignateOrgIds());
                    ticketOrderInfo.setDesignateOrgNames(account.getDesignateOrgNames());

                    List<TicketOrderCustomerInfo> customerInfos = customerMap.get(ticketOrderInfo.getProductId());

                    //保存出行人信息到库
                    if (ObjectUtil.isNotNull(customerInfos) && customerInfos.size() > 0) {
                        for (int j = 0; j < customerInfos.size(); j++) {
                            TicketOrderCustomerInfo ticketOrderCustomerInfo = customerInfos.get(j);
                            ticketOrderCustomerInfo.setOrdernoOtaordernoRelationId(ticketOrdernoOtaordernoRelation.getId());
                            int insertCount = ticketOrderCustomerInfoMapper.insert(ticketOrderCustomerInfo);
                        }
                    }

                    /**
                     * 保存产品快照start
                     */
                    //查询产品信息
                    Integer productId = ticketOrderInfo.getProductId();
                    List<TicketProductInfo> productInfos = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery().eq(TicketProductInfo::getProductId, productId));
                    //一个订单对应一个产品
                    if (ObjectUtil.isNull(productInfos) || productInfos.size() != 1) {
                        throw new ServiceException("订单中的门票查询到多条记录");
                    }

                    //创建产品信息对象
                    TicketOrderProductInfo ticketOrderProductInfo = new TicketOrderProductInfo();
                    TicketProductInfo ticketProductInfo = productInfos.get(0);
                    BeanUtil.copyProperties(ticketProductInfo, ticketOrderProductInfo, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
                    ticketOrderProductInfo.setOrderId(ticketOrderInfo.getId());
                    ticketOrderProductInfo.setProductInfoId(ticketProductInfo.getId());
                    ticketOrderProductInfo.setId(null);

                    //查询产品详情信息
                    List<TicketProductInfoDetail> details = ticketProductInfoDetailMapper.selectList(Wrappers.<TicketProductInfoDetail>lambdaQuery().eq(TicketProductInfoDetail::getProductInfoId, ticketProductInfo.getId()));

                    List<TicketOrderProductInfoDetail> OrderDetails = new ArrayList<>();
                    if (ObjectUtil.isNotNull(details) && details.size() > 0) {
                        for (int j = 0; j < details.size(); j++) {
                            TicketProductInfoDetail item = details.get(j);
                            TicketOrderProductInfoDetail orderProductInfoDetail = new TicketOrderProductInfoDetail();
                            BeanUtil.copyProperties(item, orderProductInfoDetail);
                            orderProductInfoDetail.setOrderId(ticketOrderInfo.getId());
                            orderProductInfoDetail.setProductDetailId(item.getId());
                            orderProductInfoDetail.setId(null);
                            OrderDetails.add(orderProductInfoDetail);
                        }
                    }


                    //保存产品信息和产品详情信息
                    int orderProductInfoCount = ticketOrderProductInfoMapper.insert(ticketOrderProductInfo);
                    if (orderProductInfoCount <= 0) {
                        throw new ServiceException("保存订单产品快照失败");
                    }

                    if (OrderDetails.size() > 0) {
                        int orderProductInfoDetailCount = ticketOrderProductInfoDetailMapper.normalInsertAllBatch(OrderDetails);
                    }

                    /**
                     * 保存产品快照end
                     */

                    int orderInfoCount = ticketOrderInfoMapper.insert(ticketOrderInfo);

                    sbProductNames.append(ticketOrderInfo.getProductName() + ",");

                    if (orderInfoCount > 0) {
                        returnResult.add(ticketOrderInfo);
                    } else {
                        throw new ServiceException("保存订单失败，请重试");
                    }

                }

                //设置支付参数
                String name = ticketScenicSpotInfo.getName();
                String productNames = StrUtil.isNotBlank(sbProductNames.toString()) ? sbProductNames.substring(0, sbProductNames.length() - 1) : "";
                String orderNo = IdWorker.getIdStr();
                orderParam.setId(ticketOrdernoOtaordernoRelation.getId());
                orderParam.setOrderNo(orderNo);
                orderParam.setFrpCode(FrpCodeEnum.APPLET_PAY);
                orderParam.setAmount(amount);
                orderParam.setAppId(requireCurrentMember.getWxAppId());
                orderParam.setOpenId(requireCurrentMember.getOpenId());
                orderParam.setProductName(name + ":" + productNames);

                payStr = easyPayService.orderTicket(orderParam);

            } else {
                throw new ServiceException("订单详情接口调用信息为空，请重试");
            }
        } catch (IOException e) {
            //抛出异常，释放订单
            if (ObjectUtil.isNotNull(ticketOrdernoOtaordernoRelation)) {
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("OTAOrderNo", ticketOrdernoOtaordernoRelation.getOtaOrderNo());
                    JSONObject result = DoComHttpUtil.releaseOrder(doComConfig, params);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            log.error("创建订单失败，异常信息：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return new BaseResult<String>().setResult(payStr);
    }

    @Transactional
    @Override
    public void wechatCreateAndPayOrder(OrderInfoWechatCreateAndPayParam param) {
        try {
            MemberInfoResult requireCurrentMember = MdcUtil.getRequireCurrentMember();

            //获取对应景区的账号信息
            TicketScenicSpotInfo ticketScenicSpotInfo = ticketScenicSpotInfoMapper.selectById(param.getScenicSpotId());
            if (ObjectUtil.isNull(ticketScenicSpotInfo)) {
                throw new ServiceException("查询不到对应景区的信息，请重试");
            }

            //获取产品对应的账号信息
            TicketInterfaceAccountInfo account = ticketInterfaceAccountInfoMapper.selectById(ticketScenicSpotInfo.getAccountId());

            if (ObjectUtil.isNull(account)) {
                throw new ServiceException("查询不到景区对应的账号信息，请重试");
            }

            //创建账号信息对象
            DoComConfig doComConfig = new DoComConfig(account.getMerchantCode(), account.getInterfaceAccount(), account.getSecret(), account.getBaseUrl());

            Map<String, Object> params = new HashMap<>();
            params.put("OTAOrderNo", IdWorker.getId());
            params.put("PlayDate", DateUtil.format(param.getPlayDate(), "yyyy-MM-dd"));
            params.put("Amount", param.getAmount());

            //保存产品集合
            JSONArray goodsDetails = new JSONArray();
            Map<Integer, List<TicketOrderCustomerInfo>> customerMap = new HashMap<>();
            List<OrderInfoDetailInfo> orderInfoDetailInfoList = param.getOrderInfoDetailInfoList();
            for (int i = 0; i < orderInfoDetailInfoList.size(); i++) {
                //单个产品信息封装
                JSONObject goodsDetailInfo = new JSONObject();
                OrderInfoDetailInfo orderInfoDetailInfo = orderInfoDetailInfoList.get(i);
                goodsDetailInfo.set("ProductId", orderInfoDetailInfo.getProductId());
                goodsDetailInfo.set("ProductCount", orderInfoDetailInfo.getProductCount());
                goodsDetailInfo.set("ProductPrice", orderInfoDetailInfo.getProductPrice());
                goodsDetailInfo.set("ProductSellPrice", orderInfoDetailInfo.getProductSellPrice());

                //封装顾客信息
                JSONArray customerList = new JSONArray();
                //保存出行人信息
                List<OrderInfoCustomerInfo> customerInfos = orderInfoDetailInfo.getCustomerInfos();
                OrderInfoCustomerInfo customerInfo = orderInfoDetailInfo.getCustomerInfo();

                JSONObject customer = new JSONObject();
                customer.set("CustomerName", customerInfo.getName());
                customer.set("CustomerPhone", customerInfo.getPhone());
                /*customer.put("CustomerCardId", customerData.getCardId());
                customer.put("CustomerImg", customerData.getCustomerImg());*/

                //保存出行人信息
                List<TicketOrderCustomerInfo> customerInfoList = new ArrayList<>();
                if (ObjectUtil.isNotNull(customerInfos) && customerInfos.size() > 0) {
                    for (int j = 0; j < customerInfos.size(); j++) {
                        TicketOrderCustomerInfo item = new TicketOrderCustomerInfo();
                        item.setIdCardName(customerInfos.get(j).getName());
                        item.setMobile(customerInfos.get(j).getPhone());
                        item.setMemberId(requireCurrentMember.getId());
                        item.setProductId(orderInfoDetailInfo.getProductId());
                    }
                }

                customerList.add(customer);
                goodsDetailInfo.set("CustomerData", customerList);
                customerMap.put(orderInfoDetailInfo.getProductId(), customerInfoList);
                goodsDetails.add(goodsDetailInfo);
            }

            params.put("GoodsDetails", goodsDetails);

            //调用第三方接口
            JSONObject result = DoComHttpUtil.createAndPayOrder(doComConfig, params);
            String jsonStr = null;
            log.info("TicketOrderInfoServiceImpl->wechatCreateAndPay调用道控接口返回结果：" + result.toString());
            if (StrUtil.equals(result.getStr("isTrue"), "true") && StrUtil.equals(result.getStr("code"), "200")) {
                //处理成功结果
                jsonStr = result.getStr("jsonStr");

            } else {
                throw new ServiceException(result.getStr("msg"));
            }

            if (StrUtil.isNotEmpty(jsonStr)) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                Long id = IdWorker.getId();
                //商户订单号与OTA订单号关系对象
                TicketOrdernoOtaordernoRelation ticketOrdernoOtaordernoRelation = new TicketOrdernoOtaordernoRelation(
                        id, jsonObject.getStr("OrderNo"), jsonObject.getStr("OTAOrderNo"),
                        param.getChannelId(), account.getCreatorOrgIds(), account.getCreatorOrgNames(),
                        account.getDesignateOrgIds(), account.getDesignateOrgNames(),
                        requireCurrentMember.getId(), requireCurrentMember.getNickName(), OrderPayStatusEnum.UN_PAID);
                int relationCount = ticketOrdernoOtaordernoRelationMapper.insert(ticketOrdernoOtaordernoRelation);

                JSONArray orderInfos = jsonObject.getJSONArray("OrderInfo");
                List<TicketOrderInfo> orderInfoList = parOrderInfos(orderInfos);
                for (int i = 0; i < orderInfoList.size(); i++) {
                    TicketOrderInfo ticketOrderInfo = orderInfoList.get(i);
                    ticketOrderInfo.setOrdernoOtaordernoRelationId(id);
                    ticketOrderInfo.setChannelId(param.getChannelId());

                    List<TicketOrderCustomerInfo> customerInfos = customerMap.get(ticketOrderInfo.getProductId());

                    //保存出行人信息到库
                    if (ObjectUtil.isNotNull(customerInfos) && customerInfos.size() > 0) {
                        for (int j = 0; j < customerInfos.size(); j++) {
                            TicketOrderCustomerInfo ticketOrderCustomerInfo = customerInfos.get(j);
                            ticketOrderCustomerInfo.setOrdernoOtaordernoRelationId(ticketOrdernoOtaordernoRelation.getId());
                            int insertCount = ticketOrderCustomerInfoMapper.insert(ticketOrderCustomerInfo);
                        }
                    }

                    int orderInfoCount = ticketOrderInfoMapper.insert(ticketOrderInfo);
                }

            } else {
                throw new ServiceException("创建并支付订单接口调用信息为空，请重试");
            }

        } catch (IOException e) {
            log.error("创建并支付订单失败，异常信息：" + e.getMessage());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wechatReleaseOrder(OrderInfoWechatReleaseParam param) {
        try {
            //查询订单详情获取账号信息
            TicketOrderInfo ticketOrderInfo = ticketOrderInfoMapper.selectById(param.getId());
            if (ObjectUtil.isNotEmpty(ticketOrderInfo)) {

                List<TicketOrderInfo> orderInfoList = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                        .eq(TicketOrderInfo::getOrdernoOtaordernoRelationId, ticketOrderInfo.getOrdernoOtaordernoRelationId()));

                if (ObjectUtil.isNull(orderInfoList) || orderInfoList.size() >= 2) {
                    throw new ServiceException("订单关系id对应到多张明细订单");
                }

                //获取账号信息
                TicketInterfaceAccountInfo ticketInterfaceAccountInfo = ticketInterfaceAccountInfoMapper.selectOne(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery().eq(TicketInterfaceAccountInfo::getMerchantCode, ticketOrderInfo.getMerchantCode()));
                //设置调用账号信息
                DoComConfig doComConfig = new DoComConfig(ticketInterfaceAccountInfo.getMerchantCode(), ticketInterfaceAccountInfo.getInterfaceAccount(), ticketInterfaceAccountInfo.getSecret(), ticketInterfaceAccountInfo.getBaseUrl());
                Map<String, Object> params = new HashMap<>();
                params.put("OTAOrderNo", ticketOrderInfo.getOutOrderNo());
                JSONObject result = DoComHttpUtil.releaseOrder(doComConfig, params);

                if (StrUtil.equals(result.getStr("isTrue"), "true") && StrUtil.equals(result.getStr("code"), "200")) {
                    //订单关系更新参数
                    TicketOrdernoOtaordernoRelation relation = new TicketOrdernoOtaordernoRelation();
                    relation.setId(ticketOrderInfo.getOrdernoOtaordernoRelationId());
//                    relation.setPayStatus(OrderPayStatusEnum.CANCELLING);
                    relation.setPayStatus(OrderPayStatusEnum.CANCELLED);
                    //订单更新参数
                    TicketOrderInfo ticketOrderInfoParam = new TicketOrderInfo();
                    ticketOrderInfoParam.setId(param.getId());
//                    ticketOrderInfoParam.setStatus(OrderInfoStatusEnum.CANCELING);
                    ticketOrderInfoParam.setStatus(OrderInfoStatusEnum.INVALID);

                    int relationCount = ticketOrdernoOtaordernoRelationMapper.updateById(relation);
                    int count = ticketOrderInfoMapper.updateById(ticketOrderInfoParam);
                    if (relationCount <= 0 || count <= 0) {
                        throw new ServiceException("释放订单失败");
                    }
                } else {
                    throw new ServiceException(result.getStr("msg"));
                }
            } else {
                throw new ServiceException("未查询到对应的订单信息，请重试");
            }
        } catch (IOException e) {
            throw new ServiceException("释放订单失败，请重试");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wechatPayOrder(OrderInfoWechatPayParam param) throws IOException {
        //查询订单详情获取账号信息
        TicketOrdernoOtaordernoRelation ticketOrdernoOtaordernoRelation = ticketOrdernoOtaordernoRelationMapper.selectById(param.getId());
        //判断为空和订单号是否跟查询出来的记录一致
        if (ObjectUtil.isNull(ticketOrdernoOtaordernoRelation)) {
            throw new ServiceException("不存在对应的订单关系数据");
        }

        //根据其中一条明细获取订单号
        List<TicketOrderInfo> ticketOrderInfos = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                .eq(TicketOrderInfo::getOrdernoOtaordernoRelationId, param.getId())
        );

        if (ObjectUtil.isNull(ticketOrderInfos) || ticketOrderInfos.size() >= 2) {
            throw new ServiceException("不存在对应的订单数据或多条订单明细");
        }

        //获取账号信息
        List<TicketInterfaceAccountInfo> ticketInterfaceAccountInfos = ticketInterfaceAccountInfoMapper.selectList(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery().eq(TicketInterfaceAccountInfo::getMerchantCode, ticketOrderInfos.get(0).getMerchantCode()));

        if (ObjectUtil.isNull(ticketInterfaceAccountInfos) || ticketInterfaceAccountInfos.size() <= 0 || ticketInterfaceAccountInfos.size() >= 2) {
            throw new ServiceException("获取的账号信息有多个，请重试");
        }

        TicketInterfaceAccountInfo ticketInterfaceAccountInfo = ticketInterfaceAccountInfos.get(0);
        //设置调用账号信息
        DoComConfig doComConfig = new DoComConfig(ticketInterfaceAccountInfo.getMerchantCode(), ticketInterfaceAccountInfo.getInterfaceAccount(), ticketInterfaceAccountInfo.getSecret(), ticketInterfaceAccountInfo.getBaseUrl());
        Map<String, Object> params = new HashMap<>();
        params.put("OTAOrderNo", ticketOrderInfos.get(0).getOutOrderNo());
        JSONObject result = DoComHttpUtil.payOrder(doComConfig, params);

        Date currentDate = new Date();
        //付款回调对象
        Boolean isSuccess = param.getIsSuccess();
        //返回支付|已支付订单信息
        if ((StrUtil.equals(result.getStr("isTrue"), "true") && StrUtil.equals(result.getStr("code"), "200")
                || (StrUtil.equals(result.getStr("isTrue"), "false") && StrUtil.equals(result.getStr("code"), "209")))) {
            System.out.println("jsonStr:" + result.getJSONArray("jsonStr"));
            JSONArray orderInfos = result.getJSONArray("jsonStr");

            TicketOrdernoOtaordernoRelation relation = new TicketOrdernoOtaordernoRelation();
            relation.setId(param.getId());
            if (isSuccess) {
                relation.setPayStatus(OrderPayStatusEnum.PAID);
            } else {
                relation.setPayStatus(OrderPayStatusEnum.FAIL_PAID);
            }

            relation.setPaymentType(param.getPaymentType());
            relation.setPayTime(currentDate);

            //更新订单关系表
            int count = ticketOrdernoOtaordernoRelationMapper.updateById(relation);
//            int count = ticketOrdernoOtaordernoRelationMapper.updateRelationById(relation);

            if (count <= 0) {
                throw new ServiceException("更新订单关系表失败！");
            }

            //更新订单明细
            List<TicketOrderInfo> orderInfoList = parPayOrderInfos(orderInfos);

            for (int i = 0; i < orderInfoList.size(); i++) {
                TicketOrderInfo item = orderInfoList.get(i);
                if (isSuccess) {
                    item.setStatus(OrderInfoStatusEnum.PAID);
                } else {
                    item.setStatus(OrderInfoStatusEnum.FAIL_PAID);
                }
                item.setPaymentType(param.getPaymentType());
                item.setPayTime(currentDate);
                int updateCount = ticketOrderInfoMapper.update(item, Wrappers.<TicketOrderInfo>lambdaQuery()
                        .eq(StrUtil.isNotBlank(item.getMerchantCode()), TicketOrderInfo::getMerchantCode, item.getMerchantCode())
                        .eq(ObjectUtil.isNotNull(item.getProductId()), TicketOrderInfo::getProductId, item.getProductId())
                        .eq(StrUtil.isNotBlank(item.getOrderNo()), TicketOrderInfo::getOrderNo, item.getOrderNo())
                        .eq(StrUtil.isNotBlank(item.getOutOrderNo()), TicketOrderInfo::getOutOrderNo, item.getOutOrderNo())
                        .eq(StrUtil.isNotBlank(item.getEcode()), TicketOrderInfo::getEcode, item.getEcode())
                );

//                int updateCount = ticketOrderInfoMapper.updateOrderInfo(item);
                if (updateCount <= 0) {
                    throw new ServiceException("更新订单表失败！");
                }
            }
        } else {
            throw new ServiceException(result.getStr("msg"));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void wechatRefundOrder(OrderInfoWechatRefundParam param) {
        try {
            //通过ota订单号查询商户编号
            List<TicketOrderInfo> ticketOrderInfos = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                    .eq(StrUtil.isNotBlank(param.getOtaOrderNo()), TicketOrderInfo::getOutOrderNo, param.getOtaOrderNo())
            );

            if (ObjectUtil.isNull(ticketOrderInfos) || ticketOrderInfos.size() >= 2) {
                throw new ServiceException("未查询到对应订单数据或存在多条订单详情，请重试");
            }


            /*//获取游玩日期
            Date playDate = ticketOrderInfos.get(0).getStartDate();
            String playDateStr = DateUtil.format(playDate, "yyyy-MM-dd") + " 22:30:00";
            DateTime parse = DateUtil.parse(playDateStr, "yyyy-MM-dd HH:mm:ss");

            //游玩当天22:30前才能申请退款
            if (current.getTime() > parse.getTime()) {
                throw new ServiceException("您已超过门票规定的退款有效期！");
            }*/

            //求和退款金额
            //组装退款参数
            BigDecimal amount = ticketOrderInfos.stream().map(TicketOrderInfo::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            TicketOrdernoOtaordernoRelation relation = ticketOrdernoOtaordernoRelationMapper.selectById(ticketOrderInfos.get(0).getOrdernoOtaordernoRelationId());

            if (ObjectUtil.isNull(relation)) {
                throw new ServiceException("未查询到对应订单关系数据，请重试");
            }

            //封装退款参数
            Long id = relation.getId();

            //查询付款成功的订单号
            List<EasypayPaymentRecord> easypayPaymentRecords = easypayPaymentRecordMapper.selectList(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                    .eq(EasypayPaymentRecord::getOrderId, id)
                    .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.TICKET)
                    .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
                    .orderByDesc(EasypayPaymentRecord::getCreateTime)
                    .last("limit 1")
            );

            if (ObjectUtil.isNull(easypayPaymentRecords) || easypayPaymentRecords.size() <= 0) {
                throw new ServiceException("未查询到付款订单数据，请重试");
            }

            if (easypayPaymentRecords.size() >= 2) {
                throw new ServiceException("查询到多条付款订单数据，请联系管理员");
            }

            //根据商户编号获取账号信息
            List<TicketInterfaceAccountInfo> accountInfoList = ticketInterfaceAccountInfoMapper.selectList(Wrappers.<TicketInterfaceAccountInfo>lambdaQuery()
                    .eq(TicketInterfaceAccountInfo::getMerchantCode, ticketOrderInfos.get(0).getMerchantCode())
            );

            if (ObjectUtil.isNull(accountInfoList)) {
                throw new ServiceException("未查询到对应订单账号信息，请重试");
            }

            if (accountInfoList.size() >= 2) {
                throw new ServiceException("查询到对应订单的账号信息有多个，请重试");
            }

            //封装账号信息对象
            TicketInterfaceAccountInfo accountInfo = accountInfoList.get(0);
            DoComConfig doComConfig = new DoComConfig(accountInfo.getMerchantCode(), accountInfo.getInterfaceAccount(), accountInfo.getSecret(), accountInfo.getBaseUrl());

            //封装请求数据
            Map<String, Object> params = new HashMap<>();

            //封装购买产品数据
            JSONArray goodsDetails = new JSONArray();
            for (int i = 0; i < ticketOrderInfos.size(); i++) {
                TicketOrderInfo orderInfoItem = ticketOrderInfos.get(i);
                JSONObject detail = new JSONObject();
                detail.set("ECode", orderInfoItem.getEcode());
                detail.set("ProductCount", orderInfoItem.getOrderQty());
                goodsDetails.add(detail);
            }

            params.put("OTAOrderNo", relation.getOtaOrderNo());
            params.put("GoodsDetails", goodsDetails);

            JSONObject jsonObject = DoComHttpUtil.refundOrder(doComConfig, params);

            if ("true".equals(jsonObject.getStr("isTrue")) && "200".equals(jsonObject.getStr("code"))) {
                //更新订单关系表
                TicketOrderInfo updateOrderInfoItem = new TicketOrderInfo();
                updateOrderInfoItem.setId(ticketOrderInfos.get(0).getId());
                updateOrderInfoItem.setStatus(OrderInfoStatusEnum.REFUNDING);
                int orderCount = ticketOrderInfoMapper.updateById(updateOrderInfoItem);

                if (orderCount <= 0) {
                    throw new ServiceException("更新订单关系状态不成功");
                }

                //更新订单明细表
                TicketOrdernoOtaordernoRelation updateRelationItem = new TicketOrdernoOtaordernoRelation();
                updateRelationItem.setId(relation.getId());
                updateRelationItem.setPayStatus(OrderPayStatusEnum.REFUNDING);

                int relationCount = ticketOrdernoOtaordernoRelationMapper.updateById(updateRelationItem);

                if (relationCount <= 0) {
                    throw new ServiceException("更新订单明细状态失败");
                }

                //封装退款参数
                String refundOrderNo = IdWorker.getIdStr();
                RefundParam refundParam = new RefundParam();
                refundParam.setId(id);
                refundParam.setPayType(easypayPaymentRecords.get(0).getPayType());
                refundParam.setOrderNo(easypayPaymentRecords.get(0).getOrderNo());
                refundParam.setRefundAmount(amount);
                refundParam.setRefundOrderNo(refundOrderNo);
                refundParam.setAppId(easypayPaymentRecords.get(0).getAppId());

                easyPayService.refundTicketOrder(refundParam);
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "207".equals(jsonObject.getStr("code"))) {
                //10302:订单号重复;
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10302.getErrorMeta().getException();
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "208".equals(jsonObject.getStr("code"))) {
                //10303:产品不符合售卖规则;
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10303.getErrorMeta().getException();
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "209".equals(jsonObject.getStr("code"))) {
                //10304:订单已支付;
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10304.getErrorMeta().getException();
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "210".equals(jsonObject.getStr("code"))) {
                //10305:订单号不存在;
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10305.getErrorMeta().getException();
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "211".equals(jsonObject.getStr("code"))) {
                //10306:凭证码不存在;
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10306.getErrorMeta().getException();
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "212".equals(jsonObject.getStr("code"))) {
                //10307:当前门票状态不可退;
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10307.getErrorMeta().getException();
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "213".equals(jsonObject.getStr("code"))) {
                //10308:产品不符合退款规则;
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10308.getErrorMeta().getException();
            } else if ("false".equals(jsonObject.getStr("isTrue")) && "214".equals(jsonObject.getStr("code"))) {
                //10309:订单不可退(未支付);
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10309.getErrorMeta().getException();
            } else {
                //103010:系统错误，请联系景区管理人员
                throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_103010.getErrorMeta().getException();
            }

        } catch (Exception e) {
            log.error("订单退款失败：", e);
            //103011:订单退款失败，请联系管理员
            throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_103011.getErrorMeta().getException();
//            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<WechatTicketOrderInfoListResult> wechatOrderInfos() {
        MemberInfoResult currentMember = MdcUtil.getRequireCurrentMember();
        DateTime date = DateUtil.date();

        //获取当前登陆的会员订单信息
        List<TicketOrderInfo> orderInfoList = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                .eq(TicketOrderInfo::getMemberId, currentMember.getId())
                .orderByDesc(TicketOrderInfo::getCreateTime)
        );

        //获取现在产品详细信息
        Map<Integer, TicketProductInfo> productInfoMap = new HashMap<>();
        if (ObjectUtil.isNotNull(orderInfoList) && orderInfoList.size() > 0) {
            List<Integer> productIds = orderInfoList.stream().filter(distinctByKey(item -> item.getProductId())).map(item -> item.getProductId()).collect(toList());
            List<TicketProductInfo> productInfos = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
                    .in(TicketProductInfo::getProductId, productIds)
            );

            for (int i = 0; i < productInfos.size(); i++) {
                TicketProductInfo ticketProductInfo = productInfos.get(i);
                productInfoMap.put(ticketProductInfo.getProductId(), ticketProductInfo);
            }
        }

        //获取订单产品快照信息
        Map<Long, TicketOrderProductInfo> orderProductInfoMap = new HashMap<>();
        if (ObjectUtil.isNotNull(orderInfoList) && orderInfoList.size() > 0) {
            List<Long> orderIds = orderInfoList.stream().map(item -> item.getId()).collect(toList());

            List<TicketOrderProductInfo> orderProductInfos = ticketOrderProductInfoMapper.selectList(Wrappers.<TicketOrderProductInfo>lambdaQuery()
                    .in(TicketOrderProductInfo::getOrderId, orderIds)
            );

            for (int i = 0; i < orderProductInfos.size(); i++) {
                TicketOrderProductInfo item = orderProductInfos.get(i);
                orderProductInfoMap.put(item.getOrderId(), item);
            }
        }

        List<WechatTicketOrderInfoListResult> resultList = new ArrayList<>();

        if (ObjectUtil.isNotNull(orderInfoList) && orderInfoList.size() > 0) {
            for (int i = 0; i < orderInfoList.size(); i++) {
                TicketOrderInfo item = orderInfoList.get(i);

                //当前时间-创建时间
                long diff = (date.getTime() - item.getCreateTime().getTime()) / 1000;
                WechatTicketOrderInfoListResult resultItem = new WechatTicketOrderInfoListResult();
                BeanUtil.copyProperties(item, resultItem);
                if (diff >= 1800 && ObjectUtil.equal(resultItem.getStatus(), OrderInfoStatusEnum.UN_PAID)) {
                    resultItem.setStatus(OrderInfoStatusEnum.INVALID);
                }

                if (diff < 1800 && ObjectUtil.equal(resultItem.getStatus(), OrderInfoStatusEnum.UN_PAID)) {
                    resultItem.setDiff(1800 - diff);
                }

                //设置产品信息
                WeChatProductInfoResult productInfo = new WeChatProductInfoResult();

                if (null != orderProductInfoMap.get(resultItem.getId()) &&
                        StringUtils.isNotBlank(orderProductInfoMap.get(resultItem.getId()).getBuyIntroduce())) {
                    BeanUtil.copyProperties(orderProductInfoMap.get(resultItem.getId()), productInfo);
                } else {
                    BeanUtil.copyProperties(productInfoMap.get(resultItem.getProductId()), productInfo);
                }
                resultItem.setProductInfo(productInfo);

                resultList.add(resultItem);
            }
        }

        return resultList;
    }

    /**
     * 根据key去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public WechatGetOrderInfoByIdResult wechatGetOrderInfoById(OrderInfoWechatGetOrderInfoByIdParam param) {
        Date currentDate = DateUtil.date();

        TicketOrderInfo ticketOrderInfo = ticketOrderInfoMapper.selectById(param.getId());
        if (ObjectUtil.isNull(ticketOrderInfo)) {
            throw new ServiceException("未查询到对应的订单详情，请重试");
        }

        long diff = (currentDate.getTime() - ticketOrderInfo.getCreateTime().getTime()) / 1000;
        if (diff >= 1800 && ObjectUtil.equal(ticketOrderInfo.getStatus(), OrderInfoStatusEnum.UN_PAID)) {
            ticketOrderInfo.setStatus(OrderInfoStatusEnum.INVALID);
        }
        WechatTicketOrderInfoByIdResult ticketOrderInfoByIdResult = new WechatTicketOrderInfoByIdResult();
        BeanUtil.copyProperties(ticketOrderInfo, ticketOrderInfoByIdResult);

        //如果当前时间与创建时间差值小于半小时
        if (diff < 1800 && ObjectUtil.equal(ticketOrderInfo.getStatus(), OrderInfoStatusEnum.UN_PAID)) {
            ticketOrderInfoByIdResult.setDiff(1800 - diff);
        }

        //查询订单是否有产品信息快照
        TicketOrderProductInfo ticketOrderProductInfo = ticketOrderProductInfoMapper.selectOne(Wrappers.<TicketOrderProductInfo>lambdaQuery()
                .eq(TicketOrderProductInfo::getOrderId, param.getId()));

        if (ObjectUtil.isNull(ticketOrderProductInfo)) {
            TicketProductInfo ticketProductInfo = ticketProductInfoMapper.selectOne(Wrappers.<TicketProductInfo>lambdaQuery()
                    .eq(TicketProductInfo::getProductId, ticketOrderInfo.getProductId())
            );

            if (ObjectUtil.isNull(ticketProductInfo)) {
                throw new ServiceException("未查询到订单对应的产品信息，请重试");
            }
            ticketOrderProductInfo = new TicketOrderProductInfo();
            BeanUtil.copyProperties(ticketProductInfo, ticketOrderProductInfo);
        }

        List<TicketOrderProductInfoDetail> details = ticketOrderProductInfoDetailMapper.selectList(Wrappers.<TicketOrderProductInfoDetail>lambdaQuery()
                .eq(TicketOrderProductInfoDetail::getOrderId, param.getId())
        );

        List<TicketOrderCustomerInfo> customerInfos = ticketOrderCustomerInfoMapper.selectList(Wrappers.<TicketOrderCustomerInfo>lambdaQuery()
                .eq(TicketOrderCustomerInfo::getOrdernoOtaordernoRelationId, ticketOrderInfo.getOrdernoOtaordernoRelationId())
                .eq(TicketOrderCustomerInfo::getProductId, ticketOrderInfo.getProductId())
        );

        TicketWechatProductInfoDetailResult ticketProductInfoResult = new TicketWechatProductInfoDetailResult(ticketOrderProductInfo, details);
        WechatGetOrderInfoByIdResult wechatGetOrderInfoByIdResult = new WechatGetOrderInfoByIdResult(ticketProductInfoResult, customerInfos, ticketOrderInfoByIdResult);

        return wechatGetOrderInfoByIdResult;
    }

    private TicketOrderInfo stateHandler(TicketOrderInfo ticketOrderInfo) {
        DateTime now = DateTime.now();
        //当前时间-创建时间
        long diff = (now.getTime() - ticketOrderInfo.getCreateTime().getTime()) / 1000;
        if (diff >= 1800 && ObjectUtil.equal(ticketOrderInfo.getStatus(), OrderInfoStatusEnum.UN_PAID)) {
            ticketOrderInfo.setStatus(OrderInfoStatusEnum.INVALID);
        }

        return ticketOrderInfo;
    }

    @Override
    public IPage<TicketOrderInfo> pageOrderInfo(OrderInfoPageParam param) {
        DateTime now = DateTime.now();
        //当前时间-30分钟
        DateTime preDate = now.offset(DateField.MINUTE, -30);

        /*List<Integer> productIds = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
                        .eq(ObjectUtil.isNotNull(param.getType()), TicketProductInfo::getScenicSpotType, param.getType())
                        .like(ObjectUtil.isNotNull(param.getProductId()), TicketProductInfo::getProductId, param.getProductId())
//                        .eq(TicketProductInfo::getState, ProductInfoStateEnum.NORMAL)
                        .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).stream().map(i -> i.getProductId()).collect(toList());

        if (ObjectUtil.isNull(productIds) || productIds.size() <= 0) {
            IPage<TicketOrderInfo> result = param.page();
            result.setTotal(0L);
            return result;
        }*/

        LambdaQueryWrapper<TicketOrderInfo> queryWrapper = Wrappers.<TicketOrderInfo>lambdaQuery();
        if (ObjectUtil.equal(param.getStatus(), OrderInfoStatusEnum.INVALID)) {
            //已取消订单=已取消订单+未付款超过30分钟订单
            queryWrapper.eq(TicketOrderInfo::getStatus, OrderInfoStatusEnum.INVALID)
                    .or().nested(i -> i.eq(TicketOrderInfo::getStatus, OrderInfoStatusEnum.UN_PAID).le(TicketOrderInfo::getCreateTime, preDate));
        } else if (ObjectUtil.equal(param.getStatus(), OrderInfoStatusEnum.UN_PAID)) {
            //未付款订单=30分钟内未付款订单
            queryWrapper.eq(TicketOrderInfo::getStatus, OrderInfoStatusEnum.UN_PAID).gt(TicketOrderInfo::getCreateTime, preDate);
        } else {
            queryWrapper.eq(ObjectUtil.isNotNull(param.getStatus()), TicketOrderInfo::getStatus, param.getStatus());
        }

        queryWrapper.like(StrUtil.isNotBlank(param.getOutOrderNo()), TicketOrderInfo::getOutOrderNo, param.getOutOrderNo())
                .like(StrUtil.isNotBlank(param.getMobile()), TicketOrderInfo::getMobile, param.getMobile())
                .like(StrUtil.isNotBlank(param.getProductName()), TicketOrderInfo::getProductName, param.getProductName())
                .ge(ObjectUtil.isNotNull(param.getStartDate()), TicketOrderInfo::getCreateTime, param.getStartDate())
                .le(ObjectUtil.isNotNull(param.getEndDate()), TicketOrderInfo::getCreateTime, param.getEndDate())
                .ge(ObjectUtil.isNotNull(param.getSmallAmount()), TicketOrderInfo::getAmount, param.getSmallAmount())
                .le(ObjectUtil.isNotNull(param.getBigAmount()), TicketOrderInfo::getAmount, param.getBigAmount())
                .ge(ObjectUtil.isNotNull(param.getStartPlayDate()), TicketOrderInfo::getStartDate, param.getStartPlayDate())
                .le(ObjectUtil.isNotNull(param.getEndPlayDate()), TicketOrderInfo::getStartDate, param.getEndPlayDate())
                .orderByDesc(TicketOrderInfo::getCreateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams());

        IPage<TicketOrderInfo> ticketOrderInfoIPage = ticketOrderInfoMapper.selectPage(param.page(), queryWrapper).convert(this::stateHandler);

        return ticketOrderInfoIPage;
    }

    @Override
    public IPage<TicketVerificateOrderResult> pageVerificatedOrder(OrderVerificatePageParam param) {
        List<Integer> productIds = ticketProductInfoMapper.selectList(Wrappers.<TicketProductInfo>lambdaQuery()
                .eq(ObjectUtil.isNotNull(param.getType()), TicketProductInfo::getScenicSpotType, param.getType())
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).stream().map(i -> i.getProductId()).collect(toList());

        if (ObjectUtil.isNull(productIds) || productIds.size() <= 0) {
            IPage<TicketVerificateOrderResult> page = new Page(param.getPageNum(), param.getPageSize());
            return page;
        }

        IPage<TicketVerificateOrderResult> ticketOrderInfoIPage = ticketOrderInfoMapper.selectPage(param.page(), Wrappers.<TicketOrderInfo>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getOutOrderNo()), TicketOrderInfo::getOutOrderNo, param.getOutOrderNo())
                .like(StrUtil.isNotBlank(param.getMobile()), TicketOrderInfo::getMobile, param.getMobile())
                .like(StrUtil.isNotBlank(param.getProductName()), TicketOrderInfo::getProductName, param.getProductName())
                .in(TicketOrderInfo::getProductId, productIds)
                .eq(ObjectUtil.isNotNull(param.getStatus()), TicketOrderInfo::getStatus, param.getStatus())
                .and(ObjectUtil.isNull(param.getStatus()), i -> i.eq(TicketOrderInfo::getStatus, OrderInfoStatusEnum.PART_VERIFICATION)
                        .or().eq(TicketOrderInfo::getStatus, OrderInfoStatusEnum.ALL_VERIFICATION))
                .ge(ObjectUtil.isNotNull(param.getStartDate()), TicketOrderInfo::getStartDate, param.getStartDate())
                .le(ObjectUtil.isNotNull(param.getEndDate()), TicketOrderInfo::getEndDate, param.getEndDate())
                .ge(ObjectUtil.isNotNull(param.getMinAmount()), TicketOrderInfo::getAmount, param.getMinAmount())
                .le(ObjectUtil.isNotNull(param.getMaxAmount()), TicketOrderInfo::getAmount, param.getMaxAmount())
                .ge(param.getCreateTimeStart() != null, TicketOrderInfo::getCreateTime, param.getCreateTimeStart())
                .le(param.getCreateTimeEnd() != null, TicketOrderInfo::getCreateTime, param.getCreateTimeEnd())
                .orderByDesc(TicketOrderInfo::getLastUpdateTime)
                .apply(param.getDataAuthParam().getSql(), param.getDataAuthParam().getParams())
        ).convert(this::stateHandle);

        return ticketOrderInfoIPage;
    }

    private TicketVerificateOrderResult stateHandle(TicketOrderInfo orderInfo) {
        TicketOrderInfo ticketOrderInfo = stateHandler(orderInfo);
        TicketVerificateOrderResult ticketVerificateOrderResult = new TicketVerificateOrderResult();
        BeanUtil.copyProperties(ticketOrderInfo, ticketVerificateOrderResult);
        ticketVerificateOrderResult.setSurplusVerificateNum(ticketOrderInfo.getOrderQty() - ticketOrderInfo.getUseQty());
        return ticketVerificateOrderResult;
    }

    @Override
    public TicketOrderInfoResult getOrderInfo(OrderInfoGetParam param) {
        DateTime date = DateUtil.date();

        TicketOrderInfo ticketOrderInfo = ticketOrderInfoMapper.selectById(param.getId());

        if (ObjectUtil.isNull(ticketOrderInfo)) {
            throw new ServiceException("未获取到对应的订单信息，请重试");
        }

        //当前时间-创建时间超过半小时显示已取消
        long diff = (date.getTime() - ticketOrderInfo.getCreateTime().getTime()) / 1000;
        if (diff >= 1800 && ObjectUtil.equal(ticketOrderInfo.getStatus(), OrderInfoStatusEnum.UN_PAID)) {
            ticketOrderInfo.setStatus(OrderInfoStatusEnum.INVALID);
        }

        List<TicketOrderCustomerInfo> customerInfos = ticketOrderCustomerInfoMapper.selectList(Wrappers.<TicketOrderCustomerInfo>lambdaQuery()
                .eq(TicketOrderCustomerInfo::getOrdernoOtaordernoRelationId, ticketOrderInfo.getOrdernoOtaordernoRelationId())
                .eq(TicketOrderCustomerInfo::getProductId, ticketOrderInfo.getProductId())
        );

        TicketOrderInfoDetailResult ticketOrderInfoDetailResult = new TicketOrderInfoDetailResult();
        BeanUtil.copyProperties(ticketOrderInfo, ticketOrderInfoDetailResult);

        String channelName = null;
        if (ObjectUtil.isNotNull(ticketOrderInfoDetailResult.getChannelId())) {
            TicketChannelInfo ticketChannelInfo = ticketChannelInfoMapper.selectById(ticketOrderInfoDetailResult.getChannelId());
            if (ObjectUtil.isNotNull(ticketChannelInfo)) {
                channelName = ticketChannelInfo.getName();
            }
        }

        TicketOrderProductInfo ticketOrderProductInfo = ticketOrderProductInfoMapper.selectOne(Wrappers.<TicketOrderProductInfo>lambdaQuery()
                .eq(TicketOrderProductInfo::getOrderId, param.getId())
        );

        if (ObjectUtil.isNull(ticketOrderProductInfo)) {
            //获取产品信息
            TicketProductInfo ticketProductInfo = ticketProductInfoMapper.selectOne(Wrappers.<TicketProductInfo>lambdaQuery()
                    .eq(TicketProductInfo::getMerchantCode, ticketOrderInfo.getMerchantCode())
                    .eq(TicketProductInfo::getProductId, ticketOrderInfo.getProductId())
            );

            ticketOrderProductInfo = new TicketOrderProductInfo();
            BeanUtil.copyProperties(ticketProductInfo, ticketOrderProductInfo);
        }


        //查询对应的支付信息
        EasypayPaymentRecord easypayPaymentRecord = easypayPaymentRecordMapper.normalSelectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                .eq(EasypayPaymentRecord::getOrderId, ticketOrderInfo.getOrdernoOtaordernoRelationId())
                .eq(EasypayPaymentRecord::getPayState, WhetherEnum.YES)
        );

        //查询对应的退款信息
        EasypayRefundRecord easypayRefundRecord = easypayRefundRecordMapper.normalSelectOne(Wrappers.<EasypayRefundRecord>lambdaQuery()
                .eq(EasypayRefundRecord::getOrderId, ticketOrderInfo.getOrdernoOtaordernoRelationId())
                .eq(EasypayRefundRecord::getRefundState, WhetherEnum.YES)
        );

        ticketOrderInfoDetailResult.setChannelName(channelName);
        return new TicketOrderInfoResult(ticketOrderProductInfo, ticketOrderInfoDetailResult, customerInfos, easypayPaymentRecord, easypayRefundRecord);
    }

    @Override
    public BaseResult<String> wechatPrePay(OrderInfoWechatPrePayParam param) {
        MemberInfoResult currentMember = MdcUtil.getRequireCurrentMember();

        TicketOrderInfo ticketOrderInfo = ticketOrderInfoMapper.selectById(param.getId());

        if (ObjectUtil.isNull(ticketOrderInfo)) {
            throw new ServiceException("未查询到对应订单，请重试");
        }

        //判断此时订单超时30分钟，超时不能支付
        Date currentDate = DateUtil.date();
        if (((currentDate.getTime() - ticketOrderInfo.getCreateTime().getTime()) / 1000) > 1800) {
            throw new ServiceException("订单已超过30分钟未支付，已取消");
        }

        //判断游玩时间21点后不可支付
        String playDate = DateUtil.format(ticketOrderInfo.getStartDate(), "yyyy-MM-dd") + " 21:00:00";
        if (currentDate.getTime() > DateUtil.parse(playDate, "yyyy-MM-dd HH:mm:ss").getTime()) {
            throw new ServiceException("订单已超过游玩时间的有效支付时间");
        }

        TicketOrdernoOtaordernoRelation relation = ticketOrdernoOtaordernoRelationMapper.selectById(ticketOrderInfo.getOrdernoOtaordernoRelationId());
        if (ObjectUtil.isNull(relation)) {
            throw new ServiceException("未查询到对应订单关系数据，请重试");
        }
        //查询同一个订单关系id的订单信息
        List<TicketOrderInfo> orderInfoList = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                .eq(TicketOrderInfo::getOrdernoOtaordernoRelationId, ticketOrderInfo.getOrdernoOtaordernoRelationId()));

        //有多张明细则抛出异常
        if (ObjectUtil.isNull(orderInfoList) || orderInfoList.size() >= 2) {
            throw TicketOrderErrorEnum.TICKET_ORDER_ERROR_10301.getErrorMeta().getException();
        }

        //未支付时判断支付记录
        if (ObjectUtil.equal(relation.getPayStatus(), OrderPayStatusEnum.UN_PAID)) {
            EasypayPaymentRecord paymentRecord = easypayPaymentRecordMapper.selectOne(Wrappers.<EasypayPaymentRecord>lambdaQuery()
                    .eq(EasypayPaymentRecord::getOrderType, OrderTypeEnum.TICKET)
                    .eq(EasypayPaymentRecord::getOrderId, relation.getId())
                    .last("limit 1")
                    .orderByDesc(EasypayPaymentRecord::getCreateTime)
            );
            if (paymentRecord != null) {
                if (paymentRecord.getPayState() == WhetherEnum.YES) {
                    throw new ServiceException("此订单已成功支付");
                } else {
                    boolean isPaid = easyPayService.checkOrder(paymentRecord);
                    if (isPaid) {
                        throw new ServiceException("此订单已成功支付");
                    }
                }
            }
        }

        BigDecimal amount = new BigDecimal(0);

        //支付参数
        OrderParam orderParam = new OrderParam();
        StringBuffer sbProductNames = new StringBuffer();

        for (int i = 0; i < orderInfoList.size(); i++) {
            TicketOrderInfo item = orderInfoList.get(i);
            //将订单金额累加
            amount = amount.add(item.getSellingFee().multiply(new BigDecimal(item.getOrderQty())));
            sbProductNames.append(item.getProductName() + ",");
        }

        if (amount.doubleValue() <= 0) {
            throw new ServiceException("订单累计金额小于零");
        }

        TicketProductInfo ticketProductInfo = ticketProductInfoMapper.selectOne(Wrappers.<TicketProductInfo>lambdaQuery()
                .eq(TicketProductInfo::getMerchantCode, ticketOrderInfo.getMerchantCode())
                .eq(TicketProductInfo::getProductId, ticketOrderInfo.getProductId())
        );

        if (ObjectUtil.isNull(ticketOrderInfo)) {
            throw new ServiceException("未获取到订单对应产品信息");
        }

        TicketScenicSpotInfo ticketScenicSpotInfo = ticketScenicSpotInfoMapper.selectById(ticketProductInfo.getScenicSpotId());
        if (ObjectUtil.isNull(ticketScenicSpotInfo)) {
            throw new ServiceException("未获取到景区相关信息");
        }

        String productNames = ticketScenicSpotInfo.getName() + ":" + (StrUtil.isNotBlank(sbProductNames.toString()) ? sbProductNames.substring(0, sbProductNames.length() - 1) : "");
        //设置支付参数
        orderParam.setId(relation.getId());
        String orderNo = IdWorker.getIdStr();
        orderParam.setOrderNo(orderNo);
        orderParam.setFrpCode(FrpCodeEnum.APPLET_PAY);
        orderParam.setAmount(amount);
        orderParam.setAppId(currentMember.getWxAppId());
        orderParam.setOpenId(currentMember.getOpenId());
        orderParam.setProductName(productNames);

        String result = easyPayService.orderTicket(orderParam);
        return new BaseResult<String>().setResult(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wechatRefundUpdateOrderInfo(OrderInfoWechatUpdateParam param) {
        //通过id获取关系表信息
        TicketOrdernoOtaordernoRelation relation = ticketOrdernoOtaordernoRelationMapper.selectById(param.getId());

        if (ObjectUtil.isNull(relation)) {
            throw new ServiceException("未查询到对应订单关系数据，请重试");
        }

        //通过关系id查询商户编号
        List<TicketOrderInfo> ticketOrderInfos = ticketOrderInfoMapper.selectList(Wrappers.<TicketOrderInfo>lambdaQuery()
                .eq(TicketOrderInfo::getOrdernoOtaordernoRelationId, param.getId())
        );

        if (ObjectUtil.isNull(ticketOrderInfos) || ticketOrderInfos.size() >= 2) {
            throw new ServiceException("未查询到对应订单数据或存在多条订单详情，请重试");
        }

        try {
            Date currentDate = new Date();
            for (int i = 0; i < ticketOrderInfos.size(); i++) {
                TicketOrderInfo orderInfo = ticketOrderInfos.get(i);

                TicketOrderInfo item = new TicketOrderInfo();
                item.setId(orderInfo.getId());
                //更新为退款

                if (param.getIsSuccess()) {
                    item.setStatus(OrderInfoStatusEnum.ALL_REFUND);
                    item.setRefundCnt(orderInfo.getOrderQty());
                    item.setRefundQty(orderInfo.getOrderQty());
                    item.setRefundTime(currentDate);
                } else {
                    //如果回调失败，更新状态为退款失败
                    item.setStatus(OrderInfoStatusEnum.FAIL_REFUNDING);
                }

                int count = ticketOrderInfoMapper.updateById(item);
            }

            //更新
            TicketOrdernoOtaordernoRelation updateRelation = new TicketOrdernoOtaordernoRelation();
            updateRelation.setId(relation.getId());
            //更新状态为退款中
            if (param.getIsSuccess()) {
                updateRelation.setPayStatus(OrderPayStatusEnum.REFUNDED);
                updateRelation.setRefundTime(currentDate);
            } else {
                updateRelation.setPayStatus(OrderPayStatusEnum.FAIL_REFUNDING);
            }
            int relationCount = ticketOrdernoOtaordernoRelationMapper.updateById(updateRelation);
        } catch (Exception e) {
            log.error("订单退款失败：", e);
            throw new ServiceException(e.getMessage());
        }
    }

    private List<TicketOrderInfo> parOrderInfos(JSONArray orderInfos) {
        MemberInfoResult requireCurrentMember = MdcUtil.getRequireCurrentMember();
        List<TicketOrderInfo> orderInfoList = new ArrayList<>();

        if (ObjectUtil.isNotNull(orderInfos) && orderInfos.size() > 0) {
            for (int i = 0; i < orderInfos.size(); i++) {
                JSONObject jsonObject = orderInfos.getJSONObject(i);
                TicketOrderInfo ticketOrderInfo = new TicketOrderInfo(
                        IdWorker.getId(),
                        jsonObject.getStr("MerchantCode"), jsonObject.getStr("MerchantName"),
                        jsonObject.getStr("OrderNo"),
                        jsonObject.getStr("OutOrderNo"),
                        jsonObject.getInt("ProductID"),
                        jsonObject.getBigDecimal("SettleFee"),
                        jsonObject.getBigDecimal("SellingFee"),
                        jsonObject.getInt("OrderQty"),
                        jsonObject.getStr("ECode"),
                        jsonObject.getStr("UrlECode"),
                        jsonObject.getInt("CodeQty"),
                        jsonObject.getInt("CodeCnt"),
                        jsonObject.getInt("UseQty"),
                        jsonObject.getInt("UseCnt"),
                        jsonObject.getInt("RefundQty"),
                        jsonObject.getInt("RefundCnt"),
                        DateUtil.parse(jsonObject.getStr("StartDate"), "yyyy-MM-dd'T'HH:mm:ss"),
                        DateUtil.parse(jsonObject.getStr("EndDate"), "yyyy-MM-dd'T'HH:mm:ss"),
                        EnumUtil.likeValueOf(OrderInfoStatusEnum.class, jsonObject.getInt("Status")),
                        jsonObject.getStr("Mobile"),
                        jsonObject.getStr("IDCardName"),
                        jsonObject.getStr("IDCardNo"),
                        requireCurrentMember.getId(),
                        requireCurrentMember.getNickName());

                //获取产品名称和总金额
                TicketProductInfo ticketNormalProductInfo = ticketProductInfoMapper.normalSelectOne(Wrappers.<TicketProductInfo>lambdaQuery()
                        .eq(TicketProductInfo::getProductId, ticketOrderInfo.getProductId()));
                ticketOrderInfo.setProductName(ticketNormalProductInfo.getProductName());
                ticketOrderInfo.setAmount(ticketOrderInfo.getSellingFee().multiply(new BigDecimal(ticketOrderInfo.getOrderQty())));
                orderInfoList.add(ticketOrderInfo);
            }
        }

        return orderInfoList;
    }

    private List<TicketOrderInfo> parPayOrderInfos(JSONArray orderInfos) {
        List<TicketOrderInfo> orderInfoList = new ArrayList<>();

        if (ObjectUtil.isNotNull(orderInfos) && orderInfos.size() > 0) {
            for (int i = 0; i < orderInfos.size(); i++) {
                JSONObject jsonObject = orderInfos.getJSONObject(i);
                TicketOrderInfo ticketOrderInfo = new TicketOrderInfo(
                        jsonObject.getStr("MerchantCode"), jsonObject.getStr("MerchantName"),
                        jsonObject.getStr("OrderNo"),
                        jsonObject.getStr("OutOrderNo"),
                        jsonObject.getInt("ProductID"),
                        jsonObject.getBigDecimal("SettleFee"),
                        jsonObject.getBigDecimal("SellingFee"),
                        jsonObject.getInt("OrderQty"),
                        jsonObject.getStr("ECode"),
                        jsonObject.getStr("UrlECode"),
                        jsonObject.getInt("CodeQty"),
                        jsonObject.getInt("UseQty"),
                        jsonObject.getInt("RefundQty"),
                        jsonObject.getInt("RefundCnt"),
                        DateUtil.parse(jsonObject.getStr("StartDate"), "yyyy-MM-dd'T'HH:mm:ss"),
                        DateUtil.parse(jsonObject.getStr("EndDate"), "yyyy-MM-dd'T'HH:mm:ss"),
                        EnumUtil.likeValueOf(OrderInfoStatusEnum.class, jsonObject.getInt("Status")),
                        jsonObject.getStr("Mobile"),
                        jsonObject.getStr("IDCardName"),
                        jsonObject.getStr("IDCardNo"));

                //获取产品名称和总金额
                TicketProductInfo ticketNormalProductInfo = ticketProductInfoMapper.normalSelectOne(Wrappers.<TicketProductInfo>lambdaQuery()
                        .eq(TicketProductInfo::getProductId, ticketOrderInfo.getProductId()));
                ticketOrderInfo.setProductName(ticketNormalProductInfo.getProductName());
                ticketOrderInfo.setAmount(ticketOrderInfo.getSellingFee().multiply(new BigDecimal(ticketOrderInfo.getOrderQty())));
                orderInfoList.add(ticketOrderInfo);
            }
        }

        return orderInfoList;
    }

    @Override
    public IPage<TicketOrderResult> pageRefundOrder(OrderRefundPageParam param) {
        IPage page = param.page();
        List<TicketOrderResult> ticketOrders = ticketOrderInfoMapper.selectByParams(page.offset(), param);
        Long totalPage = ticketOrderInfoMapper.countByParams(param);
        if (totalPage > 0) {
            page.setTotal(totalPage);
            page.setRecords(ticketOrders);
        }
        return page;
    }

    @Override
    public TicketOrderResult getRefundDetail(OrderRefundGetParam param) {
        TicketOrderResult result = new TicketOrderResult();
        TicketOrderInfo ticketOrderInfo = ticketOrderInfoMapper.selectById(param.getId());
        if (ticketOrderInfo != null) {
            BeanUtil.copyProperties(ticketOrderInfo, result);
            Long memberId = ticketOrderInfo.getMemberId();
            Long channelId = ticketOrderInfo.getChannelId();
            MemberInfo memberInfo = memberInfoMapper.selectById(memberId);
            if (memberInfo != null) {
                result.setWxNickName(memberInfo.getNickName());
            }
            ChannelInfoGetParam channelInfoGetParam = new ChannelInfoGetParam();
            channelInfoGetParam.setId(channelId);
            TicketChannelInfo channelInfo = ticketChannelInfoService.getChannelInfo(channelInfoGetParam);
            if (channelInfo != null) {
                result.setChannel(channelInfo.getName());
            }
            result.setRefundAmount(result.getSettleFee().multiply(new BigDecimal(result.getRefundQty())));
        } else {
            throw new ServiceException("数据不存在");
        }
        return result;
    }

    @Override
    public TicketVerificateResult getVerificatedTicket(OrderInfoGetParam param) {
        TicketVerificateResult result = new TicketVerificateResult();
        TicketOrderInfo verificatedOrder = ticketOrderInfoMapper.selectById(param.getId());
        if (verificatedOrder != null) {
            BeanUtil.copyProperties(verificatedOrder, result);
            Long memberId = verificatedOrder.getMemberId();
            Long channelId = verificatedOrder.getChannelId();
            MemberInfo memberInfo = memberInfoMapper.selectById(memberId);
            if (memberInfo != null) {
                result.setWxNickName(memberInfo.getNickName());
            }
            ChannelInfoGetParam channelInfoGetParam = new ChannelInfoGetParam();
            channelInfoGetParam.setId(channelId);
            TicketChannelInfo channelInfo = ticketChannelInfoService.getChannelInfo(channelInfoGetParam);
            if (channelInfo != null) {
                result.setChannel(channelInfo.getName());
            }
            Integer surplusVerificateNum = result.getCodeQty() - result.getUseQty();
            result.setSurplusVerificateTimes(surplusVerificateNum);
            Long ordernoOtaordernoRelationId = verificatedOrder.getOrdernoOtaordernoRelationId();
            EasypayPaymentRecord easypayPaymentRecord = easypayPaymentRecordMapper.selectOne(
                    Wrappers.<EasypayPaymentRecord>lambdaQuery().eq(EasypayPaymentRecord::getOrderId, ordernoOtaordernoRelationId)
                            .eq(EasypayPaymentRecord::getStatus, 100));
            if (easypayPaymentRecord != null) {
                result.setPayTime(easypayPaymentRecord.getPayTime());
                result.setOrderId(easypayPaymentRecord.getOrderId());
            }
            EasypayRefundRecord easypayRefundRecord = easypayRefundRecordMapper.selectOne(
                    Wrappers.<EasypayRefundRecord>lambdaQuery().eq(EasypayRefundRecord::getOrderId, ordernoOtaordernoRelationId)
                            .eq(EasypayRefundRecord::getStatus, 100).eq(EasypayRefundRecord::getNotifyStatus, 100));
            if (easypayRefundRecord != null) {
                result.setRefundTime(easypayRefundRecord.getCreateTime());
                result.setRefundOrderNo(easypayRefundRecord.getRefundOrderNo());
            }
            List<TicketOrderCustomerInfo> ticketOrderCustomerInfo = ticketOrderCustomerInfoMapper.selectList(
                    Wrappers.<TicketOrderCustomerInfo>lambdaQuery().eq(TicketOrderCustomerInfo::getOrdernoOtaordernoRelationId, ordernoOtaordernoRelationId));
            if (ticketOrderCustomerInfo != null) {
                result.setCustomers(ticketOrderCustomerInfo);
            }
            List<TicketVerificateInfo> ticketVerificateInfos = ticketVerificateInfoMapper.selectList(
                    Wrappers.<TicketVerificateInfo>lambdaQuery().eq(TicketVerificateInfo::getOrdernoOtaordernoRelationId, ordernoOtaordernoRelationId));
            if (ticketVerificateInfos != null && ticketVerificateInfos.size() > 0) {
                result.setVerificateInfos(ticketVerificateInfos);
            }
        }
        return result;
    }

    @Override
    public List<TicketVerificateChartResult> getUseCntByDate(TicketVerificatePageParam param) {
        return ticketVerificateInfoMapper.getUseCntByDate(param);
    }

    @Override
    public TicketStatisticResult verificateStatistic(TicketVerificatePageParam param) {
        TicketStatisticResult result = new TicketStatisticResult();
        Long canUsedNum = ticketOrderInfoMapper.selectCodeUnt(param);
        TicketVerificateStatisticResult verificateResult = ticketOrderInfoMapper.verificateStatistic(param);
        BeanUtil.copyProperties(verificateResult, result);
        result.setCanUsedNum(canUsedNum);
        result.setUnusedNum(canUsedNum - verificateResult.getUsedNum());
        return result;
    }

    @Override
    public WechatTicketOrderCustInfoResult wechatGetOrderCustInfo() {
        MemberInfoResult currentMember = MdcUtil.getRequireCurrentMember();
        TicketOrderInfo orderInfo = ticketOrderInfoMapper.selectOne(Wrappers.<TicketOrderInfo>lambdaQuery()
                .eq(TicketOrderInfo::getMemberId, currentMember.getId())
                .orderByDesc(TicketOrderInfo::getCreateTime)
                .last("limit 1")
        );
        if (ObjectUtil.isNull(orderInfo)) {
            return null;
        }

        WechatTicketOrderCustInfoResult result = new WechatTicketOrderCustInfoResult();
        result.setCustName(orderInfo.getIdCardName());
        result.setMobile(orderInfo.getMobile());

        return result;
    }
}
