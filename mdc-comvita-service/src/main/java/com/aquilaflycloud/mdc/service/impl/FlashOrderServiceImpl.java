package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
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
    @Resource
    private PreActivityInfoMapper preActivityInfoMapper;
    @Override
    @Transactional
    public void getFlashOrderInfo(FlashConfirmOrderParam param) {
        MemberInfoResult infoResult = MdcUtil.getRequireCurrentMember();
        PreFlashOrderInfo preFlashOrderInfo = flashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getActivityInfoId,param.getActivityInfoId())
                .eq(PreFlashOrderInfo::getMemberId,infoResult.getId()));
        if(preFlashOrderInfo!=null){
            throw new ServiceException("??????????????????");
        }
        PreActivityInfo preActivityInfo = activityInfoMapper.selectById(param.getActivityInfoId());
        if(preActivityInfo == null){
            throw new ServiceException("???????????????");
        }
        if(ActivityGettingWayEnum.OFF_LINE.equals(preActivityInfo.getActivityGettingWay())){
           List<PreFlashOrderInfo>  list = flashOrderInfoMapper.selectList(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                    .eq(PreFlashOrderInfo::getActivityInfoId,param.getActivityInfoId())
                    );
           if(list.size()>=preActivityInfo.getMaxParticipationCount()){
               throw new ServiceException("?????????????????????????????????");
            }
        }
        preFlashOrderInfo=new PreFlashOrderInfo();
        preFlashOrderInfo.setActivityInfoId(param.getActivityInfoId());
        preFlashOrderInfo.setMemberId(infoResult.getId());
        preFlashOrderInfo.setShopId(param.getShopId());
        if(StrUtil.isBlank(param.getShopName())){
            preFlashOrderInfo.setShopName("??????");
        }else{
            preFlashOrderInfo.setShopName(param.getShopName());
        }
        preFlashOrderInfo.setShopAddress(param.getShopAddress());
        String time = Convert.toStr(DateTime.now().getTime());
        String random = RandomUtil.randomNumbers(4);
        String memberIdStr = Convert.toStr(infoResult.getId());
        String verificateCode = StrUtil.subSuf(time, time.length() - 6) + random + StrUtil.subSuf(memberIdStr, memberIdStr.length() - 4);
        preFlashOrderInfo.setFlashCode(verificateCode);
        if(StrUtil.isNotBlank(infoResult.getRealName())){
            preFlashOrderInfo.setBuyerName(infoResult.getRealName());
        }else{
            preFlashOrderInfo.setBuyerName(infoResult.getNickName());
        }
        preFlashOrderInfo.setBuyerPhoneNum(infoResult.getPhoneNumber());
        preFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.TOBEWRITTENOFF);
        preFlashOrderInfo.setBeginTime(preActivityInfo.getBeginTime());
        preFlashOrderInfo.setEndTime(preActivityInfo.getEndTime());
        int orderInfo = flashOrderInfoMapper.insert(preFlashOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("?????????????????????");
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
        preOrderInfo.setActivityInfoId(preActivityInfo.getId());
        preOrderInfo.setBuyerAddress(param.getBuyerAddress());
        preOrderInfo.setBuyerProvince(param.getBuyerProvince());
        preOrderInfo.setBuyerCity(param.getBuyerCity());
        preOrderInfo.setBuyerDistrict(param.getBuyerDistrict());
        preOrderInfo.setMemberId(preOrderInfo.getMemberId());
        preOrderInfo.setBuyerName(param.getBuyerName());
        preOrderInfo.setBuyerPhone(param.getBuyerPhone());
        JSONArray dataJson= JSONArray .fromObject(preActivityInfo.getRefGoods());
        List<String> list = (List<String>) JSONArray.toCollection(dataJson);
        if(CollUtil.isEmpty(list)){
            throw new ServiceException("????????????????????????,?????????????????????");
        }
        BigDecimal price=new BigDecimal(0);
        for(String id:list) {
            if (StringUtils.isNotBlank(id)) {
                PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(id);
                if (preGoodsInfo == null) {
                    throw new ServiceException("????????????????????????,?????????????????????");
                }
                price=price.add(preGoodsInfo.getGoodsPrice());
            }
        }
        //???????????????
        preOrderInfo.setTotalPrice(price);
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("?????????????????????");
        }
        savePreOrderGoods(preActivityInfo,preOrderInfo,param);
        String content =  preFlashOrderInfo.getBuyerName() + "???"+ DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")
                +"??????????????????????????????????????????";
        orderOperateRecordService.addOrderOperateRecordLog(preFlashOrderInfo.getBuyerName(),preOrderInfo.getId(),content);

    }
    private void savePreOrderGoods( PreActivityInfo preActivityInfo,PreOrderInfo preOrderInfo,  FlashConfirmOrderParam param){
        JSONArray dataJson= JSONArray .fromObject(preActivityInfo.getRefGoods());
        List<String> list = (List<String>) JSONArray.toCollection(dataJson);
        if(CollUtil.isEmpty(list)){
            throw new ServiceException("????????????????????????,?????????????????????");
        }
        for(String id:list){
            if(StringUtils.isNotBlank(id)){
                PreGoodsInfo preGoodsInfo = goodsInfoMapper.selectById(id);
                if(preGoodsInfo==null){
                    throw new ServiceException("????????????????????????,?????????????????????");
                }
                //????????????
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
                    throw new ServiceException("?????????????????????");
                }
            }

        }

    }

    private PreActivityInfo stateHandler(PreActivityInfo info) {
        if (info == null) {
            throw new ServiceException("???????????????");
        }
        DateTime now = DateTime.now();
        if (info.getActivityState() != ActivityStateEnum.CANCELED) {
            if (now.isAfterOrEquals(info.getBeginTime()) && now.isBeforeOrEquals(info.getEndTime())) {
                info.setActivityState(ActivityStateEnum.IN_PROGRESS);
            } else if (now.isBefore(info.getBeginTime())) {
                info.setActivityState(ActivityStateEnum.NOT_STARTED);
            } else if (now.isAfter(info.getEndTime())) {
                info.setActivityState(ActivityStateEnum.FINISHED);
            }
        }
        return info;
    }

    @Override
    public IPage<PreActivityInfo> pageMemberFlash(MemberFlashPageParam param) {
        Long memberId = MdcUtil.getCurrentMemberId();
        return activityInfoMapper.pageMemberOrder(param.page(), memberId, param).convert(this::stateHandler);
    }

    @Override
    @Transactional
    public void verificationFlashOrder(FlashWriteOffOrderParam param) {
        PreFlashOrderInfo preFlashOrderInfo = flashOrderInfoMapper.selectOne(Wrappers.<PreFlashOrderInfo>lambdaQuery()
                .eq(PreFlashOrderInfo::getFlashCode,param.getFlashCode()));
        if(preFlashOrderInfo==null){
            throw new ServiceException("???????????????");
        }
        PreFlashOrderInfo newFlashOrderInfo = new PreFlashOrderInfo();
        newFlashOrderInfo.setId(preFlashOrderInfo.getId());
        PreActivityInfo activityInfo=preActivityInfoMapper.selectById(preFlashOrderInfo.getActivityInfoId());
        if(ActivityGettingWayEnum.OFF_LINE.equals(activityInfo.getActivityGettingWay())){
            if(ActivityStateEnum.CANCELED.equals(activityInfo.getActivityState())){
                 throw new ServiceException("??????????????????");
            }
            if(ActivityStateEnum.FINISHED.equals(activityInfo.getActivityState())){
                throw new ServiceException("??????????????????");
             }
            if((new Date()).after(activityInfo.getEndTime())){
                throw new ServiceException("??????????????????");
            }
        }
        if(FlashOrderInfoStateEnum.WRITTENOFF.equals(preFlashOrderInfo.getFlashOrderState())){
            throw new ServiceException("???????????????");
        }
        newFlashOrderInfo.setFlashOrderState(FlashOrderInfoStateEnum.WRITTENOFF);
        int orderInfo = flashOrderInfoMapper.updateById(newFlashOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("????????????");
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
        throw new ServiceException("???????????????");

    }

    private PreFlashOrderInfo stateHandler(PreFlashOrderInfo flashOrderInfo) {
        if (flashOrderInfo == null) {
            throw new ServiceException("???????????????");
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
                .eq(StringUtils.isNotBlank(param.getShopName()), PreFlashOrderInfo::getShopName, param.getShopName())
                .orderByDesc(PreFlashOrderInfo::getCreateTime)
        ).convert(this::stateHandler).convert(info -> {
            if(StrUtil.isBlank(info.getShopName())){
                info.setShopName("??????");
            }
            return info;
        });
    }
}
