package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoGetResult;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
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
    private PreOrderOperateRecordMapper  orderOperateRecordMapper;

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
        PreOrderOperateRecord preOrderOperateRecord = new PreOrderOperateRecord();
        preOrderOperateRecord.setTenantId(preOrderInfo.getTenantId());
        preOrderOperateRecord.setOrderId(preOrderInfo.getId());
        MemberInfo memberInfo = memberInfoMapper.normalSelectById(param.getMemberId());
        if(memberInfo != null) {
            preOrderOperateRecord.setOperatorName(memberInfo.getMemberName());
        }
        preOrderOperateRecord.setOperatorContent(memberInfo == null ? "" : memberInfo.getMemberName() + "通过扫码填写信息生成待确认订单");
        orderOperateRecordMapper.insert(preOrderOperateRecord);
        return orderInfo;
    }

    @Override
    public IPage<PreOrderInfo> pagePreOrderInfo(PreOrderInfoPageParam param) {
        IPage<PreOrderInfo> orderInfoIPage = null;
        if(param.getOrderState().getType() == 1) {
           orderInfoIPage = preOrderInfoMapper.selectPage(param.page(), Wrappers.<PreOrderInfo>lambdaQuery()
                    .eq(PreOrderInfo::getGuideId, param.getGuideId())
                    .eq(PreOrderInfo::getOrderState, param.getOrderState()));
        }else {
            orderInfoIPage = preOrderInfoMapper.selectPage(param.page(), Wrappers.<PreOrderInfo>lambdaQuery()
                    .eq(PreOrderInfo::getGuideId, param.getGuideId())
                    .notIn(PreOrderInfo::getOrderState,OrderInfoStateEnum.STAYCONFIRM));
        }
        return orderInfoIPage;
    }

    @Override
    public PreOrderInfoGetResult getConfirmOrderInfo(PreOrderInfoGetParam param) {
        PreOrderInfoGetResult orderInfoGetResult = new PreOrderInfoGetResult();
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(param.getId());
        BeanUtil.copyProperties(preOrderInfo,orderInfoGetResult);
        PreActivityInfo preActivityInfo = activityInfoMapper.selectById(preOrderInfo.getActivityInfoId());
        if(null != preActivityInfo){
            PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
            orderInfoGetResult.setPreGoodsInfo(preGoodsInfo);
        }
        return orderInfoGetResult;
    }

    @Override
    public int validationConfirmOrder(PreConfirmOrderParam param) {
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
                int updateCard = prePickingCardMapper.updateById(prePickingCard);
                if(updateCard < 0){
                    throw new ServiceException("提货卡更改状态失败。");
                }
                PreOrderGoods preOrderGoods = new PreOrderGoods();
                preOrderGoods.setCardId(prePickingCard.getId());
                preOrderGoods.setOrderId(preOrderInfo.getId());
                preOrderGoods.setCardCode(card);
                PreActivityInfo preActivityInfo = activityInfoMapper.selectById(preOrderInfo.getActivityInfoId());
                if(null != preActivityInfo){
                    PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
                    preOrderGoods.setGoodsId(preGoodsInfo.getId());
                    preOrderGoods.setGoodsCode(preGoodsInfo.getGoodsCode());
                    preOrderGoods.setGoodsName(preGoodsInfo.getGoodsName());
                    preOrderGoods.setGoodsType(preGoodsInfo.getGoodsType());
                    preOrderGoods.setGoodsPrice(preGoodsInfo.getGoodsPrice());
                }
                orderGoodsList.add(preOrderGoods);
            });
        }
        preOrderGoodsMapper.insertAllBatch(orderGoodsList);
        PreOrderOperateRecord preOrderOperateRecord = new PreOrderOperateRecord();
        preOrderOperateRecord.setTenantId(preOrderInfo.getTenantId());
        preOrderOperateRecord.setOperatorName(preOrderInfo.getGuideName());
        preOrderOperateRecord.setOrderId(preOrderInfo.getId());
        if(param.getIsThrough() == 0){
            preOrderOperateRecord.setOperatorContent("导购员：" + preOrderInfo.getGuideName()+ "对订单：" +
                    preOrderInfo.getOrderCode() + "进行了确认。");
        }else {
            preOrderOperateRecord.setOperatorContent("导购员：" + preOrderInfo.getGuideName()+ "对订单：" +
                    preOrderInfo.getOrderCode() + "进行了不通过，不通过的原因为：" + preOrderInfo.getReason());
        }
        orderOperateRecordMapper.insert(preOrderOperateRecord);
        return updateOrder;
    }

}