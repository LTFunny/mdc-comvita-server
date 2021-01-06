package com.aquilaflycloud.mdc.service.impl;

import com.aquilaflycloud.mdc.enums.pre.OrderGoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderInfoMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderOperateRecordMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderGoods;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.model.pre.PreOrderOperateRecord;
import com.aquilaflycloud.mdc.param.pre.AdministrationListParam;
import com.aquilaflycloud.mdc.param.pre.InputOrderNumberParam;
import com.aquilaflycloud.mdc.param.pre.OrderDetailsParam;
import com.aquilaflycloud.mdc.result.pre.AdministrationDetailsResult;
import com.aquilaflycloud.mdc.result.pre.AdministrationPageResult;
import com.aquilaflycloud.mdc.result.pre.RefundOrderInfoPageResult;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @Author zly
 */
@Service
public class PreOrderAdministrationServiceImpl implements PreOrderAdministrationService {

    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;
    @Resource
    private PreOrderGoodsMapper preOrderGoodsMapper;
    @Resource
    private PreOrderOperateRecordMapper preOrderOperateRecordMapper;
    @Override
    public IPage<AdministrationPageResult> pageAdministrationList(AdministrationListParam param) {
        IPage<AdministrationPageResult> page=preOrderInfoMapper.pageAdministrationList(param.page(),param);
        return page;
    }

    @Override
    public IPage<RefundOrderInfoPageResult> pageOrderInfoList(AdministrationListParam param) {
        IPage<RefundOrderInfoPageResult> page=preOrderInfoMapper.pageOrderInfoList(param.page(),param);
        return page;
    }

    @Override
    public void inputOrderNumber(InputOrderNumberParam param) {
        PreOrderInfo info=preOrderInfoMapper.selectById(param.getId());
        if(info!=null){
            info.setExpressName(param.getExpressName());
            info.setExpressOrder(param.getExpressOrder());
            info.setExpressCode(param.getExpressCode());
            preOrderInfoMapper.updateById(info);
        }else{
            throw new SecurityException("输入的主键值有误");
        }
    }

    @Override
    public AdministrationDetailsResult getOrderDetails(OrderDetailsParam param) {
        PreOrderInfo info=preOrderInfoMapper.selectById(param.getId());
        if(info==null){
            throw new SecurityException("输入的主键值有误");
        }
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,info.getId()));
        List<PreOrderOperateRecord> preOrderOperateRecordlist = preOrderOperateRecordMapper.selectList(Wrappers.<PreOrderOperateRecord>lambdaQuery()
                .eq(PreOrderOperateRecord::getOrderId,info.getId()));
        AdministrationDetailsResult result=new AdministrationDetailsResult();
       //(value = "订单编码")
        result.setOrderCode(info.getOrderCode());
        //(value = "门店名称")
        result.setShopName(info.getShopName());
       //(value = "导购员名称")
        result.setGuideName(info.getGuideName());
       //(value = "总金额")
        result.setTotalPrice(info.getTotalPrice());
       //(value = "订单状态")
        result.setOrderState(info.getOrderState());
       //(value = "创建时间")
        result.setCreateTime(info.getCreateTime());
       //(value = "确认时间")
        result.setConfirmTime(info.getConfirmTime());
       //(value = "发货时间")
        result.setDeliveryTime(info.getDeliveryTime());
       //(value = "销售小票url")
        result.setTicketUrl(info.getTicketUrl());
       //(value = "买家姓名")
        result.setBuyerName(info.getBuyerName());
       //(value = "买家手机")
        result.setBuyerPhone(info.getBuyerPhone());
       //(value = "买家详细地址")
        result.setBuyerAddress(info.getBuyerAddress());
       //(value = "买家地址邮编")
        result.setBuyerPostalCode(info.getBuyerPostalCode());
       //(value = "订单明细")
        result.setDetailsList(preOrderGoodsList);
       //(value = "操作记录")
        result.setOperationList(preOrderOperateRecordlist) ;
        return result;
    }
}
