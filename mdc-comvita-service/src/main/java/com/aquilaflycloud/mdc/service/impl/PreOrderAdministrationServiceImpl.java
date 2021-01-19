package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.param.system.FileUploadParam;
import com.aquilaflycloud.mdc.result.member.MemberScanRewardResult;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.mdc.util.PoiUtil;
import com.aquilaflycloud.org.service.IUserProvider;
import com.aquilaflycloud.org.service.provider.entity.PUserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Author zly
 */
@Slf4j
@Service
public class PreOrderAdministrationServiceImpl implements PreOrderAdministrationService {
    @Resource
    private WechatMiniProgramSubscribeMessageService wechatMiniProgramSubscribeMessageService;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;
    @Resource
    private PreOrderGoodsMapper preOrderGoodsMapper;
    @Resource
    private PreRefundOrderInfoMapper preRefundOrderInfoMapper;
    @Resource
    private PreOrderOperateRecordMapper preOrderOperateRecordMapper;
    @Resource
    private IUserProvider iUserProvider;
    @Resource
    private PrePickingCardMapper prePickingCardMapper;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private PreExpressInfoMapper preExpressInfoMapper;

    @Resource
    private PreOrderOperateRecordService orderOperateRecordService;

    @Override
    public PreOrderStatisticsResult getPreOderStatistics(PreOrderListParam param) {
        return preOrderInfoMapper.selectMaps(new QueryWrapper<PreOrderInfo>()
                .select("count(1) orderCount,"
                        + "coalesce(sum(total_price), 0) orderAllPrice")
                .lambda()
                .eq(StringUtils.isNotBlank(param.getShopId()), PreOrderInfo::getShopId, param.getShopId())
                .like(StringUtils.isNotBlank(param.getShopName()), PreOrderInfo::getShopName, param.getShopName())
                .eq(StringUtils.isNotBlank(param.getGuideName()), PreOrderInfo::getGuideName, param.getGuideName())
                .eq(StringUtils.isNotBlank(param.getOrderState()), PreOrderInfo::getOrderState, param.getOrderState())
                .eq(StringUtils.isNotBlank(param.getOrderCode()), PreOrderInfo::getOrderCode, param.getOrderCode())
                .eq(param.getMemberId() != null, PreOrderInfo::getMemberId, param.getMemberId())
                .like(StringUtils.isNotBlank(param.getBuyerName()), PreOrderInfo::getBuyerName, param.getBuyerName())
                .ge(param.getCreateStartTime() != null, PreOrderInfo::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreOrderInfo::getCreateTime, param.getCreateEndTime())
        ).stream().map(map -> {
            if ("0".equals(StrUtil.toString(map.get("orderCount")))) {
                map.put("orderPerPrice", 0);
            } else {
                map.put("orderPerPrice", NumberUtil.div(StrUtil.toString(map.get("orderAllPrice")), StrUtil.toString(map.get("orderCount")), 2));
            }
            return BeanUtil.fillBeanWithMap(map, new PreOrderStatisticsResult(), true,
                    CopyOptions.create().ignoreError());
        }).collect(Collectors.toList()).get(0);
    }

