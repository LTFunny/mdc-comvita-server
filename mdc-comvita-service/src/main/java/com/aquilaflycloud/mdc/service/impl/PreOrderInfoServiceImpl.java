package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.feign.consumer.org.IUserConsumer;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.member.MemberEditParam;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.member.MemberScanRewardResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityRewardResult;
import com.aquilaflycloud.mdc.result.pre.PreOrderGoodsGetResult;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoPageResult;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.aquilaflycloud.mdc.service.*;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.org.service.provider.entity.PUmsUserDetail;
import com.aquilaflycloud.org.service.provider.entity.PUserInfo;
import com.aquilaflycloud.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author pengyongliang
 * @Date 2020/12/29 16:28
 * @Version 1.0
 */
@Service
public class PreOrderInfoServiceImpl implements PreOrderInfoService {

    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;

    @Resource
    private PreRefundOrderInfoMapper preRefundOrderInfoMapper;

    @Resource
    private PreOrderOperateRecordService orderOperateRecordService;

    @Resource
    private WechatMiniProgramSubscribeMessageService wechatMiniProgramSubscribeMessageService;

    @Resource
    private MemberInfoMapper memberInfoMapper;

    @Resource
    private PreActivityInfoMapper activityInfoMapper;

    @Resource
    private PreGoodsInfoMapper goodsInfoMapper;

    @Resource
    private PrePickingCardMapper prePickingCardMapper;

    @Resource
    private PreOrderGoodsMapper preOrderGoodsMapper;

    @Resource
    private PreRuleInfoMapper preRuleInfoMapper;

    @Resource
    private MemberRewardService memberRewardService;

    @Resource
    private MemberService memberService;

    @Resource
    private IUserConsumer userConsumer;

