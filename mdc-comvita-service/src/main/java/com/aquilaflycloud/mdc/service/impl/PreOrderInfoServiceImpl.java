package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoPageResult;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private PreOrderOperateRecordService orderOperateRecordService;

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

    @Override
    public int addStatConfirmOrder(PreStayConfirmOrderParam param) {
        PreOrderInfo preOrderInfo = new PreOrderInfo();
        BeanUtil.copyProperties(param,preOrderInfo);
        preOrderInfo.setOrderState(OrderInfoStateEnum.STAYCONFIRM);
        String code = null;
        if(preOrderInfoMapper.getMaxOrderCode() != null) {
            code = (Integer.parseInt(preOrderInfoMapper.getMaxOrderCode()) + 1) + "";
        }else {
            code = "00001";
        }
        preOrderInfo.setScore(new BigDecimal("0"));
        preOrderInfo.setOrderCode("O"+DateUtil.format(new Date(),"yyyyMMdd")+ code);
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成待确认订单失败。");
        }

        MemberInfo memberInfo = memberInfoMapper.normalSelectById(param.getMemberId());
        String content = memberInfo == null ? "" : memberInfo.getMemberName() + "于"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"通过扫码填写信息生成待确认订单";
        orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getTenantId(),memberInfo == null ? "" : memberInfo.getMemberName(),preOrderInfo.getId(),content);
        return orderInfo;
    }

    @Override
    public void validationConfirmOrder(PreConfirmOrderParam param) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getId());
        if(null != preOrderInfo){
            throw new ServiceException("找不到此订单");
        }
        BeanUtil.copyProperties(param,preOrderInfo);
        if(param.getIsThrough() == 0){
            preOrderInfo.setOrderState(OrderInfoStateEnum.STAYRESERVATION);
        }
        List<PreOrderGoods> orderGoodsList = new ArrayList<>();
        if(param.getIsThrough() == 0) {
            param.getPrePickingCardList().stream().forEach(card ->{
                PrePickingCardValidationParam param1 = new PrePickingCardValidationParam();
                param1.setPickingCode(card);
                PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                        .eq(PrePickingCard::getPickingCode,card)
                        .eq(PrePickingCard::getPickingState,PickingCardStateEnum.NO_SALE));
                if(prePickingCard == null){
                    throw new ServiceException("其中提货卡有误，无法提交。");
                }
                prePickingCard.setPickingState(PickingCardStateEnum.SALE);
                //更改提货卡状态
                int updateCard = prePickingCardMapper.updateById(prePickingCard);
                if(updateCard < 0){
                    throw new ServiceException("提货卡更改状态失败。");
                }
                //订单明细
                PreOrderGoods preOrderGoods = new PreOrderGoods();
                preOrderGoods.setCardId(prePickingCard.getId());
                preOrderGoods.setOrderId(preOrderInfo.getId());
                preOrderGoods.setCardCode(card);
                PreActivityInfo preActivityInfo = activityInfoMapper.selectById(preOrderInfo.getActivityInfoId());
                if(null != preActivityInfo){
                    PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
                    preOrderGoods.setGoodsId(preGoodsInfo.getId());
                    preOrderGoods.setGoodsDescription(preGoodsInfo.getGoodsDescription());
                    preOrderGoods.setGoodsPicture(preGoodsInfo.getGoodsPicture());
                    preOrderGoods.setGoodsCode(preGoodsInfo.getGoodsCode());
                    preOrderGoods.setGoodsName(preGoodsInfo.getGoodsName());
                    preOrderGoods.setGoodsType(preGoodsInfo.getGoodsType());
                    preOrderGoods.setGoodsPrice(preGoodsInfo.getGoodsPrice());
                    preOrderGoods.setTenantId(preOrderInfo.getTenantId());
                }
                orderGoodsList.add(preOrderGoods);
            });
        }
        //计算总金额
        preOrderInfo.setTotalPrice(orderGoodsList.get(0).getGoodsPrice().multiply(new BigDecimal(orderGoodsList.size())));
        int updateOrder = preOrderInfoMapper.updateById(preOrderInfo);
        if(updateOrder < 0){
            throw new ServiceException("订单操作失败。");
        }
        //判断是否存在赠品，存在就添加
        PreOrderGoods preOrderGoods = new PreOrderGoods();
        PreActivityInfo preActivityInfo = activityInfoMapper.selectById(preOrderInfo.getActivityInfoId());
        PreRuleInfo preRuleInfo = preRuleInfoMapper.selectOne(Wrappers.<PreRuleInfo>lambdaQuery()
                .eq(PreRuleInfo::getId,preActivityInfo.getRefRule())
                .eq(PreRuleInfo::getRuleType,PreRuleInfoTypeEnum.ORDER_SEND));
        if(preRuleInfo != null){
            PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
            BeanUtil.copyProperties(preGoodsInfo,preOrderGoods);
            preOrderGoods.setOrderId(preOrderInfo.getId());
            preOrderGoods.setGoodsId(preGoodsInfo.getId());
            orderGoodsList.add(preOrderGoods);
        }
        preOrderGoodsMapper.insertAllBatch(orderGoodsList);
        String content;
        if(param.getIsThrough() == 0){
            content = ("导购员：" + preOrderInfo.getGuideName()+ DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")+ " 对订单：" +
                    preOrderInfo.getOrderCode() + "进行了确认。");
        }else {
            content = ("导购员：" + preOrderInfo.getGuideName()+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss") +" 对订单：" +
                    preOrderInfo.getOrderCode() + "进行了不通过，不通过的原因为：" + preOrderInfo.getReason());
        }
        orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getTenantId(),preOrderInfo.getGuideName(),preOrderInfo.getId(),content);
    }

    @Override
    public void verificationOrder(PreOrderVerificationParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
        .eq(PrePickingCard::getPassword,param.getPassword())
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
        orderGoods.setCardPsw(param.getPassword());
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
        }
        preOrderInfoMapper.updateById(preOrderInfo);

        String content = orderGoods.getVerificaterName() + "核销员于" +DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                + "对订单：(" + preOrderInfo.getOrderCode() + ")进行了核销。";
        orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getTenantId(),orderGoods.getVerificaterName(),preOrderInfo.getId(),content);
    }



    public PreOrderInfoPageResult orderPageGet(PreOrderInfo order,OrderInfoStateEnum orderInfoStateEnum1,
                                               PickingCardStateEnum cardStateEnum){
        PreOrderInfoPageResult result = BeanUtil.copyProperties(order,PreOrderInfoPageResult.class);
        List<PreOrderGoods> goods = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,result.getId()));
        result.setPreOrderGoodsList(goods);
        List<PrePickingCard> cards = new ArrayList<>();
        goods.stream().forEach(k ->{
            PrePickingCard card = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                    .eq(PrePickingCard::getPickingState,cardStateEnum)
                    .eq(PrePickingCard::getId,k.getCardId()));
            cards.add(card);
        });
        if(cards.size() != goods.size()){
            result.setIdentificationState(orderInfoStateEnum1);
        }else {
            result.setIdentificationState(order.getOrderState());
        }
        return result;
    }

    @Override
    public IPage<PreOrderInfoPageResult> orderInfoPage(PreOrderInfoPageParam param) {
        IPage<PreOrderInfoPageResult> page = null;
        if(param.getOrderState().equals(OrderInfoStateEnum.WAITINGDELIVERY)){
            page = preOrderInfoMapper.selectPage(param.page(),Wrappers.<PreOrderInfo>lambdaQuery()
                    .eq(PreOrderInfo::getOrderState,param.getOrderState())
                    .eq(PreOrderInfo::getMemberId,param.getMemberId())
                    .eq(PreOrderInfo::getChildOrderState,ChildOrderInfoStateEnum.RESERVATION_DELIVERY))
                    .convert(order ->{
                        return orderPageGet(order,OrderInfoStateEnum.PARTINGDELIVERY,PickingCardStateEnum.VERIFICATE);
                    });
                return page;
        }else if(param.getOrderState().equals(OrderInfoStateEnum.STAYRESERVATION)){
            page = preOrderInfoMapper.selectPage(param.page(),Wrappers.<PreOrderInfo>lambdaQuery()
                    .eq(PreOrderInfo::getOrderState,param.getOrderState())
                    .eq(PreOrderInfo::getMemberId,param.getMemberId())
                    .eq(PreOrderInfo::getChildOrderState,ChildOrderInfoStateEnum.STATELESS))
                    .convert(order ->{
                        return orderPageGet(order,OrderInfoStateEnum.PARTRESERVATION,PickingCardStateEnum.RESERVE);
            });
            return page;
        }else {
            page = preOrderInfoMapper.selectPage(param.page(),Wrappers.<PreOrderInfo>lambdaQuery()
                    .eq(PreOrderInfo::getOrderState,param.getOrderState())
                    .eq(PreOrderInfo::getMemberId,param.getMemberId())
                    .eq(PreOrderInfo::getChildOrderState,ChildOrderInfoStateEnum.STATELESS))
                    .convert(order ->{
                        return orderPageGet(order,OrderInfoStateEnum.PARTRESERVATION,PickingCardStateEnum.VERIFICATE);
                    });
            return page;
        }
    }

    @Override
    public PreOrderInfoPageResult orderInfoGet(PreOrderInfoGetParam param) {
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getId());
        if(preOrderInfo == null){
            throw new ServiceException("查询订单失败。");
        }
        PreOrderInfoPageResult result = new PreOrderInfoPageResult();
        if(preOrderInfo.getOrderState().equals(OrderInfoStateEnum.STAYRESERVATION) &&
                preOrderInfo.getChildOrderState().equals(ChildOrderInfoStateEnum.STATELESS)){
            result = orderPageGet(preOrderInfo,preOrderInfo.getOrderState(),PickingCardStateEnum.RESERVE);
        } else if(preOrderInfo.getOrderState().equals(OrderInfoStateEnum.STAYRESERVATION) &&
                preOrderInfo.getChildOrderState().equals(ChildOrderInfoStateEnum.RESERVATION_DELIVERY)){
                result = orderPageGet(preOrderInfo,preOrderInfo.getOrderState(),PickingCardStateEnum.VERIFICATE);
        }
        result.setReservationNum(preOrderGoodsMapper.pickingCardGet(preOrderInfo.getId(),PickingCardStateEnum.RESERVE));
        result.setIngdeliveryNum(preOrderGoodsMapper.pickingCardGet(preOrderInfo.getId(),PickingCardStateEnum.VERIFICATE));
        PreOrderExpress preOrderExpress = preOrderExpressMapper.selectOne(Wrappers.<PreOrderExpress>lambdaQuery()
                        .eq(PreOrderExpress::getOrderId,preOrderInfo.getId()));
        if(null != preOrderExpress){
            result.setPreOrderExpress(preOrderExpress);
        }
        return result;
    }


}
