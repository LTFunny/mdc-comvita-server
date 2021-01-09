package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aquilaflycloud.mdc.enums.member.RewardTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.member.MemberScanRewardResult;
import com.aquilaflycloud.mdc.result.pre.PreActivityRewardResult;
import com.aquilaflycloud.mdc.result.pre.PreOrderGoodsGetResult;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoPageResult;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.aquilaflycloud.mdc.service.MemberRewardService;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.aquilaflycloud.mdc.util.MdcUtil;
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
    private PreOrderExpressMapper preOrderExpressMapper;

    @Resource
    private MemberRewardService memberRewardService;

    @Transactional
    @Override
    public int addStatConfirmOrder(PreStayConfirmOrderParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreOrderInfo preOrderInfo = new PreOrderInfo();
        BeanUtil.copyProperties(param,preOrderInfo);
        preOrderInfo.setMemberId(infoResult.getId());
        preOrderInfo.setOrderState(OrderInfoStateEnum.STAYCONFIRM);
        preOrderInfo.setScore(new BigDecimal("0"));
        preOrderInfo.setOrderCode(MdcUtil.getTenantIncIdStr("preOrderCode", "O" + DateTime.now().toString("yyMMdd"), 5));
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成待确认订单失败。");
        }
        String content =  infoResult.getMemberName() + "于"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"通过扫码填写信息生成待确认订单";
        orderOperateRecordService.addOrderOperateRecordLog(infoResult.getMemberName(),preOrderInfo.getId(),content);
        return orderInfo;
    }

    @Override
    public void updateStatConfirmOrder(PreStayConfirmOrderParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderId());
        BeanUtil.copyProperties(param,preOrderInfo);
        int orderInfo = preOrderInfoMapper.updateById(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("修改待确认订单失败。");
        }
        String content =  infoResult.getMemberName() + "于"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"修改待确认订单";
        orderOperateRecordService.addOrderOperateRecordLog(infoResult.getMemberName(),preOrderInfo.getId(),content);
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
            PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
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
                preOrderGoods.setGoodsName(preGoodsInfo.getGoodsName());
                preOrderGoods.setGoodsType(preGoodsInfo.getGoodsType());
                preOrderGoods.setGoodsPrice(preGoodsInfo.getGoodsPrice());
                preOrderGoods.setTenantId(preOrderInfo.getTenantId());
                preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.PREPARE);
                orderGoodsList.add(preOrderGoods);
            });
            //确认订单后奖励
            if (StrUtil.isNotBlank(preActivityInfo.getRewardRuleContent())) {
                MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
                List<PreActivityRewardResult> rewardRuleList = JSONUtil.toList(JSONUtil.parseArray(preActivityInfo.getRewardRuleContent()), PreActivityRewardResult.class);
                for (PreActivityRewardResult rewardRule : rewardRuleList) {
                    memberRewardService.addPreActivityRewardRecord(memberInfo, rewardRule.getRewardType(), rewardRule.getRewardValue());
                }
            }
            //计算总金额
            preOrderInfo.setTotalPrice(orderGoodsList.get(0).getGoodsPrice().multiply(new BigDecimal(orderGoodsList.size())));
            preOrderInfo.setFailSymbol(FailSymbolEnum.NO);
            //判断是否存在赠品，存在就添加
            PreRuleInfo preRuleInfo = preRuleInfoMapper.selectOne(Wrappers.<PreRuleInfo>lambdaQuery()
                    .eq(PreRuleInfo::getId,preActivityInfo.getRefRule())
                    .eq(PreRuleInfo::getRuleType,RuleTypeEnum.ORDER_GIFTS));
            if(preRuleInfo != null){
                PreOrderGoods preOrderGoods = new PreOrderGoods();
                PreGoodsInfo preGoodsInfo1 = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
                BeanUtil.copyProperties(preGoodsInfo1,preOrderGoods);
                preOrderGoods.setId(null);
                preOrderGoods.setOrderId(preOrderInfo.getId());
                preOrderGoods.setGoodsId(preGoodsInfo1.getId());
                preOrderGoods.setGoodsType(OrderGoodsTypeEnum.GIFTS);
                preOrderGoods.setDeliveryProvince(preOrderInfo.getBuyerProvince());
                preOrderGoods.setDeliveryCity(preOrderInfo.getBuyerCity());
                preOrderGoods.setDeliveryDistrict(preOrderInfo.getBuyerDistrict());
                preOrderGoods.setDeliveryAddress(preOrderInfo.getBuyerAddress());
                orderGoodsList.add(preOrderGoods);
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
                    "订单" + preOrderInfo.getOrderCode() + "审核通过");
        } else {
            content = ("导购员：" + preOrderInfo.getGuideName() + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss") + " 对订单：" +
                    preOrderInfo.getOrderCode() + "进行了不通过，不通过的原因为：" + preOrderInfo.getReason());
            //发送微信订阅消息
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERAUDIT, null,
                    preOrderInfo.getOrderCode(), "您提交的订单已审核", "不通过",
                    "订单" + preOrderInfo.getOrderCode() + "审核不通过, 原因: " + preOrderInfo.getReason());
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
            .notIn(PreOrderGoods::getGoodsType,OrderGoodsTypeEnum.GIFTS));

        int result = preOrderGoodsMapper.pickingCardGet(orderGoods.getOrderId(), PickingCardStateEnum.VERIFICATE);
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(orderGoods.getOrderId());
        List<PreOrderGoods> OrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,orderGoods.getOrderId())
                .eq(PreOrderGoods::getGoodsType,OrderGoodsTypeEnum.GIFTS));
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
            Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord(memberInfo,null,preOrderInfo.getTotalPrice(),true);
            preOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
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
        int orderGoodsCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .eq(PreOrderGoods::getOrderGoodsState,OrderGoodsStateEnum.PRETAKE));
        if(orderGoodsCount > 0){
            result.setReservationNum(orderGoodsCount);
        }else {
            result.setReservationNum(0);
        }
        PreActivityInfo activityInfo = activityInfoMapper.selectById(order.getActivityInfoId());
        PreGoodsInfo goodsInfo = goodsInfoMapper.selectById(activityInfo.getRefGoods());
        result.setPreGoodsInfo(goodsInfo);
        int goodsCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .eq(PreOrderGoods::getGoodsType,OrderGoodsTypeEnum.PREPARE));
        result.setGoodsInfoNum(goodsCount);
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,order.getId())
                .eq(PreOrderGoods::getGoodsType,OrderGoodsTypeEnum.GIFTS));
        if(null != preOrderGoods){
            result.setGiftsGoodsInfo(preOrderGoods);
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
    public PreOrderInfoPageResult orderInfoGet(PreOrderInfoGetParam param) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderInfoId());
        if(preOrderInfo == null){
            throw new ServiceException("查询订单失败。");
        }
        PreOrderInfoPageResult result = orderInfo(preOrderInfo);
        int takenCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
                .eq(PreOrderGoods::getOrderGoodsState,OrderGoodsStateEnum.TAKEN));
        result.setIngdeliveryNum(takenCount);
        if(preOrderInfo.getOrderState().equals(OrderInfoStateEnum.BEENCOMPLETED) && param.getAfter() == 1){
            result.setState("售后/退款");
        }
        int cardCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
                .eq(PreOrderGoods::getPickingCardState,PickingCardStateEnum.RESERVE));
        int goodsCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
                .eq(PreOrderGoods::getGoodsType,OrderGoodsTypeEnum.PREPARE));
        if(preOrderInfo.getOrderState().equals(OrderInfoStateEnum.WAITINGDELIVERY) && cardCount != goodsCount){
            result.setState("部分预约");
        }
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
        .eq(PreOrderGoods::getOrderId,preOrderInfo.getId())
        .eq(PreOrderGoods::getGoodsType,OrderGoodsTypeEnum.GIFTS));
        //物流详情
        if(null != preOrderGoods){
            PreOrderExpress preOrderExpress = preOrderExpressMapper.selectOne(Wrappers.<PreOrderExpress>lambdaQuery()
                    .eq(PreOrderExpress::getOrderId,preOrderGoods.getOrderId()));
            result.setPreOrderExpress(preOrderExpress);
        }
        return result;
    }

    @Transactional
    @Override
    public void confirmReceiptOrder(PreOrderGoodsGetParam param) {
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectById(param.getOrderGoodsId());
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
        int orderGoodsCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, preOrderGoods.getOrderId()));

        int takenCount = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, preOrderGoods.getOrderId())
                .eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.TAKEN));

        if (orderGoodsCount == takenCount) {
            preOrderInfo.setOrderState(OrderInfoStateEnum.BEENCOMPLETED);
            MemberInfo memberInfo = memberInfoMapper.selectById(preOrderInfo.getMemberId());
            Map<RewardTypeEnum, MemberScanRewardResult> map = memberRewardService.addScanRewardRecord
                    (memberInfo, null, preOrderInfo.getTotalPrice(), true);
            preOrderInfo.setScore(new BigDecimal(map.get(RewardTypeEnum.SCORE).getRewardValue()));
        } else {
            preOrderInfo.setOrderState(OrderInfoStateEnum.STAYSENDGOODS);

        }
        int order = preOrderInfoMapper.updateById(preOrderInfo);
        if (order < 0) {
            throw new ServiceException("更改订单状态失败。");
        }
        if (preOrderGoods.getGoodsType() == OrderGoodsTypeEnum.GIFTS) {
            //签收赠品,发送微信订阅消息
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERSIGN, null,
                    preOrderInfo.getOrderCode(), preOrderGoods.getGoodsName(), preOrderGoods.getExpressName(),
                    preOrderGoods.getExpressOrderCode(), "订单" + preOrderInfo.getOrderCode() + "的赠品" + preOrderGoods.getGoodsName() + "已签收");
        } else {
            //签收预售商品,发送微信订阅消息
            MemberInfo memberInfo = memberInfoMapper.selectById(preOrderGoods.getReserveId());
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(memberInfo.getWxAppId())
                            .setOpenId(memberInfo.getOpenId())), MiniMessageTypeEnum.PREORDERGOODSSIGN, null,
                    preOrderGoods.getGoodsName(), preOrderGoods.getExpressName(), preOrderGoods.getExpressOrderCode(),
                    preOrderGoods.getGoodsName() + "商品已签收");
        }
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
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getOrderId());
        PreRefundOrderInfo preRefundOrderInfo = new PreRefundOrderInfo();
        BeanUtil.copyProperties(preOrderInfo, preRefundOrderInfo, CopyOptions.create().setIgnoreProperties(MdcUtil.getIgnoreNames()));
        BeanUtil.copyProperties(param, preRefundOrderInfo);
        preRefundOrderInfo.setRefundCode(MdcUtil.getTenantIncIdStr("preOrderRefundCode", "R" + DateTime.now().toString("yyMMdd"), 5));
        preRefundOrderInfo.setRefundTime(DateTime.now());
        preRefundOrderInfo.setAfterGuideId(MdcUtil.getCurrentUserId());
        preRefundOrderInfo.setAfterGuideName(MdcUtil.getCurrentUserName());
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
        ).stream().map(PreOrderGoods::getCardId).collect(Collectors.toList());
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
                .ne(PrePickingCard::getPickingState, PickingCardStateEnum.SALE)
        );
        //记录订单操作日志
        orderOperateRecordService.addOrderOperateRecordLog(MdcUtil.getCurrentUserName(), param.getOrderId(), "登记售后");
        //发送微信订阅消息
        wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                        .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERREFUNDAUDIT, null,
                preOrderInfo.getOrderCode(), preRefundOrderInfo.getRefundPrice().toString(), "通过",
                "订单" + preOrderInfo.getOrderCode() + "已成功办理售后");
    }


}
