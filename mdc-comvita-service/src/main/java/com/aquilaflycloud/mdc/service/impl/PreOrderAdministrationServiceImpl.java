package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsStateEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderGoodsTypeEnum;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.enums.pre.PickingCardStateEnum;
import com.aquilaflycloud.mdc.enums.wechat.MiniMessageTypeEnum;
import com.aquilaflycloud.mdc.mapper.*;
import com.aquilaflycloud.mdc.model.member.MemberInfo;
import com.aquilaflycloud.mdc.model.pre.*;
import com.aquilaflycloud.mdc.param.pre.*;
import com.aquilaflycloud.mdc.result.member.MemberInfoResult;
import com.aquilaflycloud.mdc.result.pre.*;
import com.aquilaflycloud.mdc.result.wechat.MiniMemberInfo;
import com.aquilaflycloud.mdc.service.PreOrderAdministrationService;
import com.aquilaflycloud.mdc.service.WechatMiniProgramSubscribeMessageService;
import com.aquilaflycloud.mdc.util.MdcUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.sop.servercommon.exception.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


/**
 * @Author zly
 */
@Service
public class PreOrderAdministrationServiceImpl implements PreOrderAdministrationService {
    @Resource
    private WechatMiniProgramSubscribeMessageService wechatMiniProgramSubscribeMessageService;
    @Resource
    private MemberInfoMapper memberInfoMapper;
    @Resource
    private PreOrderInfoMapper preOrderInfoMapper;
    @Resource
    private PreOrderGoodsMapper preOrderGoodsMapper;
    @Resource
    private PreRefundOrderInfoMapper preRefundOrderInfoMapper;
    @Resource
    private PreOrderOperateRecordMapper preOrderOperateRecordMapper;
    @Override
    public IPage<PreOrderInfo> pageAdministrationList(AdministrationListParam param) {
        IPage<PreOrderInfo> list=preOrderInfoMapper.selectPage(param.page(), Wrappers.<PreOrderInfo>lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getShopId()),PreOrderInfo::getShopId, param.getShopId())
                .eq( StringUtils.isNotBlank(param.getGuideName()),PreOrderInfo::getGuideName, param.getGuideName())
                .eq( StringUtils.isNotBlank(param.getOrderState()),PreOrderInfo::getOrderState, param.getOrderState())
                .eq( StringUtils.isNotBlank(param.getOrderCode()),PreOrderInfo::getOrderCode, param.getOrderCode())
                .like( StringUtils.isNotBlank(param.getBuyerName()),PreOrderInfo::getBuyerName, param.getBuyerName())
                .ge(param.getCreateStartTime() != null, PreOrderInfo::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreOrderInfo::getCreateTime, param.getCreateEndTime())
                .orderByDesc(PreOrderInfo::getCreateTime)
        );
        return list;
    }

    @Override
    public IPage<PreRefundOrderInfo> pageOrderInfoList(AdministrationListParam param) {
        IPage<PreRefundOrderInfo> list=preRefundOrderInfoMapper.selectPage(param.page(), Wrappers.<PreRefundOrderInfo>lambdaQuery()
                .eq( StringUtils.isNotBlank(param.getShopId()),PreRefundOrderInfo::getShopId, param.getShopId())
                .eq( StringUtils.isNotBlank(param.getGuideName()),PreRefundOrderInfo::getGuideName, param.getGuideName())
                .eq( StringUtils.isNotBlank(param.getAfterGuideName()),PreRefundOrderInfo::getAfterGuideName, param.getAfterGuideName())
                .eq( StringUtils.isNotBlank(param.getOrderCode()),PreRefundOrderInfo::getOrderCode, param.getOrderCode())
                .like( StringUtils.isNotBlank(param.getBuyerName()),PreRefundOrderInfo::getBuyerName, param.getBuyerName())
                .ge(param.getAfterSalesStartTime() != null, PreRefundOrderInfo::getReceiveTime, param.getAfterSalesStartTime())
                .le(param.getAfterSalEndTime() != null, PreRefundOrderInfo::getReceiveTime, param.getAfterSalEndTime())
                .ge(param.getCreateStartTime() != null, PreRefundOrderInfo::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreRefundOrderInfo::getCreateTime, param.getCreateEndTime())
                .orderByDesc(PreRefundOrderInfo::getCreateTime)
        );
        return list;
    }

    @Override
    @Transactional
    //1.判断是否所有商品都发货了，2.填赠品的时候是否所有商品都发货了
    public void inputOrderNumber(InputOrderNumberParam param) {
        PreOrderGoods info = preOrderGoodsMapper.selectById(param.getId());
        if (info == null) {
            throw new ServiceException("商品不存在");
        }
        PreOrderInfo preOrderInfo = preOrderInfoMapper.selectById(info.getOrderId());
        if (OrderGoodsTypeEnum.GIFTS.equals(info.getGoodsType())) { //填赠品的时候是否所有商品都发货了
            List<PreOrderGoods> list = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                    .eq(PreOrderGoods::getOrderId, info.getOrderId())
                    .notIn(PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE, OrderGoodsStateEnum.PREPARE)
            );
            if (list.size() > 0) {
                throw new ServiceException("存在商品没有发货，请填写完商品再填写赠品的快递单号");
            }
            preOrderInfo.setOrderState(OrderInfoStateEnum.STAYSIGN);
            preOrderInfoMapper.updateById(preOrderInfo);
        }
        info.setExpressName(param.getExpressName());
        info.setExpressOrderCode(param.getExpressOrder());
        info.setExpressCode(param.getExpressCode());
        info.setOrderGoodsState(OrderGoodsStateEnum.ALSENDGOODS);
        info.setPickingCardState(PickingCardStateEnum.VERIFICATE);
        preOrderGoodsMapper.updateById(info);
        if (info.getGoodsType() == OrderGoodsTypeEnum.GIFTS) {
            //赠品发货,发送订单发货微信订阅消息
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERDELIVERY, null,
                    preOrderInfo.getOrderCode(), info.getDeliveryProvince() + info.getDeliveryCity() + info.getDeliveryDistrict() + info.getDeliveryAddress(),
                    info.getExpressName(), info.getExpressOrderCode(), info.getGoodsName() + "商品已发货");
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(preOrderInfo.getAppId())
                            .setOpenId(preOrderInfo.getOpenId())), MiniMessageTypeEnum.PREORDERCHANGE, null,
                    preOrderInfo.getOrderCode(), "已发货", DateTime.now().toString(), "订单" + preOrderInfo.getOrderCode() + "已发货");
        } else {
            //商品发货,发送商品发货微信订阅消息
            MemberInfo memberInfo = memberInfoMapper.selectById(info.getReserveId());
            wechatMiniProgramSubscribeMessageService.sendMiniMessage(CollUtil.newArrayList(new MiniMemberInfo().setAppId(memberInfo.getWxAppId())
                            .setOpenId(memberInfo.getOpenId())), MiniMessageTypeEnum.PREORDERGOODSELIVERY, null,
                    info.getGoodsName(), info.getDeliveryProvince() + info.getDeliveryCity() + info.getDeliveryDistrict() + info.getDeliveryAddress(),
                    info.getExpressName(), info.getExpressOrderCode(), info.getGoodsName() + "商品已发货");
        }
    }

    @Override
    public AdministrationDetailsResult getOrderDetails(OrderDetailsParam param) {
        PreOrderInfo info=preOrderInfoMapper.selectById(param.getId());
        if(info==null){
            throw new ServiceException("输入的主键值有误");
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
       //(value = "买家地址-省")
        result.setBuyerProvince(info.getBuyerProvince());
      //(value = "买家地址-市")
        result.setBuyerCity(info.getBuyerCity());
       //(value = "买家地址-区")
        result.setBuyerDistrict(info.getBuyerDistrict());
        //(value = "买家地址邮编")
        result.setBuyerPostalCode(info.getBuyerPostalCode());
       //(value = "订单明细")
        result.setDetailsList(preOrderGoodsList);
       //(value = "操作记录")
        result.setOperationList(preOrderOperateRecordlist) ;
        result.setBuyerSex(info.getBuyerSex());
        result.setBuyerBirthday(info.getBuyerBirthday());
        result.setScore(info.getScore());
        return result;
    }

    @Override
    public AfterSalesDetailsResult getAfterOrderDetails(OrderDetailsParam param) {
        PreRefundOrderInfo info=preRefundOrderInfoMapper.selectById(param.getId());
        if(info==null){
            throw new ServiceException("输入的主键值有误");
        }
        List<PreOrderGoods> preOrderGoodsList = preOrderGoodsMapper.selectList(Wrappers.<PreOrderGoods>lambdaQuery()
                .eq(PreOrderGoods::getOrderId,info.getOrderId()));
        List<PreOrderOperateRecord> preOrderOperateRecordlist = preOrderOperateRecordMapper.selectList(Wrappers.<PreOrderOperateRecord>lambdaQuery()
                .eq(PreOrderOperateRecord::getOrderId,info.getOrderId()));
        AfterSalesDetailsResult result=new AfterSalesDetailsResult();
        BeanUtils.copyProperties(info, result);
        //(value = "订单明细")
        result.setDetailsList(preOrderGoodsList);
        //(value = "操作记录")
        result.setOperationList(preOrderOperateRecordlist) ;
        return result;
    }

    @Override
    public IPage<PreOrderGoods> pagereadySalesList(ReadyListParam param) {
        IPage<PreOrderGoods> list=preOrderGoodsMapper.selectPage(param.page(), Wrappers.<PreOrderGoods>lambdaQuery()
                .like( param.getGuideName()!=null,PreOrderGoods::getGuideName, param.getGuideName())
                .eq( param.getReserveName()!=null,PreOrderGoods::getReserveName, param.getReserveName())
                .eq( PreOrderGoods::getOrderGoodsState, OrderGoodsStateEnum.PRETAKE)
                .eq( param.getOrderCode()!=null,PreOrderGoods::getOrderCode, param.getOrderCode())
                .like( param.getReserveShop()!=null,PreOrderGoods::getReserveShop, param.getReserveShop())
                .ge(param.getCreateStartTime() != null, PreOrderGoods::getCreateTime, param.getCreateStartTime())
                .le(param.getCreateEndTime() != null, PreOrderGoods::getCreateTime, param.getCreateEndTime())
                .ge(param.getReserveStartTime() != null, PreOrderGoods::getReserveStartTime, param.getReserveStartTime())
                .le(param.getReserveEndTime() != null, PreOrderGoods::getReserveStartTime, param.getReserveEndTime())
                .orderByDesc(PreOrderGoods::getCreateTime)
        );
        return list;
    }

    @Override
    public IPage<ReportOrderPageResult> pageOrderReportList(ReportFormParam param) {
        IPage<ReportOrderPageResult> page=preOrderInfoMapper.pageOrderReportList(param.page(),param);
        return page;
    }

    @Override
    //todo 拉新数量没有加
    public IPage<ReportGuidePageResult> achievementsGuide(ReportFormParam param) {
        IPage<ReportGuidePageResult> page=preOrderInfoMapper.achievementsGuide(param.page(),param);
        return page;
    }

    @Override
    //订单管理订单报表
    public IPage<OrderPageResult> pageOrderPageResultList(AdministrationListParam param) {
        IPage<OrderPageResult> page=preOrderInfoMapper.pageOrderPageResultList(param.page(),param);
        return page;
    }

    @Override
    //订单管理销量导出
    public IPage<SalePageResult> pageSalePageResultList(AdministrationListParam param) {
        IPage<SalePageResult> page=preOrderInfoMapper.pageSalePageResultList(param.page(),param);
        return page;
    }


}
