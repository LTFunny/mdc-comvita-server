package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.PreActivityInfo;
import com.aquilaflycloud.mdc.model.pre.PreGoodsInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderOperateRecord;
import com.aquilaflycloud.mdc.param.pre.PreOrderInfoGetParam;
import com.aquilaflycloud.mdc.param.pre.PreOrderInfoPageParam;
import com.aquilaflycloud.mdc.param.pre.PreStayConfirmOrderParam;
import com.aquilaflycloud.mdc.result.pre.PreOrderInfoGetResult;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

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
        return preOrderInfoMapper.selectPage(param.page(),Wrappers.<PreOrderInfo>lambdaQuery()
                .eq(PreOrderInfo::getGuideId,param.getGuideId())
                .eq(PreOrderInfo::getOrderState,param.getOrderState()));
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

}
