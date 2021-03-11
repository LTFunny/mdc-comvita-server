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
            throw new ServiceException("活动不存在");
        }
//        PreGoodsInfo goodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
        List<PreGoodsInfo> goods = getGoods(preActivityInfo.getRefGoods());
        if(CollUtil.isEmpty(goods)){
            throw new ServiceException("活动里不存在商品,无法生成订单。");
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
            throw new ServiceException("获取导购员失败。");
        }
        preOrderInfo.setGuideId(pUmsUserDetail.getUserId());
        preOrderInfo.setGuideName(pUmsUserDetail.getRealName());
        preOrderInfo.setShopId(pUmsUserDetail.getOrgId());
        preOrderInfo.setShopName(pUmsUserDetail.getOrgName());
        preOrderInfo.setShopAddress(pUmsUserDetail.getOrgAddress());
        preOrderInfo.setActivityType(ActivityTypeEnum.PRE_SALES);
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成待确认订单失败。");
        }
        String content =  param.getBuyerName() + "于"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"通过扫码填写信息生成待确认订单";
        orderOperateRecordService.addOrderOperateRecordLog(param.getBuyerName(),preOrderInfo.getId(),content);
        //更新对应会员信息
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
        BeanUtil.copyProperties(param,preOrderInfo);
        preOrderInfo.setGuideId(id);
        preOrderInfo.setFailSymbol(FailSymbolEnum.YES_NO);
        preOrderInfo.setReason("");
        int orderInfo = preOrderInfoMapper.updateById(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("修改待确认订单失败。");
        }
        String content =  param.getBuyerName() + "于"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"修改待确认订单";
        orderOperateRecordService.addOrderOperateRecordLog(param.getBuyerName(),preOrderInfo.getId(),content);
    }

    @Transactional
    @Override
    public void validationConfirmOrder(PreConfirmOrderParam param) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getId());
        if (null == preOrderInfo) {
            throw new ServiceException("找不到此订单");
        }
        BeanUtil.copyProperties(param, preOrderInfo);
        if (param.getIsThrough() == 0) {
            preOrderInfo.setOrderState(OrderInfoStateEnum.STAYRESERVATION);
        }else {
            preOrderInfo.setFailSymbol(FailSymbolEnum.YES);
        }
        List<PreOrderGoods> orderGoodsList = new ArrayList<>();
        if (param.getIsThrough() == 0) {
            PreActivityInfo preActivityInfo = activityInfoMapper.selectById(preOrderInfo.getActivityInfoId());
            if (null == preActivityInfo) {
                throw new ServiceException("活动不存在");
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
                    throw new ServiceException("其中提货卡有误，无法提交。");
                }
                prePickingCard.setPickingState(PickingCardStateEnum.SALE);
                //更改提货卡状态
                int updateCard = prePickingCardMapper.updateById(prePickingCard);
                if (updateCard < 0) {
                    throw new ServiceException("提货卡更改状态失败。");
                }
                //订单明细
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
            //确认订单后奖励
            if (StrUtil.isNotBlank(preActivityInfo.getRewardRuleContent()) && !StrUtil.equals(preActivityInfo.getRewardRuleContent(), "[]")) {
                MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
                List<PreActivityRewardResult> rewardRuleList = JSONUtil.toList(JSONUtil.parseArray(preActivityInfo.getRewardRuleContent()), PreActivityRewardResult.class);
                for (PreActivityRewardResult rewardRule : rewardRuleList) {
                    memberRewardService.addPreActivityRewardRecord(memberInfo, rewardRule.getRewardType(), rewardRule.getRewardValue(), preActivityInfo.getId());
                }
            }
            //计算总金额
            preOrderInfo.setTotalPrice(orderGoodsList.get(0).getGoodsPrice().multiply(new BigDecimal(orderGoodsList.size())));
            preOrderInfo.setFailSymbol(FailSymbolEnum.NO);
            preOrderInfo.setConfirmTime(new Date());
            //新增规则需当同一个活动同一个用户不存在赠品且售后订单存在该订单才可新增赠品信息
            int giftsCount = preOrderInfoMapper.countOrderInfoGiftsInfo(preOrderInfo.getMemberId(),preOrderInfo.getActivityInfoId());
            if(giftsCount <= 0) {
                //判断是否存在赠品，存在就添加
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
        int updateOrder = preOrderInfoMapper.updateById(preOrderInfo);
        if(updateOrder < 0){
            throw new ServiceException("订单操作失败。");
        }
        String content;
        if (param.getIsThrough() == 0) {
            content = ("导购员：" + preOrderInfo.getGuideName() + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 对订单：" +
                    preOrderInfo.getOrderCode() + "进行了确认。");
            //发送微信订阅消息
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERAUDIT, null,
                    preOrderInfo.getOrderCode(), "您提交的订单已审核", "确认通过",
                    "您已成功购买商品。感谢您对康维他的支持。");
        } else {
            content = ("导购员：" + preOrderInfo.getGuideName() + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 对订单：" +
                    preOrderInfo.getOrderCode() + "进行了不通过，不通过的原因为：" + preOrderInfo.getReason());
            //发送微信订阅消息
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERAUDIT, null,
                    preOrderInfo.getOrderCode(), "您提交的订单已审核", "不通过",
                    "感谢您对康维他的支持。");
        }
        orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getGuideName(), preOrderInfo.getId(), content);
    }

    @Override
    public void verificationOrder(PreOrderVerificationParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
        .eq(PrePickingCard::getPassword,param.getPickingCode())
        .eq(PrePickingCard::getPickingState,PickingCardStateEnum.RESERVE));
        if(prePickingCard == null){
            throw new ServiceException("该兑换码有异常,请重新输入。");
        }
        prePickingCard.setPickingState(PickingCardStateEnum.VERIFICATE);
        int updateCard = prePickingCardMapper.updateById(prePickingCard);
        if(updateCard < 0){
            throw new ServiceException("核销失败");
        }
        PreOrderGoods orderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
        .eq(PreOrderGoods::getCardId,prePickingCard.getId()));
        orderGoods.setVerificaterId(param.getVerificaterId());
        orderGoods.setVerificaterName(param.getVerificaterName());
        orderGoods.setVerificaterOrgIds(param.getVerificaterOrgIds());
        orderGoods.setVerificaterOrgNames(param.getVerificaterOrgNames());
        orderGoods.setTakeTime(new Date());
        preOrderGoodsMapper.updateById(orderGoods);
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
            .eq(PreOrderGoods::getOrderId,orderGoods.getOrderId())
            .notIn(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));

        int result = preOrderGoodsMapper.pickingCardGet(orderGoods.getOrderId(), PickingCardStateEnum.VERIFICATE);
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(orderGoods.getOrderId());
        List<PreOrderGoods> OrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,orderGoods.getOrderId())
                .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));
        if(preOrderGoodsList.size() == result){
            preOrderInfo.setChildOrderState(ChildOrderInfoStateEnum.STATELESS);
            if(OrderGoodsList.size() > 0) {
                preOrderInfo.setOrderState(OrderInfoStateEnum.STAYSENDGOODS);
            }else {
                preOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
            }
        }else {
            preOrderInfo.setChildOrderState(ChildOrderInfoStateEnum.RESERVATION_DELIVERY);
            MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
            Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord(memberInfo,null,preOrderInfo.getId(),preOrderInfo.getTotalPrice(),true);
            if (CollUtil.isNotEmpty(map) && map.get(RewardTypeEnum.SCORE) != null) {
                preOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
            }
        }
        int order = preOrderInfoMapper.updateById(preOrderInfo);
        if(order < 0){
            throw new ServiceException("核销提货卡失败。");
        }
        String content = orderGoods.getVerificaterName() + "核销员于" +DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                + "对订单：(" + preOrderInfo.getOrderCode() + ")进行了核销。";
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
            result.setState("部分预约");
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
    public IPage<PreOrderInfoPageResult> orderInfoPage(PreOrderInfoPageParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        param.setMemberId(infoResult.getId());
        IPage<PreOrderInfo> page =  preOrderInfoMapper.pageOrderInfoPageResult(param.page(),param);
        IPage<PreOrderInfoPageResult> pageResultIPage = page.convert(order ->{
            PreOrderInfoPageResult result  = orderInfo(order);
            return result;
        });
        return pageResultIPage;
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
            throw new ServiceException("查询订单失败。");
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

    @Transactional
    @Override
    public void confirmReceiptOrderGoods(Long orderGoodsId, String operatorName) {
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectById(orderGoodsId);
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(preOrderGoods.getOrderId());
        preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.TAKEN);
        preOrderGoods.setVerificaterId(preOrderInfo.getGuideId());
        preOrderGoods.setVerificaterName(preOrderInfo.getGuideName());
        preOrderGoods.setVerificaterOrgIds(preOrderInfo.getCreatorOrgIds());
        preOrderGoods.setVerificaterOrgNames(preOrderInfo.getCreatorOrgNames());
        int orderGoods = preOrderGoodsMapper.updateById(preOrderGoods);
        if (orderGoods < 0) {
            throw new ServiceException("确认签收操作失败。");
        }
        String content = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "进行了商品签收。";
        orderOperateRecordService.addOrderOperateRecordLog(operatorName, preOrderInfo.getId(), content);
        //签收预售商品,发送微信订阅消息
        MemberInfo memberInfo = memberInfoMapper.selectById(preOrderGoods.getReserveId());
        wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(memberInfo.getWxAppId())
                        .setOpenId(memberInfo.getOpenId())), MiniMessageTypeEnum.PREORDERGOODSSIGN, null,
                preOrderGoods.getGoodsName(), preOrderGoods.getExpressName(), preOrderGoods.getExpressOrderCode(),
                "商品已签收。感谢您对康维他的支持。");
    }

    @Transactional
    @Override
    public void confirmReceiptOrder(PreOrderGetParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderId());
        preOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
        MemberInfo memberInfo = memberInfoMapper.selectById(infoResult.getId());
        Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord
                (memberInfo, null, preOrderInfo.getId(), preOrderInfo.getTotalPrice(), true);
        if (CollUtil.isNotEmpty(map) && map.get(RewardTypeEnum.SCORE) != null) {
            preOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
        }
        int order = preOrderInfoMapper.updateById(preOrderInfo);
        if (order < 0) {
            throw new ServiceException("签收订单失败。");
        }
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
                .eq(PreOrderGoods::getGoodsType,GoodsTypeEnum.GIFTS));
        if(preOrderGoods != null) {
            preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.TAKEN);
            int orderGoods = preOrderGoodsMapper.updateById(preOrderGoods);
            if (orderGoods < 0) {
                throw new ServiceException("更改商品明细状态失败。");
            }
            //签收赠品,发送微信订阅消息
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERSIGN, null,
                    preOrderInfo.getOrderCode(), preOrderGoods.getGoodsName(), preOrderGoods.getExpressName(),
                    preOrderGoods.getExpressOrderCode(), "商品已签收。感谢您对康维他的支持。");
        }
        String content = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                + "对订单：(" + preOrderInfo.getOrderCode() + ")进行了订单签收。";
        orderOperateRecordService.addOrderOperateRecordLog(infoResult.getRealName(),preOrderInfo.getId(),content);
    }

    @Override
    public PreOrderGoodsGetResult orderCardGetInfo(PreOrderCardGetParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                .eq(PrePickingCard::getPickingCode,param.getPickingCode()));
        if(prePickingCard == null){
            throw new ServiceException("查询卡号详情失败。");
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
            throw new ServiceException("订单不存在");
        }
        int refundCount = preRefundOrderInfoMapper.selectCount(Wrappers.<PreRefundOrderInfo>lambdaQuery()
                .eq(PreRefundOrderInfo::getOrderId, param.getOrderId())
        );
        if (refundCount > 0) {
            throw new ServiceException("此订单已登记退款,请勿重复操作");
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
            throw new ServiceException("登记售后失败");
        }
        //更新订单状态
        PreOrderInfo orderUpdate = new PreOrderInfo();
        orderUpdate.setId(param.getOrderId());
        orderUpdate.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
        count = preOrderInfoMapper.updateById(orderUpdate);
        if (count <= 0) {
            throw new ServiceException("登记售后失败");
        }
        //更新订单商品状态
        PreOrderGoods update = new PreOrderGoods();
        update.setOrderGoodsState(OrderGoodsStateEnum.REFUND);
        update.setPickingCardState(PickingCardStateEnum.NO_SALE);
        count = preOrderGoodsMapper.update(update, Wrappers.<PreOrderGoods>lambdaUpdate()
                .eq(PreOrderGoods::getOrderId, param.getOrderId())
        );
        if (count <= 0) {
            throw new ServiceException("登记售后失败");
        }
        //获取订单商品对应提货卡id
        List<Long> cardIdList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .select(PreOrderGoods::getCardId)
                .eq(PreOrderGoods::getOrderId, param.getOrderId())
                .isNotNull(PreOrderGoods::getCardId)
        ).stream().map(PreOrderGoods::getCardId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cardIdList)) {
            //更新提货卡状态
            PrePickingCard cardUpdate = new PrePickingCard();
            cardUpdate.setPickingState(PickingCardStateEnum.NO_SALE);
            //已出售更新为未出售
            prePickingCardMapper.update(cardUpdate, Wrappers.<PrePickingCard>lambdaUpdate()
                    .in(PrePickingCard::getId, cardIdList)
                    .eq(PrePickingCard::getPickingState, PickingCardStateEnum.SALE)
            );
            cardUpdate.setPickingState(PickingCardStateEnum.CANCEL);
            //非已出售更新为已作废
            prePickingCardMapper.update(cardUpdate, Wrappers.<PrePickingCard>lambdaUpdate()
                    .in(PrePickingCard::getId, cardIdList)
                    .ne(PrePickingCard::getPickingState, PickingCardStateEnum.NO_SALE)
            );
        }
        //退回订单奖励
        memberRewardService.refundRewardRecord(preOrderInfo.getMemberId(), preOrderInfo.getId());
        //记录订单操作日志
        orderOperateRecordService.addOrderOperateRecordLog(userInfo.getRealName(), param.getOrderId(), "登记售后");
        //发送微信订阅消息
        wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                        .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERREFUNDAUDIT, null,
                preOrderInfo.getOrderCode(), preRefundOrderInfo.getRefundPrice().toString(), "审核通过，已成功退货。",
                "退款将于7个工作日内退回到您的付款账户。");
    }

    /**
     * 获取关联的商品
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
