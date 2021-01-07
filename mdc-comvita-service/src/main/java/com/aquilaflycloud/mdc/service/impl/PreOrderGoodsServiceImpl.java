package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.pre.IsUpdateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.PrePickingCardMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PreOrderGoodsPageParam;
import com.aquilaflycloud.mdc.param.pre.PreReservationOrderGoodsParam;
import com.aquilaflycloud.mdc.service.PreOrderGoodsService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author pengyongliang
 * @Date 2020/12/31 14:27
 * @Version 1.0
 */
@Service
public class PreOrderGoodsServiceImpl implements PreOrderGoodsService {

    @Resource
    private PreOrderGoodsMapper preOrderGoodsMapper;

    @Resource
    private PrePickingCardMapper prePickingCardMapper;

    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;

    @Resource
    private PreOrderOperateRecordService orderOperateRecordService;

    @Override
    public IPage<PreOrderGoods> pagePreOrderGoods(PreOrderGoodsPageParam param) {
        return preOrderGoodsMapper.selectPage(param.page(),Wrappers.<PreOrderGoods>lambdaQuery()
        .eq(PreOrderGoods::getReserveId,param.getMemberId())
        .eq(PreOrderGoods::getOrderGoodsState,param.getOrderGoodsState()));
    }

    @Override
    public void reservationOrderGoods(PreReservationOrderGoodsParam param) {
        PreOrderGoods preOrderGoods = new PreOrderGoods();
        if(param.getIsUpdate()) {
            PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                    .eq(PrePickingCard::getPassword,param.getPassword())
                    .eq(PrePickingCard::getPickingState,PickingCardStateEnum.SALE));
            if (prePickingCard == null) {
                throw new ServiceException("提货卡状态异常，无法进行绑定");
            }
            preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getCardId, prePickingCard.getId()));
            if (preOrderGoods == null) {
                throw new ServiceException("订单明细未找到该提货卡关联的数据。");
            }
            if (StrUtil.isBlank(preOrderGoods.getCardCode())) {
                throw new ServiceException("请输入提货码。");
            }
            //更改提货卡状态
            prePickingCard.setPickingState(PickingCardStateEnum.RESERVE);
            prePickingCardMapper.updateById(prePickingCard);
            preOrderGoods.setCardPsw(param.getPassword());
            preOrderGoods.setIsUpdate(IsUpdateEnum.YES);
        }else {
            preOrderGoods = preOrderGoodsMapper.selectById(param.getOrderGoodsId());
            preOrderGoods.setIsUpdate(IsUpdateEnum.NO);
        }
        BeanUtil.copyProperties(param,preOrderGoods);
        preOrderGoods.setReserveId(param.getReserveId());
        preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.PRETAKE);
        int updateOrderGoods = preOrderGoodsMapper.updateById(preOrderGoods);
        if(updateOrderGoods < 0){
            throw new ServiceException("预约失败。");
        }
        //当全部提货卡预约完 状态改为待提货
        /*List<PreOrderGoods> orderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,orderInfo.getId()));
        int result = preOrderGoodsMapper.pickingCardGet(orderInfo.getId(),PickingCardStateEnum.RESERVE);
        if(result == orderGoodsList.size()){
            orderInfo.setChildOrderState(ChildOrderInfoStateEnum.STATELESS);
            orderInfo.setOrderState(OrderInfoStateEnum.WAITINGDELIVERY);
        }else {
            orderInfo.setChildOrderState(ChildOrderInfoStateEnum.RESERVATION_DELIVERY);
        }*/
        PreOrderInfo orderInfo = preOrderInfoMapper.selectById(preOrderGoods.getOrderId());
        orderInfo.setOrderState(OrderInfoStateEnum.WAITINGDELIVERY);
        preOrderInfoMapper.updateById(orderInfo);
        //记录日志
        if(param.getIsUpdate()) {
            String content = preOrderGoods.getReserveName() + DateUtil.format(new Date(), "yyyy-MM-dd") + " 对" +
                    preOrderGoods.getGoodsName() + "进行了预约，提货卡为：" + preOrderGoods.getCardCode();
            orderOperateRecordService.addOrderOperateRecordLog(preOrderGoods.getReserveName(), orderInfo.getId(), content);
        }else {
            String content = preOrderGoods.getReserveName() + DateUtil.format(new Date(), "yyyy-MM-dd") + " 对预约信息" +
                    preOrderGoods.getGoodsName() + "进行了修改。";
            orderOperateRecordService.addOrderOperateRecordLog(preOrderGoods.getReserveName(), orderInfo.getId(), content);
        }
    }


}
