package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.PrePickingCardMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PreOrderGoodsPageParam;
import com.aquilaflycloud.mdc.param.pre.PreReservationOrderGoodsParam;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.service.PreOrderGoodsService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        return preOrderGoodsMapper.selectPage(param.page(),Wrappers.<PreOrderGoods>lambdaQuery()
        .eq(PreOrderGoods::getReserveId,infoResult.getId())
        .eq(PreOrderGoods::getOrderGoodsState,param.getOrderGoodsState()));
    }

    @Override
    public PreOrderGoods getPreOrderGoods(PreReservationOrderGoodsParam param) {
        PreOrderGoods preOrderGoods = preOrderGoodsMapper.selectById(param.getOrderGoodsId());
        if(preOrderGoods == null){
            throw new ServiceException("找不到此订单。");
        }
        return preOrderGoods;
    }

    @Transactional
    @Override
    public void reservationOrderGoods(PreReservationOrderGoodsParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreOrderGoods preOrderGoods = new PreOrderGoods();
        PrePickingCard prePickingCard = new PrePickingCard();
        if(param.getIsUpdate().equals(IsUpdateEnum.NO)) {
            prePickingCard = prePickingCardMapper.selectOne(Wrappers.<PrePickingCard>lambdaQuery()
                    .eq(PrePickingCard::getPassword, param.getPassword())
                    .eq(PrePickingCard::getPickingState, PickingCardStateEnum.SALE));
            preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
            .eq(PreOrderGoods::getCardId,prePickingCard.getId()));
        }else {
            preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getId,param.getOrderGoodsId()));
        }
        if(param.getIsUpdate().equals(IsUpdateEnum.NO)) {
            //更改提货卡状态
            prePickingCard.setPickingState(PickingCardStateEnum.RESERVE);
            int cardCount = prePickingCardMapper.updateById(prePickingCard);
            if(cardCount < 0){
                throw new ServiceException("提货卡状态修改失败。");
            }
            preOrderGoods.setCardPsw(param.getPassword());
            preOrderGoods.setIsUpdate(IsUpdateEnum.NO);
            preOrderGoods.setReserveStartTime(new Date());
        }else {
            preOrderGoods = preOrderGoodsMapper.selectById(param.getOrderGoodsId());
            preOrderGoods.setIsUpdate(IsUpdateEnum.YES);
        }
        BeanUtil.copyProperties(param,preOrderGoods);
        preOrderGoods.setReserveId(infoResult.getId());
        preOrderGoods.setPickingCardState(PickingCardStateEnum.RESERVE);
        preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.PRETAKE);
        int updateOrderGoods = preOrderGoodsMapper.updateById(preOrderGoods);
        if(updateOrderGoods < 0){
            throw new ServiceException("预约失败。");
        }
        PreOrderInfo orderInfo = preOrderInfoMapper.selectById(preOrderGoods.getOrderId());
        orderInfo.setOrderState(OrderInfoStateEnum.WAITINGDELIVERY);
        preOrderInfoMapper.updateById(orderInfo);
        //记录日志
        if(param.getIsUpdate().equals(IsUpdateEnum.NO)) {
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