    @Transactional
    @Override
    public int addStatConfirmOrder(PreStayConfirmOrderParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreOrderInfo preOrderInfo = new PreOrderInfo();
        PreActivityInfo preActivityInfo = activityInfoMapper.selectById(param.getActivityInfoId());
        if(preActivityInfo == null){
            throw new ServiceException("???????????????");
        }
//        PreGoodsInfo goodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
        List<PreGoodsInfo> goods = getGoods(preActivityInfo.getRefGoods());
        if(CollUtil.isEmpty(goods)){
            throw new ServiceException("????????????????????????,?????????????????????");
        }
        BeanUtil.copyProperties(param,preOrderInfo);
        MdcUtil.setMemberInfo(preOrderInfo,infoResult);
        preOrderInfo.setMemberId(infoResult.getId());
        preOrderInfo.setFailSymbol(FailSymbolEnum.YES_NO);
        preOrderInfo.setOrderState(OrderInfoStateEnum.STAYCONFIRM);
        preOrderInfo.setScore(new BigDecimal("0"));
        preOrderInfo.setOrderCode(MdcUtil.getTenantIncIdStr("preOrderCode", "O" + DateTime.now().toString("yyMMdd"), 5));
        PUmsUserDetail pUmsUserDetail = userConsumer.getUserOrganization(param.getGuideId());
        if(null == pUmsUserDetail){
            throw new ServiceException("????????????????????????");
        }
        preOrderInfo.setGuideId(pUmsUserDetail.getUserId());
        preOrderInfo.setGuideName(pUmsUserDetail.getRealName());
        preOrderInfo.setShopId(pUmsUserDetail.getOrgId());
        preOrderInfo.setShopName(pUmsUserDetail.getOrgName());
        preOrderInfo.setShopAddress(pUmsUserDetail.getOrgAddress());
        preOrderInfo.setActivityType(ActivityTypeEnum.PRE_SALES);
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("??????????????????????????????");
        }
        String content =  param.getBuyerName() + "???"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"?????????????????????????????????????????????";
        orderOperateRecordService.addOrderOperateRecordLog(param.getBuyerName(),preOrderInfo.getId(),content);
        //????????????????????????
        MemberEditParam memberEditParam = new MemberEditParam();
        if (StrUtil.isBlank(infoResult.getRealName())) {
            memberEditParam.setRealName(param.getBuyerName());
        }
        if (infoResult.getSex() == null) {
            memberEditParam.setSex(param.getBuyerSex());
        }
        if (infoResult.getBirthday() == null) {
            memberEditParam.setBirthday(param.getBuyerBirthday());
        }
        if (StrUtil.isBlank(infoResult.getProvince())) {
            memberEditParam.setProvince(param.getBuyerProvince());
        }
        if (StrUtil.isBlank(infoResult.getCity())) {
            memberEditParam.setCity(param.getBuyerCity());
        }
        if (StrUtil.isBlank(infoResult.getCounty())) {
            memberEditParam.setCounty(param.getBuyerDistrict());
        }
        if (StrUtil.isBlank(infoResult.getAddress())) {
            memberEditParam.setAddress(param.getBuyerAddress());
        }
        if (BeanUtil.isNotEmpty(memberEditParam)) {
            memberService.edit(memberEditParam);
        }
        return orderInfo;
    }

    @Override
    public void updateStatConfirmOrder(PreStayConfirmOrderParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderId());
        Long id = preOrderInfo.getGuideId();
        PreOrderInfo newOrderInfo = new PreOrderInfo();
        BeanUtil.copyProperties(param,newOrderInfo);
        newOrderInfo.setId(preOrderInfo.getId());
        newOrderInfo.setGuideId(id);
        newOrderInfo.setFailSymbol(FailSymbolEnum.YES_NO);
        newOrderInfo.setReason("");
        int orderInfo = preOrderInfoMapper.updateById(newOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("??????????????????????????????");
        }
        String content =  param.getBuyerName() + "???"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"?????????????????????";
        orderOperateRecordService.addOrderOperateRecordLog(param.getBuyerName(),preOrderInfo.getId(),content);
    }

    @Transactional
    @Override
    public void validationConfirmOrder(PreConfirmOrderParam param) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getId());
        if (null == preOrderInfo) {
            throw new ServiceException("??????????????????");
        }
        PreOrderInfo newOrderInfo = new PreOrderInfo();
        BeanUtil.copyProperties(param, newOrderInfo);
        if (param.getIsThrough() == 0) {
            newOrderInfo.setOrderState(OrderInfoStateEnum.STAYRESERVATION);
        }else {
            newOrderInfo.setFailSymbol(FailSymbolEnum.YES);
        }
        List<PreOrderGoods> orderGoodsList = new ArrayList<>();
        if (param.getIsThrough() == 0) {
            PreActivityInfo preActivityInfo = activityInfoMapper.selectById(preOrderInfo.getActivityInfoId());
            if (null == preActivityInfo) {
                throw new ServiceException("???????????????");
            }
//            PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
            List<PreGoodsInfo> goods = getGoods(preActivityInfo.getRefGoods());
            PreGoodsInfo preGoodsInfo = goods.get(0);
            param.getPrePickingCardList().stream().forEach(card -> {
                PrePickingCardValidationParam param1 = new PrePickingCardValidationParam();
                param1.setPickingCode(card);
                PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                        .eq(PrePickingCard::getPickingCode, card)
                        .eq(PrePickingCard::getPickingState, PickingCardStateEnum.NO_SALE));
                if (prePickingCard == null) {
                    throw new ServiceException("???????????????????????????????????????");
                }
                PrePickingCard newPickingCard = new PrePickingCard();
                newPickingCard.setId(prePickingCard.getId());
                newPickingCard.setPickingState(PickingCardStateEnum.SALE);
                //?????????????????????
                int updateCard = prePickingCardMapper.updateById(newPickingCard);
                if (updateCard < 0) {
                    throw new ServiceException("??????????????????????????????");
                }
                //????????????
                PreOrderGoods preOrderGoods = new PreOrderGoods();
                preOrderGoods.setCardId(prePickingCard.getId());
                preOrderGoods.setOrderId(preOrderInfo.getId());
                preOrderGoods.setCardCode(card);
                preOrderGoods.setGuideId(preOrderInfo.getGuideId());
                preOrderGoods.setGuideName(preOrderInfo.getGuideName());
                preOrderGoods.setGoodsId(preGoodsInfo.getId());
                preOrderGoods.setGoodsDescription(preGoodsInfo.getGoodsDescription());
                preOrderGoods.setGoodsPicture(preGoodsInfo.getGoodsPicture());
                preOrderGoods.setGoodsCode(preGoodsInfo.getGoodsCode());
                preOrderGoods.setOrderCode(preOrderInfo.getOrderCode());
                preOrderGoods.setReserveShopId(preOrderInfo.getShopId()+"");
                preOrderGoods.setReserveShop(preOrderInfo.getShopName());
                preOrderGoods.setGoodsName(preGoodsInfo.getGoodsName());
                preOrderGoods.setGoodsType(preGoodsInfo.getGoodsType());
                preOrderGoods.setGoodsPrice(preGoodsInfo.getGoodsPrice());
                preOrderGoods.setTenantId(preOrderInfo.getTenantId());
                preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.PREPARE);
                preOrderGoods.setGiftsSymbol(GiftsSymbolEnum.AFTER);
                orderGoodsList.add(preOrderGoods);
            });
            //?????????????????????
            if (StrUtil.isNotBlank(preActivityInfo.getRewardRuleContent()) && !StrUtil.equals(preActivityInfo.getRewardRuleContent(), "[]")) {
                MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
                List<PreActivityRewardResult> rewardRuleList = JSONUtil.toList(JSONUtil.parseArray(preActivityInfo.getRewardRuleContent()), PreActivityRewardResult.class);
                for (PreActivityRewardResult rewardRule : rewardRuleList) {
                    memberRewardService.addPreActivityRewardRecord(memberInfo, rewardRule.getRewardType(), rewardRule.getRewardValue(), preActivityInfo.getId());
                }
            }
            //???????????????
            newOrderInfo.setTotalPrice(orderGoodsList.get(0).getGoodsPrice().multiply(new BigDecimal(orderGoodsList.size())));
            newOrderInfo.setFailSymbol(FailSymbolEnum.NO);
            newOrderInfo.setConfirmTime(new Date());
            //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            int giftsCount = preOrderInfoMapper.countOrderInfoGiftsInfo(preOrderInfo.getMemberId(),preOrderInfo.getActivityInfoId());
            if(giftsCount <= 0) {
                //??????????????????????????????????????????
                PreRuleInfo preRuleInfo = preRuleInfoMapper.selectOne(Wrappers.<PreRuleInfo>lambdaQuery()
                        .eq(PreRuleInfo::getId, preActivityInfo.getRefRule())
                        .eq(PreRuleInfo::getRuleType, RuleTypeEnum.ORDER_GIFTS));
                if (preRuleInfo != null) {
                    PreOrderGoods preOrderGoods = new PreOrderGoods();
                    List<PreRuleGoodsParam> preRuleGoodsParams = JSONUtil.toList(JSONUtil.parseArray(preRuleInfo.getTypeDetail()), PreRuleGoodsParam.class);
                    preRuleGoodsParams.forEach(k -> {
                        PreGoodsInfo preGoodsInfo1 = goodsInfoMapper.selectById(k.getGoodsId());
                        BeanUtil.copyProperties(preGoodsInfo1, preOrderGoods);
                        preOrderGoods.setId(null);
                        preOrderGoods.setCreateTime(new Date());
                        preOrderGoods.setLastUpdateTime(new Date());
                        preOrderGoods.setOrderId(preOrderInfo.getId());
                        preOrderGoods.setGoodsId(preGoodsInfo1.getId());
                        preOrderGoods.setGoodsType(GoodsTypeEnum.GIFTS);
                        preOrderGoods.setDeliveryProvince(preOrderInfo.getBuyerProvince());
                        preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.PRETAKE);
                        preOrderGoods.setGoodsCode(preGoodsInfo1.getGoodsCode());
                        preOrderGoods.setOrderCode(preOrderInfo.getOrderCode());
                        preOrderGoods.setReserveShopId(preOrderInfo.getShopId() + "");
                        preOrderGoods.setReserveShop(preOrderInfo.getShopName());
                        preOrderGoods.setDeliveryCity(preOrderInfo.getBuyerCity());
                        preOrderGoods.setDeliveryDistrict(preOrderInfo.getBuyerDistrict());
                        preOrderGoods.setReserveId(preOrderInfo.getMemberId());
                        preOrderGoods.setReserveName(preOrderInfo.getBuyerName());
                        preOrderGoods.setReservePhone(preOrderInfo.getBuyerPhone());
                        preOrderGoods.setDeliveryAddress(preOrderInfo.getBuyerAddress());
                        preOrderGoods.setGuideId(preOrderInfo.getGuideId());
                        preOrderGoods.setGuideName(preOrderInfo.getGuideName());
                        preOrderGoods.setGiftsSymbol(GiftsSymbolEnum.NOTAFTER);
                        orderGoodsList.add(preOrderGoods);
                    });
                }
            }
            preOrderGoodsMapper.insertAllBatch(orderGoodsList);
        }
        int updateOrder = preOrderInfoMapper.updateById(newOrderInfo);
        if(updateOrder < 0){
            throw new ServiceException("?????????????????????");
        }
        String content;
        if (param.getIsThrough() == 0) {
            content = ("????????????" + preOrderInfo.getGuideName() + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " ????????????" +
                    preOrderInfo.getOrderCode() + "??????????????????");
            //????????????????????????
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERAUDIT, null,
                    preOrderInfo.getOrderCode(), "???????????????????????????", "????????????",
                    "????????????????????????????????????????????????????????????");
        } else {
            content = ("????????????" + preOrderInfo.getGuideName() + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " ????????????" +
                    preOrderInfo.getOrderCode() + "?????????????????????????????????????????????" + preOrderInfo.getReason());
            //????????????????????????
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERAUDIT, null,
                    preOrderInfo.getOrderCode(), "???????????????????????????", "?????????",
                    "?????????????????????????????????");
        }
        orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getGuideName(), preOrderInfo.getId(), content);
    }

    @Override
    public void verificationOrder(PreOrderVerificationParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
        .eq(PrePickingCard::getPassword,param.getPickingCode())
        .eq(PrePickingCard::getPickingState,PickingCardStateEnum.RESERVE));
        if(prePickingCard == null){
            throw new ServiceException("?????????????????????,??????????????????");
        }
        PrePickingCard newPickingCard = new PrePickingCard();
        newPickingCard.setId(prePickingCard.getId());
        newPickingCard.setPickingState(PickingCardStateEnum.VERIFICATE);
        int updateCard = prePickingCardMapper.updateById(newPickingCard);
        if(updateCard < 0){
            throw new ServiceException("????????????");
        }
        PreOrderGoods orderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
        .eq(PreOrderGoods::getCardId,prePickingCard.getId()));
        PreOrderGoods newOrderGoods = new PreOrderGoods();
        newOrderGoods.setId(orderGoods.getId());
        newOrderGoods.setVerificaterId(param.getVerificaterId());
        newOrderGoods.setVerificaterName(param.getVerificaterName());
        newOrderGoods.setVerificaterOrgIds(param.getVerificaterOrgIds());
        newOrderGoods.setVerificaterOrgNames(param.getVerificaterOrgNames());
        newOrderGoods.setTakeTime(new Date());
        preOrderGoodsMapper.updateById(newOrderGoods);
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
            .eq(PreOrderGoods::getOrderId,orderGoods.getOrderId())
            .notIn(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));

        int result = preOrderGoodsMapper.pickingCardGet(orderGoods.getOrderId(), PickingCardStateEnum.VERIFICATE);
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(orderGoods.getOrderId());
        PreOrderInfo newOrderInfo = new PreOrderInfo();
        newOrderInfo.setId(preOrderInfo.getId());
        List<PreOrderGoods> OrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,orderGoods.getOrderId())
                .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));
        if(preOrderGoodsList.size() == result){
            newOrderInfo.setChildOrderState(ChildOrderInfoStateEnum.STATELESS);
            if(OrderGoodsList.size() > 0) {
                newOrderInfo.setOrderState(OrderInfoStateEnum.STAYSENDGOODS);
            }else {
                newOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
            }
        }else {
            newOrderInfo.setChildOrderState(ChildOrderInfoStateEnum.RESERVATION_DELIVERY);
            MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
            Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord(memberInfo,null,preOrderInfo.getId(),preOrderInfo.getTotalPrice(),true);
            if (CollUtil.isNotEmpty(map) && map.get(RewardTypeEnum.SCORE) != null) {
                newOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
            }
        }
        int order = preOrderInfoMapper.updateById(newOrderInfo);
        if(order < 0){
            throw new ServiceException("????????????????????????");
        }
        String content = orderGoods.getVerificaterName() + "????????????" +DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                + "????????????(" + preOrderInfo.getOrderCode() + ")??????????????????";
        orderOperateRecordService.addOrderOperateRecordLog(orderGoods.getVerificaterName(),preOrderInfo.getId(),content);
    }


    public PreOrderInfoPageResult orderInfo(PreOrderInfo order){
        PreOrderInfoPageResult result = BeanUtil.copyProperties(order, PreOrderInfoPageResult.class);
        result.setState(order.getOrderState().getName());
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));
        if(null != preOrderGoods){
            result.setGiftsGoodsInfo(preOrderGoods);
        }
        int orderGoodsCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .ne(PreOrderGoods::getOrderGoodsState,OrderGoodsStateEnum.PREPARE)
                .notIn(PreOrderGoods::getId,preOrderGoods == null ? 0L : preOrderGoods.getId()));
        if(orderGoodsCount > 0){
            result.setReservationNum(orderGoodsCount);
        }else {
            result.setReservationNum(0);
        }
        int takenCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .eq(PreOrderGoods::getOrderGoodsState,OrderGoodsStateEnum.TAKEN)
                .notIn(PreOrderGoods::getId,preOrderGoods == null ? 0L : preOrderGoods.getId()));
        result.setIngdeliveryNum(takenCount);
        PreActivityInfo activityInfo = activityInfoMapper.selectById(order.getActivityInfoId());
