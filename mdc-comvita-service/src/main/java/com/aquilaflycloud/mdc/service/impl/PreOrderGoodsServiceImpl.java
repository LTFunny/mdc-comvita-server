package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.PrePickingCardMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PrePickingCard;
import com.aquilaflycloud.mdc.param.pre.PreOrderGoodsPageParam;
import com.aquilaflycloud.mdc.param.pre.PreReservationOrderGoodsParam;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.aquilaflycloud.mdc.service.PreOrderGoodsService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
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

    @Resource
    private WechatMiniProgramSubscribeMessageService wechatMiniProgramSubscribeMessageService;

    @Override
    public IPage<PreOrderGoods> pagePreOrderGoods(PreOrderGoodsPageParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        return preOrderGoodsMapper.selectPage(param.page(), Wrappers.<PreOrderGoods>lambdaQuery()
                .isNotNull(PreOrderGoods::getGuideId)
                .eq(PreOrderGoods::getReserveId, infoResult.getId())
                .eq(PreOrderGoods::getOrderGoodsState, param.getOrderGoodsState())
                .ne(PreOrderGoods::getGoodsType, GoodsTypeEnum.GIFTS));
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
            .eq(PreOrderGoods::getCardId,prePickingCard.getId())
            .notIn(PreOrderGoods::getOrderGoodsState,OrderGoodsStateEnum.REFUND));
        }else {
            preOrderGoods = preOrderGoodsMapper.selectOne(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getId,param.getOrderGoodsId()));
        }
        PreOrderGoods newOrderGoods = new PreOrderGoods();
        newOrderGoods.setId(preOrderGoods.getId());
        if(param.getIsUpdate().equals(IsUpdateEnum.NO)) {
            //更改提货卡状态
            PrePickingCard newPickingCard = new PrePickingCard();
            newPickingCard.setId(prePickingCard.getId());
            newPickingCard.setPickingState(PickingCardStateEnum.RESERVE);
            int cardCount = prePickingCardMapper.updateById(newPickingCard);
            if(cardCount < 0){
                throw new ServiceException("提货卡状态修改失败。");
            }
            newOrderGoods.setCardPsw(param.getPassword());
            newOrderGoods.setIsUpdate(IsUpdateEnum.NO);
            newOrderGoods.setReserveStartTime(new Date());
        }else {
            preOrderGoods = preOrderGoodsMapper.selectById(param.getOrderGoodsId());
            newOrderGoods.setId(preOrderGoods.getId());
            newOrderGoods.setIsUpdate(IsUpdateEnum.YES);
        }
//        BeanUtil.copyProperties(param,preOrderGoods);
        newOrderGoods.setReserveId(infoResult.getId());
        newOrderGoods.setPickingCardState(PickingCardStateEnum.RESERVE);
        newOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.PRETAKE);
        int updateOrderGoods = preOrderGoodsMapper.updateById(newOrderGoods);
        if(updateOrderGoods < 0){
            throw new ServiceException("预约失败。");
        }
        PreOrderInfo orderInfo = preOrderInfoMapper.selectById(preOrderGoods.getOrderId());
        PreOrderInfo newOrderInfo = new PreOrderInfo();
        newOrderInfo.setId(orderInfo.getId());
        newOrderInfo.setOrderState(OrderInfoStateEnum.WAITINGDELIVERY);
        preOrderInfoMapper.updateById(newOrderInfo);
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
        //若全部预约完成发送微信订阅消息
        int count = preOrderGoodsMapper.selectCount(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId, orderInfo.getId())
                .eq(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PREPARE)
        );
        //若商品全部发完,发送微信订阅消息
        if (count == 0) {
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(orderInfo.getAppId())
                            .setOpenId(orderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERCHANGE, null,
                    orderInfo.getOrderCode(), "已成功预约商品。届时请留意快递信息。", DateTime.now().toString("yyyy年MM月dd日 HH:mm"), "订单已全部预约商品。");
        }
    }


}
