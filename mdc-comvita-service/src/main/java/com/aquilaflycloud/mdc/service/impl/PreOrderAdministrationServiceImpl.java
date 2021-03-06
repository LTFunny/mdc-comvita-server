package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
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
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
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
import java.util.*;
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
    private PreActivityInfoMapper preActivityInfoMapper;
    @Resource
    private PrePickingCardMapper prePickingCardMapper;
    @Resource
    private MemberRewardService memberRewardService;
    @Resource
    private PreFlashOrderInfoMapper flashOrderInfoMapper;

    @Resource
    private PreOrderOperateRecordService orderOperateRecordService;

    @Override
    @Transactional
    public void completeButton(PreOrderGetParam param) {
        String name= MdcUtil.getCurrentUserName();
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderId());
        PreOrderInfo newOrderInfo = new PreOrderInfo();
        newOrderInfo.setId(preOrderInfo.getId());
        newOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
        MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
        Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord
                (memberInfo, null, preOrderInfo.getId(), preOrderInfo.getTotalPrice(), true);
        if (CollUtil.isNotEmpty(map) && map.get(RewardTypeEnum.SCORE) != null) {
            newOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
        }
        int order = preOrderInfoMapper.updateById(newOrderInfo);
        if (order < 0) {
            throw new ServiceException("?????????????????????");
        }
        String content = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                + "????????????(" + preOrderInfo.getOrderCode() + ")??????????????????????????????";
        orderOperateRecordService.addOrderOperateRecordLog(name,preOrderInfo.getId(),content);
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,preOrderInfo.getId()));
        if(CollUtil.isNotEmpty(preOrderGoodsList)){
            for(PreOrderGoods preOrderGoods:preOrderGoodsList){
                PreOrderGoods newOrderGoods = new PreOrderGoods();
                newOrderGoods.setId(preOrderGoods.getId());
                newOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.TAKEN);
                int orderGoods = preOrderGoodsMapper.updateById(newOrderGoods);
                if(orderGoods < 0){
                    throw new ServiceException("?????????????????????");
                }
                PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                        .eq(PrePickingCard::getPickingCode, preOrderGoods.getCardCode())
                );
                if (prePickingCard != null) {
                    PrePickingCard newPickingCard = new PrePickingCard();
                    newPickingCard.setId(prePickingCard.getId());
                    newPickingCard.setPickingState(PickingCardStateEnum.VERIFICATE);
                    prePickingCardMapper.updateById(newPickingCard);
                }
            }
        }
    }

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
    public IPage<PreOrderInfoResult> pagePreOder(PreOrderPageParam param) {
        return preOrderInfoMapper.pagePreOder(param.page(), param).convert(result -> {
            result.setDeliveryDetailAddress(result.getBuyerProvince() + result.getBuyerCity() + result.getBuyerDistrict() + result.getBuyerAddress());
            return result;
        });
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
    //1.???????????????????????????????????????2.????????????????????????????????????????????????
    public void inputOrderNumber(InputOrderNumberParam param) {
        PreOrderGoods info = preOrderGoodsMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("???????????????");
        }
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(info.getOrderId());
            if(preOrderInfo.getFlashId()==null){
                PreOrderInfo newOrderInfo = new PreOrderInfo();
                newOrderInfo.setId(preOrderInfo.getId());
                boolean allGoodsSend = false;
                if (GoodsTypeEnum.GIFTS.equals(info.getGoodsType())) { //????????????????????????????????????????????????
                    List<PreOrderGoods> list = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                            .eq(PreOrderGoods::getOrderId, info.getOrderId())
                            .notIn(PreOrderGoods::getId, info.getId())
                            .notIn(PreOrderGoods::getGoodsType, GoodsTypeEnum.GIFTS)
                            .in(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE, OrderGoodsStateEnum.PREPARE)
                    );
                    if (list.size() > 0) {
                        throw new ServiceException("???????????????????????????????????????????????????????????????????????????");
                    }
                    newOrderInfo.setOrderState(OrderInfoStateEnum.STAYSIGN);
                    newOrderInfo.setDeliveryTime(new DateTime());
                    //????????????????????????,???????????????????????????
                    newOrderInfo.setExpressOrder(param.getExpressOrder());
                    newOrderInfo.setExpressName(param.getExpressName());
                    newOrderInfo.setExpressCode(param.getExpressCode());
                    preOrderInfoMapper.updateById(newOrderInfo);
                } else {//??????????????????????????????
                    //?????????????????????
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
                    if (CollUtil.isEmpty(list)) {//?????????????????????????????????????????????????????????????????????????????????????????????????????????
                        if (allGoodsSend) {//?????????????????????????????????????????????
                            newOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
                            newOrderInfo.setDeliveryTime(new DateTime());
                            MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
                            Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord(memberInfo, null, preOrderInfo.getId(), preOrderInfo.getTotalPrice(), true);
                            if (CollUtil.isNotEmpty(map) && map.get(RewardTypeEnum.SCORE) != null) {
                                newOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
                            }
                            preOrderInfoMapper.updateById(newOrderInfo);
                        }
                    } else {//???????????????
                        if (allGoodsSend) {
                            newOrderInfo.setOrderState(OrderInfoStateEnum.STAYSENDGOODS);
                            preOrderInfoMapper.updateById(newOrderInfo);
                            PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                                    .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
                                    .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));
                            PreOrderGoods newOrderGoods = new PreOrderGoods();
                            newOrderGoods.setId(preOrderGoods.getId());
                            newOrderGoods.setGiftsSymbol(GiftsSymbolEnum.AFTER);
                            int orderGoods = preOrderGoodsMapper.updateById(newOrderGoods);
                            if(orderGoods < 0){
                                throw new ServiceException("???????????????????????????");
                            }
                        }
                    }
                }
                PreOrderGoods newInfo = new PreOrderGoods();
                newInfo.setId(info.getId());
                newInfo.setExpressName(param.getExpressName());
                newInfo.setExpressOrderCode(param.getExpressOrder());
                newInfo.setExpressCode(param.getExpressCode());
                newInfo.setOrderGoodsState(OrderGoodsStateEnum.ALSENDGOODS);
                newInfo.setPickingCardState(PickingCardStateEnum.VERIFICATE);
                newInfo.setDeliveryTime(new DateTime());
                preOrderGoodsMapper.updateById(newInfo);

                PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                        .eq(PrePickingCard::getPickingCode, info.getCardCode())
                );
                if (prePickingCard != null) {
                    PrePickingCard newPickingCard = new PrePickingCard();
                    newPickingCard.setId(prePickingCard.getId());
                    newPickingCard.setPickingState(PickingCardStateEnum.VERIFICATE);
                    prePickingCardMapper.updateById(newPickingCard);
                }
            }else{
                //??????????????????????????????
                changeFlashState(param,preOrderInfo,info);
            }

            //??????????????????
            orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getGuideName(), preOrderInfo.getId(), "??????????????????");

            if (info.getGoodsType() == GoodsTypeEnum.GIFTS) {
                //????????????,????????????????????????????????????
                wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                                .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERDELIVERY, null,
                        preOrderInfo.getOrderCode(), info.getDeliveryProvince() + info.getDeliveryCity() + info.getDeliveryDistrict() + info.getDeliveryAddress() + "???",
                        info.getExpressName(), info.getExpressOrderCode(), "????????????????????????????????????");
            } else {
                //????????????,????????????????????????????????????
                MemberInfo memberInfo = memberInfoMapper.selectById(info.getReserveId());
                wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(memberInfo.getWxAppId())
                                .setOpenId(memberInfo.getOpenId())), MiniMessageTypeEnum.PREORDERGOODSELIVERY, null,
                        info.getGoodsName(), info.getDeliveryProvince() + info.getDeliveryCity() + info.getDeliveryDistrict() + info.getDeliveryAddress() + "???",
                        info.getExpressName(), info.getExpressOrderCode(), "????????????????????????????????????");
            }

    }

    @Override
    @Transactional
    public void inputFlashOrderNumber(InputOrderNumberParam param) {
        PreOrderInfo info = preOrderInfoMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("????????????????????????");
        }
        List<PreOrderGoods> list = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, info.getId())
        );
        if(CollUtil.isNotEmpty(list)){
            for(PreOrderGoods preOrderGoods:list){
                PreOrderGoods newOrderGoods = new PreOrderGoods();
                newOrderGoods.setId(preOrderGoods.getId());
                newOrderGoods.setExpressOrderCode(param.getExpressOrder());
                newOrderGoods.setExpressCode(param.getExpressCode());
                newOrderGoods.setExpressName(param.getExpressName());
                newOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.ALSENDGOODS);
                newOrderGoods.setPickingCardState(PickingCardStateEnum.VERIFICATE);
                newOrderGoods.setDeliveryTime(new DateTime());
                preOrderGoodsMapper.updateById(newOrderGoods);
            }
        }
        PreOrderInfo newInfo = new PreOrderInfo();
        newInfo.setId(info.getId());
        newInfo.setExpressCode(param.getExpressCode());
        newInfo.setExpressOrder(param.getExpressOrder());
        newInfo.setExpressName(param.getExpressName());
        newInfo.setOrderState(OrderInfoStateEnum.STAYSIGN);
        newInfo.setDeliveryTime(new DateTime());
        preOrderInfoMapper.updateById(newInfo);
        //??????????????????
        changeFlashState(info.getId(),info.getExpressOrder());
        //??????????????????
        String name=MdcUtil.getCurrentUserName();
        orderOperateRecordService.addOrderOperateRecordLog(name, info.getId(), "??????????????????");

    }

    private void changeFlashState(InputOrderNumberParam param, PreOrderInfo preOrderInfo, PreOrderGoods info){
        PreOrderGoods newOrderGoods = new PreOrderGoods();
        newOrderGoods.setId(info.getId());
        newOrderGoods.setExpressName(param.getExpressName());
        newOrderGoods.setExpressOrderCode(param.getExpressOrder());
        newOrderGoods.setExpressCode(param.getExpressCode());
        newOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.ALSENDGOODS);
        newOrderGoods.setPickingCardState(PickingCardStateEnum.VERIFICATE);
        newOrderGoods.setDeliveryTime(new DateTime());
        preOrderGoodsMapper.updateById(newOrderGoods);

    }
    @Override
    public AdministrationDetailsResult getOrderDetails(OrderDetailsParam param) {
        PreOrderInfo info = preOrderInfoMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("????????????????????????");
        }
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, info.getId()));
        List<PreOrderOperateRecord> preOrderOperateRecordlist = preOrderOperateRecordMapper.selectList(Wrappers.<PreOrderOperateRecord>lambdaQuery()
                .eq(PreOrderOperateRecord::getOrderId, info.getId()));
        PreActivityInfo preActivityInfo = preActivityInfoMapper.selectById(info.getActivityInfoId());
        AdministrationDetailsResult result = new AdministrationDetailsResult();
        //(value = "????????????")
        result.setOrderCode(info.getOrderCode());
        //????????????
        result.setActivityName(preActivityInfo.getActivityName());
        //(value = "????????????")
        result.setShopName(info.getShopName());
        //(value = "???????????????")
        result.setGuideName(info.getGuideName());
        //(value = "?????????")
        result.setTotalPrice(info.getTotalPrice());
        //(value = "????????????")
        result.setOrderState(info.getOrderState());
        //(value = "????????????")
        result.setCreateTime(info.getCreateTime());
        //(value = "????????????")
        result.setConfirmTime(info.getConfirmTime());
        //(value = "????????????")
        result.setDeliveryTime(info.getDeliveryTime());
        //(value = "????????????url")
        result.setTicketUrl(info.getTicketUrl());
        //(value = "????????????")
        result.setBuyerName(info.getBuyerName());
        //(value = "????????????")
        result.setBuyerPhone(info.getBuyerPhone());
        //(value = "??????????????????")
        result.setBuyerAddress(info.getBuyerAddress());
        //(value = "????????????-???")
        result.setBuyerProvince(info.getBuyerProvince());
        //(value = "????????????-???")
        result.setBuyerCity(info.getBuyerCity());
        //(value = "????????????-???")
        result.setBuyerDistrict(info.getBuyerDistrict());
        //(value = "??????????????????")
        result.setBuyerPostalCode(info.getBuyerPostalCode());
        //(value = "????????????")
        result.setDetailsList(preOrderGoodsList);
        //(value = "????????????")
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
            throw new ServiceException("????????????????????????");
        }
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, info.getOrderId()));
        List<PreOrderOperateRecord> preOrderOperateRecordlist = preOrderOperateRecordMapper.selectList(Wrappers.<PreOrderOperateRecord>lambdaQuery()
                .eq(PreOrderOperateRecord::getOrderId, info.getOrderId()));
        AfterSalesDetailsResult result = new AfterSalesDetailsResult();
        BeanUtils.copyProperties(info, result);
        //(value = "????????????")
        result.setDetailsList(preOrderGoodsList);
        //(value = "????????????")
        result.setOperationList(preOrderOperateRecordlist);
        return result;
    }

    @Override
    public IPage<PreOrderGoodsResult> pagereadySalesList(ReadyListParam param) {
        return preOrderInfoMapper.pagereadySalesList(param.page(),param).convert(result -> {
            result.setDeliveryDetailAddress(result.getDeliveryProvince() + result.getDeliveryCity() + result.getDeliveryDistrict() + result.getDeliveryAddress());
            return result;
        });

    }

    @Override
    public IPage<ReportOrderPageResult> pageOrderReportList(ReportFormParam param) {
        IPage<ReportOrderPageResult> page = preOrderInfoMapper.pageOrderReportList(param.page(), param);
        return page;
    }

    @Override
    //???????????????
    public IPage<ReportGuidePageResult> achievementsGuide(ReportFormParam param) {
        return preOrderInfoMapper.achievementsGuide(param.page(), param);
    }

    @Override
    //????????????????????????
    public IPage<OrderPageResult> pageOrderPageResultList(AdministrationListParam param) {
        return preOrderInfoMapper.pageOrderPageResultList(param.page(), param);
    }

    @Override
    //????????????????????????
    public IPage<SalePageResult> pageSalePageResultList(AdministrationListParam param) {
        return preOrderInfoMapper.pageSalePageResultList(param.page(), param);
    }

    @Override
    @Transactional
    public void importOrderCode(FileUploadParam param) {
        //?????????????????????????????????
        MultipartFile file = param.getFile();
        String fileName = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new ServiceException("????????????????????????????????????");
        }
        List<Map<String, String>> dataMap = PoiUtil.readExcel(inputStream, fileName, 0, 0, 0);

        if (null == dataMap || dataMap.size() == 0) {
            throw new ServiceException("????????????????????????????????????");
        }

        //???????????????????????????????????????
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
        //????????????????????????
        if (fieldMap.size() != 4) {
            throw new ServiceException("????????????");
        }

        //????????????????????????
        List<Long> ids = new ArrayList<>();
        Map<Long, Map<String, String>> importMap = new HashMap<>();
        for (int i = 0; i < dataMap.size(); i++) {
            Map<String, String> item = dataMap.get(i);
            //??????????????????
            String expressCodeName = fieldMap.get("expressCode");
            String expressOrderCodeName = fieldMap.get("expressOrderCode");
            String expressName = fieldMap.get("expressName");
            String idName = fieldMap.get("id");

            Long id = Long.valueOf(item.get(idName));
            boolean expressCodeSign = StrUtil.isBlank(item.get(expressCodeName));
            boolean expressOrderCodeSign = StrUtil.isBlank(item.get(expressOrderCodeName));
            boolean expressNameSign = StrUtil.isBlank(item.get(expressName));
            boolean idSign = ObjectUtil.isNull(id);

            //???????????????????????????????????????????????????
            if (expressCodeSign && expressOrderCodeSign && expressNameSign) {
                continue;
            }

            if (expressCodeSign) {
                throw new ServiceException("??????????????????????????????" + expressCodeName + "????????????");
            } else if (expressOrderCodeSign) {
                throw new ServiceException("??????????????????????????????" + expressOrderCodeName + "????????????");
            } else if (idSign) {
                throw new ServiceException("id??????????????????????????????????????????????????????????????????");
            } else if (expressNameSign) {
                throw new ServiceException("??????????????????????????????" + expressName + "????????????????????????");
            }

            ids.add(id);
            importMap.put(id, item);
        }

        if (null != ids && ids.size() > 0) {
            //???????????????id????????????????????????id????????????????????????????????????????????????????????????????????????????????????
            List<PreOrderGoods> preOrderGoods = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .in(PreOrderGoods::getId, ids)
                    .nested(item->item.eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE).or().eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.ALSENDGOODS))
                    .orderByAsc(PreOrderGoods::getGoodsType)
                    .orderByDesc(PreOrderGoods::getCreateTime)
            );

            List<PreOrderGoods> pretakeList = preOrderGoods.stream().filter(item -> ObjectUtil.equal(item.getOrderGoodsState(), OrderGoodsStateEnum.PRETAKE)).collect(Collectors.toList());
            List<PreOrderGoods> alsendgoodsList = preOrderGoods.stream().filter(item -> ObjectUtil.equal(item.getOrderGoodsState(), OrderGoodsStateEnum.ALSENDGOODS)).collect(Collectors.toList());
            //????????????????????????

            log.info("??????????????????????????????");

            //????????????????????????????????????????????????c?????????
            if (null != pretakeList && pretakeList.size() > 0) {
                for (int i = 0; i < pretakeList.size(); i++) {
                    PreOrderGoods item = preOrderGoods.get(i);
                    Map<String, String> itemDataMap = importMap.get(item.getId());

                    if (null == itemDataMap || itemDataMap.size() == 0) {
                        throw new ServiceException("??????????????????????????????????????????");
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

                    log.info("??????????????????????????????????????????{id=" + id + ", expressCode=" + expressCode + ", expressOrderCode" + expressOrderCode + ", expressName" + expressName + "}");
                    this.inputOrderNumber(inputOrderNumberParam);
                }
            }

            //??????????????????????????????
            if (null != alsendgoodsList && alsendgoodsList.size() > 0) {
                for (int i = 0; i < alsendgoodsList.size(); i++) {
                    PreOrderGoods item = alsendgoodsList.get(i);
                    Map<String, String> itemDataMap = importMap.get(item.getId());

                    if (null == itemDataMap || itemDataMap.size() == 0) {
                        throw new ServiceException("??????????????????????????????????????????");
                    }

                    String expressCode = itemDataMap.get(fieldMap.get("expressCode"));
                    String expressOrderCode = itemDataMap.get(fieldMap.get("expressOrderCode"));
                    String id = itemDataMap.get(fieldMap.get("id"));
                    String expressName = itemDataMap.get(fieldMap.get("expressName"));

                    //??????????????????????????????
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
                        //??????????????????
                        changeFlashState(updateItem.getOrderId(),updateItem.getExpressOrderCode());
                        log.info("??????????????????????????????????????????{id=" + id + ", expressCode=" + expressCode + ", expressOrderCode" + expressOrderCode + ", expressName" + expressName + "}");
                    } else {
                        throw new ServiceException("????????????????????????");
                    }
                }
            }
            log.info("??????????????????????????????");
        } else {
            throw new ServiceException("????????????????????????????????????");
        }
    }

    @Override
    @Transactional
    public void flashImportOrderCode(FileUploadParam param) {
        //?????????????????????????????????
        MultipartFile file = param.getFile();
        String fileName = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new ServiceException("????????????????????????????????????");
        }
        List<Map<String, String>> dataMap = PoiUtil.readExcel(inputStream, fileName, 0, 0, 0);

        if (null == dataMap || dataMap.size() == 0) {
            throw new ServiceException("????????????????????????????????????");
        }

        //???????????????????????????????????????
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
        //????????????????????????
        if (fieldMap.size() != 4) {
            throw new ServiceException("????????????");
        }

        //????????????????????????
        List<Long> ids = new ArrayList<>();
        Map<Long, Map<String, String>> importMap = new HashMap<>();
        for (int i = 0; i < dataMap.size(); i++) {
            Map<String, String> item = dataMap.get(i);
            //??????????????????
            String expressCodeName = fieldMap.get("expressCode");
            String expressOrderCodeName = fieldMap.get("expressOrderCode");
            String expressName = fieldMap.get("expressName");
            String idName = fieldMap.get("id");

            Long id = Long.valueOf(item.get(idName));
            boolean expressCodeSign = StrUtil.isBlank(item.get(expressCodeName));
            boolean expressOrderCodeSign = StrUtil.isBlank(item.get(expressOrderCodeName));
            boolean expressNameSign = StrUtil.isBlank(item.get(expressName));
            boolean idSign = ObjectUtil.isNull(id);
            //???????????????????????????????????????????????????
            if (expressCodeSign && expressOrderCodeSign && expressNameSign) {
                continue;
            }
            if (expressCodeSign) {
                throw new ServiceException("??????????????????????????????" + expressCodeName + "????????????");
            } else if (expressOrderCodeSign) {
                throw new ServiceException("??????????????????????????????" + expressOrderCodeName + "????????????");
            } else if (idSign) {
                throw new ServiceException("id??????????????????????????????????????????????????????????????????");
            } else if (expressNameSign) {
                throw new ServiceException("??????????????????????????????" + expressName + "????????????????????????");
            }

            ids.add(id);
            importMap.put(id, item);
        }

        if (null != ids && ids.size() > 0) {
            //???????????????id????????????????????????id????????????????????????????????????????????????????????????????????????????????????
            List<PreOrderInfo> infoList = preOrderInfoMapper.selectList(Wrappers.<PreOrderInfo>lambdaQuery()
                    .in(PreOrderInfo::getId, ids));
            log.info("??????????????????????????????");
            //????????????????????????????????????????????????c?????????
            if (null != infoList && infoList.size() > 0) {
                for (int i = 0; i < infoList.size(); i++) {
                    PreOrderInfo item = infoList.get(i);
                    Map<String, String> itemDataMap = importMap.get(item.getId());

                    if (null == itemDataMap || itemDataMap.size() == 0) {
                        throw new ServiceException("??????????????????????????????????????????");
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

                    log.info("??????????????????????????????????????????{id=" + id + ", expressCode=" + expressCode + ", expressOrderCode" + expressOrderCode + ", expressName" + expressName + "}");
                    this.inputFlashOrderNumber(inputOrderNumberParam);
                }
            }
        } else {
            throw new ServiceException("????????????????????????????????????");
        }
    }

    //????????????id???????????????
    private void changeFlashState(Long id,String code){
        PreOrderInfo preOrderInfo=preOrderInfoMapper.selectById(id);
        if(preOrderInfo!=null ){
            if(preOrderInfo.getFlashId()!=null){
                PreFlashOrderInfo preFlashOrderInfo = flashOrderInfoMapper.selectById(preOrderInfo.getFlashId());
                if(preFlashOrderInfo!=null){
                    PreFlashOrderInfo newFlashOrderInfo = new PreFlashOrderInfo();
                    newFlashOrderInfo.setId(preFlashOrderInfo.getId());
                    newFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.WRITTENOFF);
                    newFlashOrderInfo.setExpressOrder(code);
                    int orderInfo=flashOrderInfoMapper.updateById(newFlashOrderInfo);
                    if(orderInfo < 0){
                        throw new ServiceException("???????????????");
                    }
                }
            }

        }


    }
}
