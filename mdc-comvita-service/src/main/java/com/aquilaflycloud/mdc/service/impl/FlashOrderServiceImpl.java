package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.feign.consumer.org.IUserConsumer;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.service.FlashOrderService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.org.service.provider.entity.PUmsUserDetail;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zly
 */
@Slf4j
@Service
public class FlashOrderServiceImpl implements FlashOrderService {
    @Resource
    private PreActivityInfoMapper activityInfoMapper;
    @Resource
    private PreFlashOrderInfoMapper flashOrderInfoMapper;
    @Resource
    private IUserConsumer userConsumer;
    @Resource
    private PreGoodsInfoMapper goodsInfoMapper;
    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;
    @Resource
    private PreOrderOperateRecordService orderOperateRecordService;
    @Resource
    private PreOrderGoodsMapper preOrderGoodsMapper;
    @Override
    @Transactional
    public void getFlashOrderInfo(FlashConfirmOrderParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreFlashOrderInfo preFlashOrderInfo = flashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getActivityInfoId,param.getActivityInfoId())
                .eq(PreFlashOrderInfo::getMemberId,infoResult.getId()));
        if(preFlashOrderInfo!=null){
            throw new ServiceException("已参加该活动");
        }
        PreActivityInfo preActivityInfo = activityInfoMapper.selectById(param.getActivityInfoId());
        if(preActivityInfo == null){
            throw new ServiceException("活动不存在");
        }

        preFlashOrderInfo=new PreFlashOrderInfo();
        preFlashOrderInfo.setActivityInfoId(param.getActivityInfoId());
        preFlashOrderInfo.setMemberId(infoResult.getId());
        preFlashOrderInfo.setShopId(param.getShopId());
        preFlashOrderInfo.setShopName(param.getShopName());
        preFlashOrderInfo.setFlashCode(MdcUtil.getTenantIncIdStr("flashCode", "O" + DateTime.now().toString("yyMMdd"), 5));
        preFlashOrderInfo.setBuyerName(infoResult.getRealName());
        preFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.TOBEWRITTENOFF);
        int orderInfo = flashOrderInfoMapper.insert(preFlashOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成订单失败。");
        }
        if(ActivityGettingWayEnum.BY_EXPRESS.equals(preActivityInfo.getActivityGettingWay())){
            saveOrder(infoResult,preActivityInfo,preFlashOrderInfo,param);
        }
      }
    private void saveOrder( MemberInfoResult infoResult,  PreActivityInfo preActivityInfo,PreFlashOrderInfo preFlashOrderInfo,FlashConfirmOrderParam param ){
        PreOrderInfo preOrderInfo = new PreOrderInfo();
        PreGoodsInfo goodsInfo = goodsInfoMapper.selectById(preActivityInfo.getRefGoods());
        if(goodsInfo == null){
            throw new ServiceException("活动里不存在商品,无法生成订单。");
        }

        MdcUtil.setMemberInfo(preOrderInfo,infoResult);
        preOrderInfo.setMemberId(infoResult.getId());
        preOrderInfo.setFailSymbol(FailSymbolEnum.NO);
        preOrderInfo.setOrderState(OrderInfoStateEnum.STAYSENDGOODS);
        preOrderInfo.setScore(new BigDecimal("0"));
        preOrderInfo.setOrderCode(MdcUtil.getTenantIncIdStr("preOrderCode", "O" + DateTime.now().toString("yyMMdd"), 5));
        preOrderInfo.setShopId(param.getShopId());
        preOrderInfo.setShopName(param.getShopName());
        preOrderInfo.setFlashId(preFlashOrderInfo.getId());
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成订单失败。");
        }
        savePreOrderGoods(preOrderInfo,goodsInfo,param);
        String content =  preFlashOrderInfo.getBuyerName() + "于"+ DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"通过扫码填写信息生成快闪订单";
        orderOperateRecordService.addOrderOperateRecordLog(preFlashOrderInfo.getBuyerName(),preOrderInfo.getId(),content);

    }
    private void savePreOrderGoods( PreOrderInfo preOrderInfo,  PreGoodsInfo  preGoodsInfo,FlashConfirmOrderParam param){
        //订单明细
        PreOrderGoods preOrderGoods = new PreOrderGoods();
        preOrderGoods.setOrderId(preOrderInfo.getId());
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
        preOrderGoods.setOrderGoodsState(OrderGoodsStateEnum.PRETAKE);
        preOrderGoods.setGiftsSymbol(GiftsSymbolEnum.AFTER);
        preOrderGoods.setDeliveryAddress(param.getBuyerAddress());
        preOrderGoods.setDeliveryProvince(param.getBuyerProvince());
        preOrderGoods.setDeliveryCity(param.getBuyerCity());
        preOrderGoods.setDeliveryDistrict(param.getBuyerDistrict());
        preOrderGoods.setReserveId(preOrderInfo.getMemberId());
        preOrderGoods.setReserveName(param.getBuyerName());
        preOrderGoods.setReservePhone(param.getBuyerPhone());
        int orderGoodsInfo = preOrderGoodsMapper.insert(preOrderGoods);
        if(orderGoodsInfo < 0){
            throw new ServiceException("生成订单失败。");
        }
    }

    @Override
    public IPage<PreActivityInfo> pageMemberFlash(MemberFlashPageParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        List<Long> activityIds = flashOrderInfoMapper.selectList(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .select(PreFlashOrderInfo::getActivityInfoId)
                .eq(PreFlashOrderInfo::getMemberId, memberId)
                .eq(PreFlashOrderInfo::getFlashOrderState, param.getFlashOrderState())
        ).stream().map(PreFlashOrderInfo::getActivityInfoId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(activityIds)) {
            return activityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>lambdaQuery()
                    .in(PreActivityInfo::getId, activityIds)
            );
        }
        return param.page();
    }

    @Override
    @Transactional
    public void verificationFlashOrder(FlashWriteOffOrderParam param) {
        PreFlashOrderInfo preFlashOrderInfo = flashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getFlashCode,param.getFlashCode()));
        if(preFlashOrderInfo==null){
            throw new ServiceException("核销码有误。");
        }

        preFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.WRITTENOFF);
        int orderInfo=flashOrderInfoMapper.updateById(preFlashOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("核销失败。");
        }
    }

    @Override
    public String getFlashOrderCode(QueryFlashCodeParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        PreFlashOrderInfo preFlashOrderInfo = flashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getActivityInfoId, param.getActivityInfoId())
                .eq(PreFlashOrderInfo::getMemberId, memberId)
        );
        if(preFlashOrderInfo!=null){
            return preFlashOrderInfo.getFlashCode();
        }
    return null;
    }

    @Override
    public IPage<PreFlashOrderInfo> pageFlashOrderInfo(FlashPageParam param) {
        return flashOrderInfoMapper.selectPage(param.page(), Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .in(PreFlashOrderInfo::getActivityInfoId, param.getId())
                .eq(ObjectUtil.isNotNull(param.getFlashOrderState()), PreFlashOrderInfo::getFlashOrderState, param.getFlashOrderState())
                .ge(param.getCreateStartTime() != null, PreFlashOrderInfo::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreFlashOrderInfo::getCreateTime, param.getCreateEndTime())
                .eq(StringUtils.isNotBlank(param.getShopId()), PreFlashOrderInfo::getShopId, param.getShopId())
                .orderByDesc(PreFlashOrderInfo::getCreateTime)
        );
    }
}