//        PreGoodsInfo goodsInfo = goodsInfoMapper.selectById(activityInfo.getRefGoods());
        List<PreGoodsInfo> goods = getGoods(activityInfo.getRefGoods());
        PreGoodsInfo goodsInfo = goods.get(0);
        result.setPreGoodsInfo(goodsInfo);
        int goodsCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.PREPARE));
        result.setGoodsInfoNum(goodsCount);
        int cardCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .in(PreOrderGoods::getPickingCardState,PickingCardStateEnum.RESERVE,PickingCardStateEnum.VERIFICATE));
        int goodsCount2 = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.PREPARE));
        if(order.getOrderState().equals(OrderInfoStateEnum.WAITINGDELIVERY) && cardCount != goodsCount2){
            result.setState("????????????");
        }
        PreRefundOrderInfo refundOrderInfo = preRefundOrderInfoMapper.selectOne(Wrappers.<PreRefundOrderInfo>lambdaQuery()
        .eq(PreRefundOrderInfo::getOrderId,order.getId()));
        if(null != refundOrderInfo){
            result.setRefundTime(refundOrderInfo.getRefundTime());
            result.setRefundPrice(refundOrderInfo.getRefundPrice());
        }
        return result;
    }

    @Override
    public IPage<PreOrderInfoPageResult> orderInfoPage(PreOrderInfoPageApiParam param) {
        Long memberId = MdcUtil.getRequireCurrentMemberId();
        return preOrderInfoMapper.selectPage(param.page(), Wrappers.<PreOrderInfo>lambdaQuery()
                .eq(PreOrderInfo::getMemberId, memberId)
                .eq(param.getActivityType() != null, PreOrderInfo::getActivityType, param.getActivityType())
                .eq(param.getOrderState() != null, PreOrderInfo::getOrderState, param.getOrderState())
                .exists(param.getAfter() == 1, "select id from pre_refund_order_info where pre_order_info.id = pre_refund_order_info.order_id")
                .orderByDesc(PreOrderInfo::getCreateTime)
        ).convert(this::orderInfo);
    }

    @Override
    public IPage<PreOrderInfoPageResult> refundOrderPage(PreOrderInfoPageParam param) {
        PreOrderInfoPageParam pageParam = new PreOrderInfoPageParam();
        pageParam.setAfter(1);
        pageParam.setPageNum(1L);
        pageParam.setBuyerPhone(param.getBuyerPhone());
        pageParam.setPageSize(9999L);
        List<Long> longs = preOrderInfoMapper.pageOrderInfoPageResult(pageParam.page(),pageParam).getRecords()
                .stream().map(PreOrderInfo::getId).collect(Collectors.toList());
        IPage<PreOrderInfoPageResult> page =  preOrderInfoMapper.selectPage(param.page(),Wrappers.<PreOrderInfo>lambdaQuery()
        .eq(PreOrderInfo::getBuyerPhone,param.getBuyerPhone())
        .eq(PreOrderInfo::getGuideId,MdcUtil.getCurrentUserId())
        .ne(PreOrderInfo::getOrderState,OrderInfoStateEnum.STAYCONFIRM)
        .notIn(CollUtil.isNotEmpty(longs), PreOrderInfo::getId,longs)).convert(order ->{
            PreOrderInfoPageResult result  = orderInfo(order);
            return result;
        });
        return page;
    }

    @Override
    public IPage<PreOrderInfoPageResult> guideMyOrderPage(PreOrderInfoPageParam param) {
        Long id = MdcUtil.getCurrentUserId();
        param.setGuideId(id);
        IPage<PreOrderInfo> page =  preOrderInfoMapper.pageOrderInfoPageResult(param.page(),param);
        IPage<PreOrderInfoPageResult> pageResultIPage = page.convert(order ->{
            PreOrderInfoPageResult result  = orderInfo(order);
            return result;
        });
        return pageResultIPage;
    }

    @Override
    public PreOrderInfoPageResult orderInfoGet(PreOrderInfoGetParam param) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderInfoId());
        if(preOrderInfo == null){
            throw new ServiceException("?????????????????????");
        }
        PreOrderInfoPageResult result = orderInfo(preOrderInfo);
        return result;
    }

    @Transactional
    @Override
    public void confirmReceiptOrderGoods(PreOrderGoodsGetParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        confirmReceiptOrderGoods(param.getOrderGoodsId(), infoResult.getRealName());
    }

    private void confirmReceiptOrderGoods(Long orderGoodsId, String operatorName) {
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectById(orderGoodsId);
        PreOrderGoods newOrderGoods = new PreOrderGoods();
        newOrderGoods.setId(preOrderGoods.getId());
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(preOrderGoods.getOrderId());
        newOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.TAKEN);
        newOrderGoods.setVerificaterId(preOrderInfo.getGuideId());
        newOrderGoods.setVerificaterName(preOrderInfo.getGuideName());
        newOrderGoods.setVerificaterOrgIds(preOrderInfo.getCreatorOrgIds());
        newOrderGoods.setVerificaterOrgNames(preOrderInfo.getCreatorOrgNames());
        int orderGoods = preOrderGoodsMapper.updateById(newOrderGoods);
        if (orderGoods < 0) {
            throw new ServiceException("???????????????????????????");
        }
        String content = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "????????????????????????";
        orderOperateRecordService.addOrderOperateRecordLog(operatorName, preOrderInfo.getId(), content);
        //??????????????????,????????????????????????
        MemberInfo memberInfo = memberInfoMapper.selectById(preOrderGoods.getReserveId());
        wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(memberInfo.getWxAppId())
                        .setOpenId(memberInfo.getOpenId())), MiniMessageTypeEnum.PREORDERGOODSSIGN, null,
                preOrderGoods.getGoodsName(), preOrderGoods.getExpressName(), preOrderGoods.getExpressOrderCode(),
                "???????????????????????????????????????????????????");
    }

    @Transactional
    @Override
    public void confirmReceiptOrder(PreOrderGetParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        confirmReceiptOrder(param.getOrderId(), infoResult);
    }

    private void confirmReceiptOrder(Long orderId, MemberInfo memberInfo) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(orderId);
        PreOrderInfo newOrderInfo = new PreOrderInfo();
        newOrderInfo.setId(preOrderInfo.getId());
        newOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
        Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord
                (memberInfo, null, preOrderInfo.getId(), preOrderInfo.getTotalPrice(), true);
        if (CollUtil.isNotEmpty(map) && map.get(RewardTypeEnum.SCORE) != null) {
            newOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
        }
        int order = preOrderInfoMapper.updateById(newOrderInfo);
        if (order < 0) {
            throw new ServiceException("?????????????????????");
        }
        if(ActivityTypeEnum.PRE_SALES.equals(preOrderInfo.getActivityType())){
            PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
                    .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));
            if(preOrderGoods != null) {
                PreOrderGoods newOrderGoods = new PreOrderGoods();
                newOrderGoods.setId(preOrderGoods.getId());
                newOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.TAKEN);
                int orderGoods = preOrderGoodsMapper.updateById(newOrderGoods);
                if (orderGoods < 0) {
                    throw new ServiceException("?????????????????????????????????");
                }
                //????????????,????????????????????????
                wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                                .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERSIGN, null,
                        preOrderInfo.getOrderCode(), preOrderGoods.getGoodsName(), preOrderGoods.getExpressName(),
                        preOrderGoods.getExpressOrderCode(), "???????????????????????????????????????????????????");
            }
        }else{
            List<PreOrderGoods> list = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderId,preOrderInfo.getId()));
            if(CollUtil.isNotEmpty(list)) {
                for(PreOrderGoods preOrderGoods:list ){
                    PreOrderGoods newOrderGoods = new PreOrderGoods();
                    newOrderGoods.setId(preOrderGoods.getId());
                    newOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.TAKEN);
                    int orderGoods = preOrderGoodsMapper.updateById(newOrderGoods);
                    if (orderGoods < 0) {
                        throw new ServiceException("?????????????????????????????????");
                    }
                    //????????????,????????????????????????
                    wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                                    .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERSIGN, null,
                            preOrderInfo.getOrderCode(), preOrderGoods.getGoodsName(), preOrderGoods.getExpressName(),
                            preOrderGoods.getExpressOrderCode(), "???????????????????????????????????????????????????");
                }
            }
        }
        String content = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                + "????????????(" + preOrderInfo.getOrderCode() + ")????????????????????????";
        orderOperateRecordService.addOrderOperateRecordLog(memberInfo.getRealName(),preOrderInfo.getId(),content);
    }

    @Transactional
    @Override
    public void autoConfirmReceiptOrder(PreOrderGoods orderGoods, String operatorName) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(orderGoods.getOrderId());
        if (preOrderInfo.getActivityType() == ActivityTypeEnum.PRE_SALES
                && orderGoods.getGoodsType() != GoodsTypeEnum.GIFTS) {
            //????????????????????????
            confirmReceiptOrderGoods(orderGoods.getId(), operatorName);
        }
        MemberInfo memberInfo = memberInfoMapper.selectById(orderGoods.getReserveId());
        confirmReceiptOrder(orderGoods.getOrderId(), memberInfo);
    }

    @Override
    public PreOrderGoodsGetResult orderCardGetInfo(PreOrderCardGetParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                .eq(PrePickingCard::getPickingCode,param.getPickingCode()));
        if(prePickingCard == null){
            throw new ServiceException("???????????????????????????");
        }
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getCardId,prePickingCard.getId()));
        PreOrderGoodsGetResult preOrderGoodsGetResult = new PreOrderGoodsGetResult();
        BeanUtil.copyProperties(preOrderGoods,preOrderGoodsGetResult);
        PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(preOrderGoods.getGoodsId());
        preOrderGoodsGetResult.setGoodsInfo(preGoodsInfo);
        MemberInfo memberInfo = memberInfoMapper.selectById(preOrderGoods.getReserveId());
        preOrderGoodsGetResult.setBirthday(memberInfo.getBirthday());
        preOrderGoodsGetResult.setSex(memberInfo.getSex());
        return preOrderGoodsGetResult;
    }

    @Transactional
    @Override
    public void refundOrder(PreOrderRefundParam param) {
        RedisUtil.transactionalLock("refundOrderLock" + param.getOrderId());
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderId());
        if (preOrderInfo == null) {
            throw new ServiceException("???????????????");
        }
        int refundCount = preRefundOrderInfoMapper.selectCount(Wrappers.<PreRefundOrderInfo>lambdaQuery()
                .eq(PreRefundOrderInfo::getOrderId, param.getOrderId())
        );
        if (refundCount > 0) {
            throw new ServiceException("????????????????????????,??????????????????");
        }
        PUserInfo userInfo = userConsumer.getUserByid(MdcUtil.getCurrentUserId());
        PreRefundOrderInfo preRefundOrderInfo = new PreRefundOrderInfo();
        BeanUtil.copyProperties(preOrderInfo, preRefundOrderInfo, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        BeanUtil.copyProperties(param, preRefundOrderInfo);
        preRefundOrderInfo.setRefundCode(MdcUtil.getTenantIncIdStr("preOrderRefundCode", "R" + DateTime.now().toString("yyMMdd"), 5));
        preRefundOrderInfo.setRefundTime(DateTime.now());
        preRefundOrderInfo.setAfterGuideId(MdcUtil.getCurrentUserId());
        preRefundOrderInfo.setAfterGuideName(userInfo.getRealName());
        preRefundOrderInfo.setOrderCreateTime(preOrderInfo.getCreateTime());
        int count = preRefundOrderInfoMapper.insert(preRefundOrderInfo);
        if (count <= 0) {
            throw new ServiceException("??????????????????");
        }
        //??????????????????
        PreOrderInfo orderUpdate = new PreOrderInfo();
        orderUpdate.setId(param.getOrderId());
        orderUpdate.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
        count = preOrderInfoMapper.updateById(orderUpdate);
        if (count <= 0) {
            throw new ServiceException("??????????????????");
        }
        //????????????????????????
//        PreOrderGoods update = new PreOrderGoods();
//        update.setOrderGoodsState(OrderGoodsStateEnum.REFUND);
//        update.setPickingCardState(PickingCardStateEnum.NO_SALE);
//        count = preOrderGoodsMapper.update(update, Wrappers.<PreOrderGoods>lambdaUpdate()
//                .eq(PreOrderGoods::getOrderId, param.getOrderId())
//        );
//        if (count <= 0) {
//            throw new ServiceException("??????????????????");
//        }
        List<PreOrderGoods> list = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,param.getOrderId()));
        if(CollUtil.isNotEmpty(list)) {
            for(PreOrderGoods preOrderGoods : list ){
                PreOrderGoods update = new PreOrderGoods();
                update.setId(preOrderGoods.getId());
                update.setOrderGoodsState(OrderGoodsStateEnum.REFUND);
                update.setPickingCardState(PickingCardStateEnum.NO_SALE);
                count = preOrderGoodsMapper.updateById(update);
                if (count <= 0) {
                    throw new ServiceException("??????????????????");
                }
            }
        }

        //?????????????????????????????????id
        List<Long> cardIdList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .select(PreOrderGoods::getCardId)
                .eq(PreOrderGoods::getOrderId, param.getOrderId())
                .isNotNull(PreOrderGoods::getCardId)
        ).stream().map(PreOrderGoods::getCardId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cardIdList)) {
            //?????????????????????
//            PrePickingCard cardUpdate = new PrePickingCard();
//            cardUpdate.setPickingState(PickingCardStateEnum.NO_SALE);
            //???????????????????????????
//            prePickingCardMapper.update(cardUpdate, Wrappers.<PrePickingCard>lambdaUpdate()
//                    .in(PrePickingCard::getId, cardIdList)
//                    .eq(PrePickingCard::getPickingState, PickingCardStateEnum.SALE)
//            );
            List<PrePickingCard> cards = prePickingCardMapper.selectList(Wrappers.<PrePickingCard>lambdaQuery()
                    .in(PrePickingCard::getId, cardIdList)
                    .eq(PrePickingCard::getPickingState, PickingCardStateEnum.SALE));
            if(CollUtil.isNotEmpty(cards)) {
                for(PrePickingCard card : cards){
                    PrePickingCard cardUpdate = new PrePickingCard();
                    cardUpdate.setId(card.getId());
                    cardUpdate.setPickingState(PickingCardStateEnum.NO_SALE);
                    count = prePickingCardMapper.updateById(cardUpdate);
                    if (count <= 0) {
                        throw new ServiceException("?????????????????????");
                    }
                }
            }

//            cardUpdate.setPickingState(PickingCardStateEnum.CANCEL);
            //??????????????????????????????
//            prePickingCardMapper.update(cardUpdate, Wrappers.<PrePickingCard>lambdaUpdate()
//                    .in(PrePickingCard::getId, cardIdList)
//                    .ne(PrePickingCard::getPickingState, PickingCardStateEnum.NO_SALE)
//            );
            List<PrePickingCard> cards2 = prePickingCardMapper.selectList(Wrappers.<PrePickingCard>lambdaQuery()
                    .in(PrePickingCard::getId, cardIdList)
                    .eq(PrePickingCard::getPickingState, PickingCardStateEnum.NO_SALE));
            if(CollUtil.isNotEmpty(cards2)) {
                for(PrePickingCard card : cards2){
                    PrePickingCard cardUpdate = new PrePickingCard();
                    cardUpdate.setId(card.getId());
                    cardUpdate.setPickingState(PickingCardStateEnum.CANCEL);
                    count = prePickingCardMapper.updateById(cardUpdate);
                    if (count <= 0) {
                        throw new ServiceException("?????????????????????");
                    }
                }
            }
        }
        //??????????????????
        memberRewardService.refundRewardRecord(preOrderInfo.getMemberId(), preOrderInfo.getId());
        //????????????????????????
        orderOperateRecordService.addOrderOperateRecordLog(userInfo.getRealName(), param.getOrderId(), "????????????");
        //????????????????????????
        wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                        .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERREFUNDAUDIT, null,
                preOrderInfo.getOrderCode(), preRefundOrderInfo.getRefundPrice().toString(), "????????????",
                "????????????7?????????????????????????????????????????????");
    }

    /**
     * ?????????????????????
     * @param refGoods
     * @return
     */
    private List<PreGoodsInfo> getGoods(String refGoods) {
        List<PreGoodsInfo> result = new ArrayList<>();
        JSONArray array_ = JSONUtil.parseArray(refGoods);
        array_.stream().forEach(i ->{
            Long idLong = Long.parseLong(i.toString());
            PreGoodsInfo goods = goodsInfoMapper.selectById(idLong);
            if(null != goods){
                result.add(goods);
            }
        });
        return result;
    }

}
