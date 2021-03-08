package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.feign.consumer.org.IUserConsumer;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.FlashConfirmOrderParam;
import com.aquilaflycloud.mdc.param.pre.FlashWriteOffOrderParam;
import com.aquilaflycloud.mdc.param.pre.MemberFlashPageParam;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.service.FlashOrderService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.aquilaflycloud.org.service.provider.entity.PUmsUserDetail;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
    public String getFlashOrderInfo(FlashConfirmOrderParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreActivityInfo preActivityInfo = activityInfoMapper.selectById(param.getActivityInfoId());
        if(preActivityInfo == null){
            throw new ServiceException("活动不存在");
        }
        PUmsUserDetail pUmsUserDetail = userConsumer.getUserOrganization(param.getGuideId());
        if(null == pUmsUserDetail){
            throw new ServiceException("获取导购员失败。");
        }
        PreFlashOrderInfo preFlashOrderInfo=new PreFlashOrderInfo();
        preFlashOrderInfo.setActivityInfoId(param.getActivityInfoId());
        preFlashOrderInfo.setMemberId(infoResult.getId());
        preFlashOrderInfo.setShopId(pUmsUserDetail.getOrgId());
        preFlashOrderInfo.setFlashCode(MdcUtil.getTenantIncIdStr("flashCode", "O" + DateTime.now().toString("yyMMdd"), 5));
        preFlashOrderInfo.setBuyerName(infoResult.getRealName());
        preFlashOrderInfo.setShopName(pUmsUserDetail.getOrgName());
        preFlashOrderInfo.setGuideId(pUmsUserDetail.getUserId());
        preFlashOrderInfo.setGuideName(pUmsUserDetail.getRealName());
        preFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.TOBEWRITTENOFF);
        int orderInfo = flashOrderInfoMapper.insert(preFlashOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成订单失败。");
        }
        if(ActivityGettingWayEnum.BY_EXPRESS.equals(preActivityInfo.getActivityGettingWay())){
            saveOrder(infoResult,preActivityInfo,preFlashOrderInfo);
        }
        return preFlashOrderInfo.getFlashCode();
      }
    private void saveOrder( MemberInfoResult infoResult,  PreActivityInfo preActivityInfo,PreFlashOrderInfo preFlashOrderInfo ){
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
        PUmsUserDetail pUmsUserDetail = userConsumer.getUserOrganization(preFlashOrderInfo.getGuideId());
        if(null == pUmsUserDetail){
            throw new ServiceException("获取导购员失败。");
        }
        preOrderInfo.setGuideId(pUmsUserDetail.getUserId());
        preOrderInfo.setGuideName(pUmsUserDetail.getRealName());
        preOrderInfo.setShopId(pUmsUserDetail.getOrgId());
        preOrderInfo.setShopName(pUmsUserDetail.getOrgName());
        preOrderInfo.setFlashId(preFlashOrderInfo.getId());
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成订单失败。");
        }
        savePreOrderGoods(preOrderInfo,goodsInfo);
        String content =  preFlashOrderInfo.getBuyerName() + "于"+ DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"通过扫码填写信息生成快闪订单";
        orderOperateRecordService.addOrderOperateRecordLog(preFlashOrderInfo.getBuyerName(),preOrderInfo.getId(),content);

    }
    private void savePreOrderGoods( PreOrderInfo preOrderInfo,  PreGoodsInfo  preGoodsInfo){
        //订单明细
        PreOrderGoods preOrderGoods = new PreOrderGoods();
        preOrderGoods.setOrderId(preOrderInfo.getId());
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
}
