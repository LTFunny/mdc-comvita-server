package com.aquilaflycloud.mdc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aquilaflycloud.mdc.enums.pre.OrderInfoStateEnum;
import com.aquilaflycloud.mdc.mapper.PreOrderGoodsMapper;
import com.aquilaflycloud.mdc.mapper.PreOrderInfoMapper;
import com.aquilaflycloud.mdc.model.pre.PreOrderInfo;
import com.aquilaflycloud.mdc.param.pre.PreStayConfirmOrderParam;
import com.aquilaflycloud.mdc.service.PreOrderInfoService;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        preOrderInfo.setOrderCode("O"+DateUtil.format(new Date(),"yyyyMMdd")+ code);
        int orderInfo = preOrderInfoMapper.insert(preOrderInfo);
        if(orderInfo < 0){
            throw new ServiceException("生成待确认订单失败。");
        }



        return 0;
    }

}
