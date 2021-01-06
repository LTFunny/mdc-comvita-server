package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.pre.ChildOrderInfoStateEnum;
import com.aquilaflycloud.mdc.enums.pre.IsUpdateEnum;
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
import com.aquilaflycloud.mdc.result.pre.PreOrderGoodsPageResult;
import com.aquilaflycloud.mdc.service.PreOrderGoodsService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
    public IPage<PreOrderGoodsPageResult> pagePreOrderGoods(PreOrderGoodsPageParam param) {
        return preOrderGoodsMapper.orderGoodsPage(param.page(),param);
    }

    @Override
    public void reservationOrderGoods(PreReservationOrderGoodsParam param) {
        PrePickingCard prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                .eq(PrePickingCard::getPassword,param.getPassword())
                .eq(PrePickingCard::getPickingState,PickingCardStateEnum.SALE));
        if(prePickingCard == null){
            throw new ServiceException("提货卡状态异常，无法进行绑定");
        }
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getCardId,prePickingCard.getId()));
        if(preOrderGoods == null){
            throw new ServiceException("订单明细未找到该提货卡关联的数据。");
        }
        if(StrUtil.isBlank(preOrderGoods.getCardCode())){
            throw new ServiceException("请输入提货码。");
        }
        BeanUtil.copyProperties(param,preOrderGoods);
        PreOrderInfo orderInfo = preOrderInfoMapper.selectById(preOrderGoods.getOrderId());
        preOrderGoods.setReserveId(orderInfo.getMemberId());
        preOrderGoods.setCardPsw(param.getPassword());
        preOrderGoods.setIsUpdate(IsUpdateEnum.YES);
        //preOrderGoods.setOrderGoodsState(OrderInfoStateEnum.WAITINGDELIVERY);
        int updateOrderGoods = preOrderGoodsMapper.updateById(preOrderGoods);
        if(updateOrderGoods < 0){
            throw new ServiceException("预约失败。");
        }
        //更改提货卡状态
        prePickingCard.setPickingState(PickingCardStateEnum.RESERVE);
        prePickingCardMapper.updateById(prePickingCard);
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
        orderInfo.setOrderState(OrderInfoStateEnum.WAITINGDELIVERY);
        preOrderInfoMapper.updateById(orderInfo);
        //记录日志
        String content = preOrderGoods.getReserveName() +DateUtil.format(new Date(), "yyyy-MM-dd")+" 对" +
                preOrderGoods.getGoodsName() + "进行了预约，提货卡为：" + preOrderGoods.getCardCode();
        orderOperateRecordService.addOrderOperateRecordLog(orderInfo.getTenantId(),preOrderGoods.getReserveName(),orderInfo.getId(),content);
    }


}