    @Override
    public IPage<PreOrderInfo> pagePreOder(PreOrderPageParam param) {
        return preOrderInfoMapper.selectPage(param.page(), Wrappers.<PreOrderInfo>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getShopId()), PreOrderInfo::getShopId, param.getShopId())
                .like(StringUtils.isNotBlank(param.getShopName()), PreOrderInfo::getShopName, param.getShopName())
                .like(StringUtils.isNotBlank(param.getGuideName()), PreOrderInfo::getGuideName, param.getGuideName())
                .eq(ObjectUtil.isNotNull(param.getOrderState()), PreOrderInfo::getOrderState, param.getOrderState())
                .like(StringUtils.isNotBlank(param.getOrderCode()), PreOrderInfo::getOrderCode, param.getOrderCode())
                .eq(param.getMemberId() != null, PreOrderInfo::getMemberId, param.getMemberId())
                .like(StringUtils.isNotBlank(param.getBuyerName()), PreOrderInfo::getBuyerName, param.getBuyerName())
                .ge(param.getCreateStartTime() != null, PreOrderInfo::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreOrderInfo::getCreateTime, param.getCreateEndTime())
                .orderByDesc(PreOrderInfo::getCreateTime)
        );
    }

    @Override
    public IPage<PreOrderInfo> pageMobilePreOder(PreOrderPageParam param) {
        Long id = MdcUtil.getCurrentUserId();
        if(param.getConfirmState().equals("0")) {
             return preOrderInfoMapper.selectPage(param.page(), Wrappers.<PreOrderInfo>lambdaQuery()
                    .eq(PreOrderInfo::getGuideId, id)
                    .eq(StringUtils.isNotBlank(param.getShopId()), PreOrderInfo::getShopId, param.getShopId())
                    .like(StringUtils.isNotBlank(param.getShopName()), PreOrderInfo::getShopName, param.getShopName())
                    .eq(StringUtils.isNotBlank(param.getGuideName()), PreOrderInfo::getGuideName, param.getGuideName())
                    .eq(ObjectUtil.isNotNull(param.getOrderState()), PreOrderInfo::getOrderState, param.getOrderState())
                    .eq(StringUtils.isNotBlank(param.getOrderCode()), PreOrderInfo::getOrderCode, param.getOrderCode())
                    .eq(param.getMemberId() != null, PreOrderInfo::getMemberId, param.getMemberId())
                     .eq(PreOrderInfo::getFailSymbol, FailSymbolEnum.YES_NO)
                     .like(StringUtils.isNotBlank(param.getBuyerName()), PreOrderInfo::getBuyerName, param.getBuyerName())
                    .ge(param.getCreateStartTime() != null, PreOrderInfo::getCreateTime, param.getCreateStartTime())
                    .le(param.getCreateEndTime() != null, PreOrderInfo::getCreateTime, param.getCreateEndTime())
                    .orderByDesc(PreOrderInfo::getCreateTime)
            );
        }else {
             return preOrderInfoMapper.selectPage(param.page(), Wrappers.<PreOrderInfo>lambdaQuery()
                    .eq(PreOrderInfo::getGuideId, id)
                    .eq(StringUtils.isNotBlank(param.getShopId()), PreOrderInfo::getShopId, param.getShopId())
                    .like(StringUtils.isNotBlank(param.getShopName()), PreOrderInfo::getShopName, param.getShopName())
                    .eq(StringUtils.isNotBlank(param.getGuideName()), PreOrderInfo::getGuideName, param.getGuideName())
                    .ne(PreOrderInfo::getOrderState,OrderInfoStateEnum.STAYCONFIRM)
                    .eq(StringUtils.isNotBlank(param.getOrderCode()), PreOrderInfo::getOrderCode, param.getOrderCode())
                    .eq(param.getMemberId() != null, PreOrderInfo::getMemberId, param.getMemberId())
                    .eq(PreOrderInfo::getFailSymbol, FailSymbolEnum.NO)
                    .like(StringUtils.isNotBlank(param.getBuyerName()), PreOrderInfo::getBuyerName, param.getBuyerName())
                    .ge(param.getCreateStartTime() != null, PreOrderInfo::getCreateTime, param.getCreateStartTime())
                    .le(param.getCreateEndTime() != null, PreOrderInfo::getCreateTime, param.getCreateEndTime())
                    .orderByDesc(PreOrderInfo::getCreateTime));
        }
    }
    @Override
    public IPage<PreRefundOrderInfo> pageOrderInfoList(PreRefundOrderListParam param) {
        IPage<PreRefundOrderInfo> list = preRefundOrderInfoMapper.selectPage(param.page(), Wrappers.<PreRefundOrderInfo>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getShopId()), PreRefundOrderInfo::getShopId, param.getShopId())
                .like(StringUtils.isNotBlank(param.getGuideName()), PreRefundOrderInfo::getGuideName, param.getGuideName())
                .like(StringUtils.isNotBlank(param.getAfterGuideName()), PreRefundOrderInfo::getAfterGuideName, param.getAfterGuideName())
                .like(StringUtils.isNotBlank(param.getOrderCode()), PreRefundOrderInfo::getOrderCode, param.getOrderCode())
                .like(StringUtils.isNotBlank(param.getBuyerName()), PreRefundOrderInfo::getBuyerName, param.getBuyerName())
                .ge(param.getAfterSalesStartTime() != null, PreRefundOrderInfo::getRefundTime, param.getAfterSalesStartTime())
                .le(param.getAfterSalesEndTime() != null, PreRefundOrderInfo::getRefundTime, param.getAfterSalesEndTime())
                .ge(param.getCreateStartTime() != null, PreRefundOrderInfo::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreRefundOrderInfo::getCreateTime, param.getCreateEndTime())
                .orderByDesc(PreRefundOrderInfo::getCreateTime)
        );
        return list;
    }

    @Override
    @Transactional
    //1.判断是否所有商品都发货了，2.填赠品的时候是否所有商品都发货了
    public void inputOrderNumber(InputOrderNumberParam param) {
        PreOrderGoods info = preOrderGoodsMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("商品不存在");
        }
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(info.getOrderId());
        boolean allGoodsSend = false;
        if (GoodsTypeEnum.GIFTS.equals(info.getGoodsType())) { //填赠品的时候是否所有商品都发货了
            List<PreOrderGoods> list = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderId, info.getOrderId())
                    .notIn(PreOrderGoods::getId, info.getId())
                    .notIn(PreOrderGoods::getGoodsType, GoodsTypeEnum.GIFTS)
                    .in(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE, OrderGoodsStateEnum.PREPARE)
            );
            if (list.size() > 0) {
                throw new ServiceException("存在商品没有发货，请填写完商品再填写赠品的快递单号");
            }
            preOrderInfo.setOrderState(OrderInfoStateEnum.STAYSIGN);
            preOrderInfo.setDeliveryTime(new DateTime());
            //当赠品录入快递时,反填快递信息给订单
            preOrderInfo.setExpressOrder(param.getExpressOrder());
            preOrderInfo.setExpressName(param.getExpressName());
            preOrderInfo.setExpressCode(param.getExpressCode());
            preOrderInfoMapper.updateById(preOrderInfo);
        } else {//不是赠品。判断是否有
            //查询是否有赠品
            List<PreOrderGoods> list = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderId, info.getOrderId())
                    .ne(PreOrderGoods::getId, info.getId())
                    .eq(PreOrderGoods::getGoodsType, GoodsTypeEnum.GIFTS)
            );
            List<PreOrderGoods> list2 = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderId, info.getOrderId())
                    .ne(PreOrderGoods::getId, info.getId())
                    .ne(PreOrderGoods::getGoodsType, GoodsTypeEnum.GIFTS)
                    .in(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE, OrderGoodsStateEnum.PREPARE)
            );
            if (CollUtil.isEmpty(list2)) {
                allGoodsSend = true;
            }
            if (CollUtil.isEmpty(list)) {//没有赠品，查询是否这是最后一个商品，是的话填写订单表商品状态和发货时间
                if (allGoodsSend) {//是空则商品都发完了，更新订单表
                    preOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
                    preOrderInfo.setDeliveryTime(new DateTime());
                    MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
                    Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord(memberInfo, null, preOrderInfo.getId(), preOrderInfo.getTotalPrice(), true);
                    preOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
                    preOrderInfoMapper.updateById(preOrderInfo);
                }
            } else {//待发货状态
                if (allGoodsSend) {
                    preOrderInfo.setOrderState(OrderInfoStateEnum.STAYSENDGOODS);
                    preOrderInfoMapper.updateById(preOrderInfo);
                    PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
                    .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));
                    preOrderGoods.setGiftsSymbol(GiftsSymbolEnum.AFTER);
                    int orderGoods = preOrderGoodsMapper.updateById(preOrderGoods);
                    if(orderGoods < 0){
                        throw new ServiceException("修改赠品标识失败。");
                    }
                }
            }
        }
        info.setExpressName(param.getExpressName());
        info.setExpressOrderCode(param.getExpressOrder());
        info.setExpressCode(param.getExpressCode());
        info.setOrderGoodsState(OrderGoodsStateEnum.ALSENDGOODS);
        info.setPickingCardState(PickingCardStateEnum.VERIFICATE);
        info.setDeliveryTime(new DateTime());
        preOrderGoodsMapper.updateById(info);
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                .eq(PrePickingCard::getPickingCode, info.getCardCode())
        );
        if (prePickingCard != null) {
            prePickingCard.setPickingState(PickingCardStateEnum.VERIFICATE);
            prePickingCardMapper.updateById(prePickingCard);
        }
        //添加操作记录
        orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getGuideName(), preOrderInfo.getId(), "进行了发货。");

        if (info.getGoodsType() == GoodsTypeEnum.GIFTS) {
            //赠品发货,发送订单发货微信订阅消息
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERDELIVERY, null,
                    preOrderInfo.getOrderCode(), info.getDeliveryProvince() + info.getDeliveryCity() + info.getDeliveryDistrict() + info.getDeliveryAddress(),
                    info.getExpressName(), info.getExpressOrderCode(), info.getGoodsName() + "商品已发货");
        } else {
            //商品发货,发送商品发货微信订阅消息
            MemberInfo memberInfo = memberInfoMapper.selectById(info.getReserveId());
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(memberInfo.getWxAppId())
                            .setOpenId(memberInfo.getOpenId())), MiniMessageTypeEnum.PREORDERGOODSELIVERY, null,
                    info.getGoodsName(), info.getDeliveryProvince() + info.getDeliveryCity() + info.getDeliveryDistrict() + info.getDeliveryAddress(),
                    info.getExpressName(), info.getExpressOrderCode(), info.getGoodsName() + "商品已发货");
        }
    }

    @Override
    public AdministrationDetailsResult getOrderDetails(OrderDetailsParam param) {
        PreOrderInfo info = preOrderInfoMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("输入的主键值有误");
        }
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, info.getId()));
        List<PreOrderOperateRecord> preOrderOperateRecordlist = preOrderOperateRecordMapper.selectList(Wrappers.<PreOrderOperateRecord>lambdaQuery()
                .eq(PreOrderOperateRecord::getOrderId, info.getId()));
        AdministrationDetailsResult result = new AdministrationDetailsResult();
        //(value = "订单编码")
        result.setOrderCode(info.getOrderCode());
        //(value = "门店名称")
        result.setShopName(info.getShopName());
        //(value = "导购员名称")
        result.setGuideName(info.getGuideName());
        //(value = "总金额")
        result.setTotalPrice(info.getTotalPrice());
        //(value = "订单状态")
        result.setOrderState(info.getOrderState());
        //(value = "创建时间")
        result.setCreateTime(info.getCreateTime());
        //(value = "确认时间")
        result.setConfirmTime(info.getConfirmTime());
        //(value = "发货时间")
        result.setDeliveryTime(info.getDeliveryTime());
        //(value = "销售小票url")
        result.setTicketUrl(info.getTicketUrl());
        //(value = "买家姓名")
        result.setBuyerName(info.getBuyerName());
        //(value = "买家手机")
        result.setBuyerPhone(info.getBuyerPhone());
        //(value = "买家详细地址")
        result.setBuyerAddress(info.getBuyerAddress());
        //(value = "买家地址-省")
        result.setBuyerProvince(info.getBuyerProvince());
        //(value = "买家地址-市")
        result.setBuyerCity(info.getBuyerCity());
        //(value = "买家地址-区")
        result.setBuyerDistrict(info.getBuyerDistrict());
        //(value = "买家地址邮编")
        result.setBuyerPostalCode(info.getBuyerPostalCode());
        //(value = "订单明细")
        result.setDetailsList(preOrderGoodsList);
        //(value = "操作记录")
        result.setOperationList(preOrderOperateRecordlist);
        result.setBuyerSex(info.getBuyerSex());
        result.setBuyerBirthday(info.getBuyerBirthday());
        result.setScore(info.getScore());
        return result;
    }

    @Override
    public AfterSalesDetailsResult getAfterOrderDetails(OrderDetailsParam param) {
        PreRefundOrderInfo info = preRefundOrderInfoMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("输入的主键值有误");
        }
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, info.getOrderId()));
        List<PreOrderOperateRecord> preOrderOperateRecordlist = preOrderOperateRecordMapper.selectList(Wrappers.<PreOrderOperateRecord>lambdaQuery()
                .eq(PreOrderOperateRecord::getOrderId, info.getOrderId()));
        AfterSalesDetailsResult result = new AfterSalesDetailsResult();
        BeanUtils.copyProperties(info, result);
        //(value = "订单明细")
        result.setDetailsList(preOrderGoodsList);
        //(value = "操作记录")
        result.setOperationList(preOrderOperateRecordlist);
        return result;
    }

    @Override
    public IPage<PreOrderGoods> pagereadySalesList(ReadyListParam param) {
        return preOrderGoodsMapper.selectPage(param.page(), Wrappers.<PreOrderGoods>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getGuideName()), PreOrderGoods::getGuideName, param.getGuideName())
                .like(StringUtils.isNotBlank(param.getReserveName()), PreOrderGoods::getReserveName, param.getReserveName())
                .eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE)
                .like(StringUtils.isNotBlank(param.getOrderCode()), PreOrderGoods::getOrderCode, param.getOrderCode())
                .like(StringUtils.isNotBlank(param.getReserveShop()), PreOrderGoods::getReserveShop, param.getReserveShop())
                .ge(param.getCreateStartTime() != null, PreOrderGoods::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreOrderGoods::getCreateTime, param.getCreateEndTime())
                .ge(param.getReserveStartTime() != null, PreOrderGoods::getReserveStartTime, param.getReserveStartTime())
                .le(param.getReserveEndTime() != null, PreOrderGoods::getReserveStartTime, param.getReserveEndTime())
                .notIn(PreOrderGoods::getGiftsSymbol,GiftsSymbolEnum.NOTAFTER)
                .orderByDesc(PreOrderGoods::getCreateTime)
        );
    }

    @Override
    public IPage<ReportOrderPageResult> pageOrderReportList(ReportFormParam param) {
        IPage<ReportOrderPageResult> page = preOrderInfoMapper.pageOrderReportList(param.page(), param);
        return page;
    }

    @Override
    //导购员绩效
    public IPage<ReportGuidePageResult> achievementsGuide(ReportFormParam param) {
        List<PUserInfo> list = iUserProvider.listUserInfo();
        IPage<ReportGuidePageResult> page = preOrderInfoMapper.achievementsGuide(param.page(), param);
        List<ReportGuidePageResult> list2 = page.getRecords();
        if (CollUtil.isNotEmpty(list)) {
            for (PUserInfo info : list) {
                Boolean ishave = true;
                if (CollUtil.isNotEmpty(list2)) {
                    for (ReportGuidePageResult result : list2) {
                        if (result.getGuideId().equals(info.getId())) {
                            ishave = false;
                            break;
                        }
                    }
                }
                if (ishave) {
                    ReportGuidePageResult reportGuidePageResult = new ReportGuidePageResult();
                    reportGuidePageResult.setGuideName(info.getRealName());
                    reportGuidePageResult.setNewCustomerNum(0);
                    reportGuidePageResult.setOrderNumber(0);
                    reportGuidePageResult.setOrderPrice(new BigDecimal(0));
                    list2.add(reportGuidePageResult);
                }
            }
            page.setRecords(list2);
        }
        return page;
    }

    @Override
    //订单管理订单报表
    public IPage<OrderPageResult> pageOrderPageResultList(AdministrationListParam param) {
        IPage<OrderPageResult> page = preOrderInfoMapper.pageOrderPageResultList(param.page(), param);
        return page;
    }

    @Override
    //订单管理销量导出
    public IPage<SalePageResult> pageSalePageResultList(AdministrationListParam param) {
        IPage<SalePageResult> page = preOrderInfoMapper.pageSalePageResultList(param.page(), param);
        return page;
    }

    @Override
    @Transactional
    public void importOrderCode(FileUploadParam param) {
        //读导入文件的数据到集合
        MultipartFile file = param.getFile();
        String fileName = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new ServiceException("获取表格数据失败，请重试");
        }
        List<Map<String, String>> dataMap = PoiUtil.readExcel(inputStream, fileName, 0, 0, 0);

        if (null == dataMap || dataMap.size() == 0) {
            throw new ServiceException("表格数据为空，请导入数据");
        }

        //反射查询物流编号和物流单号
        Map<String, String> fieldMap = new HashMap<>();
        Field[] fields = ReflectUtil.getFields(PreOrderGoods.class);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            if ("expressCode".equals(field.getName())) {
                fieldMap.put("expressCode", annotation.value());
            } else if ("expressOrderCode".equals(field.getName())) {
                fieldMap.put("expressOrderCode", annotation.value());
            } else if ("id".equals(field.getName())) {
                fieldMap.put("id", annotation.value());
            } else if ("expressName".equals(field.getName())) {
                fieldMap.put("expressName", annotation.value());
            }
        }
        //确保导入字段名称
        if (fieldMap.size() != 4) {
            throw new ServiceException("导入失败");
        }

        //判断数据是否合法
        List<Long> ids = new ArrayList<>();
        Map<Long, Map<String, String>> importMap = new HashMap<>();
        for (int i = 0; i < dataMap.size(); i++) {
            Map<String, String> item = dataMap.get(i);
            //关键字段判空
            String expressCodeName = fieldMap.get("expressCode");
            String expressOrderCodeName = fieldMap.get("expressOrderCode");
            String expressName = fieldMap.get("expressName");
            String idName = fieldMap.get("id");

            Long id = Long.valueOf(item.get(idName));
            boolean expressCodeSign = StrUtil.isBlank(item.get(expressCodeName));
            boolean expressOrderCodeSign = StrUtil.isBlank(item.get(expressOrderCodeName));
            boolean expressNameSign = StrUtil.isBlank(item.get(expressName));
            boolean idSign = ObjectUtil.isNull(id);

            //物流相关字段都为空，则跳过这条记录
            if (expressCodeSign && expressOrderCodeSign && expressNameSign) {
                continue;
            }

            if (expressCodeSign) {
                throw new ServiceException("物流信息需填写完整：" + expressCodeName + "不能为空");
            } else if (expressOrderCodeSign) {
                throw new ServiceException("物流信息需填写完整：" + expressOrderCodeName + "不能为空");
            } else if (idSign) {
                throw new ServiceException("id不能为空，请重新导出数据进行信息填写后再导入");
            } else if (expressNameSign) {
                throw new ServiceException("物流信息需填写完整：" + expressName + "物流名称不能为空");
            }

            ids.add(id);
            importMap.put(id, item);
        }

        if (null != ids && ids.size() > 0) {
            //判断导入的id数和查询数据库的id数是否相同，根据商品类型排序，先发货预售商品，再发货赠品
            List<PreOrderGoods> preOrderGoods = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .in(PreOrderGoods::getId, ids)
                    .nested(item->item.eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE).or().eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.ALSENDGOODS))
                    .orderByAsc(PreOrderGoods::getGoodsType)
                    .orderByDesc(PreOrderGoods::getCreateTime)
            );

            List<PreOrderGoods> pretakeList = preOrderGoods.stream().filter(item -> ObjectUtil.equal(item.getOrderGoodsState(), OrderGoodsStateEnum.PRETAKE)).collect(Collectors.toList());
            List<PreOrderGoods> alsendgoodsList = preOrderGoods.stream().filter(item -> ObjectUtil.equal(item.getOrderGoodsState(), OrderGoodsStateEnum.ALSENDGOODS)).collect(Collectors.toList());
            //循环调用更新逻辑

            log.info("物流单号批量导入开始");

            //待发货，更新状态和发送发货信息给c端用户
            if (null != pretakeList && pretakeList.size() > 0) {
                for (int i = 0; i < pretakeList.size(); i++) {
                    PreOrderGoods item = preOrderGoods.get(i);
                    Map<String, String> itemDataMap = importMap.get(item.getId());

                    if (null == itemDataMap || itemDataMap.size() == 0) {
                        throw new ServiceException("表格的数据有误，请检查再重试");
                    }

                    String expressCode = itemDataMap.get(fieldMap.get("expressCode"));
                    String expressOrderCode = itemDataMap.get(fieldMap.get("expressOrderCode"));
                    String id = itemDataMap.get(fieldMap.get("id"));
                    String expressName = itemDataMap.get(fieldMap.get("expressName"));

                    InputOrderNumberParam inputOrderNumberParam = new InputOrderNumberParam();
                    inputOrderNumberParam.setId(id);
                    inputOrderNumberParam.setExpressCode(expressCode);
                    inputOrderNumberParam.setExpressOrder(expressOrderCode);
                    inputOrderNumberParam.setExpressName(expressName);

                    log.info("待发货订单物流单号更新信息：{id=" + id + ", expressCode=" + expressCode + ", expressOrderCode" + expressOrderCode + ", expressName" + expressName + "}");
                    this.inputOrderNumber(inputOrderNumberParam);
                }
            }

            //已发货，更新物流字段
            if (null != alsendgoodsList && alsendgoodsList.size() > 0) {
                for (int i = 0; i < alsendgoodsList.size(); i++) {
                    PreOrderGoods item = alsendgoodsList.get(i);
                    Map<String, String> itemDataMap = importMap.get(item.getId());

                    if (null == itemDataMap || itemDataMap.size() == 0) {
                        throw new ServiceException("表格的数据有误，请检查再重试");
                    }

                    String expressCode = itemDataMap.get(fieldMap.get("expressCode"));
                    String expressOrderCode = itemDataMap.get(fieldMap.get("expressOrderCode"));
                    String id = itemDataMap.get(fieldMap.get("id"));
                    String expressName = itemDataMap.get(fieldMap.get("expressName"));

                    //字段都一样，跳过循环
                    if (StrUtil.equals(id, item.getId().toString())
                            && StrUtil.equals(expressCode, item.getExpressCode())
                            && StrUtil.equals(expressOrderCode, item.getExpressOrderCode())
                            && StrUtil.equals(expressName, item.getExpressName())) {
                        continue;
                    }

                    PreOrderGoods updateItem = new PreOrderGoods();
                    updateItem.setId(item.getId());
                    updateItem.setExpressCode(expressCode);
                    updateItem.setExpressOrderCode(expressOrderCode);
                    updateItem.setExpressName(expressName);

                    int count = preOrderGoodsMapper.updateById(updateItem);

                    if (count > 0) {
                        log.info("待签收订单物流单号更新信息：{id=" + id + ", expressCode=" + expressCode + ", expressOrderCode" + expressOrderCode + ", expressName" + expressName + "}");
                    } else {
                        throw new ServiceException("导入失败，请重试");
                    }
                }
            }
            log.info("物流单号批量导入结束");
        } else {
            throw new ServiceException("请检查导入的数据是否有效");
        }
    }
}
