package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.aquilaflycloud.mdc.enums.pre.ChildOrderInfoStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoGetResult;
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
        String content = memberInfo == null ? "" : memberInfo.getMemberName() + "通过扫码填写信息生成待确认订单";
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
        int updateOrder = preOrderInfoMapper.updateById(preOrderInfo);
        if(updateOrder < 0){
            throw new ServiceException("订单操作失败。");
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
                }
                orderGoodsList.add(preOrderGoods);
            });
        }
        preOrderGoodsMapper.insertAllBatch(orderGoodsList);
        String content;
        if(param.getIsThrough() == 0){
            content = ("导购员：" + preOrderInfo.getGuideName()+ "对订单：" +
                    preOrderInfo.getOrderCode() + "进行了确认。");
        }else {
            content = ("导购员：" + preOrderInfo.getGuideName()+ "对订单：" +
                    preOrderInfo.getOrderCode() + "进行了不通过，不通过的原因为：" + preOrderInfo.getReason());
        }
        orderOperateRecordService.addOrderOperateRecordLog(preOrderInfo.getTenantId(),preOrderInfo.getGuideName(),preOrderInfo.getId(),content);
    }


    @Override
    public void reservationOrderGoods(PreReservationOrderGoodsParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                .eq(PrePickingCard::getPickingCode,param.getPickingCode())
                .eq(PrePickingCard::getPickingState,PickingCardStateEnum.SALE));
        if(prePickingCard == null){
            throw new ServiceException("提货卡状态异常，无法进行绑定");
        }
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getCardId,prePickingCard.getId()));
        if(preOrderGoods == null){
            throw new ServiceException("订单明细未找到该提货卡关联的数据。");
        }
        BeanUtil.copyProperties(param,preOrderGoods);
        PreOrderInfo orderInfo = preOrderInfoMapper.selectById(preOrderGoods.getOrderId());
        preOrderGoods.setReserveId(orderInfo.getMemberId());
        int updateOrderGoods = preOrderGoodsMapper.updateById(preOrderGoods);
        if(updateOrderGoods < 0){
            throw new ServiceException("预约失败。");
        }
        //更改提货卡状态
        prePickingCard.setPickingState(PickingCardStateEnum.RESERVE);
        prePickingCardMapper.updateById(prePickingCard);

        //当全部提货卡预约完 状态改为待提货
        List<PreOrderGoods> orderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,orderInfo.getId()));
        int result = preOrderGoodsMapper.pickingCardGet(orderInfo.getId(),PickingCardStateEnum.RESERVE);
        if(result == orderGoodsList.size()){
            orderInfo.setChildOrderState(null);
            orderInfo.setOrderState(OrderInfoStateEnum.WAITINGDELIVERY);
        }else {
            orderInfo.setChildOrderState(ChildOrderInfoStateEnum.RESERVATION_DELIVERY);
        }
        preOrderInfoMapper.updateById(orderInfo);
        //记录日志
        String content = preOrderGoods.getReserveName() +DateUtil.format(new Date(), "yyyy-MM-dd")+" 对" +
                preOrderGoods.getGoodsName() + "进行了预约，提货卡为：" + preOrderGoods.getCardCode();
        orderOperateRecordService.addOrderOperateRecordLog(orderInfo.getTenantId(),preOrderGoods.getReserveName(),orderInfo.getId(),content);
    }



}
