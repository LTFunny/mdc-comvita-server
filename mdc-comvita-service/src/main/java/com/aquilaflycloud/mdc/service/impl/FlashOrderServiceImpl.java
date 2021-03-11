package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.schedulerx.shade.net.sf.json.JSONArray;
import com.aquilaflycloud.dataAuth.common.BaseResult;
import com.aquilaflycloud.mdc.enums.pre.*;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.service.FlashOrderService;
import com.aquilaflycloud.mdc.service.PreOrderOperateRecordService;
import com.aquilaflycloud.mdc.util.MdcUtil;
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
        preFlashOrderInfo.setShopAddress(param.getShopAddress());
        preFlashOrderInfo.setFlashCode(MdcUtil.getTenantIncIdStr("flashCode", "O" + DateTime.now().toString("yyMMdd"), 5));
        preFlashOrderInfo.setBuyerName(infoResult.getRealName());
        preFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.TOBEWRITTENOFF);
        preFlashOrderInfo.setBeginTime(preActivityInfo.getBeginTime());
        preFlashOrderInfo.setEndTime(preActivityInfo.getEndTime());
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
        MdcUtil.setMemberInfo(preOrderInfo,infoResult);
        preOrderInfo.setMemberId(infoResult.getId());
        preOrderInfo.setFailSymbol(FailSymbolEnum.NO);
        preOrderInfo.setOrderState(OrderInfoStateEnum.STAYSENDGOODS);
        preOrderInfo.setScore(new BigDecimal("0"));
        preOrderInfo.setOrderCode(MdcUtil.getTenantIncIdStr("preOrderCode", "O" + DateTime.now().toString("yyMMdd"), 5));
        preOrderInfo.setShopId(param.getShopId());
        preOrderInfo.setShopName(param.getShopName());
        preOrderInfo.setShopAddress(param.getShopAddress());
        preOrderInfo.setFlashId(preFlashOrderInfo.getId());
        preOrderInfo.setActivityType(ActivityTypeEnum.FLASH);
        preOrderInfo.setBuyerAddress(param.getBuyerAddress());
        preOrderInfo.setBuyerProvince(param.getBuyerProvince());
        preOrderInfo.setBuyerCity(param.getBuyerCity());
        preOrderInfo.setBuyerDistrict(param.getBuyerDistrict());
        preOrderInfo.setMemberId(preOrderInfo.getMemberId());
        preOrderInfo.setBuyerName(param.getBuyerName());
        preOrderInfo.setBuyerPhone(param.getBuyerPhone());
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成订单失败。");
        }
        savePreOrderGoods(preActivityInfo,preOrderInfo,param);
        String content =  preFlashOrderInfo.getBuyerName() + "于"+ DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"通过扫码填写信息生成快闪订单";
        orderOperateRecordService.addOrderOperateRecordLog(preFlashOrderInfo.getBuyerName(),preOrderInfo.getId(),content);

    }
    private void savePreOrderGoods( PreActivityInfo preActivityInfo,PreOrderInfo preOrderInfo,  FlashConfirmOrderParam param){
        JSONArray dataJson= JSONArray .fromObject(preActivityInfo.getRefGoods());
        List<String> list = (List<String>) JSONArray.toCollection(dataJson);
        if(CollUtil.isEmpty(list)){
            throw new ServiceException("活动里不存在商品,无法生成订单。");
        }
        for(String id:list){
            if(StringUtils.isNotBlank(id)){
                PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(id);
                if(preGoodsInfo==null){
                    throw new ServiceException("活动里不存在商品,无法生成订单。");
                }
                //订单明细
                PreOrderGoods preOrderGoods = new PreOrderGoods();
                preOrderGoods.setOrderId(preOrderInfo.getId());
                preOrderGoods.setGoodsId(preGoodsInfo.getId());
                preOrderGoods.setGoodsDescription(preGoodsInfo.getGoodsDescription());
                preOrderGoods.setGoodsPicture(preGoodsInfo.getGoodsPicture());
                preOrderGoods.setGoodsCode(preGoodsInfo.getGoodsCode());
                preOrderGoods.setOrderCode(preOrderInfo.getOrderCode());
                if(StringUtils.isNotBlank(param.getShopName())){
                    preOrderGoods.setReserveShop(param.getShopName());
                }
                if(param.getShopId()!=null){
                    preOrderGoods.setReserveShopId(param.getShopId()+"");
                }
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

        }

    }

    @Override
    public IPage<PreActivityInfo> pageMemberFlash(MemberFlashPageParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        DateTime now = DateTime.now();
        FlashOrderInfoStateEnum state = param.getFlashOrderState();
        List<Long> activityIds = flashOrderInfoMapper.selectList(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .select(PreFlashOrderInfo::getActivityInfoId)
                .eq(PreFlashOrderInfo::getMemberId, memberId)
                .nested(state == FlashOrderInfoStateEnum.TOBEWRITTENOFF,
                        i -> i.eq(PreFlashOrderInfo::getFlashOrderState, FlashOrderInfoStateEnum.TOBEWRITTENOFF)
                                .ge(PreFlashOrderInfo::getEndTime, now)
                )
                .nested(state == FlashOrderInfoStateEnum.EXPIRED,
                        i -> i.eq(PreFlashOrderInfo::getFlashOrderState, FlashOrderInfoStateEnum.TOBEWRITTENOFF)
                                .lt(PreFlashOrderInfo::getEndTime, now)
                )
                .eq(state == FlashOrderInfoStateEnum.WRITTENOFF, PreFlashOrderInfo::getFlashOrderState, FlashOrderInfoStateEnum.WRITTENOFF)
                .orderByDesc(PreFlashOrderInfo::getCreateTime)
        ).stream().map(PreFlashOrderInfo::getActivityInfoId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(activityIds)) {
            return activityInfoMapper.selectPage(param.page(), Wrappers.<PreActivityInfo>query()
                    .last("order by field(id," + ArrayUtil.join(activityIds.toArray(), ",") + ")")
                    .lambda()
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
        if(FlashOrderInfoStateEnum.WRITTENOFF.equals(preFlashOrderInfo.getFlashOrderState())){
            throw new ServiceException("该码已核销。");
        }
        preFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.WRITTENOFF);
        int orderInfo=flashOrderInfoMapper.updateById(preFlashOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("核销失败。");
        }
    }

    @Override
    public BaseResult<String> getFlashOrderCode(QueryFlashCodeParam param) {
        Long id= MdcUtil.getCurrentMemberId();
        PreFlashOrderInfo preFlashOrderInfo = flashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getActivityInfoId, param.getActivityInfoId())
                .eq(PreFlashOrderInfo::getMemberId, id)
        );
        if(preFlashOrderInfo!=null){
            return new BaseResult<String>().setResult(preFlashOrderInfo.getFlashCode());
        }
        throw new ServiceException("查询失败。");

    }

    private PreFlashOrderInfo stateHandler(PreFlashOrderInfo flashOrderInfo) {
        if (flashOrderInfo == null) {
            throw new ServiceException("记录不存在");
        }
        DateTime now = DateTime.now();
        if (flashOrderInfo.getFlashOrderState() == FlashOrderInfoStateEnum.TOBEWRITTENOFF) {
            if (flashOrderInfo.getEndTime() != null && now.isAfter(flashOrderInfo.getEndTime())) {
                flashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.EXPIRED);
            }
        }
        return flashOrderInfo;
    }

    @Override
    public IPage<PreFlashOrderInfo> pageFlashOrderInfo(FlashPageParam param) {
        DateTime now = DateTime.now();
        FlashOrderInfoStateEnum state = param.getFlashOrderState();
        return flashOrderInfoMapper.selectPage(param.page(), Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getActivityInfoId, param.getId())
                .nested(state == FlashOrderInfoStateEnum.TOBEWRITTENOFF,
                        i -> i.eq(PreFlashOrderInfo::getFlashOrderState, FlashOrderInfoStateEnum.TOBEWRITTENOFF)
                                .ge(PreFlashOrderInfo::getEndTime, now)
                )
                .nested(state == FlashOrderInfoStateEnum.EXPIRED,
                        i -> i.eq(PreFlashOrderInfo::getFlashOrderState, FlashOrderInfoStateEnum.TOBEWRITTENOFF)
                                .lt(PreFlashOrderInfo::getEndTime, now)
                )
                .eq(state == FlashOrderInfoStateEnum.WRITTENOFF, PreFlashOrderInfo::getFlashOrderState, FlashOrderInfoStateEnum.WRITTENOFF)
                .ge(param.getCreateStartTime() != null, PreFlashOrderInfo::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreFlashOrderInfo::getCreateTime, param.getCreateEndTime())
                .eq(StringUtils.isNotBlank(param.getShopId()), PreFlashOrderInfo::getShopId, param.getShopId())
                .orderByDesc(PreFlashOrderInfo::getCreateTime)
        ).convert(this::stateHandler);
    }
}
